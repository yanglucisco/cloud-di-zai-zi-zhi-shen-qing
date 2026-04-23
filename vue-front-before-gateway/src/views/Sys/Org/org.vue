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
                <a-space style="margin: 5px;">
                    <span>{{ orgNameText }}:</span>
                    <a-input v-model:value="orgNameSerachKeyWord" :placeholder="orgNameText" />
                    <a-button :icon="h(SearchOutlined)" type="primary" @click="find">{{ serachButtonText }}</a-button>
                    <a-button :icon="h(ReloadOutlined)">重 置</a-button>
                    <a-button :icon="h(ReloadOutlined)" @click="test">测 试</a-button>
                </a-space>
            </div>
            <div class="right-bottom">
                <a-space style="margin: 5px">
                    <a-button :icon="h(PlusOutlined)" type="primary" @click="addOrgFunc">新 增</a-button>
                    <a-button :icon="h(DeleteOutlined)" danger ghost>批量删除</a-button>
                </a-space>
                <a-table :columns="columns" :data-source="data" :row-selection="rowSelection"
                    :pagination="paginationConfig" @change="handleTableChange">
                    <template #headerCell="{ column }">
                        <template v-if="column.key === 'orgName'">
                            <span>
                                {{ orgNameText }}
                            </span>
                        </template>
                        <template v-if="column.key === 'classify'">
                            <span>
                                {{ classifyText }}
                            </span>
                        </template>
                        <template v-if="column.key === 'sort'">
                            <span>
                                {{ sortText }}
                            </span>
                        </template>
                        <template v-if="column.key === 'action'">
                            <span>
                                {{ actionText }}
                            </span>
                        </template>
                    </template>

                    <template #bodyCell="{ column, record }">
                        <template v-if="column.key === 'OrgName'">
                            <a>
                                {{ record.OrgName }}
                            </a>
                        </template>
                        <template v-else-if="column.key === 'tags'">
                            <span>
                                <a-tag v-for="tag in record.tags" :key="tag"
                                    :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'">
                                    {{ tag.toUpperCase() }}
                                </a-tag>
                            </span>
                        </template>
                        <template v-else-if="column.key === 'action'">
                            <span>
                                <a>编辑</a>
                                <a-divider type="vertical" />
                                <a style="color: red;">删除</a>
                            </span>
                        </template>
                    </template>
                </a-table>
                <add-org ref="addOrgRef"></add-org>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, watch, h, reactive, onMounted } from 'vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { useI18n } from 'vue-i18n'
import { getOrgData, getOrgData1, getAllOrgs } from '@/api/org'
import addOrg from './add.vue'
const { t } = useI18n()
const orgNameText = ref(t('org.orgName'))
const classifyText = ref(t('org.classify'))
const sortText = ref(t('org.sort'))
const actionText = ref(t('common.action'))
const serachButtonText = ref(t('common.searchButton'))
const addOrgRef = ref(null);
const orgNameSerachKeyWord = ref('')
const addOrgFunc = () => {
    if (addOrgRef.value) {
        addOrgRef.value.showDrawer();
    }
};
const test = () => {
    getAllOrgs();
};
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
const handleTableChange = (pag, filters, sorter) => {
    // 更新分页参数
    paginationConfig.current = pag.current;
    paginationConfig.pageSize = pag.pageSize;

    // 重新加载数据
    loadData(pag.current, pag.pageSize);
};
const find = () => {
    const data1 = getOrgData()
    paginationConfig.total = data1.length
    data.value = data1
}
watch(expandedKeys, () => {
});
watch(selectedKeys, () => {
});
watch(checkedKeys, () => {
});
const paginationConfig = reactive({
    current: 1,           // 当前页码
    pageSize: 10,         // 每页条数
    total: 100,           // 总数据量
    showSizeChanger: true, // 显示页数切换器
    showQuickJumper: true, // 显示快速跳转
    showTotal: (total) => `共 ${total} 条`, // 显示总数
    pageSizeOptions: ['10', '20', '50', '100'], // 页数选项
    onChange: (page, pageSize) => {
        paginationConfig.current = page;
        loadData(page, pageSize);
    }
})
const loadData = (page, pageSize) => {
    const data1 = getOrgData1(page, pageSize)
    // paginationConfig.total = data1.length
    data.value = data1
}
const columns = [
    {
        name: 'orgName',
        dataIndex: 'orgName',
        key: 'orgName',
    },
    {
        title: 'classify',
        dataIndex: 'classify',
        key: 'classify',
    },
    {
        title: 'sort',
        dataIndex: 'sort',
        key: 'sort',
    },
    {
        title: 'action',
        key: 'action',
        dataIndex: 'action',
    }
];
const data = ref([]);
const rowSelection = {
    // 选择框列配置
    columnWidth: 60,          // 选择列宽度
    fixed: true,              // 固定在最左侧
    // 选中行变化时的回调
    onChange: (selectedRowKeys, selectedRows) => {
    },
};
onMounted(() => {

})
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