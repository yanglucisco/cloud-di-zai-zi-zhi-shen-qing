<template>
    <a-button @click="registUser">注册用户</a-button>
</template>
<script setup>
import request from '@/utils/request'
import { getUserInfo } from "@/userInfo/index"
const registUser = () => {
    const r = getUserInfo()
    debugger
    request({
        url: '/account/org/add',
        method: 'post',
        data: {
            sortCode: 1,
            parentId: 0,
            name: 'name1',
            category: '类别'
        }
    }).then(res => {
        console.log(res)
    })
}
</script>