<template>
    <h1>我是home的子组件的子组件 {{ name }}</h1>
</template>
<script setup>
import { inject } from 'vue';
const name = inject('obj').name
</script>