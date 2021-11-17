import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'


var axios = require('axios')
axios.defaults.baseURL = 'http://localhost:8888/api'
axios.defaults.withCredentials = true

Vue.prototype.$axios = axios      // 全局注册

Vue.config.productionTip = false; // 阻止生产消息

Vue.use(ElementUI)                // 完整引入element-ui


router.beforeEach((to, from, next) => { // to: 目的路由; from: 源路由
    if (to.meta.requireAuth) {          // 判断要跳转到路由是否需要登录
      console.log("get the user info: " + JSON.stringify(store.state.user))
      if (store.state.user.username && store.state.user.userid != -1) {
          next()                      // 放行
      } else {
          next({
              path: 'login',
              query: { redirect: to.fullPath }
          })
      }
    } else {
        next()
    }
})

new Vue({
    el: '#app',
    render: h => h(App),
    router,
    store,
    components: { App },
    template: '<App/>' // 表示用 <app></app> 替换 index.html 中的 <div id="app"></div>
})
