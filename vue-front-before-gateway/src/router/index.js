import { createRouter, createWebHistory, createWebHashHistory } from "vue-router"
import Home from "../layout/index.vue"
import HomeView from "../views/HomeView.vue"
import Home1View from "../views/Home1View.vue"
import LogoutView from '../views/Logout.vue'
import Login from '../views/Login.vue'
import User from '../views/User/Index.vue'
import Index from '../views/Index.vue'
import Sys from '../views/Sys/Index.vue'
import SysOrg from '../views/Sys/Org/Index.vue'
import SysUser from '../views/Sys/User/Index.vue'
import { sysinfoStore } from "@/store/sysinfo"
import { getCurrentVerifier, generateCodeChallenge, gotoLoginPage } from '../utils/pkce-util'
import { setUserAccessToken, setUserIdToken, getUserInfo } from '../userInfo/index'
import request from '@/utils/request'
import appConfig from '@/store/Singleton'
import { computed } from "vue"
import routerMap from './RouterPath.js'
import EnvUtil from '../utils/EnvUtil'

appConfig.setData('name', 'yanglu')
console.log('route index')
const router = createRouter({
  history: createWebHistory(),//createWebHistory(),
  routes: [
    {
      path: "/",
      name: "root",
      component: Home,
      children: [
        {
          path: "home",
          name: "home",
          component: Index, //() => import('@/views/Index.vue'),
        }
      ]
    }
  ],
})
router.beforeEach((to, from, next) => {
  console.log('route before each')
  console.log("router from: " + from.fullPath)
  console.log("router to: " + to.fullPath)
  const accessToken = getUserInfo().accessToken
  if (accessToken) {
    next()
  } else {
    isFromAuthorServer().then((r) => {
      next()
    }).catch(error => {
      console.log('与授权服务器交换exchagecode时出错：' + error.message)
      generateCodeChallenge().then(codeChallenge => {
        gotoLoginPage(codeChallenge)
      })
    })
  }
})
async function isFromAuthorServer() {
  debugger
  const queryString = window.location.search
  const params = new URLSearchParams(queryString)
  const code = params.get("code") // "alice"
  if (code) {
    //从授权服务器返回来的
    await exchangeCode(code)
  }
  else {
    throw new Error('请登录')
  }
}
const dynamicCreateRouter = (parentName, routerItems) => {
  routerItems.forEach(item => {
    router.addRoute(parentName, {
        path: item.component,//'sys',
        name: item.name,//'sys',
        component: routerMap.routerMap.get(item.name)
    })
    dynamicCreateRouter(item.name, item.children)
  })
}
const exchangeCode = async (code) => {
  const verifier = getCurrentVerifier()// '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// sessionStorage.getItem('pkce_verifier');
  console.log('verifier: ' + verifier)
  const tokenUrl = 'http://vue-front-before-gateway.clouddizai.com:' + EnvUtil.apiPort + '/oauth/oauth2/token'

  const body = new URLSearchParams({
    grant_type: 'authorization_code',
    code,
    redirect_uri: 'http://vue-front-before-gateway.clouddizai.com:' + EnvUtil.apiPort + '/home',
    client_id: 'pkce-client',
    code_verifier: verifier // 关键：验证身份
  })
  debugger
  try {
    const response = await fetch(tokenUrl, {
      method: 'POST',
      mode: 'cors',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body
    });
    const tokens = await response.json();
    sessionStorage.setItem("idToken", tokens.id_token);
    setUserAccessToken(tokens.access_token)
    setUserIdToken(tokens.id_token)
    console.log('exchange')
    const res = await request({
      url: '/rolemanage/sysrole/get',
      method: 'get'
    })
    dynamicCreateRouter('root', res)
    console.log('res:' + res)
    appConfig.setData('menus', res)
  } catch (error) {
    console.error('Token exchange failed:', error);
  }
}

export default router
