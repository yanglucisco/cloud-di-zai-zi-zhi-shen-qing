import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  server: {
    port: 20005,
    host: true, // 可选：允许局域网内的其他设备访问，便于真机调试
    allowedHosts: [
      'vue-front-before-gateway.clouddizai.com'
    ],
    proxy: {
      '/gateway': {
        target: 'http://gateway.clouddizai.com:20003',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/gateway/, ''), // 使用 rewrite 函数重写路径
      },
      '/oauth': {//直接与授权服务器连接，不通过网关
        target: 'http://auth-server:20001',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/oauth/, ''), // 使用 rewrite 函数重写路径
      },
    }
  },
  plugins: [
    vue(),
    vueDevTools(),
    tailwindcss(), // 添加 Tailwind Vite 插件
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
