const state = {
  // isLogin: sessionStorage.getItem('isLogin') ? sessionStorage.getItem('isLogin') :"false",
  isLogin: sessionStorage.getItem('isLogin') ? sessionStorage.getItem('isLogin') : "false"
}

const mutations = {

  ['setLoginStatus'](state, isLogin) {
    state.isLogin = isLogin;
  }
}

export default {
  namespaced: true,
  state,
  mutations
}