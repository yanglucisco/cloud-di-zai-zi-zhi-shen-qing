# 关键技术点

## 动态路由（菜单驱动）

登录后从后端获取菜单树，结构如下：
```json
{
  "name": "sys",
  "title": "系统管理",
  "icon": "UserOutlined",
  "path": "/sys",
  "component": "sys",
  "type": "subMenu",
  "children": [
    {
      "name": "orgmanage",
      "title": "组织管理",
      "path": "/sys/orgmanage",
      "component": "orgmanage",
      "type": "menu",
      "children": []
    }
  ]
}
```

递归调用 `router.addRoute()` 动态注册，同时 LayoutMenu 递归渲染菜单 UI。

## PKCE 认证流程

- 使用 `S256` 的 `code_challenge_method`
- code_verifier 当前硬编码（需改进）
- 支持 Token 过期自动跳转登录
- 登出清除 Token 并重定向到 OAuth 登出端点

## 多页签导航

`navigation.vue` 组件实现类似浏览器的标签页导航：
- 点击菜单时通过 `addPaneItem` 添加标签
- 关闭标签时自动切回对应的菜单选中状态
- 标签持久化打开的路由

## 单例模式工具

`Singleton.js` 使用类的单例模式 + 装饰 `localStorage`：
- `setData(key, value)` → 写入 localStorage
- `getData(key)` → 从 localStorage 读取
