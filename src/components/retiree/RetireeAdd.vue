<template>
  <div class="dialogAdd">
    <el-dialog v-model="retireeAddVisible" :before-close="closeDialog" :close-on-click-modal="false" :width="1200">
      <el-tabs type="card">
        <el-tab-pane ref="infoRef" label="基本信息">
          <el-row>
            <el-col :span="18">
              <el-row>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="姓名">
                      <el-input v-model="formData.data.name"></el-input>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="性别">
                      <el-radio-group v-model="formData.data.gender">
                        <el-radio style="width: 30px;" label="男"></el-radio>
                        <el-radio style="width: 30px;" label="女"></el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="退休时间">
                      <el-date-picker v-model="formData.data.retireDate"></el-date-picker>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>
              <el-row>

                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="所属党支部">
                      <el-input class="custom-input" v-model="formData.data.party">
                      </el-input>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="政治面貌">
                      <el-input v-model="formData.data.politicalIdentity"></el-input>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="入党时间">
                      <el-date-picker v-model="formData.data.joinPartyDate"></el-date-picker>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>

              <el-row>

                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="原所在单位">
                      <el-select v-model="formData.data.deptName" @change="deptSelectChange" filterable>
                        <el-option v-for="(item, index) in deptList.data" :value="item.deptName" :label="item.deptName"
                          :key="index" />
                      </el-select>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="原职级">
                      <el-input v-model="formData.data.level"></el-input>
                    </el-form-item>
                  </el-form>
                </el-col>

                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="原职务">
                      <el-input v-model="formData.data.post"></el-input>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="党组织去向">
                      <el-radio-group v-model="formData.data.stayHere">
                        <el-radio style="width: 30px;" :label="true">分局</el-radio>
                        <el-radio style="width: 30px;" :label="false">非分局</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="参加工作时间">
                      <el-date-picker v-model="formData.data.workDate"></el-date-picker>
                    </el-form-item>
                  </el-form>
                </el-col>

                <el-col :span="8">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="短号">
                      <el-input v-model="formData.data.shortTel"></el-input>

                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>


            </el-col>
            <el-col :span="6">
              <el-upload class="avatar-uploader" :action="uploadApi + '/policeInfo/retire/uploadPic'"
                :show-file-list="false" :on-success="handleSuccess" ref="file">
                <img v-if="formData.data.pic" :src="formData.data.pic" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-col>
          </el-row>

          <el-row>

            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="手机号码">
                  <el-input class="custom-input" v-model="formData.data.tel"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="家属联系方式">
                  <el-input class="custom-input" v-model="formData.data.familyTel"></el-input>

                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="实际居住地">
                  <el-input class="custom-input" v-model="formData.data.residenceAddr"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="居住类型">
                  <el-select v-model="formData.data.residenceType" placeholder="请选择">
                    <el-option key="residenct1" :label="'区内'" value="区内"></el-option>
                    <el-option key="residenct1" :label="'区外市内'" value="区外市内"></el-option>
                    <el-option key="residenct1" :label="'市外省内'" value="市外省内"></el-option>
                    <el-option key="residenct1" :label="'省外'" value="省外"></el-option>
                    <el-option key="residenct1" :label="'国外'" value="国外"></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </el-col>

          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="居住辖区">
                  <el-select v-model="formData.data.policeStation" @change="policeStationChange" filterable>
                    <el-option v-for="(item, index) in policeStationList.data" :value="item.name" :label="item.name"
                      :key="index" />
                  </el-select>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="所属社区">
                  <el-input class="custom-input" v-model="formData.data.community"></el-input>

                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="社区负责人">
                  <el-input class="custom-input" v-model="formData.data.police"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="社区联系电话">
                  <el-input class="custom-input" v-model="formData.data.policeTel"></el-input>

                </el-form-item>
              </el-form>
            </el-col>



          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="生活困难">
                  <el-radio-group v-model="formData.data.isLifeHard">
                    <el-radio style="width: 30px;" :label="true">是</el-radio>
                    <el-radio style="width: 30px;" :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="孤寡老人">
                  <el-radio-group v-model="formData.data.isLonelyOldMan">
                    <el-radio style="width: 30px;" :label="true">是</el-radio>
                    <el-radio style="width: 30px;" :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="文体爱好">
                  <el-input v-model="formData.data.activities"></el-input>
                </el-form-item>
              </el-form>
            </el-col>

            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="备注信息">
                  <el-input v-model="formData.data.remark"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="身份证">
                  <el-input v-model="formData.data.idCard"></el-input>
                </el-form-item>
              </el-form>
            </el-col>

            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="银行卡号">
                  <el-input v-model="formData.data.pensionBankCard"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="健康状况">
                  <el-input v-model="formData.data.healthStatus"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="疾病">
                  <el-input v-model="formData.data.disease"></el-input>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="历史归档">
                  <el-radio-group v-model="formData.data.isHistory">
                    <el-radio style="width: 30px;" :label="true">是</el-radio>
                    <el-radio style="width: 30px;" :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="活跃指数">
                  <el-select v-model="formData.data.activeRate" filterable>
                    <el-option value="0" label="0颗星" key="0" />
                    <el-option value="1" label="1颗星" key="0" />
                    <el-option value="2" label="2颗星" key="0" />
                    <el-option value="3" label="3颗星" key="0" />
                    <el-option value="4" label="4颗星" key="0" />
                    <el-option value="5" label="5颗星" key="0" />
                  </el-select>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="银耀分数">
                  <el-input v-model="formData.data.score"></el-input>
                </el-form-item>
              </el-form>
            </el-col>



          </el-row>

          <!-- <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="家庭情况">
                  <el-input v-model="formData.data.familyCondition"></el-input>
                </el-form-item>
              </el-form>
            </el-col> -->

          <!-- <el-col :span="6">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="薪火结对">
                  <el-radio-group v-model="formData.data.isMasterAndApprentice">
                    <el-radio style="width: 30px;" :label="true">是</el-radio>
                    <el-radio style="width: 30px;" :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </el-col> -->


        </el-tab-pane>
        <el-tab-pane ref="resumeRef" label="个人简历">
          <el-row>
            <el-col :span="24">
              <ul class="resume-ul">
                <li>
                  <el-row>
                    <el-col :span="3">序号</el-col>
                    <el-col :span="6">起始日期</el-col>
                    <el-col :span="6">结束日期</el-col>
                    <el-col :span="6">所在单位</el-col>
                    <el-col :span="3">
                      <el-button @click="addResume" type="text" size="small">新增</el-button>
                    </el-col>
                  </el-row>
                </li>
                <li v-for="(item, index) in resumeList">
                  <el-row>
                    <el-col :span="3">
                      {{ index + 1 }}
                    </el-col>
                    <el-col :span="6">
                      <el-date-picker v-model="item.startTime"></el-date-picker>
                    </el-col>
                    <el-col :span="6">
                      <el-date-picker v-model="item.endTime"></el-date-picker>
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.deptName" />
                    </el-col>
                    <el-col :span="3">
                      <el-button @click="deleteResume(index)" type="text" size="small">删除</el-button>
                    </el-col>
                  </el-row>
                </li>

              </ul>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane ref="rewardRef" label="获奖情况">
          <el-row>
            <el-col :span="24">
              <ul class="resume-ul">
                <li>
                  <el-row>
                    <el-col :span="3">序号</el-col>
                    <el-col :span="6">奖励类型</el-col>
                    <el-col :span="6">单位</el-col>
                    <el-col :span="6">获奖时间</el-col>
                    <el-col :span="3">
                      <el-button @click="addReward" type="text" size="small">新增</el-button>
                    </el-col>
                  </el-row>
                </li>
                <li v-for="(item, index) in rewardList">
                  <el-row>
                    <el-col :span="3">
                      {{ index + 1 }}
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.rewardType" />
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.dept" />
                    </el-col>
                    <el-col :span="6">
                      <el-date-picker v-model="item.rewardDate"></el-date-picker>
                    </el-col>
                    <el-col :span="3">
                      <el-button @click="deleteReward(index)" type="text" size="small">删除</el-button>
                    </el-col>
                  </el-row>
                </li>
              </ul>
            </el-col>
          </el-row>
        </el-tab-pane>
        <!-- <el-tab-pane ref="rewardRef" label="疫苗接种">
          <el-row>
            <el-col :span="24">
              <ul class="resume-ul">
                <li>
                  <el-row>
                    <el-col :span="1">序号</el-col>
                    <el-col :span="4">关系人</el-col>
                    <el-col :span="4">接种针数</el-col>
                    <el-col :span="4">疫苗类型</el-col>
                    <el-col :span="5">接种地点</el-col>
                    <el-col :span="4">接种时间</el-col>
                    <el-col :span="2">
                      <el-button @click="addVaccinationRecords" type="text" size="small">新增</el-button>
                    </el-col>
                  </el-row>
                </li>
                <li v-for="(item, index) in formData.data.vaccinationRecords">
                  <el-row>
                    <el-col :span="1">
                      {{ index + 1 }}
                    </el-col>
                    <el-col :span="4">
                      <el-input v-model="item.inoculateObj" />
                    </el-col>
                    <el-col :span="4">
                      <el-input v-model="item.batchNo" />
                    </el-col>
                    <el-col :span="4">
                      <el-input v-model="item.type" />
                    </el-col>
                    <el-col :span="5">
                      <el-input v-model="item.inoculateAddr" />
                    </el-col>
                    <el-col :span="4">
                      <el-date-picker v-model="item.inoculateAt"></el-date-picker>
                    </el-col>
                    <el-col :span="2">
                      <el-button @click="deleteVaccinationRecords(index)" type="text" size="small">删除</el-button>
                    </el-col>
                  </el-row>
                </li>
              </ul>
            </el-col>
          </el-row>
        </el-tab-pane> -->
        <el-tab-pane ref="rewardRef" label="集体活动">
          <el-row>
            <el-col :span="24">
              <ul class="resume-ul">
                <li>
                  <el-row>
                    <el-col :span="3">序号</el-col>
                    <el-col :span="6">活动名称</el-col>
                    <el-col :span="6">活动地点</el-col>
                    <el-col :span="6">活动日期</el-col>

                    <el-col :span="3">
                      <el-button @click="addActivitiesRecords" type="text" size="small">新增</el-button>
                    </el-col>
                  </el-row>
                </li>
                <li v-for="(item, index) in formData.data.activitiesRecords">
                  <el-row>
                    <el-col :span="3">
                      {{ index + 1 }}
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.name" />
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.addr" />
                    </el-col>
                    <el-col :span="6">
                      <el-date-picker v-model="item.eventDate"></el-date-picker>
                    </el-col>
                    <el-col :span="3">
                      <el-button @click="deleteActivitiesRecords(index)" type="text" size="small">删除</el-button>
                    </el-col>
                  </el-row>
                </li>
              </ul>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane ref="rewardRef" label="走访座谈">
          <el-row>
            <el-col :span="24">
              <ul class="resume-ul">
                <li>
                  <el-row>
                    <el-col :span="3">序号</el-col>
                    <el-col :span="4">走访单位</el-col>
                    <el-col :span="5">走访地点</el-col>
                    <el-col :span="5">内容</el-col>
                    <el-col :span="4">走访时间</el-col>
                    <el-col :span="3">
                      <el-button @click="addVisitRecords" type="text" size="small">新增</el-button>
                    </el-col>
                  </el-row>
                </li>
                <li v-for="(item, index) in formData.data.visitRecords">
                  <el-row>
                    <el-col :span="3">
                      {{ index + 1 }}
                    </el-col>
                    <el-col :span="4">
                      <el-input v-model="item.visitDept" />
                    </el-col>
                    <el-col :span="5">
                      <el-input v-model="item.addr" />
                    </el-col>
                    <el-col :span="5">
                      <el-input v-model="item.content" />
                    </el-col>
                    <el-col :span="4">
                      <el-date-picker v-model="item.visitDate"></el-date-picker>
                    </el-col>
                    <el-col :span="3">
                      <el-button @click="deleteVisitRecords(index)" type="text" size="small">删除</el-button>
                    </el-col>
                  </el-row>
                </li>
              </ul>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane ref="rewardRef" label="其他">
          <el-row>
            <el-col :span="24">
              <ul class="resume-ul">
                <li>
                  <el-row>
                    <el-col :span="3">序号</el-col>
                    <el-col :span="6">地点</el-col>
                    <el-col :span="6">内容</el-col>
                    <el-col :span="6">时间</el-col>
                    <el-col :span="3">
                      <el-button @click="addRemarkRecords" type="text" size="small">新增</el-button>
                    </el-col>
                  </el-row>
                </li>
                <li v-for="(item, index) in formData.data.remarkRecords">
                  <el-row>
                    <el-col :span="3">
                      {{ index + 1 }}
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.addr" />
                    </el-col>
                    <el-col :span="6">
                      <el-input v-model="item.content" />
                    </el-col>
                    <el-col :span="6">
                      <el-date-picker v-model="item.eventDate" :key="'remark' + index"></el-date-picker>
                    </el-col>
                    <el-col :span="3">
                      <el-button @click="deleteRemarkRecords(index)" type="text" size="small">删除</el-button>
                    </el-col>
                  </el-row>
                </li>
              </ul>
            </el-col>
          </el-row>
        </el-tab-pane>

      </el-tabs>
      <template #footer>
        <div class="demo-drawer__footer">
          <el-button class="el-button--custom" @click="submitAdd">确定</el-button>
          <el-button class="el-button-white" @click="cancelForm">取消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { ref, reactive, inject, nextTick, onMounted, watch } from 'vue'
