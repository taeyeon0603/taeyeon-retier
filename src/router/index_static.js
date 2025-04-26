import {createRouter, createWebHistory} from 'vue-router'
import nprogress from 'nprogress'
import 'nprogress/nprogress.css'
import {getBoundList} from '../api/router'
import dynamicRouter from "./dynamicRouter"

nprogress.configure({showSpinner: false})

const routes = [
  {
    path: '/',
    name: 'login',
    meta: {
      title: '登陆页'
    },
    component: () => import('@/views/login/login')
  },
  {
    path: '/layout',
    name: 'layout',
    meta: {
      title: '主页'
    },
    component: () => import('@/views/layout/Index_static'),
    children: [
      {
        "path": "/retiree",
        "name": "retiree",
        "redirect": "/retiree/list",
        "meta":
          {
            "title": "退休人员"
          },
        "component": () => import('@/views/layout/Retiree'),
        "children": [
          {
            "path": "/retiree/list",
            "name": "retireeList",
            "meta":
              {
                "title": "列表"
              },
            "component": () => import('@/views/Retiree/list'),
          }]
      },
      {
        "path": "/systemConfig",
        "name": "systemConfig",
        "meta":
          {
            "title": "系统配置"
          },
        "redirect": "/systemConfig/User",
        "component": () => import('@/views/layout/Retiree'),
        "children": [
          {
            "path": "/systemConfig/user",
            "meta":
              {
                "title": "用户"
              },
            "name": "userList",
            "component": () => import('@/views/systemConfig/User'),
          },
          {
            "path": "/systemConfig/role",
            "name": "roleList",
            "meta":
              {
                "title": "角色"
              },
            "component": () => import('@/views/systemConfig/Role'),
          },
          {
            "path": "/systemConfig/permission",
            "name": "permissionList",
            "meta":
              {
                "title": "权限"
              },
            "component": () => import('@/views/systemConfig/Permission'),
          }]
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  nprogress.start()
  if (to.path != '/') {
    if (sessionStorage.getItem("isLogin") != "false" && sessionStorage.getItem("isLogin") != null) {
        next()
    } else {
      next("/")
    }
  } else {
    next()
  }

})

router.afterEach(() => {
  nprogress.done()
})


export default router
