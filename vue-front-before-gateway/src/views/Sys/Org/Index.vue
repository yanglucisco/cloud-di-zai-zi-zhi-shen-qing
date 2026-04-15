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
            <div class="right-top">右上</div>
            <div class="right-bottom">右下</div>
        </div>
    </div>
</template>
<script setup>
import { ref, watch } from 'vue';
const treeData = [
    {
        title: 'parent 1',
        key: '0-0',
        children: [
            {
                title: 'parent 1-0',
                key: '0-0-0',
                disabled: true,
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
    min-height: 60vh;
}

.left {
    width: 250px;
    background-color: antiquewhite;
    /* 左侧固定宽度 */
}

.right {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.right-top {
    height: 200px;
    background-color: lightcyan;
    /* 右上固定高度 */
}

.right-bottom {
    flex: 1;
    background-color: linen;
    /* 右下自适应 */
}
</style>