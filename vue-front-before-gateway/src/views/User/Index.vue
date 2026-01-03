<template>
    <a-button @click="addOrg">新增机构</a-button>
    <a-button @click="addDict">新增字典</a-button>
    <a-button @click="addPosition">新增职位</a-button>
</template>
<script setup>
import request from '@/utils/request'
import { useMessage } from '@/utils/useMessage';

const { success, error, warning, loading } = useMessage();
const addOrg = () => {
    request({
        url: '/account/org/add',
        method: 'post',
        data: {
            sortCode: 1,
            parentId: '662110244318613506',
            name: '机构名称',
            category: '机构类别'
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
            parentId: '662110244318613506',
            name: '字典名称',
            category: '字典类别'
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
            orgId: '662110244318613506',
            name: '职位名称',
            category: '职位类别'
        }
    }).then(res => {
        success(res)
        console.log(res)
    })
}
</script>