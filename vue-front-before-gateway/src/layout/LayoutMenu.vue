<template>
    <div v-for="menu in menuList">
        <a-sub-menu :key="menu.name" v-if="menu.type === 'subMenu'">
            <template #title>
                <component :is="icons.get(menu.icon)" />
                <span>
                    {{ menu.title }}
                </span>
            </template>
            <LayoutMenu :menuList="menu.children"></LayoutMenu>
        </a-sub-menu>
        <a-menu-item :key="menu.name" v-else @click="clickMenu(menu)">
            <component :is="icons.get(menu.icon)" />
            <span>{{ menu.title }}</span>
        </a-menu-item>
    </div>
</template>
<script setup>
import { ref } from 'vue'
import {
    MenuUnfoldOutlined, MenuFoldOutlined, VideoCameraOutlined, UserOutlined, UploadOutlined, AppstoreAddOutlined,
    AppstoreOutlined, HomeOutlined, SettingOutlined
} from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { useMessage } from '@/utils/useMessage'

const router = useRouter()
const { success, error, warning, loading } = useMessage()
const props = defineProps({
    // 自定义对象属性
    menuList: {
        type: Array,
        default: () => (
            [
                {
                    name: 'index',
                    type: 'menu',
                    icon: 'UserOutlined',
                    title: '首页',
                    children: [
                        {
                            name: 'index',
                            type: 'menu',
                            icon: 'UserOutlined',
                            title: '首页',
                            children: []
                        }
                    ]
                }
            ]
        )
    }
})
const icons = new Map()
icons.set('UserOutlined', UserOutlined)
const selectedKeys = ref([])
const clickMenu = (item) => {
    success(item.name)
    router.push(item.path)
}
</script>