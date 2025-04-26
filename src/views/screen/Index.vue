<template>
  <div class="screen-container flex column">
    <div class="screen-header">
      <div v-if="isAdmin" class="ht" @click="goHt">[后台管理]</div>
 
    </div>

    <div class="screen-main flex row">
      <div class="lr flex column">
        <div class="main-row">
          <div class="title" style="     justify-content: flex-start;">
         

          </div>
          <div class="swiper-title"><i class="el-icon-reading customIcon"></i>党组织架构</div>
          <div class="container jiagou" style="margin-top:0px">
            <vue3-seamless-scroll :list="array_s" class="scroll" :step="0.18" :hover="true" :class-option="classOption">
              <div class="item" v-for="item in array_s">
                <span class="dzz-item">
                  <div class="dept" @click="handleClickPart(item.dept)">{{ item.dept }}</div>
                  <div class="shuji">书记:</div>
                  <div v-if="isAdmin" class="name1" @click="handleClickName(item.shuji)">{{ item.shuji }}</div>
                  <div v-else class="name1">{{ item.shuji }}</div>
                  <div class="fushuji">委员:</div>
                  <div v-if="isAdmin" class="name2" @click="handleClickName(item.fushuju)">{{ item.fushuju }} </div>
                  <div v-else class="name2">{{ item.fushuju }} </div>
                  <div v-if="isAdmin" class="name2" @click="handleClickName(item.fushuju1)">{{ item.fushuju1 }}</div>
                  <div v-else class="name2">{{ item.fushuju1 }}</div>
                </span>
              </div>
            </vue3-seamless-scroll>
          </div>
        </div>
        <div class="main-row carousel">
          <div class="title">
            <span class="man" @click="handleClickGender('男')">男
              {{ number.male }}人</span>
            <span class="women" @click="handleClickGender('女')">女
              {{ number.female }}人</span>
          </div>
          <div class="prev">
            <img :src="arrowLeft" @click="arrowClick('next')" />
          </div>

          <div class="next">
            <img :src="arrowRight" @click="arrowClick('prev')" />
          </div>
          <el-carousel ref="carousel" interval="5000" trigger="click" arrow="never" indicator-position="none">
            <el-carousel-item>
              <div class="carousel-title"><i class="el-icon-reading customIcon"></i>政治面貌</div>
              <div class="container" style="margin-top:0px">
                <PieCharts></PieCharts>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="carousel-title"> <i class="el-icon-reading customIcon"></i>党龄分布</div>
              <div class="container" style="margin-top:0px">
                <PieChartsDl></PieChartsDl>
              </div>
            </el-carousel-item>

            <!-- <el-carousel-item>
              <div class="carousel-title"> <i class="el-icon-reading customIcon"></i>工龄分布</div>
              <div class="container" style="margin-top:0px">
                <PieChartsJl></PieChartsJl>
              </div>
            </el-carousel-item> -->

            <el-carousel-item>
              <div class="carousel-title"> <i class="el-icon-menu customIcon"></i>年龄分布
              </div>
              <div class="container" style="margin-top:0px">
                <pie-charts-age></pie-charts-age>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="carousel-title"> <i class="el-icon-reading customIcon"></i>居住地分布</div>
              <div class="container" style="margin-top:0px">
                <PieChartsJZD></PieChartsJZD>
              </div>
            </el-carousel-item>
          </el-carousel>
          <!-- <swiper ref="swiper2" class="swiper-container" loop :autoplay="swiperOptions2" :modules="[Autoplay]"
            :speed="1500" :hover="true" :hover-stop="true" :touch-start-prevent-default="false">
            <swiper-slide class="swiper-slide" :key="10">
              <div class="swiper-title"><i class="el-icon-reading customIcon"></i>政治面貌</div>
              <div class="container" style="margin-top:0px">
                <PieCharts></PieCharts>
              </div>
            </swiper-slide>
            <swiper-slide class="swiper-slide" :key="2">
              <div class="swiper-title"> <i class="el-icon-reading customIcon"></i>党龄分布</div>
              <div class="container" style="margin-top:0px">
                <PieChartsDl></PieChartsDl>
              </div>
            </swiper-slide>
            <swiper-slide class="swiper-slide" :key="1">
              <div class="swiper-title"> <i class="el-icon-reading customIcon"></i>工龄分布</div>
              <div class="container" style="margin-top:0px">
                <PieChartsJl></PieChartsJl>
              </div>
            </swiper-slide>
            <swiper-slide class="swiper-slide" :key="0">
              <div class="swiper-title"> <i class="el-icon-menu customIcon"></i>年龄分布
              </div>
              <div class="container" style="margin-top:0px">
                <pie-charts-age></pie-charts-age>
              </div>
            </swiper-slide>
            <swiper-slide class="swiper-slide" :key="11">
              <div class="swiper-title"> <i class="el-icon-reading customIcon"></i>居住地分布</div>
              <div class="container" style="margin-top:0px">
                <PieChartsJZD></PieChartsJZD>
              </div>
            </swiper-slide>
          </swiper> -->
        </div>
      </div>
      <div class="center">
        <index-map></index-map>
      </div>
      <div class="lr flex column">
        <div class="main-row" style="height:calc(40% - 1vh)">
          <div class="title" style="justify-content:flex-start">
            <i class="el-icon-office-building customIcon"></i>
            <el-tooltip effect="customized" placement="left-start">
              <template #content>
                <p class="pop-title">“银耀指数”星级评定规则</p>
                <p class="pop-content">评定原则上以参加集体活动情况为主要依据，以每次集体活动结束为时间节点（首次评定时间为2024年11月）进行动态评定。</p>
                <p class="pop-content">五星：一天多次参加活动，以及本月被走访、获得荣誉者。</p>
                <p class="pop-content">四星：一天内参加过一次活动，以及本月被走访、获得荣誉者。</p>
                <p class="pop-content">三星：一天内参加过一次活动，或者本月被走访、获得荣誉者。</p>
                <p class="pop-content">二星：偶尔参加活动者，本月无被走访记录、获得荣誉者。</p>
                <p class="pop-content">一星：生活不能自理或者无法正常出行者。</p>
                <p class="pop-bottom">（此评定由退休党支部牵头组织，解释权在退休党支部）</p>
              </template>
              <div>银耀指数</div>
            </el-tooltip>
          </div>
          <div class="container">
            <!-- <bar-charts-row></bar-charts-row> -->
            <line-charts></line-charts>
          </div>
        </div>
        <div class="main-row" style="height:calc(60% - 1vh)">
          <div class="title" style="justify-content:flex-start">
            <i class="el-icon-user-solid customIcon"></i>
            <el-tooltip effect="customized" placement="left-start">
              <template #content>
                <p style="font-size:16px;font-weight:bold;line-height: 3vh;">
                  活动动态：退休人员原所在单位、所在辖区地、结对在职党支部等将平时走访、慰问、茶话会、“薪火结对”等活动的照片、视频、文字及时报退休党支部，由内勤录入“退休人员系统”的“活动动态”栏目。
                </p>

              </template>
              <div>活动动态</div>
            </el-tooltip>
          </div>
          <div class="container">
            <pcs-vite></pcs-vite>
          </div>

        </div>
      </div>
    </div>
    <div class="screen-footer flex row">
      <div class="footer-main">
        <bar-charts></bar-charts>
      </div>
      <div class="footer-main">
        <bar-charts-right></bar-charts-right>
      </div>
    </div>

    <dialog-table></dialog-table>
    <retiree-detail></retiree-detail>
    <DialogRowBar></DialogRowBar>
  </div>
