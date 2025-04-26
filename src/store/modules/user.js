import { login, logout } from '@/api/login'

const state = {
  userInfo: sessionStorage.getItem('userInfo') ? JSON.parse(sessionStorage.getItem('userInfo')) : {
    id: "",
    username: "",
    name: "",
    policeNum: "",
    deptId: "",
    deptName: "",
    roles: []
  }
}

const getters = {
  userInfo(state) {
    return state.userInfo
  }
}
const actions = {
  Login({ commit }, loginInfo) {
    return new Promise(async (resolve, reject) => {
      try {
        let res = await login(loginInfo)
        const userInfo = res.data;
        // console.log(userInfo)
        if (res.code === 200) {
          commit('setUserInfo', userInfo)
          commit(`login/setLoginStatus`, "true", { root: true })
          // commit(`login/setRole`, userInfo.roles[0].role, {root: true})
          sessionStorage.setItem("isLogin", "true")
          // sessionStorage.setItem("role", userInfo.roles[0].role)
        }
        resolve(res)
      } catch (error) {
        reject(error)
      }
    })
  }
}

const mutations = {
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo;
  },
  logout() {
    try {
      commit(`login/setLoginStatus`, "false", { root: true })
      // commit(`login/setRole`, "", {root: true})
    } catch (e) {

    }
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
