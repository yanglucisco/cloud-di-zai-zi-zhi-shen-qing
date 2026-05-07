<!-- components/DynamicBreadcrumb.vue -->
<template>
  <a-breadcrumb>
    <!-- 遍历动态生成的面包屑列表 -->
    <a-breadcrumb-item v-for="(route, index) in breadcrumbs" :key="index">
      <!-- 使用动态组件渲染图标 -->
      <component :is="route.meta?.icon" v-if="route.meta?.icon" style="margin-right: 4px;" />
      <!-- 如果是最后一项，只显示名称，不可点击 -->
      <span v-if="index === breadcrumbs.length - 1">{{ route.meta?.breadcrumbName }}</span>
      <!-- 否则，使用 router-link 实现点击跳转 -->
      <router-link v-else :to="route.path">
        {{ route.meta?.breadcrumbName }}
      </router-link>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

// 核心逻辑：通过计算属性监听 route.matched 的变化
// route.matched 是一个数组，包含了当前路由路径上所有匹配的路由记录
const breadcrumbs = computed(() => {
  // 过滤掉没有设置 breadcrumbName 的路由，并按需添加“首页”
  let matched = route.matched.filter(item => item.meta && item.meta.breadcrumbName);
  
  // 可选：如果第一个面包屑不是“首页”，可以手动添加
  const first = matched[0];
  if (!first || first.meta.breadcrumbName !== '首页') {
     // 这里可以根据项目需求决定是否添加一个默认的“首页”
     // matched = [{ path: '/', meta: { breadcrumbName: '首页' } }, ...matched];
  }
  
  return matched;
});
</script>