import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import i18n from './locales'
import 'ant-design-vue/dist/reset.css'

const app = createApp(App)
app.use(router)
const pinia = createPinia()
app.use(pinia)
app.use(Antd)
app.use(i18n)
app.mount('#app')
