<template>
  <div class="deepDiv">
    <el-container style="height: 100%">
      <el-aside style="width:220px;height: 100%;box-shadow: 2px 0 6px #00152959;z-index:10">
        <el-row style="height:100%">
          <el-col :span="24" style="height:100%">
            <h3 class="system-title">
              <!--              <img src="@/assets/images/lcgalogo.png" style="width: 25px;    vertical-align: middle;margin-right: 5px"/>-->
              退休人员信息管理
            </h3>
            <el-menu :router="true" :default-active="menuIndex" :default-openeds="defaultOpeneds"
              class="el-menu-vertical-demo" text-color="#5f5f5f" active-text-color="#ffffff">

              <el-menu-item v-if="isAdmin" index="/screen">
                <span>驾驶舱</span>
              </el-menu-item>
              <template v-for="(item, index) in menuList">
                <el-submenu :index="item.id" v-if="item.children.length != 0" :key="index * 30">
                  <template #title>
                    <span>{{ item.meta.title }}</span>
                  </template>
                  <el-menu-item v-for="(item_children, indexs) in item.children" :key="indexs"
                    :index="item_children.path">
                    {{ item_children.meta.title }}
                  </el-menu-item>
                </el-submenu>

                <el-menu-item :index="item.path" v-else :key="index * 2">
                  <template #title>
                    <span>{{ item.meta.title }}</span>
                  </template>
                </el-menu-item>
              </template>
            </el-menu>
          </el-col>
        </el-row>
        <el-row style="position: absolute;bottom: 50px">
          <el-col :span="24" style="text-align: center;width: 220px;color: #969696;font-size: 14px;">
            Copyright © 2025<br />
          </el-col>
        </el-row>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px; padding: 0;background-color: #FFFFFF; height:50px">
          <el-row>
            <el-col :span="24">
              <div class="user-info">
                你好，
                <el-dropdown @command="handleCommand">
                  <span class="el-dropdown-link">
                    {{ policeName }}
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="logout">注销</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </el-col>
          </el-row>
        </el-header>
        <el-main style="padding: 0;height: 100%;background-color: #f2f2f2;overflow-x:hidden">

          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in state.list">{{ item.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>

          <router-view v-slot="{ Component }">
            <transition name="fade-transform-m" mode="out-in">
              <!--              <keep-alive>-->
              <component :is="Component" />
              <!--              </keep-alive>-->
            </transition>
          </router-view>
        </el-main>
      </el-container>

    </el-container>
  </div>
</template>

<script>
import { useRouter, useRoute } from 'vue-router'
import { getUserInfo, logout } from "@/api/login"
import { onMounted, computed, onBeforeMount, reactive, ref, watch, provide } from "vue"
import bus from '@/bus'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { getBoundList } from '@/api/router'
export default {
  name: "Index",
  setup() {
    let store = useStore()
    let policeName = ref(store.state.user.userInfo.name)


    let isAdmin = ref(false)
    provide('isAdmin', isAdmin)
    
    for (let item of store.state.user.userInfo.roles) {
      if (item.name == '超级管理员') {
        isAdmin.value = true
      }
    }

    let menuIndex = "/"
    let defaultOpeneds = ['/retiree/list']
    let router = useRouter()
    let route = useRoute()
    // console.log(route.path)
    menuIndex = route.path
    let state = reactive({
      list: []
    })
    let menuList = ref([])
    //面包屑

    onMounted(() => {
      let matched = route.matched
      state.list = matched
      // console.log('mounted', state)
      getRouterList()
      let userRoles = store.state.user.userInfo.roles


    })

    watch(() => route.matched, (newVal, oldVal) => {
      let matched = newVal
      state.list = matched
      console.log('watch', state)

    })

    function handleCommand(command) {
      if (command === "logout") {
        loginout()
        //   const userInfo = {
        //     id: "",
        //     username: "",
        //     name: "",
        //     policeNum: "",
        //     deptId: "",
        //     deptName: ""
        //   }
        //   store.commit('user/setUserInfo', userInfo)
        //   store.commit(`login/setLoginStatus`, "false", {root: true})
        //   router.replace("/login")
        //   ElMessage.success('注销成功！')
        // }
      }
    }

    function getRouterList() {
      getBoundList().then(res => {
        if (res.code === 200) {
          menuList.value = res.data
        }
      }).catch(err => {

      })
    }


    function loginout() {
      logout().then(res => {
        if (res.code == 200) {
          const userInfo = {
            id: "",
            username: "",
            name: "",
            policeNum: "",
            deptId: "",
            deptName: ""
          }
          store.commit('user/setUserInfo', userInfo)
          store.commit(`login/setLoginStatus`, "false", { root: true })
          router.replace("/")
          ElMessage.success('注销成功！')

        }
      }).catch(err => {

      })
    }


    return { policeName, menuIndex, defaultOpeneds, state, handleCommand, menuList, isAdmin }
  }
}
</script>


<style>
/*fade-transform*/
.fade-transform-m-leave-active,
.fade-transform-m-enter-active {
  transition: all .3s;
}

.fade-transform-m-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-m-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
<style scoped lang="scss">
:deep(.el-dropdown-link) {
  cursor: pointer;
  font-size: 16px;
  color: $font-color-theme;
  font-weight: 400;
  letter-spacing: 1px;
}


:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: $font-color-theme;
  font-weight: bold;
}

:deep(.el-breadcrumb) {
  margin: 15px 0px 0px 25px;
}


.deepDiv {
  height: 100%;

  .user-info {
    /*padding: 0 30px;*/
    text-align: right;
    font-size: 16px;
    color: $font-color-theme;
    font-weight: 400;
    letter-spacing: 1px;
    padding-right: 20px;
  }

  .el-header {
    /*background-color: #131313 !important;*/
    color: #333;
    line-height: 50px;
    box-shadow: 0 2px 8px #00000026;
    z-index: 2;
  }

  .el-aside {
    color: #333;
  }

  a {
    text-decoration: none;
    color: $font-color;
  }

  :deep(.el-menu) {
    border: none;

    .is-active {
      background-color: $font-color-theme  !important;
      /*font-weight: bold;*/
      border-right: 4px solid $menu-border-color;
    }

    .el-submenu__title {
      background-color: $font-color;
    }

    .is-opened {
      .el-submenu__title {
        color: $font-color-theme  !important;
        font-weight: bold;
      }
    }

    .el-menu-item:focus,
    .el-menu-item:hover {
      background-color: $menu-hover;
    }
  }

  .el-menu-vertical-demo {
    height: calc(100% - 60px);
  }

  .system-title {
    text-align: center;
    /*background-color: #1f1f1f;*/
    margin: 0px;
    height: 50px;
    line-height: 50px;
    color: $font-color-theme;
    font-size: 18px;
    font-weight: bold;
    width: 220px;
  }

}

main::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

main::-webkit-scrollbar-thumb {
  background-color: $font-color-theme;
}
</style>
