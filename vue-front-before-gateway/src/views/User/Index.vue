<template>
    <a-button @click="addOrg">新增机构</a-button>
    <a-button @click="addDict">新增字典</a-button>
    <a-button @click="addPosition">新增职位</a-button>
    <a-button @click="addUser">新增用户</a-button>
    <a-button @click="addRole">新增角色</a-button>
    <a-button @click="getAllOrgs">获取所有机构</a-button>
    <a-button @click="addMoudle">新增模块</a-button>
    <a-button @click="getAllResources">获取所有资源</a-button>
    <div>
        <a-button @click="addMenu">新增菜单</a-button>
        <a-button @click="addMulu">新增目录</a-button>
        <a-button @click="addZiCaiDan">新增子菜单</a-button>
    </div>
</template>
<script setup>
import request from '@/utils/request'
import { useMessage } from '@/utils/useMessage';
import { CodeFilled } from '@ant-design/icons-vue';

const { success, error, warning, loading } = useMessage();
const addMenu = () => {
    request({
        url: '/rolemanage/sysmenu/add',
        method: 'post',
        data: {
            menuType: "MENU",
            visible: "TRUE",
            sortCode: 2,
            title: "系统首页",
            parentId: 0,
            module: "662992182143229952",
            path: "/index",
            component: "index/index",
            name: "index",
            icon: "home-outlined"
        }
    }).then(res => { 
        success(res)
    })
}
const addMulu = () => {
    request({
        url: '/rolemanage/sysmenu/add',
        method: 'post',
        data: {
            menuType: "MULU",
            visible: "TRUE",
            sortCode: 4,
            title: "组织架构",
            parentId: 0,
            icon: "apartment-outlined",
            module: "662992182143229952",
            name: 'ORG'
        }
    }).then(res => { 
        success(res)
    })
}
const addZiCaiDan = () => {
    request({
        url: '/rolemanage/sysmenu/add',
        method: 'post',
        data: {
            menuType: "MENU",
            visible: "TRUE",
            sortCode: 2,
            title: "职位管理",
            parentId: '663266670017650688',
            module: "662992182143229952",
            path: "/sys/position",
            component: "sys/position/index",
            name: "sysPosition",
            icon: "home-outlined"
        }
    }).then(res => { 
        success(res)
    })
}
const addOrg = () => {
    request({
        url: '/account/org/add',
        method: 'post',
        data: {
            sortCode: 1,
            parentId: '0',
            name: '地质一队',
            category: 'COMPANY'
        }
    }).then(res => {
        success(res)
        console.log(res)
    })
}
const addDict = () => {
    request({
        url: '/account/dict/add',
        method: 'post',
        data: {
            sortCode: 1,
            parentId: '663203487814914048',
            name: '外链',
            code: 'WAI_LIAN',
        }
    }).then(res => {
        success(res)
        console.log(res)
    })
}
const addPosition = () => {
    request({
        url: '/account/position/add',
        method: 'post',
        data: {
            sortCode: 1,
            orgId: '662517722747375616',
            name: '报盘申请',
            category: 'JI_CENG'
        }
    }).then(res => {
        success(res)
        console.log(res)
    })
}
const addUser = () => {
    request({
        url: '/account/sysUser/add',
        method: 'post',
        data: {
            gender: 'MALE',
            account: 'yanglu',
            name: '杨璐',
            orgId: '662517722747375616',
            positionId: '662536651490332672'
        }
    }).then(res => {
        success(res)
        console.log(res)
    })
}
const addRole = () => {
    request({
        url: '/rolemanage/sysrole/add',
        method: 'post',
        data: {
            sortCode: 1,
            // orgId: '662517722747375616',
            name: '测试角色系统管理员',
            category: 'TEST_ROLE_SYS_ADMIN'
        }
    }).then(res => {
        success(res)
    })
}
const getAllResources = () => {
    request({
        url: '/rolemanage/sysresource/all',
        method: 'get'
    }).then(res => {
        success(res)
    })
}
const getAllOrgs = () => {

    request({
        url: '/account/org/all',
        method: 'get'
    }).then(res => {
        success(res)
    })
}
const addMoudle = () => {
    request({
        url: '/rolemanage/sysmodule/add',
        method: 'post',
        data: {
            sortCode: 1,
            title: '业务',
            icon: 'CodeFilled',
            color: '#000000',
        }
    }).then(res => {
        success(res)
    })
}
</script>