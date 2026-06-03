# 目录结构

```
vue-front-before-gateway/
├── .env.prod                    # 生产环境变量
├── .gitignore
├── .vscode/                     # IDE 配置
├── index.html                   # 入口 HTML
├── jsconfig.json                # JS 配置（路径别名）
├── package.json                 # 依赖 & 脚本
├── vite.config.js               # Vite 配置（代理、插件、别名）
├── README.md
│
├── public/                      # 静态资源
│
└── src/                         # 核心源码
    ├── main.js                  # 应用入口（挂载 Pinia、Router、Antd、i18n）
    ├── App.vue                  # 根组件（提供 Antd 国际化的 <a-config-provider>）
    │
    ├── api/                     # API 请求层
    │   ├── catalog.js           # 目录相关接口
    │   ├── dict.js              # 字典接口（含定时刷新）
    │   ├── org.js               # 组织架构接口
    │   └── user.js              # 用户相关接口
    │
    ├── layout/                  # 布局组件
    │   ├── index.vue            # 主布局（侧边栏 + 顶栏 + 内容区 + 导航标签）
    │   ├── index-test.vue       # 测试布局
    │   ├── LayoutMenu.vue       # 递归菜单组件（支持无限层级子菜单）
    │   ├── navigation.vue       # 标签导航栏（多页签）
    │   ├── breadcrumb.vue       # 面包屑导航
    │   └── UserBar.vue          # 用户信息栏
    │
    ├── views/                   # 页面视图
    │   ├── Index.vue            # 首页
    │   ├── HomeView.vue         # 主页面（路由占位）
    │   ├── Home1View.vue        # 备用主页
    │   ├── Home2View.vue        # 备用主页
    │   ├── Login.vue            # 登录页
    │   ├── Logout.vue           # 登出页
    │   ├── ChildOfHomeView.vue          # 子页面
    │   ├── ChildOfChildOfHomeView.vue   # 子页面的子页面
    │   ├── Component/
    │   │   └── MyTestMenu.vue           # 测试菜单组件
    │   ├── User/
    │   │   ├── Index.vue        # 用户中心
    │   │   └── index2.vue       # 用户中心备用
    │   └── Sys/
    │       ├── Index.vue        # 系统管理首页
    │       ├── Org/
    │       │   ├── Index.vue    # 组织架构页面
    │       │   ├── org.vue      # 组织管理页面
    │       │   ├── add.vue      # 添加组织
    │       │   ├── orgSelectTree.vue    # 组织树选择器
    │       │   └── sanjimulu/
    │       │       └── caidan1.vue      # 三级菜单示例
    │       └── User/
    │           └── Index.vue    # 系统用户管理
    │
    ├── router/                  # 路由配置
    │   ├── index.js             # 路由定义 + 导航守卫（PKCE 认证 + 动态路由）
    │   └── RouterPath.js        # 路由映射表（动态组件加载）
    │
    ├── store/                   # 状态管理（Pinia）
    │   ├── useGlobalStore.js    # 全局状态（菜单折叠、主题）
    │   ├── sysinfo.js           # 系统信息（菜单列表）
    │   ├── pkce.js              # PKCE 认证状态
    │   ├── orgData.js           # 组织数据
    │   └── Singleton.js         # 单例模式工具（封装 localStorage）
    │
    ├── utils/                   # 工具函数
    │   ├── request.js           # Axios 封装（拦截器、Token 注入、401 处理）
    │   ├── pkce-util.js         # PKCE 流程工具（code_verifier/challenge）
    │   ├── EnvUtil.js           # 环境变量工具
    │   ├── logger.ts            # 日志工具
    │   ├── timer.js             # 定时器工具
    │   ├── enum.js              # 枚举常量
    │   └── useMessage.js        # 消息提示封装
    │
    ├── userInfo/                # 用户认证信息管理
    │   └── index.js             # Token 存取、登出、获取当前用户
    │
    ├── locales/                 # 国际化
    │   ├── index.js             # i18n 实例（中英文 + Antd 本地化）
    │   └── lang/
    │       ├── zh-cn.js         # 中文语言包
    │       └── en.js            # 英文语言包
    │
    ├── libs/
    │   └── my-lib.js            # 自定义库
    │
    ├── testData/
    │   └── org.js               # 测试组织数据
    │
    ├── style/                   # 样式
    │   ├── index.less           # 主样式入口
    │   ├── default.less         # 默认主题变量
    │   ├── colors.less          # 颜色变量
    │   ├── realdark.less        # 深色主题
    │   └── test-less.less       # 测试样式
    │
    ├── tailwind.css             # Tailwind CSS 入口
    └── [main.js](src/main.js)             # 应用入口
```
