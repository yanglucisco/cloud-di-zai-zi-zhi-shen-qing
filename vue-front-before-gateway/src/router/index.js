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
appConfig.setData('name', 'yanglu')
debugger
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
        },
        // {
        //   path: "index",
        //   name: "index",
        //   component: Index, //() => import('@/views/Index.vue'),
        // },
        // {
        //   path: "sys",
        //   name: "sys",
        //   component: Sys, //() => import('@/views/Sys/Index.vue'),
        //   children: [
        //     {
        //       path: "org",
        //       name: "sysorg",
        //       component: SysOrg, //() => import('@/views/Sys/Org/Index.vue'),
        //     },
        //     {
        //       path: "user",
        //       name: "sysuser",
        //       component: SysUser, //() => import('@/views/Sys/User/Index.vue'),
        //     }
        //   ]
        // },
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
const exchangeCode = async (code) => {
  const verifier = getCurrentVerifier()// '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// sessionStorage.getItem('pkce_verifier');
  console.log('verifier: ' + verifier)
  const tokenUrl = 'http://vue-front-before-gateway.clouddizai.com:20005/oauth/oauth2/token'
  // const tokenUrl = 'http://auth-server:20001/oauth2/token';

  const body = new URLSearchParams({
    grant_type: 'authorization_code',
    code,
    redirect_uri: 'http://vue-front-before-gateway.clouddizai.com:20005/home',
    client_id: 'pkce-client',
    code_verifier: verifier // 关键：验证身份
  });

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
    console.log('res:' + res)
    appConfig.setData('menus', res)
  } catch (error) {
    console.error('Token exchange failed:', error);
  }
}
export default router
