// 路由配置文件
import Vue from 'vue'
import Router from 'vue-router'
import Login from "@/components/Login"
import Home from "@/components/Home"
import Library from "@/components/library/Library"
import Individ from "@/components/home/Individ"

Vue.use(Router)

// 并没有吧首页的反问路径设置为 home/index, 仍然可以通过 /index 访问首页;
// 这样配置感觉不到 /home 这个路径的存在; 之后添加新的页面直接在 children 中添加相应的内容；
export default new Router({
    mode: 'history',
    routes: [{
            path: '/home',
            name: 'Home',
            component: Home,
            redirect: '/library',
            children: [
                {
                    path: '/library',
                    name: 'Library',
                    component: Library,
                    meta: {
                        requireAuth: true
                    }
                },
                {
                  path: '/individ',
                  name: 'Individ',
                  component: Individ,
                  meta: {
                    requireAuth: true
                  }
                }
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: Login
        }
    ]
})
