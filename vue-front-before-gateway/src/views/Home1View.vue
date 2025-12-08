<template>
    <span>首页11111</span>
    <div>{{ userInfo }}</div>
    <div>测试角色</div>
    <div>{{ testrole }}</div>
</template>
<script>
import { ref, onMounted } from 'vue'
import { getInfo } from '@/api/user' // 导入具体的API方法
import { getCatalog, testRole } from '@/api/catalog'
import { getUserInfo } from '@/userInfo'

export default {
    data() {
        return {
            userInfo: ref({}),
            testrole: ref({})
        }
    },
    methods() {

    },
    async mounted() {
        debugger
        console.log('home1 onMounted:')
        const token = getUserInfo().accessToken// sessionStorage.getItem('idToken')
        console.log('home1 onMounted 123:')
        if (token) {
            console.log('home1 idToken:' + token)
        }
        try {
            const data = await getCatalog() // 调用接口
            this.userInfo.value = data.userName + " " + getUserInfo().accessToken
            const testrolev = await testRole()
            this.testrole.value = testrolev + " idtoken:" + sessionStorage.getItem("idToken")
        } catch (error) {
            console.error('获取用户信息失败', error)
        } finally {
        }
    }
}
export const testState = ref({name: 'yanglu'})
</script>