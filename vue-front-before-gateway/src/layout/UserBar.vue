<template>
    <div class="user-bar">
        <div class="item user-avatar">
            <FullscreenOutlined v-if="!isFullScreen" @click="fullScreenClick" />
            <FullscreenExitOutlined v-else @click="fullScreenClick" />
        </div>
        <a-dropdown class="item">
            <div class="user-avatar">
                <a-avatar>
                    超管
                </a-avatar>
            </div>
            <template #overlay>
                <a-menu>
                    <a-menu-item key="uc" @click="handleUser('uc')">
                        <UserOutlined style="margin-right: 8px" />
                        <span>个人中心</span>
                    </a-menu-item>
                    <a-menu-item key="clearCache" @click="handleUser('clearCache')">
                        <loading3-quarters-outlined style="margin-right: 8px" />
                        <span>清理缓存</span>
                    </a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="outLogin" @click="logout">
                        <export-outlined style="margin-right: 8px" />
                        <span>退出登录</span>
                    </a-menu-item>
                </a-menu>
            </template>
        </a-dropdown>
    </div>
</template>
<script setup>
import { ref } from 'vue'
import screenFull from 'screenfull'
import {
    FullscreenOutlined, FullscreenExitOutlined, UserOutlined, Loading3QuartersOutlined, ExportOutlined
} from '@ant-design/icons-vue'
import { getUserIdToken, cleanToken } from '../userInfo/index'
const isFullScreen = ref(false)
const fullScreenClick = () => {
    isFullScreen.value = !isFullScreen.value
    const element = document.documentElement
    if (screenFull.isEnabled) {
        screenFull.toggle(element)
    }
}
const logout = () => {
    console.log('cleanToken')
    const idToken = getUserIdToken()
    window.location.href = `http://auth-server:20001/connect/logout?id_token_hint=${idToken}&post_logout_redirect_uri=http://vue-front-before-gateway.clouddizai.com:20005`
    cleanToken()
    console.log('注销成功')
}
</script>
<style lang="less" scoped>
.user-bar {
    display: flex;
    align-items: center;
    height: 100%;
    margin: 0 10px;
}

.user-bar .item:hover {
    background-color: #d2e1f1;
    /* 悬浮时的背景色，例如浅蓝色 */
}

.user-bar .user-avatar {
    height: 45px;
    display: flex;
    align-items: center;
    margin: 10px 10px;
    width: 45px;
    justify-content: center;
}

.user-bar .user-avatar label {
    display: inline-block;
    margin-left: 5px;
    cursor: pointer;
}

.panel-item {
    padding: 0 10px;
    cursor: pointer;
    height: 100%;
    display: flex;
    align-items: center;
}
</style>
