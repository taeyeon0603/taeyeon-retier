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

      <el-table-column label="标题">
        <template #default="scope">
          <div class="news-title" @click="handleClickDetail(scope.row)">{{ scope.row.title }}
          </div>

        </template>

      </el-table-column>

      <el-table-column key="createAt" prop="createAt" label="创建时间" width="200" :formatter="formatDate">
      </el-table-column>
      <el-table-column label="" width="100" key="operation">
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 10px;text-align: right" background @current-change="handleCurrentChange"
      :current-page="tableParam.page" :page-sizes="[10, 15, 20]" :page-size="tableParam.pageSize"
      layout="total,prev,pager,next,jumper" :total="files_count">
    </el-pagination>
    <news-form></news-form>
    <news-detail></news-detail>
  </div>
</template>

<script>
import { reactive, ref, onMounted, inject, provide, nextTick, onUnmounted } from "vue"
import { workNewsList, workNewsDelete } from '@/api/work'
import bus from "@/bus"
import newsForm from "@/components/work/newsList/newsForm"
import newsDetail from "@/components/work/newsList/newsDetail"
import { ElMessage, ElMessageBox } from 'element-plus'
import moment from "moment"

export default {
  name: "customTable",
  components: {
    newsForm,
    newsDetail
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
      title: '',
      content: '',
      page: 1,
      pageSize: 10
    })
    let dialog = ref(false)
    provide('dialog', dialog)

    let dialog_detail = ref(false)
    provide('dialog_detail', dialog_detail)

    // function handleSizeChange(para) {
    //   page.value = 1
    //   pageSize.value = para
    //   postAppList()
    // }


    function handleCurrentChange(para) {
      tableParam.page = para
      postWorkNewsList()
    }

    //获取应用列表
    function postWorkNewsList() {
      loading.value = true
      workNewsList(tableParam).then(res => {
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


    function handleClickDetail(rowValue) {
      dialog_detail.value = true
      bus.$emit('handleDetail', rowValue)
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

          console.log(item);
          workNewsDelete(item.id).then((res) => {
            // console.log(res)
            if (res.code == 200) {
              ElMessage({
                type: 'success',
                message: '删除成功！'
              })
              postWorkNewsList()
            }
          }).catch((err) => {

          })
        }
      }).catch(() => {

      })
    }

    const formatDate = ((row, value, cellValue) => {
      if (cellValue != null && cellValue != "") {
        return moment(cellValue).format("YYYY-MM-DD")
      } else {
        return ""
      }

    })


    const callbackSearch = (key) => {
      tableParam.title = key.title
      tableParam.content = key.content
      tableParam.page = 1
      postWorkNewsList()
    }
    const callbackRefreshApi = () => {
      postWorkNewsList()
    }

    bus.$on('refreshApi', callbackRefreshApi)
    bus.$on('handleSearchNews', callbackSearch)

    // 组件销毁时，也及时销毁编辑器
    onUnmounted(() => {
      bus.$off("handleSearchNews", callbackSearch)
      bus.$off('refreshApi', callbackRefreshApi)
    })

    onMounted(() => {
      postWorkNewsList()
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
      handleAddBtn,
      formatDate,
      handleClickDetail
    }
  }
}
</script>
<style scoped lang="scss">
.news-title {
  color: #318ef0;
  cursor: pointer;

  &:hover {
    color: #dc0d63;
  }
}


.icon-type {
  font-size: 16px;
}

.file-ul {
  list-style: none;
  margin: 0;
  padding: 0;

  li {
    margin-right: 10px;
    display: flex;
    align-items: center;
    float: left;

    img {
      width: 18px;
      height: 18px;
    }

    a {
      text-decoration: none;
      color: #1173d8;
    }
  }
}

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