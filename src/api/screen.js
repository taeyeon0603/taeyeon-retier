import axios from "@/api/axios";

//年龄分布
export const getAgeDist = params => {
  return axios.fetchGet("/index/ageDist", '');
};
//单位分布
export const getDeptDist = params => {
  return axios.fetchGet("/index/deptDist", '');
};

//性别分布
export const getGenderDist = params => {
  return axios.fetchGet("/index/genderDist", '');
};

//党员数量
export const getPartyCount = params => {
  return axios.fetchGet("/index/partyCount", '');
};

//特殊人群
export const getSpecialCount = params => {
  return axios.fetchGet("/index/specialCount", '');
};

//派出所走访
export const postVisitRecord = params => {
  return axios.fetchPostForm("/index/visitRecord", '');
};

//照片轮播
export const getWorkNews = params => {
  return axios.fetchGet("/index/workNews", '');
};

//工龄统计
export const getPoliceAge = params => {
  return axios.fetchGet("/index/policeAgeDist", '');
};


//党龄统计
export const getPartyAge = params => {
  return axios.fetchGet("/index/partyAgeDist", '');
};

//居住地分布统计
export const getResidenceType = params => {
  return axios.fetchGet("/index/residenceTypeDist", '');
};

//活跃指数统计
export const getActiveDist = params => {
  return axios.fetchGet("/index/activeDist", '');
};

