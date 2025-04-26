<template>
  <el-container style="height: 100%">
    <el-main>
      <div class="search">
        <ul>
          <li>
            <div class="label">系统名称</div>
            <div class="item">
              <el-select v-model="searchForm.webIds" multiple clearable filterable placeholder="请选择" collapse-tags>
                <el-option v-for="item in appNameOption.data" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </li>
          <li>
            <div class="label">终端IP</div>
            <div class="item">
              <el-input v-model="searchForm.ip" placeholder="终端IP"></el-input>
            </div>
          </li>
          <li>
            <div class="label">终端部门</div>
            <div class="item">
              <el-select v-model="searchForm.terminalDeptIds" multiple collapse-tags clearable filterable
                placeholder="请选择">
                <el-option v-for="item in terminalDeptOption.data" :key="item.value" :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
          </li>
          <li>
            <div class="label">证书姓名</div>
            <div class="item">
              <el-input v-model="searchForm.certName" placeholder="证书姓名"></el-input>
            </div>
          </li>
          <li>
            <div class="label">证书号码</div>
            <div class="item">
              <el-input v-model="searchForm.certNum" placeholder="证书人身份证号"></el-input>
            </div>
          </li>
          <li>
            <div class="label">证书部门</div>
            <div class="item">
              <el-select v-model="searchForm.certDeptIds" multiple collapse-tags clearable filterable placeholder="请选择">
                <el-option v-for="item in certDeptOption.data" :key="item.value" :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
          </li>
          <li>
            <div class="label">查询关键字</div>
            <div class="item">
              <el-input v-model="searchForm.keyword" placeholder="查询关键字"></el-input>
            </div>
          </li>
          <li>
            <div class="label">访问时间</div>
            <div class="item">
              <el-date-picker v-model="dtRange.data" type="daterange" unlink-panels range-separator="至"
                start-placeholder="开始日期" end-placeholder="结束日期" :shortcuts="shortcuts" :default-time="defaultTime">
              </el-date-picker>
            </div>
          </li>

          <li>
            <el-button style="margin-left: 15px" size="medium" type="primary" @click="handleSearch">查询</el-button>
          </li>
        </ul>
      </div>


      <el-divider></el-divider>

      <el-table v-loading="loading" ref="multipleTable" :data="tableData.data"
        style="width: calc(100% - 20px);margin-left: 10px;" size="small" stripe @expand-change="expandChange"
        height="500" :row-key="rowKey" :expand-row-keys="expands.data">

        <el-table-column type="expand">
          <template #default="props">
            <el-form v-if="!props.row.isPhoto" label-position="right" inline class="demo-table-expand">
              <el-form-item label="访问者">
                <span>
                  终端IP（{{ props.row.ip }}），
                  终端责任人（{{ props.row.terminalUserName }}），
                  终端部门（{{ props.row.terminalDept }}），
                  证书号码（{{ props.row.pkiUserId }}），
                  证书姓名（{{ props.row.pkiUserName }}），
                  证书部门（{{ props.row.pkiDeptName }}）
                </span>
              </el-form-item>
              <el-form-item label="访问系统名称">
                <span>
                  {{ props.row.webSiteName }}
                </span>
              </el-form-item>
              <el-form-item label="系统地址">
                <span>
                  {{ props.row.url }}
                </span>
              </el-form-item>
              <el-form-item label="访问时间">
                <span>
                  {{ moment(props.row.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
                </span>
              </el-form-item>

              <el-form-item style="margin: 20px 0px 10px 0px " label="查询关键字">
                <el-input v-if="props.row.isRequests" type="textarea" :rows="5" style="width: 829px;font-size:12px"
                  readonly v-model="props.row.requests"></el-input>

                <el-input v-else type="textarea" :rows="3" style="width: 60%;font-size:12px" readonly
                  v-model="props.row.reqMatchAct"></el-input>
                <el-button style="margin-left: 20px" size="mini" type="primary"
                  @click="props.row.isRequests = !props.row.isRequests">
                  查看请求内容
                </el-button>
              </el-form-item>

              <el-form-item style="margin: 10px 0px 20px 0px " label="响应关键字">
                <el-input type="textarea" :rows="3" style="width: 60%;font-size:12px" readonly
                  v-model="props.row.ansMatchAct"></el-input>
                <el-button style="margin-left: 20px" size="mini" type="primary" @click="checkPhoto(props.row.id)">响应快照
                </el-button>

              </el-form-item>

              <el-form-item label="关联日志">
                <el-table :data="props.row.logData" size="small" stripe max-height="230px" style="width: calc(60%);">

                  <el-table-column type="index" label="序号" width="50">
                  </el-table-column>
                  <el-table-column key="url" prop="url" label="系统地址">
                  </el-table-column>
                  <el-table-column key="reqMatchAct" prop="reqMatchAct" label="查询关键字">
                  </el-table-column>
                  <el-table-column key="ansMatchAct" prop="ansMatchAct" label="响应关键字">
                  </el-table-column>

                </el-table>
              </el-form-item>

            </el-form>
            <el-row v-else>
              <el-col :span="24">
                <el-row>
                  <el-col :span="4" style="line-height: 28px;font-size: 15px;color:#2977c7;font-weight: bold">
                    响应快照
                  </el-col>
                  <el-col :span="20" style="text-align: right">
                    <el-button size="mini" type="primary" circle icon="el-icon-arrow-left"
                      :disabled="props.row.ansTextCount === 0" @click="props.row.ansTextCount -= 1"></el-button>
                    <el-button size="mini" type="primary" circle icon="el-icon-arrow-right"
                      :disabled="props.row.ansTextCount === props.row.ansText.length - 1"
                      @click="props.row.ansTextCount += 1"></el-button>
                    <el-button size="mini" type="success" @click="props.row.isPhoto = !props.row.isPhoto">返回</el-button>
                  </el-col>
                </el-row>
                <el-row style="margin-top: 10px;padding: 10px;background-color: #efefef">
                  <el-col :span="23" v-html="props.row.ansText[props.row.ansTextCount]">

                  </el-col>
                </el-row>

              </el-col>
            </el-row>

          </template>
        </el-table-column>

        <el-table-column type="index" label="序号" width="50">
        </el-table-column>

        <el-table-column key="name" prop="name" label="访问时间" width="150" :formatter="changeTime">
        </el-table-column>
        <el-table-column key="ip" prop="ip" width="150" label="终端IP">
        </el-table-column>

        <el-table-column key="pkiUserId" prop="pkiUserId" width="200" label="证书号码">
        </el-table-column>
        <el-table-column key="pkiUserName" prop="pkiUserName" width="80" label="证书姓名">
        </el-table-column>
        <el-table-column key="webSiteName" prop="webSiteName" label="访问系统名称">
        </el-table-column>
        <el-table-column key="reqMatchAct" prop="reqMatchAct" label="查询关键字">
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top: 10px;text-align: right" background @current-change="handleCurrentChange"
        @size-change="handleSizeChange" :current-page="page" :page-sizes="[10, 20, 30, 40, 50]" :page-size="pageSize"
        layout="total,prev,pager,next,jumper,sizes" :total="files_count">
      </el-pagination>

    </el-main>

  </el-container>
</template>

<script>

import { reactive, ref, onMounted, watch } from 'vue'
import { getAppList, getCertDepts, getTerminalDepts } from '@/api/dict'
import { postLogList, getLogDetail } from '@/api/log'
import moment from 'moment'

export default {
  name: "SystemLog",
  setup() {

    let multipleTable = ref(null)
    let page = ref(1)
    let pageSize = ref(10)
    let files_count = ref(0)
    let loading = ref(false)
    let tableData = reactive({
      data: []
    })
    let dtRange = reactive({
      data: [moment({ hour: 0, minute: 0, seconds: 0 }).subtract(7, 'days').valueOf(), new Date()]
      // data: [new Date().setTime(new Date().getTime() - 3600 * 1000 * 24 * 7), new Date()]
    })
    let searchForm = reactive({
      webIds: [],
      certDeptIds: [],
      certName: '',
      certNum: '',
      start: dtRange.data[0],
      end: dtRange.data[1],
      ip: '',
      keyword: '',
      terminalDeptIds: [],
      page: 1,
      pageSize: 10
    })
    let appNameOption = reactive({
      data: []
    })
    let certDeptOption = reactive({
      data: []
    })
    let terminalDeptOption = reactive({
      data: []
    })
    const shortcuts = [{
      text: '最近一周',
      value: (() => {
        const end = new Date()
        const start = new Date()
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
        return [start, end]
      })(),
    }, {
      text: '最近一个月',
      value: (() => {
        const end = new Date()
        const start = new Date()
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
        return [start, end]
      })(),
    }, {
      text: '最近三个月',
      value: (() => {
        const end = new Date()
        const start = new Date()
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
        return [start, end]
      })(),
    }, {
      text: '今年以来',
      value: (() => {
        let end = new Date()
        let start = new Date()
        start.setMonth(0)
        start.setDate(1)
        return [start, end]
      })(),
    }]

    // 日期选择器配置
    const defaultTime = [
      new Date(2000, 1, 1, 0, 0, 0),
      new Date(2000, 2, 1, 23, 59, 59)
    ]

    let expands = reactive({
      data: []
    })

    function getLogList() {
      loading.value = true
      postLogList(searchForm).then(res => {
        if (res.code === 200) {
          loading.value = false
          tableData.data = res.data.content
          files_count.value = res.data.totalElements
        }
      })
    }


    function getDict() {
      getAppList().then(res => {
        // console.log('app', res)
        if (res.code === 200) {
          appNameOption.data = res.data
        }
      })
      getCertDepts().then(res => {
        if (res.code === 200) {
          certDeptOption.data = res.data
        }
      })
      getTerminalDepts().then(res => {
        if (res.code === 200) {
          terminalDeptOption.data = res.data
        }
      })
    }


    function rowKey(row) {
      return row.id

    }

    function expandChange(row, expandedRow) {
      // console.log('row', row)
      console.log('expandedRow', expandedRow)
      getLogDetail({
        logId: row.id
      }).then(res => {
        if (res.code === 200) {
          for (let item of tableData.data) {
            if (item.id === row.id) {
              item.logData = res.data.children
              item.ansText = res.data.ansText
              item.ansTextCount = 0
              item.isRequests = false
              item.isPhoto = false
              if (expands.data.indexOf(item.id) < 0) {
                expands.data.push(item.id)
              } else {
                expands.data.splice(expands.data.indexOf(item.id), 1)
              }
              console.log(expands)
            }
          }
        }
      })

    }

    function handleCurrentChange(val) {
      searchForm.page = val
      getLogList()
    }

    function handleSizeChange(val) {
      searchForm.pageSize = val
      searchForm.page = 1
      getLogList()
    }

    function handleSearch() {
      // console.log(searchForm)
      searchForm.page = 1
      getLogList()
    }

    function changeTime(val) {
      if (val.updateTime) {
        return moment(val.updateTime).format("YYYY-MM-DD HH:mm:ss")
      } else {
        return ""
      }
    }

    function checkPhoto(id) {
      for (let item of tableData.data) {
        if (item.id === id) {
          console.log('checkPhoto', item)
          item.isPhoto = true
        }
      }
    }

    watch(() => dtRange.data, (newVal, oldVal) => {
      searchForm.start = newVal[0]
      searchForm.end = newVal[1]
    })

    onMounted(() => {
      getDict()
      getLogList()
    })

    return {
      searchForm,
      page,
      pageSize,
      multipleTable,
      files_count,
      loading,
      tableData,
      appNameOption,
      certDeptOption,
      terminalDeptOption,
      dtRange,
      shortcuts,
      defaultTime,
      handleCurrentChange,
      handleSizeChange,
      handleSearch,
      changeTime,
      expandChange,
      moment,
      rowKey,
      expands,
      checkPhoto
    }

  }
}
</script>

<style scoped lang="scss">
.el-main {
  background-color: white;
  margin: 15px 20px 15px 20px;
  padding: 15px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);

}

.el-divider--horizontal {
  margin: 15px 0;
}

.search {
  height: 130px;

  ul {
    list-style: none;
    padding: 0px;
    margin: 0px;

    li {
      float: left;
      width: calc(33% - 10px);
      margin-right: 10px;
      height: 40px;
      margin-bottom: 5px;
      line-height: 40px;

      .label {
        width: 80px;
        line-height: 40px;
        height: 40px;
        float: left;
        text-align: right;
        color: #383838;
        font-size: 13px;
      }

      .item {
        float: left;
        margin-left: 10px;
        width: calc(100% - 90px);
        text-align: left;


        :deep(.el-range-editor.el-input__inner) {
          width: 100%;

          .el-range-separator {
            line-height: 22px;
          }

        }


        :deep(.el-select) {
          width: 100%;

        }

        :deep(.el-input__inner) {
          height: 30px;
          line-height: 30px;

        }


      }
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
</style>