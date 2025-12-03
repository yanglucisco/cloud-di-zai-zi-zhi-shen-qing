<template>
    <span>首页11111</span>
    <div>{{ userInfo }}</div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getInfo } from '@/api/user' // 导入具体的API方法
const userInfo = ref(null)
onMounted(async () => {
    debugger
    console.log('home1 onMounted:')
    const token = sessionStorage.getItem('idToken')
    console.log('home1 onMounted 123:')
    if(token){
        console.log('home1 idToken:' + token)
    }
    try {
        debugger
        const data = await getInfo() // 调用接口
        userInfo.value = data.userName
    } catch (error) {
        console.error('获取用户信息失败', error)
    } finally {
    }
})
</script>