<template>
  <div style="width: 100%; height: 100%">
    <el-dialog v-model="printDialog" :before-close="closeDialog" :width="800" :close-on-click-modal="false">
      <div id="printDom">
        <table class="big-title">
          <tr>
            <td>基本信息</td>
          </tr>
        </table>

        <table class="table2" cellspacing="0" cellpadding="0">
          <tr>
            <td class="title">姓名</td>
            <td>{{ detailData.data.name }}</td>
            <td class="title">性别</td>
            <td>{{ detailData.data.gender }}</td>
            <td colspan="2" rowspan="3">
              <img :src="detailData.data.pic" style="width: 100px" />
            </td>
          </tr>
          <tr>
            <td class="title">出生年月</td>
            <td>{{ formatDate(detailData.data.birth) }}</td>
            <td class="title">年龄</td>
            <td>{{ detailData.data.age }}</td>
          </tr>
          <tr>
            <td class="title">身份证</td>
            <td>{{ detailData.data.idCard }}</td>
            <td class="title">退休时间</td>
            <td>{{ formatDate(detailData.data.retireDate) }}</td>
          </tr>
          <tr>
            <td class="title">所属党支部</td>
            <td>{{ detailData.data.party }}</td>
            <td class="title">政治面貌</td>
            <td>{{ detailData.data.politicalIdentity }}</td>
            <td class="title">入党时间</td>
            <td>{{ formatDate(detailData.data.joinPartyDate) }}</td>
          </tr>

          <tr>
            <td class="title">党龄</td>
            <td>{{ detailData.data.partyAge }}</td>
            <td class="title">组织去向</td>
            <td>{{ detailData.data.stayHere === true ? "分局" : detailData.data.stayHere === false ? "非分局" : "" }}</td>
            <td class="title">参加工作时间</td>
            <td>{{ formatDate(detailData.data.workDate) }}</td>
          </tr>
          <tr>
            <td class="title">工龄</td>
            <td>{{ detailData.data.policeAge }}</td>
            <td class="title">原所在单位</td>
            <td>{{ detailData.data.deptName }}</td>
            <td class="title">原职务</td>
            <td>{{ detailData.data.post }}</td>
          </tr>
          <tr>
            <td class="title">原职级</td>
            <td>{{ detailData.data.level }}</td>
            <td class="title">短号</td>
            <td>{{ detailData.data.shortTel }}</td>
            <td class="title">手机</td>
            <td>{{ detailData.data.tel }}</td>
          </tr>
          <tr>
            <td class="title">家属联系号码</td>
            <td>{{ detailData.data.familyTel }}</td>
            <td class="title">实际居住地</td>
            <td>{{ detailData.data.residenceAddr }}</td>
            <td class="title">居住类型</td>
            <td>{{ detailData.data.residenctType }}</td>
          </tr>
          <tr>
            <td class="title">所属社区</td>
            <td>{{ detailData.data.community }}</td>
            <td class="title">居住辖区</td>
            <td>{{ detailData.data.policeStation }}</td>
            <td class="title">社区负责人</td>
            <td>{{ detailData.data.police }}</td>
          </tr>
          <tr>
            <td class="title">社区联系电话</td>
            <td>{{ detailData.data.policeTel }}</td>
            <td class="title">银耀指数</td>
            <td>
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
            </td>
            <td class="title">文体爱好</td>
            <td colspan="3">{{ detailData.data.activities }}</td>
          </tr>
          <tr>
            <td class="title">健康状况</td>
            <td>{{ detailData.data.healthStatus }}</td>
            <td class="title">重大疾病</td>
            <td>{{ detailData.data.disease }}</td>
            <td class="title">生活困难</td>
            <td>{{ detailData.data.isLifeHard === true ? "是" : detailData.data.isLifeHard === false ? "否" : "" }}</td>
          </tr>
          <tr>
            <td class="title">孤寡老人</td>
            <td>{{ detailData.data.isLonelyOldMan === true ? "是" : detailData.data.isLonelyOldMan === false ? "否" : "" }}</td>
            <td class="title">银行卡号</td>
            <td>{{ detailData.data.pensionBankCard }}</td>

            <td class="title">备注信息</td>
            <td>{{ detailData.data.remark }}</td>
          </tr>
          <tr></tr>
        </table>
        <table class="big-title">
          <tr>
            <td>简历</td>
          </tr>
        </table>
        <table class="table3" cellspacing="0" cellpadding="0">
          <thead v-if="detailData.data.resumeList.length > 0">
            <tr>
              <th>起始时间</th>
              <th>截止时间</th>
              <th>所在单位</th>
            </tr>
          </thead>
          <tr v-if="detailData.data.resumeList.length > 0" v-for="item in detailData.data.resumeList">
            <td>{{ item.startTime }}</td>
            <td>{{ item.endTime }}</td>
            <td>{{ item.deptName }}</td>
          </tr>
          <tr v-if="detailData.data.resumeList.length === 0">
            <td style="border: none !important">无</td>
          </tr>
        </table>
        <div class="print-bottom">
          <div style="width: 70%">内容：__________________________________________________</div>
          <div style="width: 30%">签名栏：_______________</div>
        </div>
      </div>
      5
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, inject, nextTick, onMounted } from "vue"
import bus from "@/bus"
import moment from "moment"
import print from "print-js"

export default {
  name: "printCum",
  setup() {
    let detailData = reactive({
      data: {},
    })
    let printDialog = ref(false)

    bus.$on("printCustom", (val) => {
      detailData.data = val
      console.log(val)
      printDialog.value = true
      nextTick(() => {
        printJS({
          printable: "printDom",
          type: "html",
          header: null,
          targetStyles: ["*"],
          onPrintDialogClose: function () {
            console.log("close")
          },
        })
      })
    })

    function closeDialog() {
      printDialog.value = false
    }

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

    return { detailData, formatDate, formatTime, printDialog, closeDialog }
  },
}
</script>

<style scoped lang="scss">
.star-style {
  img {
    width: 20px;
  }
}

.print-bottom {
  width: 100%;
  height: 30px;
  margin-top: 50px;

  div {
    float: left;
    text-align: left;
    color: #000000;
    font-size: 20px;
  }
}

.big-title {
  width: 100%;
  height: 40px;
  font-size: 24px;
  color: #000;
  font-weight: bold;
}

.table3 {
  width: 100%;
  font-size: 12px;
  color: #000;

  thead {
    font-weight: bold;
    font-size: 14px;

    th {
      height: 40px;
    }
  }

  tr td {
    border-bottom: 1px solid #ececec;
    height: 25px;
  }
}

.table2 {
  width: 100%;
  height: 420px;
  font-size: 12px;
  color: #000;
  border-left: 1px solid #8c8c8c;
  border-top: 1px solid #8c8c8c;
  word-break: normal;

  tr td {
    width: 60px;
    height: 46px;
    border-bottom: 1px solid #8c8c8c;
    border-right: 1px solid #8c8c8c;
    padding: 0px;
    margin: 0px;
  }

  .title {
    font-weight: bold;
  }
}
</style>
