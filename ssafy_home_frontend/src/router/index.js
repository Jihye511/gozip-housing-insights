import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '@/views/MainPage.vue';
import MapPage from '@/views/MapPage.vue';
import CommunityPage from '@/views/CommunityPage.vue';
import CalculatorPage from '@/views/CalculatorPage.vue';
import LoginPage from '@/views/LoginPage.vue';
import SignUpPage from '@/views/SignUpPage.vue'

const routes = [
  { path: '/', component: MainPage },
  { path: '/map', component: MapPage },
  { path: '/community', component: CommunityPage },
  { path: '/calculator', component: CalculatorPage },
  { path: '/login', component: LoginPage },
  { path: '/signup', component: SignUpPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
