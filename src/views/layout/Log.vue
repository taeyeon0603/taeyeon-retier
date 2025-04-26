<template>
  <div>
    <router-view v-slot="{Component}">
      <transition name="fade-transform-m" mode="out-in">
        <!--              <keep-alive>-->
        <component :is="Component"/>
        <!--              </keep-alive>-->
      </transition>
    </router-view>
  </div>
</template>

<script>
  export default {
    name: "Log"
  }
</script>

<style scoped>

</style>