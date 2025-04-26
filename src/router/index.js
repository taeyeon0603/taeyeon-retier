import { createRouter, createWebHistory } from 'vue-router'
import nprogress from 'nprogress'
import 'nprogress/nprogress.css'
import { getBoundList } from '../api/router'
import dynamicRouter from "./dynamicRouter"
import { nextTick } from "vue";

nprogress.configure({ showSpinner: false })

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
        component: () => import('@/views/layout/Index'),
        children: []
    },
    {
        path: '/screenNL',
        name: 'screenNL',
        meta: {
            title: '驾驶舱'
        },
        component: () => import('@/views/screen/IndexNL'),
        children: []
    },
    {
        path: '/screen',
        name: 'screen',
        meta: {
            title: '统计分析'
        },
        component: () => import('@/views/screen/Index'),
        children: []
    },
    // {
    //     path: '/screenBak',
    //     name: 'screenBak',
    //     meta: {
    //         title: '驾驶舱'
    //     },
    //     component: () => import('@/views/screen/IndexBak'),
    //     children: []
    // },
    // {
    //     path: '/screenMap',
    //     name: 'screenMap',
    //     meta: {
    //         title: '驾驶舱'
    //     },
    //     component: () => import('@/views/screen/IndexMap'),
    //     children: []
    // },

    {
        path: '/StepOneBgNL',
        name: 'stepOneBgNL',
        meta: {
            title: '驾驶舱'
        },
        component: () => import('@/views/screen/StepOneBgNL'),
        children: []
    },
    {
        path: '/stepOne',
        name: 'stepOne',
        meta: {
            title: '驾驶舱'
        },
        component: () => import('@/views/screen/StepOneBg'),
        children: []
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

let newRoutes = null
router.beforeEach((to, from, next) => {
    nprogress.start()
    if (to.path != '/' && to.path != '/screen' && to.path != '/screenNL' && to.path != '/StepOneBgNL') {
        if (sessionStorage.getItem("isLogin") != "false" && sessionStorage.getItem("isLogin") != null) {
            if (!newRoutes) {
                getBoundList().then(res => {
                    let data = res.data
                    for (let item of data) {
                        router.addRoute("layout", {
                            path: `${item.path}`,
                            name: item.name,
                            meta: { title: item.meta.title },
                            component: () => import(`@/views/${item.component}`)
                        })
                        for (let childrenItem of item.children) {
                            router.addRoute(item.name, {
                                path: `${childrenItem.path}`,
                                name: childrenItem.name,
                                meta: { title: childrenItem.meta.title },
                                component: () => import(`@/views/${childrenItem.component}`)
                            })
                        }
                    }
                    newRoutes = true
                    // console.log(router.getRoutes())
                    // console.log(router.hasRoute('systemLog'))
                    next({ ...to, replace: true })
                }).catch(err => {
                })
            } else {
                next()
            }
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
