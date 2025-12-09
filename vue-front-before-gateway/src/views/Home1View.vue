<template>
    <span>首页11111</span>
    <!-- <h1>测试i18n {{ $t('message.searchButton') }}</h1> -->
    <div>
        <button @click="testPina">测试pina</button>
    </div>
    <div>{{ userInfo }}</div>
    <div>测试角色</div>
    <div>{{ testrole }}</div>
    <div>{{ pkceStore.currentVerifier }}</div>
    <div>
        <button @click="toHome">转到home页面</button>
    </div>
</template>
<script>
import { ref, onMounted } from 'vue'
import { getInfo } from '@/api/user' // 导入具体的API方法
import { getCatalog, testRole } from '@/api/catalog'
import { getUserInfo } from '@/userInfo'
import { usePkceStore } from '@/store/pkce'
import { useRouter } from 'vue-router'
// import { useI18n } from 'vue-i18n'

export default {
    data() {
        return {
            router: useRouter(),
            pkceStore: usePkceStore(),
            userInfo: ref({}),
            testrole: ref({})
        }
    },
    methods: {
        testPina() {
            this.pkceStore.setVer()
        },
        toHome() {
            this.router.push('/home')
        }
    },
    async mounted() {
        const token = getUserInfo().accessToken// sessionStorage.getItem('idToken')
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
export const testState = ref({ name: 'yanglu' })
</script>