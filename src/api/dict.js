import axios from "@/api/axios";

//获取应用字典
export const getAppList = params => {
  return axios.fetchGet("/dict/appList", '');
};
//获取证书单位字典
export const getCertDepts = params => {
  return axios.fetchGet("/dict/certDepts", '');
};

//获取终端单位字典
export const getTerminalDepts = params => {
  return axios.fetchGet("/dict/terminalDepts", '');
};


