<template>
    <!-- <h1>layout index</h1> -->
    <!-- <div class="header">
        <RouterLink to="/home" style="margin: 10px">首页</RouterLink>
    </div>
    <div>
        <button @click="test">退出登录</button>
    </div> -->
    <div>
        <!-- <div>
            <p class="bg-lightBlue">
                成功集成 Tailwind CSS！
            </p>
        </div>
        <div>
            <button @click="btnClick">展开/收起</button>
            <h1>
                {{ menuIsCollapse }}
            </h1>
        </div> -->
        <a-layout>
            <a-layout-sider :collapsed="menuIsCollapseA" collapsible :theme="sideTheme" width="210">
                <header>
                    <h1>header</h1>
                </header>
            </a-layout-sider>
            <a-layout>
                <h1>h2</h1>
            </a-layout>
        </a-layout>
        <!-- <a-layout>
            <h1>2</h1>
        </a-layout> -->
    </div>
</template>
<script setup>
import { cleanToken, getUserIdToken } from '../userInfo'
import { RouterLink } from 'vue-router'
import { ThemeModeEnum } from '@/utils/enum'
import { computed, ref } from 'vue'
const menuIsCollapseA = ref(false)
// const theme = computed(() => {
//     return store.theme
// })
const sideTheme = computed(() => {
    return ThemeModeEnum.REAL_DARK
    // return ThemeModeEnum.REAL_DARK // ThemeModeEnum.DARK 
    // return theme.value === ThemeModeEnum.REAL_DARK ? ThemeModeEnum.DARK : theme.value
})
const menuIsCollapse = computed(() => {
    return menuIsCollapseA
})
const btnClick = () => {
    menuIsCollapseA.value = !menuIsCollapseA.value
    // console.log('menuIsCollapseA:' + menuIsCollapse.value)
}
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