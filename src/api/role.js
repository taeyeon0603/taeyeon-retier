import axios from "@/api/axios";

//角色列表
export const roleList = params => {
  return axios.fetchPost("/role/list", params)
}

//修改角色权限
export const updatePermissions = params => {
  return axios.fetchPost("/role/updatePermissions", params)
}

//添加角色
export const addRole = params => {
  return axios.fetchPostForm("/role/add", params)
}
//更新角色
export const updateRole = (id, params) => {
  return axios.fetchPut("/role/" + id + "/update", params)
}
//删除角色
export const deleteRole = params => {
  return axios.fetchDeleteUrl("/role/" + params, "")
}
//查找单个角色
export const getRoleById = params => {
  return axios.fetchGet("/role/" + params, "")
}
//角色持有权限列表
export const getPermissionByRole = params => {
  return axios.fetchGet("/role/" + params + "/permission", "")
}


