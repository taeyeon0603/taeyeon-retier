<template>
  <div class="table-container">

    <el-row class="action-bar">
      <el-col :span="12">

        <el-button type="success" :disabled="selectCount != 1" icon="el-icon-edit" circle @click="handleEditBtn">
        </el-button>
        <el-button type="danger" :disabled="selectCount == 0" icon="el-icon-delete" circle @click="handleDeleteBtn">
        </el-button>
        <el-button type="warning" icon="el-icon-download" circle @click="handleExcelOut"></el-button>

      </el-col>
    </el-row>

    <el-table v-loading="loading" ref="multipleTable" :data="tableData.data" tooltip-effect="dark"
      style="width: calc(100% - 20px);margin-left: 10px;margin-top: 10px;" @selection-change="handleSelectChange">

      <el-table-column type="selection" width="35">
      </el-table-column>

      <el-table-column type="index" label="编号" width="50">
      </el-table-column>

      <el-table-column key="name" prop="name" label="姓名">
      </el-table-column>
      <el-table-column key="idCard" prop="idCard" label="身份证">
      </el-table-column>
      <el-table-column key="politicalIdentity" prop="politicalIdentity" label="政治面貌">
      </el-table-column>

      <el-table-column key="deptName" prop="deptName" label="原部门">
      </el-table-column>
      <el-table-column key="retireDate" prop="retireDate" label="退休时间" :formatter="formatRetireDate">
      </el-table-column>

      <el-table-column key="party" prop="party" label="退休党支部">
      </el-table-column>
      <el-table-column label="" width="100" key="operation">
        <template #default="scope">
          <el-button type="text" class="customText" size="small" @click="retireeDetail(scope.row)">详情
          </el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-pagination style="margin-top: 10px;text-align: right" background @current-change="handleCurrentChange"
      :current-page="tableParam.page" :page-sizes="[10, 15, 20]" :page-size="tableParam.pageSize"
      layout="total,prev,pager,next,jumper" :total="files_count">
    </el-pagination>
    <retiree-detail></retiree-detail>
    <retiree-add></retiree-add>
    <excel-out-history></excel-out-history>
    <!--    <role-form></role-form>-->
    <!--    <empower></empower>-->
    <!--    <bind-route></bind-route>-->
  </div>
</template>

<script>
import { reactive, ref, onMounted, inject, provide, onUnmounted } from "vue"
import { postRetireList, deleteRetire } from '@/api/retiree'
import bus from "@/bus"
import { ElMessage, ElMessageBox } from 'element-plus'
import moment from 'moment'
import RetireeDetail from "./RetireeDetail"
import RetireeAdd from "./RetireeAdd"
import ExcelOutHistory from "./ExcelOutHistory";

import table2excel from 'js-table2excel'

export default {
  name: "customTable",
  components: {
    RetireeDetail,
    RetireeAdd,
    ExcelOutHistory
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
      idCard: '',
      dept: '',
      isHistory: true,
      page: 1,
      pageSize: 10
    })

    let dialog = ref(false)
    provide('dialog', dialog)

    let powerDrawer = ref(false)
    provide('powerDrawer', powerDrawer)

    let retireeDetailVisible = ref(false)
    provide('retireeDetailVisible', retireeDetailVisible)
    let retireeAddVisible = ref(false)
    provide('retireeAddVisible', retireeAddVisible)

    // function handleSizeChange(para) {
    //   page.value = 1
    //   pageSize.value = para
    //   postAppList()
    // }


    function handleCurrentChange(para) {
      tableParam.page = para
      retireeList()
    }

    //获取角色列表
    function retireeList() {
      loading.value = true
      postRetireList(tableParam).then(res => {
        // console.log(res)

        if (res.code === 200) {
          tableData.data = res.data.content
          files_count.value = res.data.totalElements
          loading.value = false
        }
      }).catch(err => {

      })
    }


    // let formatJson = (filterVal, jsonData) => {
    //   return jsonData.map((v) => filterVal.map((j) => v[j]))
    // }

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

    function retireeDetail(val) {
      retireeDetailVisible.value = true
      bus.$emit('retireeDetailDialog', val)
    }

    function handleAddBtn() {
      retireeAddVisible.value = true
      bus.$emit('retireeAddDialog')
    }

    function handleEditBtn() {
      retireeAddVisible.value = true
      bus.$emit('retireeEditDialog', selectData.data)
    }

    bus.$on('refreshTableData', () => {
      retireeList()
    })

    function handleDeleteBtn() {
      ElMessageBox.confirm('确定要删除这' + selectCount.value + '项吗?', '提示', {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {

        for (let item of selectData.data) {
          deleteRetire(item.id).then((res) => {
            // console.log(res)
            if (res.code == 200) {
              ElMessage({
                type: 'success',
                message: '删除成功！'
              })
              retireeList()
            }
          }).catch((err) => {

          })
        }
      }).catch(() => {

      })
    }


    const callbackSearch = (key) => {
      tableParam.name = key.name
      tableParam.dept = key.dept
      tableParam.idCard = key.idCard
      tableParam.page = 1
      retireeList()
    }
    const callbackRefreshApi = () => {
      retireeList()
    }

    bus.$on('refreshApi', callbackRefreshApi)
    bus.$on('handleSearchRetiree', callbackSearch)

    // 组件销毁时，也及时销毁编辑器
    onUnmounted(() => {
      bus.$off("handleSearchRetiree", callbackSearch)
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

    let excelDialogVisible = ref(false)
    provide('excelDialogVisible', excelDialogVisible)

    function handleExcelOut(val) {
      excelDialogVisible.value = true
      bus.$emit('handleExcelOut', val)
    }

    function formatRetireDate(val) {
      if (val.retireDate) {
        return moment(val.retireDate).format("YYYY-MM-DD")
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
      retireeList()
      setTimeout(() => {
        // outExcel()
      }, 2000)
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
      formatRetireDate,
      formatUpdateAt,
      handleEmpower,
      retireeDetail,
      handleExcelOut
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

  :deep(.el-divider) {
    background-color: #efefef;
  }

  :deep(.el-table th) {
    background-color: #efefef;
  }

  :deep(.el-table td) {
    padding: 8px 0px;
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


}
</style>