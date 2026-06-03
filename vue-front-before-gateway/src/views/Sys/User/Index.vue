<template>
  <div class="container">
    <div class="left">
      <OrgTree @select="handleTreeSelect" />
    </div>
    <div class="right">
      <!-- 搜索栏 -->
      <div class="right-top">
        <a-space class="search-bar" style="margin: 5px;">
          <span class="search-label">{{ t('common.searchKey') }}：</span>
          <a-input v-model:value="searchKeyword" :placeholder="t('user.placeholderNameAndSearchKey')" class="search-input"  />
          <span class="search-label">{{ t('user.userStatus') }}：</span>
          <a-select v-model:value="searchStatus" :placeholder="t('user.placeholderUserStatus')" class="search-select" allow-clear>
            <a-select-option value="ENABLE">{{ t('user.enable') }}</a-select-option>
            <a-select-option value="DISABLE">{{ t('user.disable') }}</a-select-option>
          </a-select>
          <a-button :icon="h(SearchOutlined)" type="primary" :loading="loading" class="search-btn" @click="handleSearch">{{ t('common.searchButton') }}</a-button>
          <a-button :icon="h(ReloadOutlined)" class="search-btn" @click="handleReset">{{ t('common.resetButton') }}</a-button>
        </a-space>
      </div>
      <!-- 操作栏 + 表格 -->
      <div class="right-bottom">
        <div class="action-bar">
          <a-space style="margin: 5px">
            <a-button :icon="h(PlusOutlined)" type="primary" @click="handleAdd">{{ t('common.addButton') }}</a-button>
            <a-button :icon="h(UploadOutlined)">{{ t('common.imports') }}</a-button>
            <a-button :icon="h(DownloadOutlined)">{{ t('user.batchExportButton') }}</a-button>
            <a-popconfirm :title="t('user.popconfirmDeleteUser')" @confirm="handleBatchDelete">
              <a-button :icon="h(DeleteOutlined)" danger ghost>{{ t('common.batchRemoveButton') }}</a-button>
            </a-popconfirm>
          </a-space>
        </div>
        <a-table
          :columns="columns"
          :data-source="data"
          :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
          :pagination="paginationConfig"
          @change="handleTableChange"
          :loading="loading"
          row-key="id"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'avatar'">
              <a-avatar :src="record.avatar" :size="32">
                <template #icon><UserOutlined /></template>
              </a-avatar>
            </template>
            <template v-else-if="column.key === 'gender'">
              <span>{{ genderText(record.gender) }}</span>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-switch
                :checked="record.status === 'ENABLE'"
                @change="(checked) => handleStatusChange(record, checked)"
              />
            </template>
            <template v-else-if="column.key === 'action'">
              <a-space>
                <a @click="handleEdit(record)">{{ t('common.editButton') }}</a>
                <a-divider type="vertical" />
                <a-popconfirm :title="t('user.popconfirmDeleteUser')" @confirm="handleDelete(record)">
                  <a style="color: red;">{{ t('common.removeButton') }}</a>
                </a-popconfirm>
                <a-divider type="vertical" />
                <a-dropdown :trigger="['click']">
                  <a class="ant-dropdown-link" @click.prevent>
                    {{ t('common.more') }}
                    <DownOutlined />
                  </a>
                  <template #overlay>
                    <a-menu>
                      <a-menu-item>
                        <a @click="handleGrantRole(record)">{{ t('user.grantRole') }}</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a @click="handleGrantPermission(record)">{{ t('user.grantPermission') }}</a>
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
              </a-space>
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, h, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useMessage } from '@/utils/useMessage'
import {
  SearchOutlined, ReloadOutlined, PlusOutlined, DeleteOutlined,
  UploadOutlined, DownloadOutlined, UserOutlined, DownOutlined
} from '@ant-design/icons-vue'
import OrgTree from '@/components/OrgTree.vue'
import { getUserPage, deleteUserByIds, updateUserStatus } from '@/api/user'
import { getTestUserPage } from '@/testData/user'

const { t } = useI18n()
const { success, error, warning } = useMessage()

// 搜索条件
const searchKeyword = ref('')
const searchStatus = ref(undefined)
const selectedOrgId = ref('')

// 表格数据
const data = ref([])
const loading = ref(false)
const selectedRowKeys = ref([])

