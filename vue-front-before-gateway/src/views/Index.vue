<template>
    <h1>系统首页</h1>
    <MyTestMenu :user="myUser" />
</template>
<script setup>
import MyTestMenu from './Component/MyTestMenu.vue'
const myUser = {
    name: 'menu1',
    menu: [
        {
            deep: 1, text: '菜单1', children: {
                name: 'menu1-1',
                menu: [
                    {
                        deep: 2,
                        text: '菜单1-1',
                        children:
                        {
                            name: 'menu1-1-1',
                            menu: [
                                {
                                    deep: 3,
                                    text: '菜单1-1-1',
                                    children: []
                                }
                            ]
                        }
                    },
                    {
                        deep: 1,
                        text: '菜单1-2',
                        children: []
                    }
                ]
            }
        }
    ]
}
</script>
