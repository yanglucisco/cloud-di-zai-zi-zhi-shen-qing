<template>
  <a-layout>
    <a-layout-sider
      v-model:collapsed="collapsed"
      :trigger="null"
      collapsible
      :width="240"
      breakpoint="lg"
      @collapse="onCollapse"
    >
      <!-- Logo区域 -->
      <div class="logo">
        <h2 v-if="!collapsed">管理系统</h2>
        <h2 v-else>Admin</h2>
      </div>
      
      <!-- 菜单区域 -->
      <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        theme="dark"
        mode="inline"
        :inline-collapsed="collapsed"
        @click="handleMenuClick"
        @openChange="onOpenChange"
      >
        <!-- 递归渲染菜单项 -->
        <template v-for="menu in menuData" :key="menu.path || menu.id">
          <template v-if="!menu.hidden">
            <!-- 有子菜单的情况 -->
            <a-sub-menu
              v-if="menu.children && menu.children.length > 0"
              :key="menu.path || menu.id"
            >
              <template #title>
                <span>
                  <Icon v-if="menu.icon" :type="menu.icon" />
                  <span class="menu-title">{{ menu.title || menu.name }}</span>
                </span>
              </template>
              
              <!-- 递归渲染子菜单 -->
              <template v-for="subMenu in menu.children" :key="subMenu.path || subMenu.id">
                <template v-if="!subMenu.hidden">
                  <!-- 子菜单还有子菜单 -->
                  <a-sub-menu
                    v-if="subMenu.children && subMenu.children.length > 0"
                    :key="subMenu.path || subMenu.id"
                  >
                    <template #title>
                      <span>
                        <Icon v-if="subMenu.icon" :type="subMenu.icon" />
                        <span>{{ subMenu.title || subMenu.name }}</span>
                      </span>
                    </template>
                    
                    <!-- 渲染三级菜单 -->
                    <template v-for="thirdMenu in subMenu.children" :key="thirdMenu.path || thirdMenu.id">
                      <template v-if="!thirdMenu.hidden">
                        <!-- 三级菜单项 -->
                        <a-menu-item
                          v-if="!thirdMenu.children || thirdMenu.children.length === 0"
                          :key="thirdMenu.path || thirdMenu.id"
                        >
                          <Icon v-if="thirdMenu.icon" :type="thirdMenu.icon" />
                          <span>{{ thirdMenu.title || thirdMenu.name }}</span>
                        </a-menu-item>
                        
                        <!-- 如果还有四级菜单，可以继续递归，但建议最多三级 -->
                      </template>
                    </template>
                  </a-sub-menu>
                  
                  <!-- 二级菜单项 -->
                  <a-menu-item
                    v-else
                    :key="subMenu.path || subMenu.id"
                  >
                    <Icon v-if="subMenu.icon" :type="subMenu.icon" />
                    <span>{{ subMenu.title || subMenu.name }}</span>
                  </a-menu-item>
                </template>
              </template>
            </a-sub-menu>
            
            <!-- 没有子菜单的一级菜单项 -->
            <a-menu-item
              v-else
              :key="menu.path || menu.id"
            >
              <Icon v-if="menu.icon" :type="menu.icon" />
              <span class="menu-title">{{ menu.title || menu.name }}</span>
            </a-menu-item>
          </template>
        </template>
      </a-menu>
    </a-layout-sider>
    
    <a-layout>
      <a-layout-header class="layout-header">
        <div class="header-left">
          <menu-unfold-outlined
            v-if="collapsed"
            class="trigger"
            @click="toggleCollapsed"
          />
          <menu-fold-outlined
            v-else
            class="trigger"
            @click="toggleCollapsed"
          />
          <a-breadcrumb class="breadcrumb">
            <a-breadcrumb-item>首页</a-breadcrumb-item>
            <a-breadcrumb-item>当前页面</a-breadcrumb-item>
          </a-breadcrumb>
        </div>
        
        <div class="header-right">
          <a-dropdown>
            <a class="ant-dropdown-link" @click.prevent>
              <UserOutlined style="margin-right: 8px" />
              {{ userName }}
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item>
                  <UserOutlined />
                  个人中心
                </a-menu-item>
                <a-menu-item>
                  <SettingOutlined />
                  系统设置
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item @click="handleLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>
      
      <a-layout-content class="layout-content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  UserOutlined,
  SettingOutlined,
  LogoutOutlined
} from '@ant-design/icons-vue'

// 图标组件
import Icon from '@/components/Icon.vue'

// 获取路由实例
const router = useRouter()
const route = useRoute()

// 响应式数据
const collapsed = ref(false)
const selectedKeys = ref([])
const openKeys = ref([])
const menuData = ref([])
const userName = ref('管理员')

