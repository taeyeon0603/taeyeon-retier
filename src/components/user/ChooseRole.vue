<template>
  <div class="custom-add">
    <el-dialog title="绑定角色" v-model="roleDialogVisible" ref="drawer" :width="400">
      <el-row class="checkbox-row">
        <el-col :span="24" style="text-align: left">
          <el-checkbox-group v-model="checkList">
            <el-checkbox v-for="itemChildren in tableData.data" :label="itemChildren.id">{{ itemChildren.name }}
            </el-checkbox>
          </el-checkbox-group>
        </el-col>
      </el-row>
      <template #footer>
        <div class="demo-drawer__footer">
          <el-button class="el-button--custom" @click="submitAdd" :loading="formLoading">{{ formLoading ?
              '提交中...' : '确定'
          }}
          </el-button>
          <el-button class="el-button-white" @click="cancelForm">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, inject, nextTick, onMounted } from 'vue'
import { roleList } from '@/api/role'
import { getRoleByUser, userBindRole } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"

export default {
  name: "Empower",
  setup() {
    let forms = ref(null)
    let drawerTitle = ref('新增')
    let roleDialogVisible = inject('roleDialogVisible')
    let formLoading = ref(false)
    let permissionData = reactive({
      list: []
    })
    let userId = ref(0)
    let tableData = reactive({
      data: []
    })
    let tableParam = reactive({
      name: '',
      page: 1,
      pageSize: 100
    })
    let checkList = ref([])

    bus.$on('handleRole', (val) => {
      userId.value = val.id

      getRoleByUser(val.id).then(res => {
        if (res.code === 200) {
          checkList.value = []
          for (let item of res.data) {
            checkList.value.push(item.id)
          }
        }
      }).catch(err => {

      })
      console.log(val)
    })

    //获取角色列表
    function postRoleList() {
      roleList(tableParam).then(res => {
        if (res.code === 200) {
          tableData.data = res.data.content
        }
      }).catch(err => {

      })
    }

    function submitAdd() {
      let permissionIds = []
      for (let item of permissionData.list) {
        if (item.checkList.length > 0) {
          permissionIds = permissionIds.concat(item.checkList)
        }
      }
      userBindRole({
        roles: checkList.value,
        userId: userId.value
      }).then(res => {
        if (res.code === 200) {
          ElMessage.success('操作成功！')
          roleDialogVisible.value = false
          bus.$emit('refreshTableData')
        }
      }).catch(err => {
      })
    }

    function checkboxChange() {
      // console.log(permissionData.list)
    }

    function cancelForm() {
      roleDialogVisible.value = false
    }

    onMounted(() => {
      postRoleList()
    })


    return {
      forms,
      drawerTitle,
      roleDialogVisible,
      permissionData,
      formLoading,
      submitAdd,
      cancelForm,
      checkboxChange,
      tableData,
      checkList
    }
  }
}
</script>

<style scoped lang="scss">
.custom-add {
  text-align: left;


  :deep(.el-dialog__title) {
    color: $font-color-theme;
    font-weight: bold;
  }

  :deep(.el-dialog__body) {
    padding-top: 0px;
    padding-bottom: 10px;
  }

  :deep(.el-form-item__content) {
    text-align: left;
  }

  :deep(.el-input__inner) {
    border: 1px solid #757575;
  }


  :deep(.el-divider--horizontal) {
    margin: 8px 0px 18px 0px;
    background-color: #636363;
  }
}

.checkbox-row {
  margin-top: 20px;


  .content {
    margin-top: 10px;
    margin-left: 20px;
    text-align: left;
  }


  :deep(.el-checkbox__inner::after) {
    border-color: #ffffff;
  }

}
</style>