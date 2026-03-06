import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig(({ command, mode }) => {
  // 加载环境变量
  const env = loadEnv(mode, process.cwd(), '')
  // const port = import.meta.env.VITE_API_PORT
  return {
    server: {
      port: env.VITE_API_PORT,
      host: true, // 可选：允许局域网内的其他设备访问，便于真机调试
      allowedHosts: [
        'vue-front-before-gateway.clouddizai.com'
      ],
      proxy: {
        '/gateway': {
          target: 'http://gateway.clouddizai.com:20003',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/gateway/, ''), // 使用 rewrite 函数重写路径
        }
      }
    },
    plugins: [
      vue(),
      vueDevTools(),
      tailwindcss(), // 添加 Tailwind Vite 插件
    ],
    css: {
      preprocessorOptions: {
        less: {
          javascriptEnabled: true,
          // plugins: [new Less2CssVariablePlugin()]
        }
      }
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
  }
})