// 模拟从后端获取菜单数据
const fetchMenuData = async () => {
  try {
    // 这里应该是真实的API调用
    // const response = await axios.get('/api/menus')
    // menuData.value = response.data
    
    // 模拟数据
    menuData.value = [
      {
        id: 1,
        name: '仪表盘',
        title: '仪表盘',
        path: '/dashboard',
        icon: 'DashboardOutlined',
        children: [
          {
            id: 11,
            name: '分析页',
            title: '分析页',
            path: '/dashboard/analysis',
            icon: 'LineChartOutlined'
          },
          {
            id: 12,
            name: '工作台',
            title: '工作台',
            path: '/dashboard/workplace',
            icon: 'DesktopOutlined'
          },
          {
            id: 13,
            name: '监控页',
            title: '监控页',
            path: '/dashboard/monitor',
            icon: 'MonitorOutlined',
            hidden: false
          }
        ]
      },
      {
        id: 2,
        name: '系统管理',
        title: '系统管理',
        icon: 'SettingOutlined',
        children: [
          {
            id: 21,
            name: '用户管理',
            title: '用户管理',
            path: '/system/user',
            icon: 'UserOutlined',
            children: [
              {
                id: 211,
                name: '用户列表',
                title: '用户列表',
                path: '/system/user/list',
                icon: 'UnorderedListOutlined'
              },
              {
                id: 212,
                name: '用户角色',
                title: '用户角色',
                path: '/system/user/role',
                icon: 'TeamOutlined'
              }
            ]
          },
          {
            id: 22,
            name: '菜单管理',
            title: '菜单管理',
            path: '/system/menu',
            icon: 'MenuOutlined'
          },
          {
            id: 23,
            name: '角色管理',
            title: '角色管理',
            path: '/system/role',
            icon: 'SafetyCertificateOutlined',
            hidden: false
          }
        ]
      },
      {
        id: 3,
        name: '表单页面',
        title: '表单页面',
        icon: 'FormOutlined',
        children: [
          {
            id: 31,
            name: '基础表单',
            title: '基础表单',
            path: '/form/basic',
            icon: 'FormOutlined'
          },
          {
            id: 32,
            name: '分步表单',
            title: '分步表单',
            path: '/form/step',
            icon: 'SolutionOutlined'
          }
        ]
      },
      {
        id: 4,
        name: '列表页面',
        title: '列表页面',
        path: '/list',
        icon: 'TableOutlined'
      },
      {
        id: 5,
        name: '个人中心',
        title: '个人中心',
        path: '/profile',
        icon: 'UserOutlined',
        hidden: false
      }
    ]
    
    // 初始化选中的菜单项
    updateSelectedKeys()
    
  } catch (error) {
    console.error('获取菜单数据失败:', error)
    message.error('获取菜单数据失败')
  }
}

// 更新选中的菜单项
const updateSelectedKeys = () => {
  const currentPath = route.path
  selectedKeys.value = [currentPath]
  
  // 根据当前路径设置展开的菜单
  const findParentPaths = (menus, targetPath, parentPaths = []) => {
    for (const menu of menus) {
      if (menu.path === targetPath) {
        return parentPaths
      }
      if (menu.children) {
        const found = findParentPaths(menu.children, targetPath, [...parentPaths, menu.path || menu.id])
        if (found) return found
      }
    }
    return null
  }
  
  const parentPaths = findParentPaths(menuData.value, currentPath)
  if (parentPaths) {
    openKeys.value = parentPaths
  }
}

// 切换侧边栏折叠状态
const toggleCollapsed = () => {
  collapsed.value = !collapsed.value
}

// 侧边栏折叠回调
const onCollapse = (collapsedStatus) => {
  collapsed.value = collapsedStatus
  if (!collapsedStatus) {
    // 展开时恢复之前展开的菜单
    updateSelectedKeys()
  }
}

// 菜单展开变化
const onOpenChange = (keys) => {
  openKeys.value = keys
}

// 处理菜单点击
const handleMenuClick = ({ key }) => {
  // 查找菜单项
  const findMenuItem = (menus, targetKey) => {
    for (const menu of menus) {
      if (menu.path === targetKey || menu.id === targetKey) {
        return menu
      }
      if (menu.children) {
        const found = findMenuItem(menu.children, targetKey)
        if (found) return found
      }
    }
    return null
  }
  
  const menuItem = findMenuItem(menuData.value, key)
  if (menuItem && menuItem.path) {
    // 导航到对应的路由
    router.push(menuItem.path)
    selectedKeys.value = [key]
  }
}

// 处理菜单项点击
const handleMenuItemClick = (menu) => {
  if (menu.path) {
    router.push(menu.path)
    selectedKeys.value = [menu.path || menu.id]
  }
}

// 退出登录
const handleLogout = () => {
  // 清除登录信息
  localStorage.removeItem('token')
  // 跳转到登录页
  router.push('/login')
  message.success('已退出登录')
}

// 监听路由变化
watch(() => route.path, () => {
  updateSelectedKeys()
})

// 组件挂载时获取菜单数据
onMounted(() => {
  fetchMenuData()
})
</script>

<style scoped>
.logo {
  height: 60px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.1);
  text-align: center;
  overflow: hidden;
}

.logo h2 {
  color: white;
  font-size: 18px;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.menu-icon {
  margin-right: 8px;
  font-size: 16px;
}

.menu-title {
  font-size: 14px;
}

.layout-header {
  background: #fff;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 1;
}

.header-left {
  display: flex;
  align-items: center;
}

.trigger {
  font-size: 18px;
  cursor: pointer;
  transition: color 0.3s;
  margin-right: 16px;
}

.trigger:hover {
  color: #1890ff;
}

.breadcrumb {
  margin-left: 16px;
}

.header-right {
  display: flex;
  align-items: center;
}

.ant-dropdown-link {
  cursor: pointer;
  color: rgba(0, 0, 0, 0.85);
}

.layout-content {
  margin: 16px;
  padding: 16px;
  background: #fff;
  min-height: 280px;
  overflow: auto;
}
</style>