</template>

<script>

import bus from "@/bus"
import BarCharts from "./BarCharts"
import BarChartsRight from "./BarChartsRight"
import LineCharts from "./LineCharts"
import BarChartsRow from "./BarChartsRow"
import PieCharts from "./PieCharts"
import PieChartsJZD from "./PieChartsJZD"
import PieChartsAge from "./PieChartsAge"
import PieChartsJl from "./PieChartsJl"
import PieChartsDl from "./PieChartsDl"
import PcsVite from "./PcsVite"
import IndexMap from "./IndexMap"
import { getGenderDist } from "../../api/screen"
import { onMounted, ref, reactive, provide } from "vue"
import { useRouter, useRoute } from 'vue-router'
import DialogTable from "@/views/screen/DialogTable"
import DialogRowBar from "@/views/screen/DialogRowBar"
import RetireeDetail from "@/components/retiree/RetireeDetail"
import { postRetireList } from '@/api/retiree'
import SwiperCore, { Autoplay } from "swiper";
SwiperCore.use([Autoplay]);
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/swiper-bundle.css";
import { useStore } from 'vuex'
import arrowLeft from "@/assets/screen/arrow-left.png"
import arrowRight from "@/assets/screen/arrow-right.png"


export default {
  name: "Index",
  components: { RetireeDetail, BarCharts, BarChartsRight, LineCharts, BarChartsRow, PieCharts, PieChartsJZD, PieChartsAge, PieChartsJl, PieChartsDl, PcsVite, IndexMap, DialogTable, DialogRowBar, Swiper, SwiperSlide },
  setup() {

    let store = useStore()
    let username = store.state.user.userInfo.name
    let isAdmin = ref(false)
    let carousel = ref(null)
    for (let item of store.state.user.userInfo.roles) {
      if (item.name == '超级管理员' || item.name == '职能部门') {
        isAdmin.value = true
      }
    }

    let router = useRouter()

    let tableData = reactive({
      data: []
    })
    let number = reactive({
      total: 0,
      female: 0,
      male: 0
    })
    let title = ref('')
    let specialNumber = reactive({
      masterAndApprentice: 0,
      lifeHard: 0,
      lonelyOldMan: 0
    })
    let swiper1 = ref(null)
    let swiper2 = ref(null)
    // swiper1.value.stopAutoplay();
    let array_s = [{
      dept: "退休第一支部",
      shuji: "李四",
      fushuju: "王五",
      fushuju1: "老六",
    }, {
      dept: "退休第二支部",
      shuji: "李四",
      fushuju: "李四",
      fushuju1: "李四",
    }, {
      dept: "退休第二支部",
      shuji: "李四",
      fushuju: "李四",
      fushuju1: "李四",
    }, {
      dept: "退休第二支部",
      shuji: "李四",
      fushuju: "李四",
      fushuju1: "李四",
    }, {
      dept: "退休第二支部",
      shuji: "李四",
      fushuju: "李四",
      fushuju1: "李四",
    }, {
      dept: "退休第二支部",
      shuji: "李四",
      fushuju: "李四",
      fushuju1: "李四",
    }, {
      dept: "退休第二支部",
      shuji: "李四",
      fushuju: "李四",
      fushuju1: "李四",
    }]

    let swiperOptions1 = reactive({
      delay: 4000,
      pauseOnMouseEnter: true,
      disableOnInteraction: false,
    })

    let swiperOptions2 = reactive({
      delay: 5000,
      pauseOnMouseEnter: true,
      disableOnInteraction: false,
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

    function retireeList(parma) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        gender: parma,
        isHistory: false,
      }).then(res => {
        if (res.code == 200) {
          tableData.data = res.data.content
          bus.$emit('retireeListDialog', {
            list: tableData.data,
            title: title.value
          })
        }
      }).catch(err => {
      })
    }

    let retireeDetailVisible = ref(false)
    provide('retireeDetailVisible', retireeDetailVisible)

    function retireeListByName(parma) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        name: parma,
        isHistory: false,
      }).then(res => {
        if (res.code == 200) {
          tableData.data = res.data.content
          retireeDetailVisible.value = true
          bus.$emit('retireeDetailDialog', tableData.data[0])
          // bus.$emit('retireeListDialog', {
          //   list: tableData.data,
          //   title: title.value
          // })

        }
      }).catch(err => {
      })
    }

    function retireeListByPart(parma) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        partyBranch: parma.replace('退休', ''),
        isHistory: false,
      }).then(res => {
        if (res.code == 200) {
          tableData.data = res.data.content
          bus.$emit('retireeListDialog', {
            list: tableData.data,
            title: title.value
          })
          // bus.$emit('retireeListDialog', {
          //   list: tableData.data,
          //   title: title.value
          // })

        }
      }).catch(err => {
      })
    }
    function arrowClick(arrow) {
      console.log(arrow)
      if (arrow == "next") {
        carousel.value.next()
      } else {
        carousel.value.prev()

      }
    }

    function handleClickGender(parma) {
      retireeList(parma)
    }

    function handleClickName(parma) {
      retireeListByName(parma)
    }
    function handleClickPart(parma) {
      retireeListByPart(parma)
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

    return { carousel, arrowClick, arrowLeft, arrowRight, isAdmin, username, number, specialNumber, goHt, goFirst, handleClickGender, swiper1, swiper2, swiperOptions1, swiperOptions2, array_s, handleClickName, handleClickPart }
  }
}
</script>

