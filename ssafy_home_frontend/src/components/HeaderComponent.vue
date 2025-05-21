<script>
import { useUserStore } from '@/stores/user';
import { onMounted } from 'vue';

export default {
  name: 'HeaderComponent',
  setup() {
    const userStore = useUserStore();

    onMounted(() => {
      userStore.fetchUser();
    });

    const loginWithNaver = () => {
      window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
    };

    return {
      userStore,
      loginWithNaver,
      logout: userStore.logout,
    };
  },
};
</script>

<template>
  <header class="bg-white shadow py-4">
    <div class="max-w-7xl mx-auto flex items-center justify-between px-4">
      <router-link to="/" class="flex items-center space-x-2">
        <img src="@/assets/gozip_logo.png" alt="로고" class="h-8 w-auto" />
        <span class="text-xl font-bold text-green-600">부동산 정보 플랫폼</span>
      </router-link>

      <nav class="space-x-6 flex items-center">
        <router-link to="/map" class="text-gray-700 hover:underline">지도</router-link>
        <router-link to="/community" class="text-gray-700 hover:underline">커뮤니티</router-link>
        <router-link to="/calculator" class="text-gray-700 hover:underline">계산기</router-link>

        <div v-if="userStore.user" class="flex items-center space-x-4">
          <span class="text-sm text-gray-600">
            안녕하세요, <strong>{{ userStore.user.name || userStore.user.user_id }}</strong>님
          </span>
          <button @click="logout" class="bg-gray-500 text-white px-4 py-1 rounded">로그아웃</button>
        </div>

        <button v-else @click="loginWithNaver" class="bg-green-500 text-white px-4 py-1 rounded">
          로그인
        </button>
      </nav>
    </div>
  </header>
</template>
