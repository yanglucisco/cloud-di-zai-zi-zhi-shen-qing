<template>
    <span>首页11111</span>
    <div>{{ userInfo }}</div>
</template>
<script setup>
import { onMounted } from 'vue'

onMounted(async () => {
    console.log('home1 onMounted:')
    const token = sessionStorage.getItem('token')
    console.log('home1 onMounted 123:')
    if(token){
        console.log('home1 token:' + token)
        return
    }
})
</script>