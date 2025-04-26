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

      <el-table-column key="name" prop="name" label="姓名">
      </el-table-column>
      <el-table-column key="idCard" prop="idCard" label="身份证号">
      </el-table-column>
      <el-table-column key="policeNum" prop="policeNum" label="工号">
      </el-table-column>

      <el-table-column key="deptName" prop="deptName" label="单位">
      </el-table-column>
      <el-table-column key="username" prop="username" label="账号名">
      </el-table-column>
      <el-table-column key="updateAt" prop="updateAt" label="更新时间" :formatter="updateAtChange">
      </el-table-column>
      <el-table-column key="createAt" prop="createAt" label="创建时间" :formatter="createAtChange">
      </el-table-column>
      <el-table-column label="" width="220" key="operation">
        <template #default="scope">
          <el-button type="text" class="customText" size="small" @click="handleRole(scope.row)">绑定角色
          </el-button>
          <el-button type="text" class="customText" size="small" @click="handleDeptView(scope.row)">可视部门
          </el-button>
          <el-button type="text" class="customText" size="small" @click="handleDeptControl(scope.row)">管辖派出所
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 10px;text-align: right" background @current-change="handleCurrentChange"
      :current-page="tableParam.page" :page-sizes="[10, 15, 20]" :page-size="tableParam.pageSize"
      layout="total,prev,pager,next,jumper" :total="files_count">
    </el-pagination>
    <user-form></user-form>
    <choose-role></choose-role>
    <dept-view></dept-view>
    <dept-control></dept-control>
  </div>
</template>

<script>
import { reactive, ref, onMounted, inject, provide, onUnmounted } from "vue"
import { userList, deleteUser } from '@/api/user'
import bus from "@/bus"
import DeptView from "./DeptView"
import DeptControl from "./DeptControl"
import UserForm from "@/components/user/UserForm"
import ChooseRole from "@/components/user/ChooseRole"
import { ElMessage, ElMessageBox } from 'element-plus'
import moment from "moment"

export default {
  name: "customTable",
  components: {
    UserForm,
    ChooseRole,
    DeptView,
    DeptControl
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
      deptName: '',
      name: '',
      page: 1,
      pageSize: 10,
      username: ""
    })
    let dialog = ref(false)
    provide('dialog', dialog)

    let roleDialogVisible = ref(false)
    provide('roleDialogVisible', roleDialogVisible)

    // function handleSizeChange(para) {
    //   page.value = 1
    //   pageSize.value = para
    //   postAppList()
    // }


    function handleCurrentChange(para) {
      tableParam.page = para
      postUserList()
    }

    //获取应用列表
    function postUserList() {
      loading.value = true
      userList(tableParam).then(res => {
        console.log(res)

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

    function handleRole(val) {
      roleDialogVisible.value = true
      bus.$emit('handleRole', val)
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
          deleteUser(item.id).then((res) => {
            // console.log(res)
            if (res.code == 200) {
              ElMessage({
                type: 'success',
                message: '删除成功！'
              })
              postUserList()
            }
          }).catch((err) => {

          })
        }
      }).catch(() => {

      })
    }


    let viewDrawer = ref(false)
    provide('viewDrawer', viewDrawer)

    function handleDeptView(val) {
      viewDrawer.value = true
      bus.$emit('handleView', val)
    }

    let controlDrawer = ref(false)
    provide('controlDrawer', controlDrawer)

    function handleDeptControl(val) {
      controlDrawer.value = true
      bus.$emit('handleControl', val)
    }

    function updateAtChange(val) {
      if (val.updateAt) {
        return moment(val.updateAt).format("YYYY-MM-DD")
      } else {
        return ""
      }
    }

    function createAtChange(val) {
      if (val.createAt) {
        return moment(val.createAt).format("YYYY-MM-DD")
      } else {
        return ""
      }
    }


    const callbackSearch = (key) => {
      tableParam.name = key.name
      tableParam.deptName = key.deptName
      tableParam.username = key.username
      tableParam.page = 1
      postUserList()
    }
    const callbackRefreshApi = () => {
      postUserList()
    }

    bus.$on('refreshApi', callbackRefreshApi)
    bus.$on('handleSearchUser', callbackSearch)

    // 组件销毁时，也及时销毁编辑器
    onUnmounted(() => {
      bus.$off("handleSearchUser", callbackSearch)
      bus.$off('refreshApi', callbackRefreshApi)
    })



    onMounted(() => {
      postUserList()
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
      handleDeleteBtn,
      handleEditBtn,
      tableParam,
      updateAtChange,
      createAtChange,
      handleRole,
      handleAddBtn,
      handleDeptView,
      handleDeptControl
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