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
import { ref, computed } from 'vue';
import { orgDataStore } from '@/store/orgData';

const value = ref(undefined);
const orgData = orgDataStore();
const treeData = computed(() => orgData.treeData);
const emit = defineEmits([
  'clear'
])
const handleChange = (val, label, extra) => {
  orgData.setCurrentNodeValue(val);
  if(val === undefined){
    emit('clear');
  }
};
const setCurrentNode = (currentNode) => {
  value.value = currentNode;
  orgData.setCurrentNodeValue(currentNode);
};
defineExpose({
  setCurrentNode
})
</script>