<template>
  <div class="min-h-screen bg-gray-100 flex">

    <!-- Sidebar (Menu 1) -->
    <aside class="w-64 bg-white border-r p-4 space-y-4">
      <router-link to="/" class="flex items-center space-x-2 hover:opacity-80">
        <img src="@/assets/gozip_logo.png" alt="로고" class="h-6 w-auto" />
        <h2 class="text-xl font-bold text-green-600">검색</h2>
      </router-link>
      <div>
        <label class="block text-sm font-medium mb-1">지역으로 검색</label>
        <select class="w-full border p-2 rounded mb-2">
          <option>시 선택</option>
        </select>
        <select class="w-full border p-2 rounded mb-2">
          <option>구 선택</option>
        </select>
        <select class="w-full border p-2 rounded mb-2">
          <option>동 선택</option>
        </select>
        <button class="w-full bg-green-600 text-white p-2 rounded">검색</button>
      </div>
      <div>
        <label class="block text-sm font-medium mb-1">아파트로 검색</label>
        <div class="flex items-center gap-2">
          <input class="flex-1 border p-2 rounded" placeholder="아파트명을 입력하세요" />
          <button class="bg-green-600 text-white px-3 py-2 rounded">검색</button>
        </div>
      </div>
      <div>
        <h3 class="font-bold text-gray-700 mt-4 mb-2">검색 결과</h3>
        <ul class="space-y-2">
          <li class="border p-2 rounded hover:bg-gray-100">
            <p class="font-semibold">한강 파크뷰</p>
            <p class="text-xs text-gray-500">서울시 강남구 압구정동 123-45</p>
            <p class="text-green-600 text-sm">12억 5,000만원</p>
          </li>
          <li class="border p-2 rounded hover:bg-gray-100">
            <p class="font-semibold">센트럴 파크</p>
            <p class="text-xs text-gray-500">서울시 강남구 청담동 456-78</p>
            <p class="text-green-600 text-sm">15억 2,000만원</p>
          </li>
        </ul>
      </div>
    </aside>

    <!-- Panel (Menu 2) -->
    <div class="w-[360px] bg-white p-4 border-r">
      <h2 class="text-xl font-bold mb-2">한강 파크뷰</h2>
      <p class="text-gray-600 text-sm mb-4">서울시 강남구 압구정동 123-45</p>
      <div class="space-y-2">
        <div>
          <h4 class="font-semibold text-sm text-gray-700">가격 및 정보</h4>
          <p class="text-sm">12억 5,000만원 • 84m² • 아파트</p>
        </div>
        <div>
          <h4 class="font-semibold text-sm text-gray-700">시세 그래프</h4>
          <div class="h-24 bg-gray-100 rounded flex items-center justify-center text-gray-400">
            시세 그래프 영역
          </div>
        </div>
        <div>
          <h4 class="font-semibold text-sm text-gray-700">거주자 리뷰</h4>
          <ul class="text-sm text-gray-700 space-y-1">
            <li>"교통이 편리하고 주변 편의시설이 잘 갖춰져 있어요" — 김* | 2023.05.12</li>
            <li>"층간소음이 조금 있지만 전반적으로 만족스러워요" — 이* | 2023.04.23</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Map 영역 -->
    <div class="flex-1 bg-gray-50 flex items-center justify-center text-green-700 font-semibold">
      카카오맵 지도 영역<br />
      <span class="text-gray-500 text-sm">실제 구현시 카카오맵 API로 대체됩니다</span>
    </div>
  </div>
</template>

<script>

export default {
  name: 'MapPage',

};
</script>

<style scoped></style>
