<template>
  <header class="bg-white shadow py-4">
    <div class="max-w-7xl mx-auto flex items-center justify-between px-4">
      <!-- 로고 (클릭 시 메인 이동) -->
      <router-link to="/" class="flex items-center space-x-2">
        <img src="@/assets/gozip_logo.png" alt="로고" class="h-8 w-auto" />
        <span class="text-xl font-bold text-green-600">부동산 정보 플랫폼</span>
      </router-link>

      <!-- 메뉴 -->
      <nav class="space-x-6">
        <router-link to="/map" class="text-gray-700 hover:underline">지도</router-link>
        <router-link to="/community" class="text-gray-700 hover:underline">커뮤니티</router-link>
        <router-link to="/calculator" class="text-gray-700 hover:underline">계산기</router-link>
        <router-link to="/login" class="bg-green-500 text-white px-4 py-1 rounded">로그인</router-link>
      </nav>
    </div>
  </header>
</template>

<script>
export default {
  name: 'HeaderComponent',
};
</script>

<style scoped>
</style>
