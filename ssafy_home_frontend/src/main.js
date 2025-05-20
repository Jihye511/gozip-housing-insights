import { createApp } from 'vue';
import App from './App.vue'; // ✔️ 반드시 App.vue 사용
import router from './router';

import './assets/main.css';


const app = createApp(App);

app.use(router);
app.mount('#app');
