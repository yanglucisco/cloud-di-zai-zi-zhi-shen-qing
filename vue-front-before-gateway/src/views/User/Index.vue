<template>
    <a-button @click="addOrg">新增机构</a-button>
    <a-button @click="addDict">新增字典</a-button>
    <a-button @click="addPosition">新增职位</a-button>
    <a-button @click="addUser">新增用户</a-button>
</template>
<script setup>
import request from '@/utils/request'
import { useMessage } from '@/utils/useMessage';
import { CodeFilled } from '@ant-design/icons-vue';

const { success, error, warning, loading } = useMessage();
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
            parentId: '662582222380994560',
            name: '女',
            code: 'FEMALE',
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
            account: 'yanglu1',
            name: '杨璐1',
            orgId: '662517722747375616',
            positionId: '662536651490332672'
        }
    }).then(res => {
        success(res)
        console.log(res)
    })
}
</script>