// 分页配置
const paginationConfig = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条`,
  pageSizeOptions: ['10', '20', '50', '100'],
})

// 表格列定义
const columns = computed(() => [
  { title: t('user.avatar'), dataIndex: 'avatar', key: 'avatar', width: 60, align: 'center' },
  { title: t('user.account'), dataIndex: 'account', key: 'account', width: 120 },
  { title: t('user.name'), dataIndex: 'name', key: 'name', width: 100 },
  { title: t('user.gender'), dataIndex: 'gender', key: 'gender', width: 60, align: 'center' },
  { title: t('user.phone'), dataIndex: 'phone', key: 'phone', width: 130 },
  { title: t('model.org'), dataIndex: 'orgName', key: 'org', width: 150 },
  { title: t('user.userStatus'), dataIndex: 'status', key: 'status', width: 80, align: 'center' },
  { title: t('common.action'), key: 'action', width: 220, fixed: 'right' },
])

// 性别显示映射
const genderText = (gender) => {
  if (gender === 'M' || gender === '男') return t('user.male')
  if (gender === 'F' || gender === '女') return t('user.female')
  return t('user.unknown')
}

// 获取用户列表
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: paginationConfig.current,
      pageSize: paginationConfig.pageSize,
    }
    if (searchKeyword.value) params.keyword = searchKeyword.value
    if (searchStatus.value) params.status = searchStatus.value
    if (selectedOrgId.value) params.orgId = selectedOrgId.value

    const res = await getUserPage(params)
    data.value = res.records || res.list || []
    paginationConfig.total = res.total || 0
  } catch (err) {
    // API 请求失败时使用测试数据
    console.warn('使用测试数据代替 API 请求')
    const params = {
      page: paginationConfig.current,
      pageSize: paginationConfig.pageSize,
      keyword: searchKeyword.value,
      status: searchStatus.value,
      orgId: selectedOrgId.value,
    }
    const res = getTestUserPage(params)
    data.value = res.records
    paginationConfig.total = res.total
  } finally {
    loading.value = false
  }
}

// 树节点选择
const handleTreeSelect = (keys, info) => {
  if (keys.length > 0) {
    selectedOrgId.value = info.node.key || info.node.id
    paginationConfig.current = 1
    fetchData()
  }
}

// 搜索
const handleSearch = () => {
  paginationConfig.current = 1
  fetchData()
}

// 重置
const handleReset = () => {
  searchKeyword.value = ''
  searchStatus.value = undefined
  paginationConfig.current = 1
  fetchData()
}

// 表格变化（分页、排序等）
const handleTableChange = (pag) => {
  paginationConfig.current = pag.current
  paginationConfig.pageSize = pag.pageSize
  fetchData()
}

// 选择变化
const onSelectChange = (keys) => {
  selectedRowKeys.value = keys
}

// 状态切换
const handleStatusChange = async (record, checked) => {
  const newStatus = checked ? 'ENABLE' : 'DISABLE'
  try {
    await updateUserStatus({ id: record.id, status: newStatus })
    success(checked ? '启用成功' : '禁用成功')
    record.status = newStatus
  } catch (err) {
    console.error('更新状态失败:', err)
    error('更新状态失败')
  }
}

// 新增用户
const handleAdd = () => {
  // TODO: 打开新增用户抽屉/弹窗
}

// 编辑用户
const handleEdit = (record) => {
  // TODO: 打开编辑用户抽屉/弹窗
  console.log('编辑用户', record)
}

// 删除单个用户
const handleDelete = async (record) => {
  try {
    await deleteUserByIds([record.id])
    success('删除成功')
    fetchData()
  } catch (err) {
    console.error('删除失败:', err)
    error('删除失败')
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (!selectedRowKeys.value.length) {
    warning('请先选择要删除的用户')
    return
  }
  try {
    await deleteUserByIds(selectedRowKeys.value)
    success('批量删除成功')
    selectedRowKeys.value = []
    fetchData()
  } catch (err) {
    console.error('批量删除失败:', err)
    error('批量删除失败')
  }
}

// 授权角色
const handleGrantRole = (record) => {
  // TODO: 打开授权角色弹窗
  console.log('授权角色', record)
}

// 授权权限
const handleGrantPermission = (record) => {
  // TODO: 打开授权权限弹窗
  console.log('授权权限', record)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
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
}

.right {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.right-top {
  background-color: white;
  margin-bottom: 10px;
}

.right-bottom {
  flex: 1;
  background-color: white;
}

.action-bar {
  padding: 5px 0;
}

.action-bar .ant-btn {
  padding: 4px 15px;
}

.ant-dropdown-link {
  cursor: pointer;
}

.search-bar {
  display: flex !important;
  align-items: center;
  flex-wrap: wrap;
  padding: 6px 12px;
}

.search-label {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  line-height: 24px;
}

.search-input {
  width: 200px;
  height: 32px;
}

.search-select {
  width: 130px;
}

.search-btn {
  padding-left: 16px !important;
  padding-right: 16px !important;
}
</style>
