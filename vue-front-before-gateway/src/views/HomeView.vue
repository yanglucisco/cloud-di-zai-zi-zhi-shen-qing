<template>
    <span>首页</span>
    <div>{{ userInfo }}</div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getInfo } from '@/api/user' // 导入具体的API方法

const userInfo = ref(null)

const exchangeCode = async (code) => {
    const verifier = '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// sessionStorage.getItem('pkce_verifier');
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
        console.log('Access Token:', tokens.access_token);
        sessionStorage.setItem("token", tokens.access_token);
    } catch (error) {
        console.error('Token exchange failed:', error);
    }
}

onMounted(async () => {
    const queryString = window.location.search
    const params = new URLSearchParams(queryString)

    const code = params.get('code') // "alice"
    if(code){ //从授权服务器返回来的
        exchangeCode(code)
    }
    // try {
    //     const data = await getInfo() // 调用接口
    //     userInfo.value = data.userName
    // } catch (error) {
    //     console.error('获取用户信息失败', error)
    // } finally {
    // }
})
</script>