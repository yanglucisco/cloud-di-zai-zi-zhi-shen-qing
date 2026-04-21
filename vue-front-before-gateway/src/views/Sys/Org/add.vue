<template>
    <a-drawer :title="title" :width="360" :open="open" :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }" @close="onClose">
        <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
            <a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="上级组织：" name="parent">
                        <a-select v-model:value="form.parent" placeholder="请选择上级组织">
                            <a-select-option value="private">Private</a-select-option>
                            <a-select-option value="public">Public</a-select-option>
                        </a-select>
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
                    <a-form-item label="组织分类：" name="type">
                        <a-select v-model:value="form.type" placeholder="请选择组织分类">
                            <a-select-option value="private">Private</a-select-option>
                            <a-select-option value="public">Public</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="排序" name="sort">
                        <a-input v-model:value="form.sort" placeholder="请输入排序号" />
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
import { reactive, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { allOrg } from '@/testData/org';
// 表单引用
const formRef = ref();
const { t } = useI18n();
const title = ref(t('org.addOrg'))
const form = reactive({
    name: '',
    parent: '',
    type: '',
    sort: 0,
});
const rules = {
    name: [
        {
            required: true,
            message: '请输入组织名称',
        },
    ],
    parent: [
        {
            required: false,
            message: 'please enter url',
        },
    ],
    type: [
        {
            required: true,
            message: '请选择组织分类',
        },
    ],
    sort: [
        {
            required: true,
            message: '请输入排序号',
        },
    ]
};
const open = ref(false);
const showDrawer = () => {
    open.value = true;
};
const onClose = () => {
    open.value = false;
};
const onSubmit = () => {
    if (!formRef.value) return;
    try {
        // 触发表单校验
        formRef.value.validate();

        // 校验通过，执行保存逻辑
        console.log('表单数据:' + form);
        allOrg.push(form);
        open.value = false;

    } catch (error) {
        console.log('校验失败:', error);
    }
};
// 只暴露指定的方法/数据
defineExpose({
    showDrawer
});
</script>