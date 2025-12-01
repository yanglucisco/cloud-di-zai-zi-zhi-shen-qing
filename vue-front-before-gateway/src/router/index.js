import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Home1View from '../views/Home1View.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/home',
      name: 'home2',
      component: HomeView,
    },
    {
      path: '/home1',
      name: 'home1',
      component: Home1View,
    }
  ],
})
export default router
