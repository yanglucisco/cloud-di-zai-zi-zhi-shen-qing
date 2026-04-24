<template>
  <a-tree-select v-model:value="value" show-search style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择上级" allow-clear
    tree-default-expand-all :tree-data="treeData" tree-node-filter-prop="label" @change="handleChange">
    <template #title="{ value: val, label }">
      {{ label }}
    </template>
  </a-tree-select>
</template>
<script setup>
import { ref, onMounted, onUnmounted, getCurrentInstance } from 'vue';
import { orgDataStore } from '@/store/orgData';

const value = ref({});
const orgData = orgDataStore();
const treeData = ref([]);
const handleChange = (value, label, extra) => {
  console.log('change 事件触发，当前值：', value);
  orgData.setCurrentNodeValue(value);
};
onMounted(() => {
  treeData.value = orgData.treeData;
});
onUnmounted(() => {
});
</script>