<style>
.el-popper.is-customized {
  background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
  box-shadow: 0px 0px 5px #6b6b6b inset;
  max-width: 600px;
}

.el-popper.is-customized .el-popper__arrow::before {
  background: linear-gradient(45deg, #b2e68d, #bce689);

}
</style>

<style scoped lang="scss">
.pop-title {
  font-size: 18px;
  text-align: center;
  font-weight: bold;
}

.pop-content {
  font-size: 14px;
}

.pop-bottom {
  font-size: 14px;
  text-align: right;
  padding-right: 10px;
}

.swiper-container {
  height: 100%;
  width: 100%;
}

.flex {
  display: flex;
  justify-content: center;
  align-items: center;

}

.row {
  flex-direction: row;
}

.column {
  flex-direction: column;
}



.screen-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  // background-color: #001a33;
  background-image: url(../../assets/screen/blueBg3.jpg);
  background-size: 100% 100%;

  .screen-header {
    background-image: url(../../assets/screen/heander.png);
    background-size: 100% 100%;
    height: 80px;
    width: 100%;

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

  .screen-main {
    width: 100vw;
    height: calc(70vh - 80px);

    .lr {
      width: 25vw;
      margin-left: 0.5vw;
      margin-right: 0.5vw;
      height: 100%;

      .carousel {}

      .carousel:hover {
        .prev {
          opacity: 1;
        }

        .next {
          opacity: 1;
        }
      }

      .main-row {
        width: 25vw;
        height: calc(50% - 1vh);
        margin-bottom: 1vh;
        background-color: #00082766;
        border-radius: 10px;
        position: relative;

        .prev {
          position: absolute;
          left: 0.5vw;
          top: 15vh;
          z-index: 100000;
          width: 2vw;
          opacity: 0;

          img {
            width: 2vw;
            opacity: 0.4;
            cursor: pointer;
          }

          img:hover {
            opacity: 1;
          }
        }

        .next {
          position: absolute;
          right: 0.5vw;
          top: 15vh;
          width: 2vw;
          z-index: 100000;
          opacity: 0;

          img {
            width: 2vw;
            opacity: 0.4;
            cursor: pointer;
          }

          img:hover {
            opacity: 1;
          }
        }


        :deep(.el-carousel__arrow) {
          background-color: #d2d2d230;
        }

        .dzz {
          margin-left: 7.5vw;
          font-size: 15px;
          // color: rgb(23, 218, 175);
          // text-shadow: #13b3ff 0 0 10px;

          z-index: 2000;

          .shuji {
            color: #ff5757;
          }

          .fushuji {
            color: #ff5757;
          }

          .name {
            cursor: pointer;
            transition: all 0.2s ease;
            color: #ffc800;
          }

          .name:hover {
            color: #1c85ee;
          }
        }


        .man {
          margin-left: 1vw;
          font-size: 15px;
          color: rgb(23, 218, 175);
          text-shadow: #13b3ff 0 0 10px;
          cursor: pointer;
          z-index: 2000;
        }

        .man:hover {
          color: rgb(253, 198, 15);
        }

        .women:hover {
          color: rgb(253, 198, 15);
        }

        .women {
          margin-left: 1vw;
          font-size: 15px;
          color: rgb(23, 218, 175);
          text-shadow: #13b3ff 0 0 10px;
          cursor: pointer;
          margin-right: 4vw;
          z-index: 2000;
        }

        .title {
          background-image: url(../../assets/screen/small-title.png);
          background-repeat: no-repeat;
          background-size: 100% 100%;
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 5vh;
          display: flex;
          align-items: center;
          justify-content: flex-end;
          font-size: 18px;
          color: white;
          padding-left: 25px;
          letter-spacing: 2px;
          // text-shadow: #13b3ff 0 0 10px;




          .customIcon {
            color: #2977c7;
            font-size: 24px;
            margin-right: 10px;
            text-shadow: #13b3ff 0 0 0px;
          }

        }

        .swiper-title {
          height: 5vh;
          display: flex;
          align-items: center;
          font-size: 18px;
          color: #ff3030;
          padding-left: 25px;
          letter-spacing: 2px;
          text-shadow: #000000 2px 3px 0px;
          font-weight: bold;


          .customIcon {
            color: #2977c7;
            font-size: 24px;
            margin-right: 10px;
            text-shadow: #13b3ff 0 0 0px;
          }
        }

        .carousel-title {
          height: 5vh;
          display: flex;
          align-items: center;
          font-size: 18px;
          color: #fff;
          padding-left: 25px;
          letter-spacing: 2px;
          text-shadow: #13b3ff 0 0 0px;

          .customIcon {
            color: #2977c7;
            font-size: 24px;
            margin-right: 10px;
            text-shadow: #13b3ff 0 0 0px;
          }

        }

        .container {
          margin-top: 5vh;

          .scroll {
            height: 23vh;
            margin-top: 1.6vh;
            overflow: hidden;
          }

        }

        .jiagou {
          display: flex;
          flex-direction: column;
          margin-top: 2vh;

          .item {
            width: 23vw;
            height: 4vh;
            margin-left: 2vw;
            display: flex;
            align-items: center;

            .dzz-item {
              font-size: 15px;
              color: #fff;
              display: flex;
              flex-direction: row;
              align-items: center;

              .dept {

                width: 5.5vw;
                background-color: #8e0000;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 5px;
                cursor: pointer;
                transition: all 0.2s ease;
                color: #ffe000;

              }

              .dept:hover {
                background-color: rgb(253, 198, 15);
                font-weight: bold;
              }

              .shuji {
                color: #ff5757;
                width: 2.3vw;
                margin-left: 1vw;
              }

              .name1 {
                width: 3vw;
                cursor: pointer;
                transition: all 0.2s ease;
                color: #ffc800;
              }

              .name2 {
                width: 3vw;
                cursor: pointer;
                transition: all 0.2s ease;
                color: #ffc800;
              }

              .name1:hover {
                color: #1c85ee;
              }

              .name2:hover {
                color: #1c85ee;
              }

              .fushuji {
                color: #ff5757;
                width: 2.3vw;
                margin-left: 1vw;
              }
            }
          }
        }
      }
    }

    .center {
      width: 48vw;
    }


  }

  .screen-footer {
    height: 30vh;
    width: 100vw;

    .footer-main {
      width: 49vw;
      margin-left: 0.5vw;
      margin-right: 0.5vw;
      background-color: #000827ad;
      border-radius: 10px;
    }
  }

  .title-container {
    padding-left: 20px;
    width: calc(100% - 20px);
    background: #00143173;
    border-radius: 20px;
    margin-bottom: 20px;



    .container {
      height: calc(25vh - 20px);
      margin-top: 5vh;

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
    // background-image: url(../../assets/screen/blue-bg.png);
    // background-size: 100% 100%;
    // background-repeat: no-repeat;
    height: 30vh;
    background: #00143173;
    border-radius: 20px;

  }

  .left {
    margin: 0px 10px 0px 20px;
  }

  .right {
    margin: 0px 20px 0px 10px;

  }
}
</style>