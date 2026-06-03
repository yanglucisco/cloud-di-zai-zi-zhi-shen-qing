# 技术栈 & 运行

## 技术栈总结

| 类别 | 技术 | 版本 |
|------|------|------|
| 框架 | Vue 3 | ^3.5.25 |
| 构建 | Vite | ^7.2.4 |
| 路由 | vue-router | ^4.6.3 |
| 状态管理 | Pinia | ^3.0.4 |
| UI 库 | Ant Design Vue | ^4.2.6 |
| 图标 | @ant-design/icons-vue | ^7.0.1 |
| HTTP | Axios | ^1.13.2 |
| 国际化 | vue-i18n | ^11.2.2 |
| 样式 | Less / Tailwind CSS | ^4.4.2 / ^4.1.17 |
| 加密 | crypto-js | ^4.2.0 |
| 全屏 | screenfull | ^6.0.2 |

## 运行脚本

| 命令 | 说明 |
|------|------|
| `npm run dev` / `npm run local` | 本地开发 |
| `npm run test` | 测试模式（--mode test） |
| `npm run build` | 生产构建 |
| `npm run preview` | 预览构建产物 |

## 环境变量

| 变量名 | 说明 |
|--------|------|
| `VITE_API_PORT` | 开发服务器端口 |
| `GATE_WAY_URL` | 后端网关地址（代理目标） |
| `VITE_AUTH_SERVER_URL` | OAuth2 授权服务器 URL |

配置在 `.env.prod` 中，通过 Vite 的 `loadEnv` 加载。