import bus from "@/bus"
import { postRetireAdd, postRewardAdd, postResumeAdd, postRetireNewAdd, postRetireExtraAdd } from "@/api/retiree"
import { getDeptList, getPoliceStationList } from "@/api/user"

import { ElMessage } from 'element-plus'
import moment from "moment"

export default {
  name: "RetireeAdd",
  setup() {

    let type = inject("type")

    let data = () => {
      return {
        data: {
          id: "",
          activeRate: "",
          activities: "",
          community: "",
          deptId: "",
          deptName: "",
          disease: "",
          vaccinationRecords: [
            {
              inoculateObj: "",
              batchNo: "",
              type: "",
              inoculateAddr: "",
              inoculateAt: ""
            }
          ],
          familyMembers: [
            {
              name: "",
              relation: "",
              tel: "",
              wechat: ""
            }
          ],
          activitiesRecords: [
            {
              addr: "",
              eventDate: "",
              name: ""
            }
          ],
          remarkRecords: [
            {
              addr: "",
              eventDate: "",
              content: ""
            }
          ],
          visitRecords: [
            {
              addr: "",
              content: "",
              visitDate: "",
              visitDept: ""
            }
          ],
          residenceType: "",
          stayHere: true,
          workDate: null,
          fixedTel: "",
          gender: "男",
          healthStatus: "",
          idCard: "",
          isAlive: true,
          isLifeHard: false,
          isLonelyOldMan: false,
          isMasterAndApprentice: false,
          isHistory: false,
          joinPartyDate: "",
          level: "",
          familyTel: "",
          moveInAt: null,
          moveOutAt: null,
          moveOutDept: "",
          name: "",
          party: "",
          pensionBankCard: "",
          pic: "",
          police: "",
          policeCode: "",
          policeStation: "",
          policeStationId: 0,
          policeTel: "",
          politicalIdentity: "",
          post: "",
          remark: "",
          residenceAddr: "",
          retireDate: "",
          shortTel: "",
          tel: "",
          score: null
        }
      }
    }

    let formData = reactive(data())

    let uploadApi = process.env.VUE_APP_BASE_URL
    let resumeList = ref([{
      startTime: '',
      endTime: '',
      deptName: ''
    }])
    let rewardList = ref([{
      approveAddress: "",
      approveLevel: "",
      codeNumber: "",
      dept: "",
      id: 0,
      linkAddress: "",
      rewardDate: "",
      rewardType: "",
      types: ""
    }])

    const infoRef = ref(null)
    const resumeRef = ref(null)
    const rewardRef = ref(null)

    let retireeAddVisible = inject('retireeAddVisible')

    function closeDialog() {
      retireeAddVisible.value = false
    }

    bus.$on('retireeDetailDialog', (val) => {

    })

    bus.$on('retireeAddDialog', (val) => {
      Object.assign(formData, data())
      rewardList.value = [{
        approveAddress: "",
        approveLevel: "",
        codeNumber: "",
        dept: "",
        id: 0,
        linkAddress: "",
        rewardDate: "",
        rewardType: "",
        types: ""
      }]
      resumeList.value = [{
        startTime: '',
        endTime: '',
        deptName: ''
      }]
    })

    function formatDateTime(par) {
      return par === null || par === "" ? "" : moment(par).format("YYYY-MM-DD")
    }


    bus.$on('retireeEditDialog', (val) => {

      formData.data.deptId = ""
      for (let item of deptList.data) {
        if (item.deptName == val[0].deptName) {
          formData.data.deptId = item.deptId
        }
      }

      formData.data.policeStationId = ""
      for (let item of policeStationList.data) {
        if (item.name == val[0].policeStation) {
          formData.data.policeStationId = item.id
        }
      }

      val[0].joinPartyDate = formatDateTime(val[0].joinPartyDate)
      val[0].retireDate = formatDateTime(val[0].retireDate)
      val[0].moveInAt = formatDateTime(val[0].moveInAt)
      val[0].moveOutAt = formatDateTime(val[0].moveOutAt)
      val[0].workDate = formatDateTime(val[0].workDate)

      val[0].resumeList.map(item => {
        item.startTime = formatDateTime(item.startTime)
        item.endTime = formatDateTime(item.endTime)
        return item
      })
      val[0].rewardList.map(item => {
        item.rewardDate = formatDateTime(item.rewardDate)
        return item
      })
      val[0].remarkList.map(item => {
        item.eventDate = formatDateTime(item.eventDate)
        return item
      })
      val[0].visitList.map(item => {
        item.visitDate = formatDateTime(item.visitDate)
        return item
      })
      val[0].vaccinationList.map(item => {
        item.inoculateAt = formatDateTime(item.inoculateAt)
        return item
      })

      val[0].activitiesList.map(item => {
        item.eventDate = formatDateTime(item.eventDate)
        return item
      })

      formData.data = val[0]
      resumeList.value = formData.data.resumeList
      rewardList.value = formData.data.rewardList
      formData.data.remarkRecords = formData.data.remarkList
      formData.data.visitRecords = formData.data.visitList
      formData.data.vaccinationRecords = formData.data.vaccinationList
      formData.data.activitiesRecords = formData.data.activitiesList


    })

    function handleSuccess(res, file) {
      if (res.code === 200) {
        formData.data.pic = res.data
      }
    }

    //第三方联系人添加删除
    function addFamily() {
      formData.data.familyMembers.unshift({
        name: "",
        relation: "",
        tel: "",
        wechat: ""
      })
    }

    function deleteFamily(index) {
      formData.data.familyMembers.splice(index, 1)
    }

    //疫苗添加删除
    function addVaccinationRecords() {
      formData.data.vaccinationRecords.unshift({
        inoculateObj: "",
        batchNo: "",
        type: "",
        inoculateAddr: "",
        inoculateAt: ""
      })
    }

    function deleteVaccinationRecords(index) {
      formData.data.vaccinationRecords.splice(index, 1)
    }

    //老干部活动添加删除
    function addActivitiesRecords() {
      formData.data.activitiesRecords.unshift({
        addr: "",
        eventDate: "",
        name: ""
      })
    }

    function deleteActivitiesRecords(index) {
      formData.data.activitiesRecords.splice(index, 1)
    }

    //其他添加删除
    function addRemarkRecords() {
      formData.data.remarkRecords.unshift({
        addr: "",
        eventDate: "",
        name: ""
      })
    }

    function deleteRemarkRecords(index) {
      formData.data.remarkRecords.splice(index, 1)
    }

    //走访添加删除
    function addVisitRecords() {
      formData.data.visitRecords.unshift({
        addr: "",
        eventDate: "",
        name: ""
      })
    }

    function deleteVisitRecords(index) {
      formData.data.visitRecords.splice(index, 1)
    }

    ///简历添加删除
    function addResume() {
      resumeList.value.unshift({
        startTime: '',
        endTime: '',
        deptName: ''
      })
    }

    function deleteResume(index) {
      resumeList.value.splice(index, 1)
    }


    ///奖励添加删除
    function addReward() {
      rewardList.value.unshift({
        approveAddress: "",
        approveLevel: "",
        codeNumber: "",
        dept: "",
        id: 0,
        linkAddress: "",
        rewardDate: "",
        rewardType: "",
        types: ""
      })
    }

    function deleteReward(index) {
      rewardList.value.splice(index, 1)
    }

    let policeStationList = reactive({
      data: []
    })
    function getPoliceStationListData() {
      getPoliceStationList().then(res => {
        if (res.code == 200) {
          policeStationList.data = res.data
        }
      })
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
    function deptSelectChange(val) {
      formData.data.deptId = ""
      for (let item of deptList.data) {
        if (item.deptName == val) {
          formData.data.deptId = item.deptId
        }
      }
    }
    function policeStationChange(val) {
      formData.data.policeStationId = ""
      for (let item of policeStationList.data) {
        if (item.name == val) {
          formData.data.policeStationId = item.id
        }
      }
    }

    function retireAdd() {
      let successSubmit = true
      let errorMessages = ''
      // for (let item of resumeList.value) {
      //   if (!item.startTime) {
      //     successSubmit = false
      //     errorMessages = '履历开始时间不能为空'
      //   }
      // }
      if (successSubmit === true) {

        if (formData.data.joinPartyDate == "") {
          formData.data.joinPartyDate = null
        }
        if (formData.data.moveInAt == "") {
          formData.data.moveInAt = null
        }
        if (formData.data.moveOutAt == "") {
          formData.data.moveOutAt = null
        }
        if (formData.data.retireDate == "") {
          formData.data.retireDate = null
        }

        if (type === "list") {
          postRetireAdd(formData.data).then(res => {
            if (res.code === 200) {
              retireeAddVisible.value = false
              if (formData.data.id) {
                ElMessage.success('修改成功！')
                resumeAdd(formData.data.id)
              } else {
                ElMessage.success('新增成功！')
                resumeAdd(res.data)
              }
              bus.$emit('refreshTableData')
            } else {
              ElMessage.error(res.message)
            }
          }).catch(err => {
            ElMessage.error(err)
          })
        } else if (type === "new") {
          postRetireNewAdd(formData.data).then(res => {
            if (res.code === 200) {
              retireeAddVisible.value = false
              if (formData.data.id) {
                ElMessage.success('修改成功！')
                resumeAdd(formData.data.id)
              } else {
                ElMessage.success('新增成功！')
                resumeAdd(res.data)
              }
              bus.$emit('refreshTableData')
            } else {
              ElMessage.error(res.message)
            }
          }).catch(err => {
            ElMessage.error(err)
          })
        } else {
          postRetireExtraAdd(formData.data).then(res => {
            if (res.code === 200) {
              retireeAddVisible.value = false
              if (formData.data.id) {
                ElMessage.success('修改成功！')
                resumeAdd(formData.data.id)
              } else {
                ElMessage.success('新增成功！')
                resumeAdd(res.data)
              }
              bus.$emit('refreshTableData')
            } else {
              ElMessage.error(res.message)
            }
          }).catch(err => {
            ElMessage.error(err)
          })
        }
      } else {
        ElMessage.error(errorMessages)
      }

    }

    function resumeAdd(id) {
      postResumeAdd(id, resumeList.value).then(res => {
        if (res.code === 200) {
          rewardAdd(id)
        }
      }).catch(err => {
      })
    }

    function rewardAdd(id) {
      postRewardAdd(id, rewardList.value).then(res => {
        if (res.code === 200) {
          nextTick(() => {
            bus.$emit('refreshApi')
          })
        }
      }).catch(err => {

      })
    }

    function submitAdd() {
      retireAdd()
    }

    function cancelForm() {
      retireeAddVisible.value = false
    }


    onMounted(() => {
      getDeptListData()
      getPoliceStationListData()
    })
    return {
      retireeAddVisible,
      formData,
      closeDialog,
      resumeList,
      rewardList,
      addFamily,
      deleteFamily,
      addVaccinationRecords,
      deleteVaccinationRecords,
      addActivitiesRecords,
      deleteActivitiesRecords,
      addVisitRecords,
      deleteVisitRecords,
      addResume,
      deleteResume,
      addReward,
      deleteReward,
      addRemarkRecords,
      deleteRemarkRecords,
      submitAdd,
      cancelForm,
      uploadApi,
      handleSuccess,
      infoRef,
      resumeRef,
      rewardRef,
      deptList,
      deptSelectChange,
      policeStationList,
      policeStationChange

    }
  }
}
</script>

<style scoped lang="scss">
.dialogAdd {

  .custom-input {
    :deep(.el-input__inner) {
      background-color: #ffebdd;
    }
  }


  .highPower {
    font-size: 24px;

  }

  :deep(.avatar-uploader .el-upload) {
    border: 1px dashed #9e9e9e;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  :deep(.avatar-uploader .el-upload:hover) {
    border-color: #409EFF;
  }

  :deep(.avatar-uploader-icon) {
    font-size: 28px;
    color: #8c939d;
    width: 131px;
    height: 174px;
    line-height: 185px;
    text-align: center;
  }

  :deep(.avatar) {
    width: 131px;
    height: 174px;
    display: block;
  }


  .el-row {
    margin-bottom: 5px;
  }

  .family-ul {
    list-style: none;
    padding: 0px;
    margin: 0px;
    width: 100%;

    .el-row {
      justify-content: center;
      align-items: center;
    }

    li:first-child {
      color: $tree-father-node-color;
      font-size: 16px;
      margin-bottom: 10px;
    }

    li {
      width: 100%;
      font-size: 14px;
      margin-bottom: 5px;

      :deep(.el-input__inner) {
        width: 180px;
      }

      :deep(.el-input) {
        text-align: center;
      }

    }
  }

  .resume-ul {
    list-style: none;
    padding: 0px;
    margin: 0px;

    .el-row {
      justify-content: center;
      align-items: center;
    }

    li:first-child {
      color: $tree-father-node-color;
      font-size: 16px;
      margin-bottom: 10px;
    }

    li {
      width: 100%;
      font-size: 14px;
      margin-bottom: 5px;

      :deep(.el-input__inner) {
        width: 170px;
      }

      :deep(.el-input--prefix .el-input__inner) {
        padding-left: 10px;
      }

      :deep(.el-input--suffix .el-input__inner) {
        padding-right: 10px;
      }


      :deep(.el-input__prefix) {
        left: 0px;
      }

      :deep(.el-input) {
        text-align: center;
      }

    }
  }


  :deep(.el-select) {
    .el-input__suffix {
      top: -5px !important;
    }
  }

  :deep(.el-input__inner) {
    height: 25px;
    line-height: 25px;
    width: 163px;
  }

  :deep(.el-input) {
    text-align: left;
  }

  :deep(.el-form-item__label) {
    width: 90px !important;
    padding: 0px;
  }

  .el-form--inline .el-form-item__label {
    color: $font-color-theme;
  }

  .el-form-item {
    margin: 0px
  }

  :deep(.el-divider) {
    background-color: #efefef;
  }

  .el-divider--horizontal {
    margin: 3px 0px 3px 0px !important;
  }

  .el-table td {
    border-bottom: 1px solid #eaeaea;
  }

  :deep(.el-tabs__content) {
    overflow-y: auto;
    height: 420px;
  }

  :deep(.el-tabs__item) {
    font-size: 16px !important;
  }


  :deep(.demo-table-expand) {
    font-size: 0;

    label {
      width: 120px;
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