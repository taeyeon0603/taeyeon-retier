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

      <el-table-column key="title" prop="title" label="标题">
      </el-table-column>


      <el-table-column width="420" label="附件">
        <template #default="scope">
          <ul class="file-ul">
            <li v-for="item in scope.row.files">

              <img
                v-if="(item.name.substr(-3, 3) == 'png' || item.name.substr(-3, 3) == 'jpg' || item.name.substr(-3, 3) == 'gif')"
                src="../../../assets/icon/wenjianleixing-biaozhuntu-tupianwenjian.png" />
              <img v-else-if="(item.name.substr(-3, 3) == 'rar' || item.name.substr(-3, 3) == 'zip')"
                src="../../../assets/icon/wenjianleixing-biaozhuntu-yasuowenjian.png" />
              <img v-else-if="(item.name.substr(-3, 3) == 'ppt' || item.name.substr(-3, 3) == 'ptx')"
                src="../../../assets/icon/wenjianleixing-biaozhuntu-huandengpian.png" />
              <img v-else-if="(item.name.substr(-3, 3) == 'xls' || item.name.substr(-3, 3) == 'lsx')"
                src="../../../assets/icon/wenjianleixing-biaozhuntu-gongzuobiao.png" />
              <img v-else-if="(item.name.substr(-3, 3) == 'doc' || item.name.substr(-3, 3) == 'ocx')"
                src="../../../assets/icon/wenjianleixing-biaozhuntu-Wordwendang.png" />
              <img v-else src="../../../assets/icon/wenjianleixing-biaozhuntu-weizhiwenjian.png" />
              <a :href="item.path" target="_blank">{{ item.name }}</a>
            </li>
          </ul>
        </template>

      </el-table-column>
      <el-table-column key="remark" prop="remark" label="备注">
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
    <work-info-form></work-info-form>
  </div>
</template>

<script>
import { reactive, ref, onMounted, inject, provide, nextTick, onUnmounted } from "vue"
import { workInfoList, workInfoDelete } from '@/api/work'
import bus from "@/bus"
import workInfoForm from "@/components/work/infoList/WorkInfoForm"
import { ElMessage, ElMessageBox } from 'element-plus'
import moment from "moment"

export default {
  name: "customTable",
  components: {
    workInfoForm
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
      page: 1,
      pageSize: 10
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
      postWorkInfoList()
    }

    //获取应用列表
    function postWorkInfoList() {
      loading.value = true
      workInfoList(tableParam).then(res => {
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

          console.log(item);
          workInfoDelete(item.id).then((res) => {
            // console.log(res)
            if (res.code == 200) {
              ElMessage({
                type: 'success',
                message: '删除成功！'
              })
              postWorkInfoList()
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
      tableParam.page = 1
      postWorkInfoList()
    }
    const callbackRefreshApi = () => {
      postWorkInfoList()
    }

    bus.$on('refreshApi', callbackRefreshApi)
    bus.$on('handleSearchWorkInfo', callbackSearch)

    // 组件销毁时，也及时销毁编辑器
    onUnmounted(() => {
      bus.$off("handleSearchWorkInfo", callbackSearch)
      bus.$off('refreshApi', callbackRefreshApi)
    })

    onMounted(() => {
      postWorkInfoList()
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
      handleRole,
      handleAddBtn,
      formatDate
    }
  }
}
</script>
<style scoped lang="scss">
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