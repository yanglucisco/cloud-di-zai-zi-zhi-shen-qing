<template>
  <div class="login-container">
    
    <a-card class="login-card" :title="title">
      <a-form
        :model="formState"
        name="loginForm"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          name="username"
          :rules="[{ required: true, message: userNamePlaceholder }]"
        >
          <a-input v-model:value="formState.username" :placeholder="userNamePlaceholder" size="large">
            <template #prefix>
              <UserOutlined class="input-prefix" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item
          name="password"
          :rules="[{ required: true, message: passwordPlaceholder }]"
        >
          <a-input-password 
            v-model:value="formState.password" 
            :placeholder="passwordPlaceholder"
            size="large"
          >
            <template #prefix>
              <LockOutlined class="input-prefix" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button 
            type="primary" 
            html-type="submit" 
            size="large" 
            :loading="loading"
            block
          >
            {{ loading ? '登录中...' : '登录' }}
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
const title = ref(t('login.systemName'))
const userNamePlaceholder=ref(t('login.accountPlaceholder'))
const passwordPlaceholder=ref(t('login.PWPlaceholder'))
// 响应式数据
const formState = reactive({
  username: '',
  password: ''
});

const loading = ref(false);

// 表单提交成功
const onFinish = async (values) => {
  loading.value = true;
  
  try {
    // 模拟登录请求，实际项目中替换为真实的API调用
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    console.log('登录数据:', values);
    message.success('登录成功！');
    
    // 这里可以添加跳转逻辑，例如：
    // router.push('/dashboard');
    
  } catch (error) {
    message.error('登录失败，请重试');
  } finally {
    loading.value = false;
  }
};

// 表单提交失败（验证不通过）
const onFinishFailed = (errorInfo) => {
  console.log('验证失败:', errorInfo);
  message.warning('请填写完整的登录信息');
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  /* min-height: 100%; */
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  /* padding: 20px; */
}

.login-card {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.login-card :deep(.ant-card-head-title) {
  text-align: center;
  font-size: 1.5em;
  font-weight: 600;
}

.input-prefix {
  color: rgba(0, 0, 0, 0.25);
}
</style>