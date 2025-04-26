<template>
  <div id="app">
    <router-view/>
  </div>

</template>
<script>
  import {login} from "@/api/login"
  import {getUserInfo} from "@/api/login"
  import {onMounted, computed, onBeforeMount, provide} from "vue"
  import {useStore} from 'vuex'
  import * as echarts from "echarts";

  export default {
    name: "app",
    setup() {

      provide('ec', echarts)//引入echarts

      let store = useStore()

      function loginApi() {
        login({
          username: "zhengyuan"
        }).then(res => {
          if (res.code === 200) {
            console.log('login success')
          }
        })
      }


      function saveState() {
        sessionStorage.setItem("userInfo", JSON.stringify(store.state.user.userInfo))
        sessionStorage.setItem("isLogin", store.state.login.isLogin)
      }

      onMounted(() => {
        // if (process.env.VUE_APP_ENV_FIELD == ".env.dev") {
        //   loginApi()
        //   getUserInfoApi()
        // }
        window.addEventListener('unload', saveState)
        window.addEventListener('load', () => {
          sessionStorage.removeItem("userInfo")
          // sessionStorage.removeItem("isLogin")
        })

      })
    },

  }

</script>

<style lang="scss">


  #app {
    height: 100%
  }

  #nprogress .bar {
    background: $font-color-theme !important;
  }

  @import "@/style/_style.scss";


  @font-face {
    font-family: jiayan;
    src: url(assets/font/家颜字体.ttf)
    /*src:url('../assets/font/DS-DIGIT.TTF')*/
  }
  @font-face {
    font-family: shuzi;
    src: url(assets/font/DS-DIGIT.TTF)
    /*src:url('../assets/font/DS-DIGIT.TTF')*/
  }
  @font-face {
    font-family: youyuan;
    src: url(assets/font/SIMYOU_0.TTF)
    /*src:url('../assets/font/DS-DIGIT.TTF')*/
  }

  @font-face {
    font-family: hylx;
    src: url(assets/font/汉仪菱心体简.ttf)
    /*src:url('../assets/font/DS-DIGIT.TTF')*/
  }

  @font-face {
    font-family: hysx;
    src: url(assets/font/汉仪尚巍手书W.ttf)
    /*src:url('../assets/font/DS-DIGIT.TTF')*/
  }

</style>
