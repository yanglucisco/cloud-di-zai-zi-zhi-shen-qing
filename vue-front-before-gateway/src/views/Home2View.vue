<template>
    <span>首页11111</span>
    <div>{{ userInfo }}</div>
    <div>测试角色</div>
    <div>{{ testrole }}</div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getInfo } from '@/api/user' // 导入具体的API方法
import { getCatalog, testRole } from '@/api/catalog'
import { getUserInfo } from '@/userInfo'
const userInfo = ref(null)
const testrole = ref(null)
onMounted(async () => {
    // debugger
    console.log('home1 onMounted:')
    const token = getUserInfo().accessToken// sessionStorage.getItem('idToken')
    console.log('home1 onMounted 123:')
    if(token){
        console.log('home1 idToken:' + token)
    }
    try {
        const data = await getCatalog() // 调用接口
        userInfo.value = data.userName + " " + getUserInfo().accessToken
        const testrolev = await testRole()
        testrole.value = testrolev + " idtoken:" + sessionStorage.getItem("idToken")
    } catch (error) {
        console.error('获取用户信息失败', error)
    } finally {
    }
})
export const testState = ref({name: 'yanglu'})
</script>