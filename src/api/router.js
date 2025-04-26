import axios from "@/api/axios";

//角色绑定路由
export const routerBind = (params1, params2) => {
  return axios.fetchPostForm("/router/" + params1 + "/bind", params2)
}
//某一角色路由表
export const getRoleRouterById = params => {
  return axios.fetchGet("/router/" + params + "/list", "")
}
//当前角色路由表
export const getBoundList = params => {
  return axios.fetchGet("/router/boundList", "")
}

//路由表初始化(慎用，会先清空路由表)
export const routerInit = params => {
  return axios.fetchPost("/router/init", params)
}

//所有路由表
export const getAllRouterList = params => {
  return axios.fetchGet("/router/list", "")
}