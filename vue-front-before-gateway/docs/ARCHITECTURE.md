# 核心架构设计

## 启动流程

```
index.html
    ↓
main.js
    ├── createApp(App)
    ├── createPinia() → app.use(pinia)
    ├── app.use(router)
    ├── app.use(Antd)         ← Ant Design Vue
    ├── app.use(i18n)         ← vue-i18n 国际化
    ├── import './style/index.less'
    └── app.mount('#app')
```

## 认证流程 (PKCE + OAuth2)

```
┌────────────────────────────────────────────────────────────────┐
│ 1. 用户访问页面（无 Token）                                     │
│    router.beforeEach → 发现无 accessToken                       │
│    → 调用 isFromAuthorServer() 检查是否有授权回调 code           │
│    → 没有 code → 调用 gotoLoginPage()                         │
│    → 重定向到 OAuth 授权服务器                                  │
├────────────────────────────────────────────────────────────────┤
│ 2. 授权服务器回调（携带 code）                                   │
│    → exchangeCode(code)                                        │
│    → 用 code + code_verifier 换取 access_token + id_token      │
│    → 保存 Token 到 localStorage                                │
│    → 请求 /rolemanage/sysrole/get 获取用户菜单权限                │
│    → 保存菜单到 Singleton（实际存 localStorage）                  │
│    → saveAlldics() 加载字典数据                                  │
├────────────────────────────────────────────────────────────────┤
│ 3. 动态路由创建                                                 │
│    → 根据后端返回的菜单树，调用 dynamicCreateRouter()             │
│    → 从 routerMap 查找对应组件，动态添加子路由                    │
│    → 再次导航到目标页面                                          │
└────────────────────────────────────────────────────────────────┘
```

## 路由体系

**静态路由**（router/index.js）：
| 路径 | 名称 | 组件 | 说明 |
|------|------|------|------|
| `/` | root | layout/index.vue | 主布局容器 |
| `/home` | home | views/Index.vue | 首页 |
| `/usercenter` | usercenter | views/User/Index.vue | 用户中心 |

**动态路由**（登录后根据权限动态添加）：
- 通过 `dynamicCreateRouter()` 根据后端菜单数据递归创建
- 路由映射由 `RouterPath.js` 维护，`routerMap` 为 `Map<路由名, 组件>`

## 状态管理架构

```
Pinia Stores:
├── useGlobalStore     → 全局 UI 状态（菜单折叠、主题）
├── sysinfoStore       → 系统信息（菜单列表）
└── pkceStore          → PKCE 认证状态

Singleton (工具类)：
└── appConfig          → 封装 localStorage（setData/getData/getAllData）
                      → 存储菜单数据、配置信息
```

## 数据请求架构

```
请求流程：
┌─────────────┐    ┌──────────────┐    ┌─────────────┐
│  API 模块    │ → │ Axios 实例    │ → │ 后端服务     │
│ (api/*.js)  │    │ (request.js) │    │ (网关代理)   │
└─────────────┘    └──────────────┘    └─────────────┘
                          │
                    ┌─────┴─────┐
                    │ 拦截器处理  │
                    │ • 注入 Token│
                    │ • 401→重新登录│
                    │ • 统一错误处理│
                    └───────────┘

Vite 代理配置：
  /gateway/* → 目标网关地址（.env 配置）
```

## 布局体系

```
layout/index.vue (主布局)
├── a-layout-sider (左侧菜单)
│   ├── Logo 区域
│   └── LayoutMenu (递归组件 → 支持无限级子菜单)
│       └── a-sub-menu / a-menu-item
│
└── a-layout (右侧内容区)
    ├── a-layout-header (顶栏)
    │   ├── 折叠按钮
    │   ├── 系统/业务切换菜单
    │   ├── 面包屑导航
    │   └── UserBar (用户信息栏)
    ├── navigation (多页签导航)
    └── a-layout-content
        └── router-view (页面渲染区域)
```
