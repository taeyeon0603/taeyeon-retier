import axios from "@/api/axios";

export const getMapDataJson = params => {
  return axios.fetchGet("http://" + location.host + "/lucheng_police.geojson", params);
};
