import './assets/main.css'
import axios from 'axios'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'  // 引入持久化插件

import App from './App.vue'
import router from './router'

// 创建 Pinia 实例
const pinia = createPinia()

// 注册持久化插件
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
axios.defaults.baseURL = 'http://localhost:8000'

// 使用 Pinia 和 router
app.use(pinia)
app.use(router)

app.mount('#app')
