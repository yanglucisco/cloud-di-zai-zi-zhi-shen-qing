import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import i18n from './locales'
import 'ant-design-vue/dist/reset.css'
import 'vue3-ui/dist/index.es.css'
// import './tailwind.css'
// import './style/index.less'

const app = createApp(App)
app.use(router)
const pinia = createPinia()
app.use(pinia)
app.use(Antd)
app.use(i18n)
app.mount('#app')
