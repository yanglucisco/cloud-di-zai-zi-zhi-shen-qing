<template>
  <a-tree-select v-model:value="value" show-search style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="Please select" allow-clear
    tree-default-expand-all :tree-data="treeData" tree-node-filter-prop="label">
    <template #title="{ value: val, label }">
      <b v-if="val === 'parent 1-1'" style="color: #08c">sss</b>
      <template v-else>{{ label }}</template>
    </template>
  </a-tree-select>
</template>
<script setup>
import { ref, onMounted, onUnmounted, getCurrentInstance } from 'vue';
import { orgDataStore } from '@/store/orgData';

const orgData = orgDataStore();
const updateData = (data) => {
  treeData.value = data;
};
const treeData = ref([
  {
    label: 'root 1',
    value: 'root 1',
    children: [
      {
        label: 'parent 1',
        value: 'parent 1',
        children: [
          {
            label: 'parent 1-0',
            value: 'parent 1-0',
            children: [
              {
                label: 'my leaf',
                value: 'leaf1',
              },
              {
                label: 'your leaf',
                value: 'leaf2',
              },
            ],
          },
          {
            label: 'parent 1-1',
            value: 'parent 1-1',
          },
        ],
      },
      {
        label: 'parent 2',
        value: 'parent 2',
      },
    ],
  },
]);
onMounted(() => {
  treeData.value = orgData.treeData;
});
onUnmounted(() => {
});
</script>