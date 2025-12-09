<template>
    <h1>我是home的子组件 {{ msg }}</h1>

    <ChildOfChildOfHomeView></ChildOfChildOfHomeView>
</template>
<script setup>
import ChildOfChildOfHomeView from './ChildOfChildOfHomeView.vue'
import { inject } from 'vue';

const msg = inject('msg')

</script>