import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  server: {
    port: 20002,
    host: true, // 可选：允许局域网内的其他设备访问，便于真机调试
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:20000',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''), // 使用 rewrite 函数重写路径
      },
    },
  },
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
