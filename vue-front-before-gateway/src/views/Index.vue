<template>
    <h1>系统首页 {{ title }}</h1>
</template>
<script setup>
import request from '@/utils/request'
import { onMounted, ref } from 'vue'
const title = ref('')
onMounted(() => {
    request({
      url: '/rolemanage/sysresource/test',
      method: 'get'
    }).then(res => {
        console.log(res)
        title.value = res // res.length + " 个 " 
    })
})
</script>
