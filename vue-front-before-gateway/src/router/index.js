import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";
import Home from "../layout/index.vue";
import HomeView from "../views/HomeView.vue";
import Home1View from "../views/Home1View.vue";
import LogoutView from "../views/Logout.vue";
import Login from "../views/Login.vue";
import User from "../views/User/Index.vue";
import Index from "../views/Index.vue";
import Sys from "../views/Sys/Index.vue";
import SysOrg from "../views/Sys/Org/Index.vue";
import SysUser from "../views/Sys/User/Index.vue";
import { sysinfoStore } from "@/store/sysinfo";
import {
  getCurrentVerifier,
  generateCodeChallenge,
  gotoLoginPage,
} from "../utils/pkce-util";
import {
  setUserAccessToken,
  setUserIdToken,
  getUserInfo,
} from "../userInfo/index";
import request from "@/utils/request";
import appConfig from "@/store/Singleton";
import { computed } from "vue";
import routerMap from "./RouterPath.js";
import EnvUtil from "../utils/EnvUtil";
import { getUserIdToken } from "../userInfo/index";

appConfig.setData("name", "yanglu");
console.log("route index");
const router = createRouter({
  history: createWebHistory(), //createWebHistory(),
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
      ],
    },
  ],
});
// const dynamicRoutes = [
//   {
//     path: '/admin',
//     name: 'Admin',
//     component: () => import('@/views/Admin.vue')
//   }
// ]
router.beforeEach((to, from, next) => {
   
  console.log("route before each");
  console.log("router from: " + from.fullPath);
  console.log("router to: " + to.fullPath);
  const accessToken = getUserInfo().accessToken;
  if (accessToken) {
    if (!router.hasRoute(to.name)) {
      const menus = appConfig.getData('menus')
      dynamicCreateRouter('root', menus)
      // 添加后重新导航
      return next(to.fullPath);
    }
    next();
  } else {
    isFromAuthorServer()
      .then((r) => {
        next();
      })
      .catch((error) => {
        console.log("与授权服务器交换exchagecode时出错：" + error.message);
        generateCodeChallenge().then((codeChallenge) => {
          gotoLoginPage(codeChallenge);
        });
      });
  }
});
async function isFromAuthorServer() {
   
  const queryString = window.location.search;
  const params = new URLSearchParams(queryString);
  const code = params.get("code"); // "alice"
  if (code) {
    //从授权服务器返回来的
    await exchangeCode(code);
  } else {
    throw new Error("请登录");
  }
}
const dynamicCreateRouter = (parentName, routerItems) => {
  routerItems.forEach(item => {
    let itemRoute = {
        //此处不能直接用item.path，因为有parentName，所以需要拼接完整路径
        path: item.component,//'sys',
        name: item.name,//'sys',
        component: routerMap.routerMap.get(item.name)
    }
    router.addRoute(parentName, itemRoute)
    dynamicCreateRouter(item.name, item.children)
  })
}
const exchangeCode = async (code) => {
  const verifier = getCurrentVerifier(); // '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// sessionStorage.getItem('pkce_verifier');
  console.log("verifier: " + verifier);
  const tokenUrl = EnvUtil.AUTH_SERVER_URL + "/oauth2/token";

  const body = new URLSearchParams({
    grant_type: "authorization_code",
    code,
    redirect_uri:
      "http://vue-front-before-gateway.clouddizai.com:" +
      EnvUtil.apiPort +
      "/home",
    client_id: "pkce-client",
    code_verifier: verifier, // 关键：验证身份
  });
   
  try {
    const response = await fetch(tokenUrl, {
      method: "POST",
      mode: "cors",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body,
    });
    const tokens = await response.json();
    sessionStorage.setItem("idToken", tokens.id_token);
    setUserAccessToken(tokens.access_token);
    setUserIdToken(tokens.id_token);
    console.log("exchange");
    const res = await request({
      url: "/rolemanage/sysrole/get",
      method: "get",
    });
    // dynamicCreateRouter('root', res)
    console.log("res:" + res);
    appConfig.setData("menus", res);
    router.push("index")
  } catch (error) {
    console.error("Token exchange failed:", error);
  }
};

// 辅助函数
function isLoggedIn() {
  return !!getUserIdToken();
}

function isRouteAdded(name) {
  return router.hasRoute(name); // 检查一个标志性路由是否已添加
}

export default router;
