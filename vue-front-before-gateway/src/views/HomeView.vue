<template>
    <span>首页</span>
    <div>{{ userInfo }}</div>
    <div>pina {{ pkceStore.currentVerifier }}</div>
    <h1>测试i18n {{ t('model.user') }}</h1>
    <button @click="changeLanguage('en')">English</button>
    <button @click="changeLanguage('zh-cn')">中文</button>
    <ChildOfHomeView></ChildOfHomeView>
</template>
<script setup>
import { ref, onMounted, provide } from 'vue'
import { getInfo } from '@/api/user' // 导入具体的API方法
import { getCatalog } from '@/api/catalog'
import { getCurrentVerifier } from '../utils/pkce-util'
import { useRouter } from 'vue-router'
import { getUserInfo } from '@/userInfo'
import { testState } from './Home1View.vue'
import { usePkceStore } from '@/store/pkce'
import ChildOfHomeView from './ChildOfHomeView.vue'
import { useI18n } from 'vue-i18n'

const { t, locale } = useI18n()


const userInfo = ref(null)
const pkceStore = usePkceStore()
const msg = provide('msg', 'hello')
const obj = provide('obj', {id: 'id1', name: 'name1'})

const changeLanguage = (lang) => {
  locale.value = lang
}

onMounted(async () => {
    console.log('pkce 的值: ' + pkceStore.currentVerifier)
    // console.log('home onMounted:')
    // const token = sessionStorage.getItem('token')
    // console.log('home onMounted 123:')
    // if(token){
    //     console.log('home token:' + token)
    //     return
    // }
    // const queryString = window.location.search
    // const params = new URLSearchParams(queryString)

    // const code = params.get('code') // "alice"
    // if(code){ //从授权服务器返回来的
    //     exchangeCode(code)
    //     return
    // }
    try {
        const data = await getInfo() // 调用接口
        userInfo.value = testState.value.name + data + " " + getUserInfo().accessToken
    } catch (error) {
        console.error('获取用户信息失败', error)
    } finally {
    }
})
</script>