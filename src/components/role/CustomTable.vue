<template>
  <div class="table-container">

    <el-row class="action-bar">
      <el-col :span="12">

        <el-button type="primary" icon="el-icon-plus" circle @click="handleAddBtn"></el-button>
        <el-button type="success" :disabled="selectCount != 1" icon="el-icon-edit" circle @click="handleEditBtn">
        </el-button>
        <el-button type="danger" :disabled="selectCount == 0" icon="el-icon-delete" circle @click="handleDeleteBtn">
        </el-button>

      </el-col>
    </el-row>

    <el-table v-loading="loading" ref="multipleTable" :data="tableData.data" tooltip-effect="dark"
      style="width: calc(100% - 20px);margin-left: 10px;margin-top: 10px;" size="small"
      @selection-change="handleSelectChange">

      <el-table-column type="selection" width="35">
      </el-table-column>

      <el-table-column type="index" label="编号" width="50">
      </el-table-column>

      <el-table-column key="name" prop="name" label="角色名称">
      </el-table-column>
      <el-table-column key="updateAt" prop="updateAt" label="更新时间" :formatter="formatUpdateAt">
      </el-table-column>

      <el-table-column key="updateBy" prop="updateBy" label="更新人">
      </el-table-column>
      <el-table-column key="createAt" prop="createAt" label="创建时间" :formatter="formatCreateAt">
      </el-table-column>

      <el-table-column key="createBy" prop="createBy" label="创建人">
      </el-table-column>
      <el-table-column label="" width="150" key="operation">
        <template #default="scope">
          <el-button type="text" class="customText" size="small" @click="handleBindRoute(scope.row)">路由
          </el-button>
          <el-button type="text" class="customText" size="small" @click="handleEmpower(scope.row)">权限
          </el-button>

        </template>
      </el-table-column>

    </el-table>
    <el-pagination style="margin-top: 10px;text-align: right" background @current-change="handleCurrentChange"
      :current-page="tableParam.page" :page-sizes="[10, 15, 20]" :page-size="tableParam.pageSize"
      layout="total,prev,pager,next,jumper" :total="files_count">
    </el-pagination>
    <role-form></role-form>
    <empower></empower>
    <bind-route></bind-route>

  </div>
</template>

<script>
import { reactive, ref, onMounted, inject, provide, onUnmounted } from "vue"
import { roleList, deleteRole, getPermissionByRole } from '@/api/role'
import bus from "@/bus"
import RoleForm from "@/components/role/RoleForm"
import Empower from "./Empower"
import { ElMessage, ElMessageBox } from 'element-plus'
import moment from 'moment'
import BindRoute from "./BindRoute"


