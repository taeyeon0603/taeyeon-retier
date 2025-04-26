//动态路由定义

let dynamicRouter = {
  routes: [
    {
      path: '/layout',
      name: 'layout',
      meta: {
        title: '主页'
      },
      component: () => import('@/views/layout/Index'),
      children: []
    }
  ]
}

export default dynamicRouter