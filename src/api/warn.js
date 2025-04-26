import axios from "@/api/axios";

// 获取高频访问告警记录
export const postFrequentAccessRecord= params => {
  return axios.fetchPost("/warn/frequentAccessRecord", params);
};

// 查询特定对象3天以上预警记录(时间限定月份)
export const postManyTimeSearchWarnRecord= params => {
  return axios.fetchPost("/warn/manyTimeSearchWarnRecord", params);
};

// 员工被查询告警
export const postSearchPoliceInfoRecord= params => {
  return axios.fetchPost("/warn/searchPoliceInfoRecord", params);
};

// 非正常时间访问
export const postAbnormalTimeAccessWarnRecord= params => {
  return axios.fetchPost("/warn/abnormalTimeAccessWarnRecord", params);
};
// 非正常时间访问取证日志
export const postAbnormalTimeAccessWarnRecordLog= params => {
  return axios.fetchPost("/warn/abnormalTimeAccessWarnRecord/logs", params);
};

// 行为异常告警7天未登录
export const postUserNotAccessIn7days= params => {
  return axios.fetchPost("/warn/userNotAccessIn7days", params);
};

// 行为异常告警近3个月未登录
export const postUserNotAccessIn3month= params => {
  return axios.fetchPost("/warn/userNotAccessIn3month", params);
};


