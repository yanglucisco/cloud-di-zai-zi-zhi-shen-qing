<template>
    <div class="container">
        <div class="left">
            <a-tree v-model:expandedKeys="expandedKeys" v-model:selectedKeys="selectedKeys"
                v-model:checkedKeys="checkedKeys" :tree-data="treeData">
                <template #title="{ title, key }">
                    <span>{{ title }}</span>
                </template>
            </a-tree>
        </div>
        <div class="right">
            <div class="right-top">
                <a-space>
                    <span>名称关键词：</span>
                    <a-input v-model:value="value" placeholder="请输入组织名称关键词" />
                    <a-button :icon="h(SearchOutlined)" type="primary">查 询</a-button>
                    <a-button :icon="h(ReloadOutlined)" >重 置</a-button>
                </a-space>
            </div>
            <div class="right-bottom">右下</div>
        </div>
    </div>
</template>
<script setup>
import { ref, watch, h } from 'vue';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue';
const treeData = [
    {
        title: 'parent 1',
        key: '0-0',
        children: [
            {
                title: 'parent 1-0',
                key: '0-0-0',
                // disabled: true,
                children: [
                    {
                        title: 'leaf',
                        key: '0-0-0-0',
                        disableCheckbox: true,
                    },
                    {
                        title: 'leaf',
                        key: '0-0-0-1',
                    },
                ],
            },
            {
                title: 'parent 1-1',
                key: '0-0-1',
                children: [
                    {
                        key: '0-0-1-0',
                        title: 'sss',
                    },
                ],
            },
        ],
    },
];
const expandedKeys = ref(['0-0-0', '0-0-1']);
const selectedKeys = ref(['0-0-0', '0-0-1']);
const checkedKeys = ref(['0-0-0', '0-0-1']);
watch(expandedKeys, () => {
    console.log('expandedKeys', expandedKeys);
});
watch(selectedKeys, () => {
    console.log('selectedKeys', selectedKeys);
});
watch(checkedKeys, () => {
    console.log('checkedKeys', checkedKeys);
});
</script>
<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.container {
    display: flex;
    min-height: 300px;
    background-color: rgb(245, 245, 245);
}

.left {
    width: 250px;
    margin-right: 10px;
    background-color: white;
    /* 左侧固定宽度 */
}

.right {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.right-top {
    /* height: 50px; */
    background-color: white;
    /* 右上固定高度 */
    margin-bottom: 10px;
}

.right-bottom {
    flex: 1;
    background-color: white;
    /* 右下自适应 */
}
</style>