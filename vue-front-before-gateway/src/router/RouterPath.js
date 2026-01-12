const routerMap = new Map()
routerMap.set('sys', () => import('@/views/Sys/Index.vue'));
routerMap.set('sysUser', () => import('@/views/Sys/User/Index.vue'))
routerMap.set('sysOrg', () => import('@/views/Sys/Org/Index.vue'))
routerMap.set('index', () => import('@/views/Index.vue'))

export default {
    routerMap
}