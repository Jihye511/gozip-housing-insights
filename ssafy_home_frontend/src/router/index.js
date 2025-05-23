import { createRouter, createWebHistory } from 'vue-router'
import MainPage from '@/views/MainPage.vue'
import MapPage from '@/views/MapPage.vue'
import CommunityPage from '@/views/CommunityPage.vue'
import CalculatorPage from '@/views/CalculatorPage.vue'
import LoginPage from '@/views/LoginPage.vue'
import SignUpPage from '@/views/SignUpPage.vue'
import AdminPage from '@/views/AdminPage.vue'
import { useUserStore } from '@/stores/user'
const routes = [
  { path: '/', component: MainPage },
  { path: '/map', component: MapPage },
  { path: '/community', component: CommunityPage },
  { path: '/calculator', component: CalculatorPage },
  { path: '/login', component: LoginPage },
  { path: '/signup', component: SignUpPage },
  {
    path: '/admin',
    component: AdminPage,
    meta: { requiresAuth: true, requiresRole: 'ROLE_ADMIN' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 전역 가드: 인증 및 권한 체크
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // 로그인 필요
  if (to.meta.requiresAuth && !userStore.user) {
    return next('/login')
  }
  // 관리자 권한 체크
  if (to.meta.requiresRole && userStore.user?.role !== to.meta.requiresRole) {
    return next('/') // 권한 없으면 홈으로
  }
  next()
})

export default router
