<template>
  <a-drawer :title="title" :width="480" :open="open" :body-style="{ paddingBottom: '80px' }"
    :footer-style="{ textAlign: 'right' }" @close="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('user.account')" name="account">
            <a-input v-model:value="form.account" :placeholder="t('user.account')" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :label="t('user.name')" name="name">
            <a-input v-model:value="form.name" :placeholder="t('user.name')" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="密码" name="password" v-if="!isEditMode">
            <a-input-password v-model:value="form.password" placeholder="请输入密码" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('user.gender')" name="gender">
            <a-select v-model:value="form.gender" :placeholder="t('user.gender')" allow-clear>
              <a-select-option v-for="item in genderOptions" :key="item.value" :value="item.value">
                {{ item.label }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="昵称" name="nickname">
            <a-input v-model:value="form.nickname" placeholder="请输入昵称" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('user.phone')" name="phone">
            <a-input v-model:value="form.phone" :placeholder="t('user.phone')" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="邮编" name="postalCode">
            <a-input v-model:value="form.postalCode" placeholder="请输入邮编" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="出生日期" name="birthDate">
            <a-date-picker v-model:value="form.birthDate" style="width: 100%" placeholder="请选择出生日期" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :label="t('model.org')" name="orgId" required>
            <a-tree-select
              v-model:value="form.orgId"
              show-search
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="请选择所属机构"
              allow-clear
              tree-default-expand-all
              :tree-data="orgTreeData"
              tree-node-filter-prop="label"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <a-space>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" :loading="submitting" @click="onSubmit">提交</a-button>
    </a-space>
  </a-drawer>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { addUser } from '@/api/user'
import { getOrgTypesDic } from '@/api/dict'
import { orgDataStore } from '@/store/orgData'
import { useMessage } from '@/utils/useMessage'

const { success, error } = useMessage()
const { t } = useI18n()
const formRef = ref()
const open = ref(false)
const submitting = ref(false)
const isEditMode = ref(false)
const title = ref('新增用户')
const genderOptions = ref([])
const orgTreeData = ref([])
const orgData = orgDataStore()

const form = reactive({
  account: '',
  name: '',
  gender: undefined,
  nickname: '',
  phone: '',
  postalCode: '',
  birthDate: undefined,
  orgId: undefined,
  password: '',
})

const rules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' },
  ],
  orgId: [
    { required: true, message: '请选择所属机构', trigger: 'change' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
}

const init = () => {
  form.account = ''
  form.name = ''
  form.gender = undefined
  form.nickname = ''
  form.phone = ''
  form.postalCode = ''
  form.birthDate = undefined
  form.orgId = undefined
  form.password = ''
}

const showDrawer = (record = null) => {
  open.value = true
  if (record) {
    isEditMode.value = true
    title.value = '编辑用户'
    Object.assign(form, record)
  } else {
    isEditMode.value = false
    title.value = '新增用户'
    init()
  }
}

const onClose = () => {
  open.value = false
}

const onSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch (validationError) {
    console.log('校验失败:', validationError)
    return
  }
  submitting.value = true
  try {
    // 构造提交数据
    const submitData = { ...form }
    if (submitData.birthDate) {
      submitData.birthDate = submitData.birthDate.format('YYYY-MM-DD')
    }
    if (isEditMode.value) {
      // TODO: 编辑用户，后续实现
      success('修改用户成功!')
    } else {
      if (!submitData.orgId) {
        submitData.orgId = orgData.currentNodeValue
      }
      await addUser(submitData)
      success('新增用户成功!')
    }
    emit('success')
    open.value = false
  } catch (err) {
    console.error('提交失败:', err)
    debugger
    error(err?.response?.data?.message || err?.message || '提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const emit = defineEmits(['success'])

onMounted(() => {
  genderOptions.value = getOrgTypesDic()
  orgTreeData.value = orgData.treeData
})

defineExpose({
  showDrawer,
})
</script>
