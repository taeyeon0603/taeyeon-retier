import axios from "@/api/axios";

//用户列表
export const userList = params => {
  return axios.fetchPost("/user/list", params)
}
//绑定角色
export const userBindRole = params => {
  return axios.fetchPost("/user/bindRole", params)
}
//添加用户
export const addUser = params => {
  return axios.fetchPostForm("/user/add", params)
}
//更新用户
export const updateUser = (id, params) => {
  return axios.fetchPutJson("/user/" + id + "/update", params)
}
//删除用户
export const deleteUser = params => {
  return axios.fetchDeleteUrl("/user/" + params, "")
}
//查找单个用户
export const getUserById = params => {
  return axios.fetchGet("/user/" + params, "")
}

//获取所有部门
export const getDeptList = params => {
  return axios.fetchGet("/user/deptList", "")
}
//已綁定角色列表
export const getRoleByUser = params => {
  return axios.fetchGet("/user/" + params + "/roles", "")
}

//获取可视部门
export const getDeptView = params => {
  return axios.fetchPostForm("/user/getDeptView", params)
}

//修改可视部门
export const updateDeptView = params => {
  return axios.fetchPost("/user/updateDeptView", params)
}

//获取所有管辖派出所
export const getPoliceStationList = params => {
  return axios.fetchGet("/user/policeStationList", "")
}


//获取管辖派出所
export const getDeptControl = params => {
  return axios.fetchPostForm("/user/getManageDept", params)
}

//修改管辖派出所
export const updateDeptControl = params => {
  return axios.fetchPost("/user/updateManageDept", params)
}