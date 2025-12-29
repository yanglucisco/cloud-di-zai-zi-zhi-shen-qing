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
        url: '/account/mytest/test',
        method: 'get',
    }).then(res => {
        console.log(res)
    })
}
</script>