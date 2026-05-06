<template>
    <div class="container">
        <div class="left">
            <a-tree v-model:expandedKeys="expandedKeys" v-model:selectedKeys="selectedKeys"
                v-model:checkedKeys="checkedKeys" :tree-data="treeData" @select="handleTreeSelect">
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
                    <a-button :icon="h(SearchOutlined)" type="primary" :loading="tableState.loading" @click="findByName">{{ serachButtonText
                        }}</a-button>
                    <a-button :icon="h(ReloadOutlined)" @click="reset">重 置</a-button>
                    <a-button :icon="h(ReloadOutlined)" @click="test">测 试</a-button>
                </a-space>
            </div>
            <div class="right-bottom">
                <a-space style="margin: 5px">
                    <a-button :icon="h(PlusOutlined)" type="primary" @click="addOrgFunc">新 增</a-button>
                    <!-- Updated Batch Delete Button -->
                    <a-popconfirm title="确定要批量删除选中的机构吗?" ok-text="确定" cancel-text="取消" @confirm="handleBatchDelete">
                        <a-button :icon="h(DeleteOutlined)" danger ghost>批量删除</a-button>
                    </a-popconfirm>
                </a-space>
                <a-table :columns="columns" 
                         :data-source="data" 
                         :row-selection="{ selectedRowKeys: tableState.selectedRowKeys, onChange: onSelectChange }"
                         :pagination="paginationConfig" @change="handleTableChange">
                    <template #headerCell="{ column }">
                        <template v-if="column.key === 'name'">
                            <span>
                                {{ orgNameText }}
                            </span>
                        </template>
                        <template v-if="column.key === 'category'">
                            <span>
                                {{ classifyText }}
                            </span>
                        </template>
                        <template v-if="column.key === 'sortCode'">
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
                                <a @click="editOrg(record)">编辑</a>
                                <a-divider type="vertical" />
                                <!-- Add Popconfirm for safety -->
                                <a-popconfirm title="确定要删除该机构吗?" ok-text="确定" cancel-text="取消"
                                    @confirm="handleDelete(record)">
                                    <a style="color: red;">删除</a>
                                </a-popconfirm>
                            </span>
                        </template>
                    </template>
                </a-table>
                <add-org ref="addOrgRef" @success="handleAddSuccess"></add-org>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, watch, h, reactive, onMounted, getCurrentInstance } from 'vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { useI18n } from 'vue-i18n'
import { getAllOrgs, findAllListOrgs, deleteOrgByIds } from '@/api/org'
import addOrg from './add.vue'
import { orgDataStore } from '@/store/orgData';
import { useMessage } from '@/utils/useMessage';

const { success, error, warning, loading } = useMessage()
const orgData = orgDataStore();
const { t } = useI18n()
const orgNameText = ref(t('org.orgName'))
const classifyText = ref(t('org.classify'))
const sortText = ref(t('org.sort'))
const actionText = ref(t('common.action'))
const serachButtonText = ref(t('common.searchButton'))
const addOrgRef = ref(null);
const orgNameSerachKeyWord = ref('')

const tableState = reactive({
  selectedRowKeys: [],
  // Check here to configure the default column
  loading: false,
});
const addOrgFunc = () => {
    if (addOrgRef.value) {
        addOrgRef.value.showDrawer();
    }
};
const test = () => {
    data.value = [{key: 1,
        name: 'name2',
        category: 'category2',
        sortCode: 'sortCode2',
        action: 'action2'
    },
    {key: 2,
        name: 'name2',
        category: 'category2',
        sortCode: 'sortCode2',
        action: 'action2'
    }];
};
// Handle tree selection
const handleTreeSelect = (keys, info) => {
    if (keys.length > 0) {
        // Reset to page 1 when filtering by tree node
        paginationConfig.current = 1;
        find(1, paginationConfig.pageSize, orgNameSerachKeyWord.value, info.node.id);
    }
};
// Handle single delete
const handleDelete = async (record) => {
    try {
        await deleteOrgByIds([record.id]); // Assuming record has an 'id' field
        success('删除成功');
        // Refresh current page data
        find(paginationConfig.current, paginationConfig.pageSize);
    } catch (error) {
        console.error("Delete failed:", error);
        error('删除失败');
    }
};
const onSelectChange = selectedRowKeys => {
  tableState.selectedRowKeys = selectedRowKeys;
};
// Handle batch delete
const handleBatchDelete = async () => {
    if (!tableState.selectedRowKeys || tableState.selectedRowKeys.length === 0) {
        warning('请先选择要删除的机构');
        return;
    }

    try {
        await deleteOrgByIds(tableState.selectedRowKeys);
        success('批量删除成功');
        // Clear selection
        tableState.selectedRowKeys = [];
        // Refresh current page data
        find(paginationConfig.current, paginationConfig.pageSize);
    } catch (error) {
        console.error("Batch delete failed:", error);
        error('批量删除失败');
    }
};
const treeData = ref([]);
const expandedKeys = ref(['0-0-0', '0-0-1']);
const selectedKeys = ref(['0-0-0', '0-0-1']);
const checkedKeys = ref(['0-0-0', '0-0-1']);
const handleTableChange = (pag, filters, sorter) => {
    // 更新分页参数
    paginationConfig.current = pag.current;
    paginationConfig.pageSize = pag.pageSize;

    // 重新加载数据
    find(pag.current, pag.pageSize);
};

const findByName = () => {
    find(paginationConfig.current, paginationConfig.pageSize, orgNameSerachKeyWord.value);
    success('查询机构成功!');
};
const reset = () => {
    orgNameSerachKeyWord.value = '';
    findByName();
};
// New Edit Function
const editOrg = (record) => {
    if (addOrgRef.value) {
        // Pass the record to the child component to populate the form
        addOrgRef.value.showDrawer(record);
    }
};
const find = async (page, pageSize, orgName = '', selectId = '') => {
    tableState.loading = true;
    try {
        const res = await findAllListOrgs({
            name: orgName,
            page: page,
            pageSize: pageSize,
            parentId: selectId
        });
        // Assuming res contains the list and possibly total count
        // Adjust based on your actual API response structure
        data.value = res.list || res;
        paginationConfig.total = res.total || res.length;

    } catch (error) {
        console.error("Failed to load org data:", error);
    } finally {
        tableState.loading = false;
    }
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
        find(page, pageSize);
    }
})
const columns = [
    {
        name: 'name',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'category',
        dataIndex: 'category',
        key: 'category',
    },
    {
        title: 'sortCode',
        dataIndex: 'sortCode',
        key: 'sortCode',
    },
    {
        title: 'action',
        dataIndex: 'action',
        key: 'action',
    }
];
const data = ref([
]);
const handleAddSuccess = () => {
    find(paginationConfig.current, paginationConfig.pageSize);
};
onMounted(async () => {
    try {
        const res = await getAllOrgs();
        treeData.value = res;
        orgData.setTreeData(res);
        find(1, 10);
    } catch (error) {
        console.error("Failed to load tree data:", error);
    }
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