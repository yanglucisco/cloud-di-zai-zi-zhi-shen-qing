<script setup></script>

<template>
  <h1>You did it!</h1>
  <p>
    Visit <a href="https://vuejs.org/" target="_blank" rel="noopener">vuejs.org</a> to read the
    documentation
  </p>
  <div>

    <div>{{ userInfo }}</div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { getInfo } from '@/api/user'; // 导入具体的API方法

const userInfo = ref(null);
const loading = ref(false);

onMounted(async () => {
  loading.value = true;
  try {
    const data = await getInfo(); // 调用接口
    userInfo.value = data.data.userName
  } catch (error) {
    console.error('获取用户信息失败', error);
  } finally {
    loading.value = false;
  }
});
</script>

<!-- <template>
  <div>
    <div v-if="loading">加载中...</div>
    <div v-else-if="userInfo">{{ userInfo.name }}</div>
  </div>
</template> -->

<style scoped></style>
