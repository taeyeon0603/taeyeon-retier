import axios from "@/api/axios";

//获取权限类别（级联信息）
export const permissionList = params => {
  return axios.fetchGet("/permission/groups", "")
}

//添加权限
export const addPermission = params => {
  return axios.fetchPostForm("/permission/add", params)
}
//更新权限
export const updatePermission = (id, params) => {
  return axios.fetchPut("/permission/" + id + "/update", params)
}
//删除权限
export const deletePermission = params => {
  return axios.fetchDeleteUrl("/permission/" + params, "")
}
//查找单个权限
export const getPermissionById = params => {
  return axios.fetchGet("/permission/" + params, "")
}

