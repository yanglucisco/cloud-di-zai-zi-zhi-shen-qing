<script setup>
import i18n from '@/locales'

const locale = i18n.global.messages.value[i18n.global.locale.value].lang

</script>

<template>
  <a-config-provider :locale="locale">
    <div style="height: calc(100vh - 3px); margin: 0; padding: 0;">
      <RouterView />
    </div>
  </a-config-provider>
</template>

<style scoped>
</style>
<!-- src/App.vue -->
<!-- <template>
  <div>
    <p class="bg-lightBlue">
          成功集成 Tailwind CSS！
    </p>
  </div>
</template> -->
