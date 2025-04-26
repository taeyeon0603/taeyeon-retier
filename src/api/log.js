import axios from "@/api/axios";

//获取应用系统日志
export const postLogList= params => {
  return axios.fetchPost("/log/list", params);
};

//获取日志详情
export const getLogDetail= params => {
  return axios.fetchGet("/log/detail", params);
};




