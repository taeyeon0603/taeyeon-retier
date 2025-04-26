import axios from "@/api/axios";

export const login =params =>{
  return axios.fetchPostForm("/login",params)
}

export const logout =params =>{
  return axios.fetchPostForm("/logout","")
}

export const updatePwd =params =>{
  return axios.fetchPostForm("/updatePwd",params)
}

export const getUserInfo =params =>{
  return axios.fetchGet("/getUserInfo","")
}

