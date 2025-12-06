<script setup>
import { RouterLink } from 'vue-router'
import { cleanToken, getUserIdToken } from './userInfo'
const test = () => {
  console.log('cleanToken')
  
  const idToken = getUserIdToken()
  window.location.href=`http://auth-server:20001/connect/logout?id_token_hint=${idToken}&post_logout_redirect_uri=http://vue-front-before-gateway.clouddizai.com:20005`
  cleanToken()
  console.log('注销注销注销注销')
}
</script>

<template>
  <div class="header">
    <RouterLink to="/home" style="margin: 10px">首页</RouterLink>
  </div>
  <div>
    <button @click="test">退出登录</button>
  </div>
  <div>
    <RouterView />
  </div>
</template>

<style scoped>
.header {
  vertical-align: middle;
  text-align: center;
  margin: 10px;
}
</style>
