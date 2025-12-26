import { createRouter, createWebHistory, createWebHashHistory } from "vue-router"
import Home from "../layout/index.vue"
import HomeView from "../views/HomeView.vue"
import Home1View from "../views/Home1View.vue"
import LogoutView from '../views/Logout.vue'
import Login from '../views/Login.vue'
import { getCurrentVerifier, generateCodeChallenge, gotoLoginPage } from '../utils/pkce-util'
import { setUserAccessToken, setUserIdToken, getUserInfo } from '../userInfo/index'
const router = createRouter({
  history: createWebHistory(),//createWebHistory(),
  routes: [
    {
      path: "/",
      name: "home",
      component: Home,
    },
    {
      path: "/home",
      name: "home2",
      component: HomeView,
    },
    {
      path: "/home1",
      name: "home1",
      component: Home1View,
    },
    {
      path: "/logout",
      name: "logout",
      component: LogoutView,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    }
  ],
})
router.beforeEach((to, from, next) => {
  // debugger
  console.log("router from: " + from)
  console.log("router to: " + to)
  const accessToken = getUserInfo().accessToken
  if (accessToken) {
    next()
  } else {
    isFromAuthorServer().then((r) => {
      next()
    }).catch(error => {
      console.log(error.message)
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
  throw new Error('请登录')
}
const exchangeCode = async (code) => {
    const verifier = getCurrentVerifier()// '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// sessionStorage.getItem('pkce_verifier');
    console.log('verifier: ' + verifier)
    const tokenUrl = 'http://vue-front-before-gateway.clouddizai.com:20005/oauth/oauth2/token';

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
        debugger
        // console.log('id_token:', tokens.id_token);
        sessionStorage.setItem("idToken", tokens.id_token);
        setUserAccessToken(tokens.access_token)
        setUserIdToken(tokens.id_token)
        router.push('/home1')
    } catch (error) {
        console.error('Token exchange failed:', error);
    }
}
export default router
