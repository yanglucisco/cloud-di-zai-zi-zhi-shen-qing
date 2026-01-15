<template>
    <h1>系统首页</h1>
    <MyTestMenu :user="myUser" />
</template>
<script setup>
import MyTestMenu from './Component/MyTestMenu.vue'
import request from '@/utils/request'
import { onMounted } from 'vue'
const myUser = [
    {
        deep: 1,
        text: '菜单1',
        children: [
            {
                deep: 2,
                text: '菜单1-1',
                children:
                    [
                        {
                            deep: 3,
                            text: '菜单1-1-1',
                            children: []
                        }
                    ]
            },
            {
                deep: 2,
                text: '菜单1-2',
                children: [
                    {
                        deep: 3,
                        text: '菜单1-2-1111',
                        children: []
                    }
                ]
            }
        ]
    }
]
onMounted(() => {
    request({
      url: '/rolemanage/sysresource/all',
      method: 'get'
    }).then(res => console.log(res))
})
</script>
