<template>
  <div class="custom-add">
    <el-drawer title="授权" v-model="powerDrawer" direction="rtl" ref="drawer" :size="565">
      <el-row class="checkbox-row" v-for="item in permissionData.list">
        <el-col :span="24">
          <div class="label">{{ item.label }}</div>
          <div class="content">
            <el-checkbox-group @change="checkboxChange" v-model="item.checkList">
              <el-checkbox v-for="itemChildren in item.children" :label="itemChildren.id">{{ itemChildren.name }}
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
import { ref, reactive, inject, nextTick } from 'vue'
import { addRole, getRoleById, updateRole, updatePermissions } from '@/api/role'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"

export default {
  name: "Empower",
  setup() {
    let forms = ref(null)
    let drawerTitle = ref('新增')
    let powerDrawer = inject('powerDrawer')
    let formLoading = ref(false)
    let permissionData = reactive({
      list: []
    })
    let roleId = ref(0)
    bus.$on('handlePower', (val) => {
      // for (let item of val) {
      //   addForm.listData.push(item)
      // // }
      roleId.value = val.id
      permissionData.list = val.permissionGroup
      for (let item of permissionData.list) {
        item.checkList = []
        for (let itemChild of item.children) {
          if (itemChild.checked === true) {
            item.checkList.push(itemChild.id)
          }
        }
      }
    })
    function submitAdd() {
      let permissionIds = []
      for (let item of permissionData.list) {
        if (item.checkList.length > 0) {
          permissionIds = permissionIds.concat(item.checkList)
        }
      }
      updatePermissions({
        permissions: permissionIds,
        roleId: roleId.value
      }).then(res => {
        if (res.code === 200) {
          ElMessage.success('授权成功！')
          powerDrawer.value = false
          bus.$emit('refreshApi')
        }
      }).catch(err => {
      })
    }

    function checkboxChange() {
      // console.log(permissionData.list)
    }

    function cancelForm() {
      powerDrawer.value = false
    }


    return {
      forms,
      drawerTitle,
      powerDrawer,
      permissionData,
      formLoading,
      submitAdd,
      cancelForm,
      checkboxChange
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
    margin-bottom: 5px;
  }




}
</style>