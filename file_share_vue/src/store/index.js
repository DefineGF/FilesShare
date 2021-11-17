import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        user: { // 记录用户信息
            username: window.localStorage.getItem('user' || '[]') == null ?
              '' : JSON.parse(window.localStorage.getItem('user' || '[]')).username,
            userid: window.localStorage.getItem('user' || '[]') == null ?
            -1 : Number(JSON.parse(window.localStorage.getItem("user" || '[]')).userid)
        },
    },
    mutations: {
        login(state, user) {
          console.log("get the user name : " + user.username + " user_id: " + user.userid)
          state.user.username = user.username
          state.user.userid = user.userid
          window.localStorage.setItem('user', JSON.stringify(user))
        }
    }
})
