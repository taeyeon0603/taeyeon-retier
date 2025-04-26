import axios from "@/api/axios";

//获取退休人员列表
export const postRetireList = params => {
  return axios.fetchPost("/policeInfo/retireList", params);
};
//获取全部退休人员
export const postRetireAllList = params => {
  return axios.fetchPost("/policeInfo/retireList/all", params);
};

//退休录入
export const postRetireAdd = params => {
  return axios.fetchPost("/policeInfo/retire", params);
};

//奖励情况录入
export const postRewardAdd = (id, params) => {
  return axios.fetchPost("/policeInfo/" + id + "/updateReward", params);
};
//履历录入
export const postResumeAdd = (id, params) => {
  return axios.fetchPost("/policeInfo/" + id + "/updateResume", params);
};

//删除人员
export const deleteRetire = params => {
  return axios.fetchDeleteUrl("/policeInfo/retire/" + params + "/delete", "")
}

//获取新人人员列表
export const postRetireNewList = params => {
  return axios.fetchPost("/policeInfo/retireList/new", params);
};
//获取新人全部人员
export const postRetireAllNewList = params => {
  return axios.fetchPost("/policeInfo/retireList/all/new", params);
};
//新人录入
export const postRetireNewAdd = params => {
  return axios.fetchPost("/policeInfo/retire/new", params);
};

//获取编外人员列表
export const postRetireExtraList = params => {
  return axios.fetchPost("/policeInfo/retireList/extra", params);
};
//获取编外全部人员
export const postRetireAllExtraList = params => {
  return axios.fetchPost("/policeInfo/retireList/all/extra", params);
};
//编外录入
export const postRetireExtraAdd = params => {
  return axios.fetchPost("/policeInfo/retire/extra", params);
};


