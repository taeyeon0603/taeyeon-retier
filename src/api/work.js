import axios from "@/api/axios";

//新增信息
export const workInfoAdd = params => {
  return axios.fetchPost("/work/info/add", params);
};

//删除信息
export const workInfoDelete = params => {
  return axios.fetchPost("/work/info/del/" + params, "")
}

//列表信息
export const workInfoList = params => {
  return axios.fetchPost("/work/infoList" , params)
}

//新增动态
export const workNewsAdd = params => {
  return axios.fetchPost("/work/news/add" , params)
}

//删除动态
export const workNewsDelete = params => {
  return axios.fetchPost("/work/news/del/" + params, "")
}

//列表动态
export const workNewsList = params => {
  return axios.fetchPost("/work/newsList" , params)
}


