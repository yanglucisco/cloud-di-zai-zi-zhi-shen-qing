<template>
    <a-layout style="margin: 3px; padding: 3px; background-color: transparent; border-radius: 10px;">
        <a-layout-sider class="left-column" v-model:collapsed="collapsed" :trigger="null" collapsible>
            <div class="logo">
                <img class="img"
                    src="data:image/png;base64,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" />
                <div>
                    <br />
                    <span class="title" v-if="!collapsed">地灾资质管理系统</span>
                </div>
            </div>
            <div class="scrollable-menu-container">
                <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline" v-for="menu in menuList">
                    <a-sub-menu :key="menu.name" v-if="menu.type === 'subMenu'">
                        <template #title>
                            <component :is="icons.get(menu.icon)" />
                            <span>
                                {{ menu.title }}
                            </span>
                        </template>
                        <div v-for="item in menu.children">
                            <a-menu-item :key="item.name" @click="clickMenu(item)">
                                <component :is="icons.get(menu.icon)" />
                                <span>{{ menu.title }}</span>
                            </a-menu-item>
                        </div>
                    </a-sub-menu>
                    <a-menu-item :key="menu.name" v-else @click="clickMenu(menu)">
                        <component :is="icons.get(menu.icon)" />
                        <span>{{ menu.title }}</span>
                    </a-menu-item>
                </a-menu>
            </div>
        </a-layout-sider>
        <a-layout class="right-column">
            <a-layout-header class="contentheader">
                <div style="display: flex;">
                    <menu-unfold-outlined style="margin-left: 5px;" v-if="collapsed" class="trigger"
                        @click="() => (collapsed = !collapsed)" />
                    <menu-fold-outlined style="margin-left: 5px;" v-else class="trigger"
                        @click="() => (collapsed = !collapsed)" />
                    <a-menu class="layout-items-center">
                        <a-menu-item key="xiTong" style="width: auto; overflow: visible;">
                            <AppstoreAddOutlined />
                            <span>系统</span>
                        </a-menu-item>
                        <a-menu-item key="yeWu">
                            <AppstoreOutlined />
                            <span>业务</span>
                        </a-menu-item>
                    </a-menu>
                </div>
                <div style="display: flex;">
                    <UserBar></UserBar>
                </div>
            </a-layout-header>
            <a-layout-content class="content">
                <router-view></router-view>
            </a-layout-content>
        </a-layout>
    </a-layout>
</template>
<script setup>
import { ref } from 'vue'
import {
    MenuUnfoldOutlined, MenuFoldOutlined, VideoCameraOutlined, UserOutlined, UploadOutlined, AppstoreAddOutlined,
    AppstoreOutlined, HomeOutlined, SettingOutlined
} from '@ant-design/icons-vue'

import UserBar from './UserBar.vue'
import { useRouter } from 'vue-router'
import { useMessage } from '@/utils/useMessage';
import { onMounted } from 'vue'
import request from '@/utils/request'
import { sysinfoStore } from '@/store/sysinfo'
import appConfig from '@/store/Singleton'
const icons = new Map()
icons.set('UserOutlined', UserOutlined)
const { success, error, warning, loading } = useMessage();

const menuList = ref([]);
const router = useRouter()
const selectedKeys = ref([])
const collapsed = ref(false)
const clickMenu = (item) => {
    success(item.name)
    router.push(item.path)
}
// mounted 生命周期
onMounted(() => {
    menuList.value = appConfig.getData('menus')
})
</script>
<style scoped>
/* 可滚动的菜单容器 */
.scrollable-menu-container {
    height: 100%;
    /* 继承父容器(.app-sider)的高度 */
    overflow-y: auto;
    /* 关键：启用垂直滚动条 */
    /* (可选) 隐藏滚动条样式，保持界面简洁 */
    /* 兼容 Firefox */
}

.left-column {
    height: calc(100vh - 64px);
    border-radius: 10px 0px 0px 10px;
}

.right-column {
    height: calc(100vh - 64px);
    border-radius: 0px 10px 10px 0px;
}

.contentheader {
    border-radius: 0px 10px 0px 0px;
    background: #fff;
    padding: 0;
    display: flex;
    justify-content: space-between;
}

.heder .trigger {
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    cursor: pointer;
    transition: color 0.3s;
}

.heder .trigger:hover {
    color: #1890ff;

}

.logo .img {
    height: 36px;
    margin: 10px 10px;
}

.logo {
    display: flex;
}

.site-layout .site-layout-background {
    background: #fff;
}

.title {
    color: #fff;
}

.layout-items-center {
    display: flex;
    align-items: center;
}

.content {
    /* :style="{ margin: '1px 1px', padding: '3px', background: '#fff', minHeight: '280px' } */
    margin: 10px;
    padding: 3px;
    background-color: #fff;
    min-height: 280px;
    border-radius: 0px 0px 10px 0px;
}
</style>