<template>
    <div v-for="item in user">
        <h1>{{ item.deep }} {{ item.text }}</h1>
        <!-- <h1>{{ item }}</h1> -->
        <MyTestMenu :user="item.children" />
    </div>
</template>
<script setup>
const props = defineProps({
    // 自定义对象属性
    user: {
        type: Array,
        default: () => ([
            {
                deep: 1,
                text: 'text1',
                children: []
            },
            {
                deep: 1,
                text: 'text2',
                children: []
            }
        ])
    }
})
// 访问嵌套属性
// console.log(props.user.name)
// console.log(props.user.address.city)
</script>