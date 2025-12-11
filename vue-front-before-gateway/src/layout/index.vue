<template>
    <!-- <h1>layout index</h1> -->
    <!-- <div class="header">
        <RouterLink to="/home" style="margin: 10px">首页</RouterLink>
    </div>
    <div>
        <button @click="test">退出登录</button>
    </div> -->
    <a-layout>
        <h1>1</h1>
    </a-layout>
    <a-layout>
        <h1>2</h1>
    </a-layout>
</template>
<script setup>
import { cleanToken, getUserIdToken } from '../userInfo'
import { RouterLink } from 'vue-router'
const test = () => {
    console.log('cleanToken')
    const idToken = getUserIdToken()
    cleanToken()
    window.location.href = `http://auth-server:20001/connect/logout?id_token_hint=${idToken}&post_logout_redirect_uri=http://vue-front-before-gateway.clouddizai.com:20005`
}
</script>
<style scoped>
.header {
    vertical-align: middle;
    text-align: center;
    margin: 10px;
}
</style>