<template>
  <div class="custom-add">
    <el-drawer title="可视部门" v-model="viewDrawer" direction="rtl" ref="drawer" :size="565">
      <el-row class="checkbox-row">
        <el-col :span="24">
          <div class="content">
            <el-checkbox-group @change="checkboxChange" v-model="checkList">
              <el-checkbox v-for="item in deptList.data" :label="item.deptId">{{ item.deptName }}
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </el-col>
      </el-row>
      <el-divider content-position="left"></el-divider>
      <div class="demo-drawer__footer">

        <el-button class="el-button--custom" @click="submitAdd" :loading="formLoading">{{ formLoading ?
            '提交中...' : '确定'
        }}
        </el-button>
        <el-button class="el-button-white" @click="cancelForm">取消</el-button>
      </div>

    </el-drawer>
  </div>
</template>

<script>
import { ref, reactive, inject, onMounted } from 'vue'

import { getDeptList, getDeptView, updateDeptView } from "@/api/user"
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"

export default {
  name: "DeptView",
  setup() {
    let forms = ref(null)
    let drawerTitle = ref('新增')
    let viewDrawer = inject('viewDrawer')
    let formLoading = ref(false)
    let checkList = ref([])
    let permissionData = reactive({
      list: []
    })
    let userId = ref(0)
    bus.$on('handleView', (val) => {
      console.log(val)
      userId.value = val.id
      getDeptView({ userId: val.id }).then(res => {
        if (res.code == 200) {
          checkList.value = []
          for (let item of res.data) {
            checkList.value.push(item.deptId)
          }
        }
      })
    })
    function submitAdd() {
      updateDeptView({
        userId: userId.value,
        deptIds: checkList.value
      }).then(res => {
        if (res.code === 200) {
          ElMessage.success('修改成功！')
          viewDrawer.value = false
          bus.$emit('refreshApi')
        }
      }).catch(err => {
      })
    }

    function checkboxChange() {
      // console.log(permissionData.list)
    }

    function cancelForm() {
      viewDrawer.value = false
    }


    let deptList = reactive({
      data: []
    })
    function getDeptListData() {
      getDeptList().then(res => {
        if (res.code == 200) {
          deptList.data = res.data
        }
      })
    }


    onMounted(() => {
      getDeptListData()
    })
    return {
      forms,
      drawerTitle,
      viewDrawer,
      permissionData,
      formLoading,
      submitAdd,
      cancelForm,
      checkboxChange,
      deptList,
      checkList
    }
  }
}
</script>

<style scoped lang="scss">
.custom-add {


  :deep(.el-form-item__content) {
    text-align: left;

  }


  :deep(.el-drawer__header span) {
    outline: none;
    color: $font-color-theme;
    font-size: 20px;
    font-weight: bold;
    text-align: left;
  }

  :deep(.el-divider--horizontal) {
    margin: 8px 0px 18px 0px;
  }
}


.checkbox-row {
  margin-top: 20px;

  .label {
    font-size: 20px;
  }

  .content {
    margin-top: 10px;
    margin-left: 20px;
    text-align: left;
  }

  :deep(.el-checkbox) {
    width: 150px;
    margin-bottom: 15px;
  }




}
</style>