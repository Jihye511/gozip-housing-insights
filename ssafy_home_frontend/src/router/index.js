import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '@/views/MainPage.vue';
import MapPage from '@/views/MapPage.vue';
import CommunityPage from '@/views/CommunityPage.vue';
import CalculatorPage from '@/views/CalculatorPage.vue';
import LoginPage from '@/views/LoginPage.vue';

const routes = [
  { path: '/', component: MainPage },
  { path: '/map', name: 'MapPage', component: MapPage },
  { path: '/community', component: CommunityPage },
  { path: '/calculator', component: CalculatorPage },
  { path: '/login', component: LoginPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
