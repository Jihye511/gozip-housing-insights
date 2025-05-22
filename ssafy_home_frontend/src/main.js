import { createApp } from 'vue';
import App from './App.vue'; // ✔️ 반드시 App.vue 사용
import router from './router';
import { createPinia } from 'pinia';

import './assets/main.css';


const app = createApp(App);
const pinia = createPinia();
app.use(router);
app.use(pinia);
app.mount('#app');
