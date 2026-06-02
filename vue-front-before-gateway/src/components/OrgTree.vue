<template>
  <div class="org-tree-wrapper" :key="treeKey">
    <a-tree
      v-model:expandedKeys="expandedLocal"
      v-model:selectedKeys="selectedLocal"
      v-model:checkedKeys="checkedLocal"
      :tree-data="computedTreeData"
      @select="handleSelect"
    >
      <template #title="{ title, key }">
        <span>{{ title }}</span>
      </template>
    </a-tree>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { getAllOrgs } from '@/api/org'
import { orgDataStore } from '@/store/orgData'

const props = defineProps({
  /** 外部传入的树数据；不传则自动从 store 或 API 加载 */
  treeData: {
    type: Array,
    default: null,
  },
  /** 是否在组件挂载时自动加载树数据（仅当未传入 treeData 时有效） */
  autoLoad: {
    type: Boolean,
    default: true,
  },
  /** 外部控制展开节点（不传则内部管理） */
  expandedKeys: {
    type: Array,
    default: null,
  },
  /** 外部控制选中节点（不传则内部管理） */
  selectedKeys: {
    type: Array,
    default: null,
  },
  /** 外部控制勾选节点（不传则内部管理） */
  checkedKeys: {
    type: Array,
    default: null,
  },
})

const emit = defineEmits([
  'select',
  'update:expandedKeys',
  'update:selectedKeys',
  'update:checkedKeys',
])

const orgData = orgDataStore()
const localTreeData = ref([])

/** 树数据：优先 prop，其次本地 */
const computedTreeData = computed(() => props.treeData ?? localTreeData.value)

// ============ 展开 / 选中 / 勾选 状态（外部优先，否则内部管理） ============
const expandedLocal = ref([])
const selectedLocal = ref([])
const checkedLocal = ref([])

// 从 props 初始化内部状态
const initFromProps = () => {
  expandedLocal.value = props.expandedKeys ?? []
  selectedLocal.value = props.selectedKeys ?? []
  checkedLocal.value = props.checkedKeys ?? []
}
onMounted(initFromProps)

// 外部 props 变化时同步到内部
watch(() => props.expandedKeys, (val) => { if (val !== null) expandedLocal.value = val })
watch(() => props.selectedKeys, (val) => { if (val !== null) selectedLocal.value = val })
watch(() => props.checkedKeys, (val) => { if (val !== null) checkedLocal.value = val })

// 内部状态变化时通知外部
watch(expandedLocal, (val) => emit('update:expandedKeys', val))
watch(selectedLocal, (val) => emit('update:selectedKeys', val))
watch(checkedLocal, (val) => emit('update:checkedKeys', val))

const handleSelect = (keys, info) => {
  emit('select', keys, info)
}

// 强制刷新 key，解决某些情况下 a-tree 数据更新后不刷新问题
const treeKey = ref(0)

onMounted(async () => {
  // 如果外部给了 treeData，不再自动加载
  if (props.treeData) return

  if (props.autoLoad) {
    if (orgData.treeData && orgData.treeData.length > 0) {
      localTreeData.value = orgData.treeData
    } else {
      try {
        const res = await getAllOrgs()
        localTreeData.value = res
        orgData.setTreeData(res)
        // 数据加载后刷新 key 强制 a-tree 重新渲染
        treeKey.value += 1
      } catch (error) {
        console.error('加载组织树数据失败:', error)
      }
    }
  }
})
</script>

<style scoped>
.org-tree-wrapper {
  width: 250px;
  background-color: white;
  overflow-y: auto;
}
</style>
