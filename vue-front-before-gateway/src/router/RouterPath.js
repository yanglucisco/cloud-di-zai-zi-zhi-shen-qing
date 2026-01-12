const routerMap = new Map()
routerMap.set('index', () => import('@/views/Index.vue'));
routerMap.set('orgStru', () => import('@/views/Sys/Index.vue'));
routerMap.set('orgManage', () => import('@/views/Sys/User/Index.vue'))
routerMap.set('userManage', () => import('@/views/Sys/Org/Index.vue'))
routerMap.set('positionManage', () => import('@/views/Index.vue'))

export default {
    routerMap
}