<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="header">
    <RouterLink to="/home" style="margin: 10px">首页</RouterLink>
  </div>
  <div>
    <RouterView />
  </div>
</template>

<style scoped>
.header {
  vertical-align: middle;
  text-align: center;
  margin: 10px;
}
</style>
