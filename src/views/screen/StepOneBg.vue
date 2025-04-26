<template>
  <div class="deep" data-aos="zoom-in">
    <div class="pic" @click="handleClick"></div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, inject } from "vue"
import "aos/dist/aos.css"
import AOS from "aos/dist/aos.js"
import { useRouter } from "vue-router"
import enter from "@/assets/screen/enter.png"

export default {
  name: "StepOneBg",
  setup() {
    let router = useRouter()
    const handleClick = () => {
      router.replace("/screen")
    }
    onMounted(() => {
      AOS.init({
        offset: 200,
        duration: 1000,
        easing: "ease-in-out-quad",
      })
    })

    return { handleClick, enter }
  },
}
</script>
<style scoped lang="scss">
.deep {
  background-image: url(@/assets/screen/step1Bg.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;

  .pic {
    background-image: url(@/assets/screen/enter1.png);
    background-size: 100% 100%;
    background-repeat: no-repeat;
    position: absolute;
    left: 50%;
    top: 54%;
    width: 12vw;
    height: 7.3vh;
    cursor: pointer;
    // transition: all 0.5s ease;
    animation: scale 1s infinite; /* 使用动画，并设置循环无限次 */
  }

  // .pic:hover {
  //   background-image: url(@/assets/screen/enter1-active.png);
  //   top: 53%;
  // }
}
@keyframes scale {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>
