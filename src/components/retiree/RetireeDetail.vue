<template>
  <div class="dialogDetail">
    <el-dialog v-model="retireeDetailVisible" :before-close="closeDialog" :width="1050" :close-on-click-modal="false">

      <el-tabs type="card">

        <el-tab-pane label="基本信息">

          <el-row>
            <el-col :span="18">
              <el-row>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="姓名">
                      <span>{{ detailData.data.name }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="性别">
                      <span>{{ detailData.data.gender }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="出生年月">
                      <span>{{ formatDate(detailData.data.birth) }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>
              <el-divider></el-divider>
              <el-row>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="年龄">
                      <span>{{ detailData.data.age }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="身份证号码">
                      <span>{{ detailData.data.idCard }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="退休时间">
                      <span>{{ formatDate(detailData.data.retireDate) }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>
              <el-divider></el-divider>
              <el-row>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="所属党支部">
                      <span>{{ detailData.data.party }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="政治面貌">
                      <span>{{ detailData.data.politicalIdentity }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="入党时间">
                      <span>{{ formatDate(detailData.data.joinPartyDate) }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>


              </el-row>
              <el-divider></el-divider>
              <el-row>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="党龄">
                      <span>{{ detailData.data.partyAge }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="党组织去向">
                      <span>{{ detailData.data.stayHere === true ? "分局" : detailData.data.stayHere === false ? "非分局" :
                          ""
                      }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="参加工作时间">
                      <span>{{ formatDate(detailData.data.workDate) }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>

              </el-row>
            </el-col>
            <el-col :span="6">
              <el-image style="width:131px;padding-left: 50px;height: 174px;" :src="detailData.data.pic" :fit="'fill'">
              </el-image>
            </el-col>
          </el-row>
          <el-divider></el-divider>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="工龄">
                  <span>{{ detailData.data.policeAge }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">


                <el-tooltip effect="customized" placement="top-start">
                  <template #content>
                    <p class="pop-content">
                      原所在单位：退休人员退休时原部门所属现“三定”部门名称。因机构改革、更名等原因<br />退休人员退休时原部门现在不存在、更名等的，按原部门业务延续或者关联原则确定现为“三定”部门名称。
                    </p>
                  </template>

                  <el-form-item label="原所在单位">
                    <span>{{ detailData.data.deptName }}</span>
                  </el-form-item>
                </el-tooltip>

              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">

                <el-tooltip effect="customized" placement="top-start">
                  <template #content>
                    <p class="pop-content">
                      原职务：曾担任领导的为退出领导岗位时的原职务，未担任领导的为员工。 </p>
                  </template>
                  <el-form-item label="原职务">
                    <span>{{ detailData.data.post }}</span>
                  </el-form-item>
                </el-tooltip>


              </el-form>
            </el-col>

            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">


                <el-tooltip effect="customized" placement="top-start">
                  <template #content>
                    <p class="pop-content">
                      原职级：为退休人员退休时的原职级。</p>
                  </template>
                  <el-form-item label="原职级">
                    <span>{{ detailData.data.level }}</span>
                  </el-form-item>
                </el-tooltip>

              </el-form>
            </el-col>

          </el-row>
          <el-divider></el-divider>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="短号">
                  <span>{{ detailData.data.shortTel }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="手机">
                  <span>{{ detailData.data.tel }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="家属联系号码">
                  <span>{{ detailData.data.activities }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="实际居住地">
                  <span>{{ detailData.data.residenceAddr }}</span>
                </el-form-item>
              </el-form>
            </el-col>

          </el-row>
          <el-divider></el-divider>

          <el-row>

            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="居住类型">
                  <span>{{ detailData.data.residenceType }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="所属社区">
                  <span>{{ detailData.data.community }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">

                <el-tooltip effect="customized" placement="top-start">
                  <template #content>
                    <p class="pop-content">
                      居住地辖区：为退休人员现长期居住在该市级地区内的划分区域。 </p>
                  </template>
                  <el-form-item label="辖区居住地">
                    <span>{{ detailData.data.policeStation }}</span>
                  </el-form-item>
                </el-tooltip>

              </el-form>
            </el-col>

            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="社区负责人">
                  <span>{{ detailData.data.police }}</span>
                </el-form-item>
              </el-form>
            </el-col>

          </el-row>
          <el-divider></el-divider>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="社区联系电话">
                  <span>{{ detailData.data.policeTel }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="银耀指数">

                  <span class="star-style" v-if="detailData.data.activeRate == 0.5">
                    <img src="../../assets/images/star-half.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 1">
                    <img src="../../assets/images/star-all.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 1.5">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-half.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 2">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 2.5">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-half.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 3">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 3.5">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-half.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 4">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 4.5">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-half.png" />
                  </span>
                  <span class="star-style" v-else-if="detailData.data.activeRate === 5">
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                    <img src="../../assets/images/star-all.png" />
                  </span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="文体爱好">
                  <span>{{ detailData.data.activities }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="健康状况">
                  <span>{{ detailData.data.healthStatus }}</span>
                </el-form-item>
              </el-form>
            </el-col>

          </el-row>
          <el-divider></el-divider>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="重大疾病">
                  <span>{{ detailData.data.disease }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="生活困难">
                  <span>{{
                      detailData.data.isLifeHard === true ? "是" : detailData.data.isLifeHard === false ? "否" : ""
                  }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="孤寡老人">
                  <span>{{
                      detailData.data.isLonelyOldMan === true ? "是" : detailData.data.isLonelyOldMan === false ? "否" : ""
                  }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="银行卡号">
                  <span>{{ detailData.data.pensionBankCard }}</span>
                </el-form-item>
              </el-form>
            </el-col>

          </el-row>

          <el-divider></el-divider>
          <el-row>


            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="历史归档">
                  <span>{{
                      detailData.data.isHistory === true ? "是" : detailData.data.isHistory === false ? "否" : ""
                  }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="银耀分数">
                  <span>{{ detailData.data.score }}</span>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="12">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="备注信息">
                  <span>{{ detailData.data.remark }}</span>
                </el-form-item>
              </el-form>
            </el-col>

          </el-row>
        </el-tab-pane>
        <el-tab-pane label="个人简历">
          <el-row>
            <el-col :span="24">
              <div id="printDom1">
                <el-table :data="detailData.data.resumeList" style="width:100%" max-height="500" size="small">
                  <el-table-column type="index" label="序号" width="80">
                  </el-table-column>
                  <el-table-column prop="startTime" label="起始时间" width="150">
                  </el-table-column>
                  <el-table-column prop="endTime" label="截止时间" width="150">
                  </el-table-column>
                  <el-table-column prop="deptName" label="所在单位">
                  </el-table-column>

                </el-table>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="获奖情况">
          <el-row>
            <el-col :span="24">
              <el-table :data="detailData.data.rewardList" style="width: 100%" stripe max-height="500" size="small">
                <el-table-column type="index" label="序号" width="50"></el-table-column>
                <el-table-column prop="rewardType" label="奖励类型" key="rewardType"></el-table-column>
                <el-table-column prop="dept" label="单位" key="dept"></el-table-column>
                <el-table-column prop="rewardDate" label="获奖时间" key="rewardDate"></el-table-column>


              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>
        <!-- <el-tab-pane label="疫苗接种">
          <el-row>
            <el-col :span="24">
              <el-table :data="detailData.data.vaccinationList" style="width:100%" max-height="480" size="small">
                <el-table-column type="index" label="序号" width="80">
                </el-table-column>
                <el-table-column prop="inoculateObj" label="关系人" width="150">
                </el-table-column>
                <el-table-column prop="batchNo" label="接种针数" width="150">
                </el-table-column>
                <el-table-column prop="type" label="疫苗类型" width="150">
                </el-table-column>
                <el-table-column prop="inoculateAddr" label="接种地点">
                </el-table-column>
                <el-table-column prop="inoculateAt" label="接种时间" width="150" :formatter="formatTime">
                </el-table-column>

              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane> -->
        <el-tab-pane label="集体活动">
          <el-row>
            <el-col :span="24">
              <el-table :data="detailData.data.activitiesList" style="width:100%" max-height="480" size="small">
                <el-table-column type="index" label="序号" width="80">
                </el-table-column>
                <el-table-column prop="eventDate" label="活动日期" width="150" :formatter="formatTime">
                </el-table-column>
                <el-table-column prop="addr" label="活动地点" width="150">
                </el-table-column>
                <el-table-column prop="name" label="活动名称">
                </el-table-column>

              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="走访座谈">
          <el-row>
            <el-col :span="24">
              <el-table :data="detailData.data.visitList" style="width:100%" max-height="500" size="small">
                <el-table-column type="index" label="序号" width="80">
                </el-table-column>
                <el-table-column prop="visitDate" label="走访时间" width="150" :formatter="formatTime">
                </el-table-column>
                <el-table-column prop="visitDept" label="走访单位" width="150">
                </el-table-column>
                <el-table-column prop="addr" label="走访地点">
                </el-table-column>
                <el-table-column prop="content" label="内容">
                </el-table-column>

              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="其他">
          <el-row>
            <el-col :span="24">
              <el-table :data="detailData.data.remarkList" style="width:100%" max-height="500" size="small">
                <el-table-column type="index" label="序号" width="80">
                </el-table-column>
                <el-table-column prop="eventDate" label="时间" width="150" :formatter="formatTime">
                </el-table-column>
                <el-table-column prop="addr" label="地点" width="150">
                </el-table-column>
                <el-table-column prop="content" label="内容">
                </el-table-column>

              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>

      <el-button type="primary" @click="printCustom">打印</el-button>
    </el-dialog>
    <print-cum v-show="printShow"></print-cum>
  </div>
</template>

<script>
import { ref, reactive, inject, provide, nextTick, onMounted } from 'vue'
import bus from "@/bus"
import moment from "moment"
import printCum from "@/components/retiree/Print"

export default {
  name: "RetireeDetail",
  components: { printCum },
  setup() {
    let detailData = reactive({
      data: {}
    })
    let printShow = ref(false)


    let retireeDetailVisible = inject('retireeDetailVisible')

    function printCustom() {
      printShow.value = true
      bus.$emit('printCustom', detailData.data)
    }

    function closeDialog() {
      retireeDetailVisible.value = false
    }


    bus.$on('retireeDetailDialog', (val) => {
      detailData.data = val
      detailData.data.resumeList.map(item => {
        item.startTime = item.startTime != null ? moment(item.startTime).format("YYYY-MM-DD") : ""
        item.endTime = item.endTime != null ? moment(item.endTime).format("YYYY-MM-DD") : ""
        return item
      })

      detailData.data.rewardList.map(item => {
        item.rewardDate = moment(item.rewardDate).format("YYYY-MM-DD")
        return item
      })

    })

    function formatDate(val) {
      if (val) {
        return moment(val).format("YYYY-MM-DD")
      } else {
        return ""
      }
    }

    function formatTime(row, column, cellValue) {
      return cellValue != null ? moment(cellValue).format("YYYY-MM-DD") : ""
    }


    return { retireeDetailVisible, detailData, closeDialog, formatDate, formatTime, printCustom, printShow }
  }
}
</script>

<style scoped lang="scss">
.pop-content {
  font-size: 14px;
  font-weight: bold;
}

.dialogDetail {

  .star-style {
    img {
      width: 20px;

    }
  }

  .family-ul {
    list-style: none;
    padding: 0px;
    margin: 0px;
    width: 600px;

    li:first-child {
      color: $tree-father-node-color;
      font-size: 16px;
      margin-bottom: 10px;
    }

    li {
      width: 100%;
      font-size: 14px;
      margin-bottom: 5px;
      margin-top: 5px;
    }
  }

  .el-dialog {
    width: 80% !important;
  }

  :deep(.el-form-item__label) {
    width: 85px !important;
    padding: 0px;
  }

  .el-form--inline .el-form-item__label {
    color: $font-color-theme;
  }

  .el-form-item {
    margin: 0px
  }

  .el-divider--horizontal {
    margin: 3px 0px 3px 0px !important;
  }

  .el-table td {
    border-bottom: 1px solid #eaeaea;
  }

  :deep(.el-tabs__item) {
    font-size: 16px !important;
  }

  :deep(.el-tabs__content) {
    overflow-y: auto;
    height: 500px;
  }

  :deep(.demo-table-expand) {
    font-size: 0;

    label {
      width: 100px;
      color: $tree-father-node-color;
      font-size: 14px;
    }

    .el-form-item {
      margin-left: 0;
      margin-bottom: 0;
      width: 100%;

      .el-form-item__content {
        font-size: 14px;
        line-height: inherit;
        display: flex;
        align-items: center;
        padding: 0px 5px 0px 0px;
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

.config-span-list {
  font-size: 14px;
  color: #57a0bd
}
</style>