const routerMap = new Map()
routerMap.set('index', () => import('@/views/Index.vue'));
routerMap.set('index1', () => import('@/views/Index.vue'));
routerMap.set('orgstru', () => import('@/views/Sys/Index.vue'));
routerMap.set('orgmanage', () => import('@/views/Sys/Org/org.vue'))
routerMap.set('usermanage', () => import('@/views/Sys/User/Index.vue'))
routerMap.set('positionmanage', () => import('@/views/Index.vue'))
routerMap.set('caidan1', () => import('@/views/Sys/Org/sanjimulu/caidan1.vue'))
routerMap.set('sanjimulu', () => import('@/views/Sys/Org/Index.vue'));
// /orgstru/sanjimulu/caidan1 /orgstru/orgmanage
export default {
    routerMap
}