export default {
  name: "customTable",
  components: {
    RoleForm,
    Empower,
    BindRoute

  },
  setup() {
    //列表模块
    let multipleTable = ref(null)
    let files_count = ref(0)
    let loading = ref(true)
    let tableData = reactive({
      data: []
    })

    let tableParam = reactive({
      name: '',
      page: 1,
      pageSize: 10
    })

    let dialog = ref(false)
    provide('dialog', dialog)

    let powerDrawer = ref(false)
    provide('powerDrawer', powerDrawer)


    let viewDrawer = ref(false)
    provide('viewDrawer', viewDrawer)


    let routeDrawer = ref(false)
    provide('routeDrawer', routeDrawer)

    // function handleSizeChange(para) {
    //   page.value = 1
    //   pageSize.value = para
    //   postAppList()
    // }


    function handleCurrentChange(para) {
      tableParam.page = para
      postRoleList()
    }

    //获取角色列表
    function postRoleList() {
      loading.value = true
      roleList(tableParam).then(res => {
        // console.log(res)

        if (res.code === 200) {
          tableData.data = res.data.content
          files_count.value = res.data.totalElements
          loading.value = false
        }
      }).catch(err => {

      })
    }

    let selectCount = ref(0)
    let selectData = reactive({
      data: []
    })

    function handleSelectChange(val) {
      selectCount.value = val.length
      selectData.data = val
    }

    function clearSelect() {
      multipleTable.value.clearSelection()
    }


    function handleEmpower(val) {
      powerDrawer.value = true
      bus.$emit('handlePower', val)
    }

    function handleBindRoute(val) {
      routeDrawer.value = true
      bus.$emit('handleBindRoute', val)
    }

    function handleAddBtn() {
      dialog.value = true
      bus.$emit('handleAdd')
    }

    function handleEditBtn() {
      dialog.value = true
      bus.$emit('handleEdit', selectData.data)
    }


    function handleDeleteBtn() {
      ElMessageBox.confirm('确定要删除这' + selectCount.value + '项吗?', '提示', {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {

        for (let item of selectData.data) {
          deleteRole(item.id).then((res) => {
            // console.log(res)
            if (res.code == 200) {
              ElMessage({
                type: 'success',
                message: '删除成功！'
              })
              postRoleList()
            }
          }).catch((err) => {

          })
        }
      }).catch(() => {

      })
    }

    const callbackSearch = (key) => {
      tableParam.name = key.name
      tableParam.page = 1
      postRoleList()
    }
    const callbackRefreshApi = () => {
      postRoleList()
    }

    bus.$on('refreshApi', callbackRefreshApi)
    bus.$on('handleSearchRole', callbackSearch)

    // 组件销毁时，也及时销毁编辑器
    onUnmounted(() => {
      bus.$off("handleSearchRole", callbackSearch)
      bus.$off('refreshApi', callbackRefreshApi)
    })

    // function getPermission(val) {
    //   if (val.permissionList) {
    //     if (val.permissionList.length > 0) {
    //       let result = ""
    //       for (let item of val.permissionList) {
    //         result = result + item.name + ','
    //       }
    //       return result
    //     } else {
    //       return ""
    //     }
    //   } else {
    //     return ""
    //   }
    // }

    function formatCreateAt(val) {
      if (val.createAt) {
        return moment(val.createAt).format("YYYY-MM-DD HH:mm:ss")
      } else {
        return ""
      }
    }

    function formatUpdateAt(val) {
      if (val.updateAt) {
        return moment(val.updateAt).format("YYYY-MM-DD HH:mm:ss")
      } else {
        return ""
      }
    }


    onMounted(() => {
      postRoleList()
    })


    return {
      multipleTable,
      tableData,
      loading,
      files_count,
      handleCurrentChange,
      handleSelectChange,
      selectCount,
      clearSelect,
      handleAddBtn,
      handleDeleteBtn,
      handleEditBtn,
      tableParam,
      formatCreateAt,
      formatUpdateAt,
      handleEmpower,
      handleBindRoute
    }
  }
}
</script>
<style scoped lang="scss">
.table-container {
  height: calc(100% - 40px);
  width: calc(100% - 40px);
  margin: 0px 20px 20px 20px;

  .action-bar {
    height: 70px;
    width: calc(100% - 20px);
    margin-left: 10px;
    background-color: $operation-background;
    border: 1px solid $operation-border;
    border-radius: 2px;

    div {
      line-height: 68px;
      padding-left: 15px;
      padding-right: 15px;
      text-align: left;

      :deep(.el-button) {
        color: white;
      }
    }
  }


  :deep(.el-table th) {
    background-color: #efefef;
  }

  :deep(.el-table__header-wrapper) {

    border-bottom: 1px solid #d2d2d2 !important;
    background-color: #efefef;
  }

  :deep(.el-table) {

    div::-webkit-scrollbar {
      width: 10px;
      height: 10px;
    }

    div::-webkit-scrollbar-thumb {
      background-color: #9c9c9c;
    }
  }

  :deep(.demo-table-expand) {
    font-size: 0;

    label {
      width: 100px;
      color: #2977c7;
      font-weight: bold;
      font-size: 12px;
    }

    .el-form-item {
      margin-left: 0;
      margin-bottom: 0;
      width: 100%;

      .el-form-item__content {
        font-size: 12px;

      }
    }

    .el-table__header tr,
    .el-table__header th {
      padding: 0;
      height: 40px;
      line-height: 40px;
    }
  }
}
</style>