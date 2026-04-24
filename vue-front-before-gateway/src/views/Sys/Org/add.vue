<template>
    <a-drawer :title="title" :width="360" :open="open" :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }" @close="onClose">
        <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
            <a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="上级组织：" name="parentId">
                        <org-select-tree></org-select-tree>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="组织名称" name="name">
                        <a-input v-model:value="form.name" placeholder="请输入组织名称" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="组织分类：" name="category" >
                        <a-select v-model:value="form.category" :options="orgTypeOptions" placeholder="请选择组织分类">
                            
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="排序" name="sortCode">
                        <a-input-number :style="{ width: '100%' }" id="inputNumber" v-model:value="form.sortCode" :min="1" :max="100000000" />
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
        <a-space>
            <a-button @click="onClose">取消</a-button>
            <a-button type="primary" @click="onSubmit">提交</a-button>
        </a-space>
    </a-drawer>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { addOrg } from '@/api/org';
import { getOrgTypesDic } from '@/api/dict';
import orgSelectTree from './orgSelectTree.vue';
import { orgDataStore } from '@/store/orgData';
import { useMessage } from '@/utils/useMessage';

const { success, error, warning, loading } = useMessage()
const orgData = orgDataStore();
// 表单引用
const formRef = ref();
const { t } = useI18n();
const title = ref(t('org.addOrg'))
const form = ref({});
const orgTypeOptions = ref([]);
const rules = {
    name: [
        {
            required: true,
            message: '请输入组织名称',
        },
    ],
    parentId: [
        {
            required: false,
            message: 'please enter url',
        },
    ],
    category: [
        {
            required: true,
            message: '请选择组织分类',
        },
    ],
    sortCode: [
        {
            required: true,
            message: '请输入排序号',
        },
    ]
};
const open = ref(false);
const init = () => {
    form.value.category = undefined;
    form.value.sortCode = 0;
    form.value.parentId = '';
    form.value.name = '';
};
const showDrawer = () => {
    open.value = true;
    // form = {
    //     name: '',
    // parent: '',
    // type: undefined,
    // sort: 0,
    // }
    init();
    
};
const onClose = () => {
    open.value = false;
};
const onSubmit = () => {
    if (!formRef.value) return;
    try {
        // 触发表单校验
        formRef.value.validate();
        // allOrg.push(form);
        form.value.parentId = orgData.currentNodeValue;
        addOrg(form.value).then(res => {
            success('新增机构成功!');
            open.value = false;}).catch(error => {
                error(error);
            });
    } catch (error) {
        console.log('校验失败:', error);
    }
};
onMounted(() => {
    orgTypeOptions.value = getOrgTypesDic();
    init();
});
// 只暴露指定的方法/数据
defineExpose({
    showDrawer
});
</script>