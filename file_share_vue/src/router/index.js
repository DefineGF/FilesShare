// ·�������ļ�
import Vue from 'vue'
import Router from 'vue-router'
import Login from "@/components/Login"
import Home from "@/components/Home"
import Library from "@/components/library/Library"
import Individ from "@/components/home/Individ"

Vue.use(Router)

// ��û�а���ҳ�ķ���·������Ϊ home/index, ��Ȼ����ͨ�� /index ������ҳ;
// �������øо����� /home ���·���Ĵ���; ֮������µ�ҳ��ֱ���� children �������Ӧ�����ݣ�
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
