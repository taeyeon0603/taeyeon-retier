<template>
  <div class="screen-container">
    <el-row>
      <el-col :span="24" class="screen-header">
        <div class="ht" @click="goHt">[后台管理]</div>
        <div class="ht" @click="goFirst">[返回首页]</div>


      </el-col>
    </el-row>
    <el-row>
      <el-col :span="7">
        <div class="title-container">
          <div class="title">
            <i class="el-icon-menu customIcon"></i>年龄分布
          </div>
          <div class="container">
            <!-- <line-charts></line-charts> -->
            <pie-charts-age></pie-charts-age>
          </div>
        </div>
        <div class="title-container">
          <div class="title">
            <i class="el-icon-reading customIcon"></i>政治面貌
          </div>
          <div class="container">
            <pie-charts></pie-charts>
          </div>
        </div>
      </el-col>
      <el-col :span="10" class="screen-center">
        <!-- <div class="center-image"></div>
        <div class="person-number heart" style="position: absolute;top: 357px;left: 298px;">
          <div class="title">总</div>
          <div class="number">{{ number.total }}</div>
        </div>
        <div class="person-number heart" style="position: absolute;top: 200px;left: 35px;">
          <div class="title">男</div>
          <div class="number">{{ number.male }}</div>
        </div>
        <div class="person-number heart" style="position: absolute;top: 100px;right: 10px;">
          <div class="title">女</div>
          <div class="number">{{ number.female }}</div>
        </div> -->
        <index-map></index-map>
      </el-col>
      <el-col :span="7" class="title-container">
        <div class="title-container">
          <div class="title">
            <i class="el-icon-office-building customIcon"></i>活跃指数
          </div>
          <div class="container">
            <!-- <bar-charts-row></bar-charts-row> -->
            <line-charts></line-charts>
          </div>
        </div>
        <div class="title-container">
          <div class="title">
            <i class="el-icon-user-solid customIcon"></i>干部走访
          </div>
          <pcs-vite></pcs-vite>
          <!-- <div class="container">
            <div class="special-container">
              <div class="special">
                <div class="special-title">薪火结对</div>
                <div class="special-number">{{ specialNumber.masterAndApprentice }}</div>
              </div>
              <div class="special">
                <div class="special-title">生活特别困难</div>
                <div class="special-number">{{ specialNumber.lifeHard }}</div>
              </div>
              <div class="special">
                <div class="special-title">孤寡老人</div>
                <div class="special-number">{{ specialNumber.lonelyOldMan }}</div>
              </div>
            </div>
          </div> -->
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div class="footer-container left">
          <bar-charts></bar-charts>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="footer-container right">
          <bar-charts-right></bar-charts-right>
        </div>
      </el-col>
    </el-row>
    <dialog-table></dialog-table>
  </div>
</template>

<script>
import BarCharts from "./BarCharts"
import BarChartsRight from "./BarChartsRight"
import LineCharts from "./LineCharts"
import BarChartsRow from "./BarChartsRow"
import PieCharts from "./PieCharts"
import PieChartsAge from "./PieChartsAge"
import PcsVite from "./PcsVite"
import IndexMap from "./IndexMap"
import { getGenderDist } from "../../api/screen"
import { onMounted, ref, reactive } from "vue"
import { useRouter, useRoute } from 'vue-router'
import DialogTable from "@/views/screen/DialogTable"


export default {
  name: "Index",
  components: { BarCharts, BarChartsRight, LineCharts, BarChartsRow, PieCharts, PieChartsAge, PcsVite, IndexMap, DialogTable },
  setup() {

    let router = useRouter()

    let number = reactive({
      total: 0,
      female: 0,
      male: 0
    })
    let specialNumber = reactive({
      masterAndApprentice: 0,
      lifeHard: 0,
      lonelyOldMan: 0
    })

    function getData() {
      getGenderDist().then(res => {
        if (res.code === 200) {
          number.female = res.data.female
          number.male = res.data.male
          number.total = res.data.total
        }
      })

    }
    const goHt = () => {
      router.replace('/retiree/list')
    }
    const goFirst = () => {
      router.replace('/stepOne')
    }

    onMounted(() => {
      getData()
    })

    return { number, specialNumber, goHt, goFirst }
  }
}
</script>

<style scoped lang="scss">
.screen-container {
  height: 100%;
  width: 100%;
  background-color: #001a33;
  // background-image: url(../../assets/screen/heanderBg5.jpg);
  // background-size: 100% 100%;

  .screen-header {
    background-image: url(../../assets/screen/heander0831.png);
    height: 80px;
    width: 100%;
    background-size: 100% 100%;

    .ht {
      float: right;
      color: #fff;
      font-weight: bold;
      margin-top: 30px;
      margin-right: 30px;
      cursor: pointer;
    }

    .ht:hover {
      color: #2977c7;
      border-bottom: 1px solid #2977c7;
    }

  }

  .title-container {
    padding-left: 20px;
    width: calc(100% - 20px);

    .title {
      background-image: url(../../assets/screen/small-title.png);
      background-repeat: no-repeat;
      background-size: 100% 100%;
      height: 5vh;
      display: flex;
      align-items: center;
      font-size: 18px;
      color: white;
      padding-left: 25px;
      letter-spacing: 2px;

      .customIcon {
        color: #2977c7;
        font-size: 24px;
        margin-right: 10px;
      }

    }

    .container {
      height: 25vh;

      .special-container {
        display: flex;
        align-items: center;
        flex-direction: row;
        height: 25vh;

        .special {
          margin-right: 10px;
          background-image: url("../../assets/screen/border.png");
          background-size: 100% 100%;
          width: 30%;
          height: 11vh;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-direction: column;

          .special-title {
            font-size: 16px;
            color: #d2d2d2;
            font-family: jiayan;
            margin-bottom: 5px;
          }

          .special-number {
            font-size: 24px;
            color: #00eaf9;
            font-family: shuzi;

          }
        }
      }
    }
  }

  .screen-center {

    .center-image {
      position: absolute;
      width: 50%;
      height: 50vh;
      background-image: url("../../assets/screen/lcgalogo1.png");
      background-size: 100% 100%;
      left: 25%;
      top: 9%;
    }

    .heart {
      animation: heart 1.3s ease-in-out 2.7s infinite alternate;
    }

    @keyframes heart {
      from {
        transform: translate(0, 0)
      }

      to {
        transform: translate(0, 10px);
      }
    }

    .person-number {
      background-image: url("../../assets/screen/number.png");
      background-size: 100% 100%;
      width: 200px;
      height: 75px;

      .title {
        float: left;
        font-size: 22px;
        color: #d2d2d2;
        font-family: jiayan;
        margin-top: 38px;
        margin-left: 10px;
      }

      .number {
        float: left;
        font-size: 46px;
        color: #00eaf9;
        font-family: shuzi;
        margin-left: 42px;
        margin-top: 3px;
      }
    }
  }

  .footer-container {
    background-image: url(../../assets/screen/blue-bg.png);
    background-size: 100% 100%;
    background-repeat: no-repeat;
    height: 30vh;

  }

  .left {
    margin: 0px 10px 0px 20px;
  }

  .right {
    margin: 0px 20px 0px 10px;

  }
}
</style>