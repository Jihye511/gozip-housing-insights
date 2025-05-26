<template>
  <div class="h-screen overflow-y-scroll snap-y snap-mandatory scroll-smooth">
    <!-- Section 0: 메인 소개 -->
    <main class="snap-start min-h-screen flex flex-col items-center justify-center py-20 text-center bg-gradient-to-b from-white to-blue-50">
      <h2 class="text-4xl font-bold mb-4">
        <span class="text-blue-600">당신의 공간</span>, <span class="text-black">우리의</span> <span class="text-green-500">데이터</span><span class="text-black">로 완성됩니다.</span>
      </h2>
      <p class="text-gray-700 mb-8 max-w-2xl">
        최적의 부동산 결정을 위한 데이터 기반 플랫폼으로,
        복잡한 부동산 시장을 쉽고 명확하게 이해할 수 있도록 도와드립니다.
      </p>
      <div class="space-x-4">
        <button
          @click="$router.push('/map')"
          class="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600"
        >
          서비스 시작하기
        </button>
        <button class="border border-blue-500 text-blue-500 px-6 py-2 rounded hover:bg-blue-50">더 알아보기</button>
      </div>
    </main>

    <!-- Section 1: 지도 -->
    <section class="snap-start min-h-screen flex flex-col md:flex-row items-center justify-center bg-white py-20 px-6 gap-10">
      <div class="bg-blue-50 p-6 rounded-xl shadow w-full md:w-96">
        <h3 class="text-blue-600 text-lg font-semibold mb-4">🗺 데이터 기반 부동산 지도</h3>
        <ul class="text-sm space-y-3 text-gray-800">
          <li>🔵 <strong>실시간 시세 그래프:</strong> 지역별 시세를 시각적으로 파악</li>
          <li>🔵 <strong>스마트 매물 검색:</strong> 원하는 조건에 딱 맞는 매물 탐색</li>
          <li>🔵 <strong>신뢰할 수 있는 리뷰:</strong> 실거주자의 솔직한 후기 확인</li>
        </ul>
      </div>
      <div class="text-left max-w-xl">
        <h2 class="text-3xl font-bold text-gray-800">
          지역별 <span class="text-blue-600">부동산 정보</span><br />를 한눈에 파악하세요
        </h2>
        <p class="text-gray-700 mt-4">
          인공지능이 분석한 부동산 시세와 지역 인프라 정보를 통해 투자와 거주를 위한 결정을 내릴 수 있습니다.
        </p>
        <a href="#" class="text-blue-600 mt-4 inline-block hover:underline" @click="$router.push('/map')">지도 서비스 이용하기 →</a>
      </div>
    </section>

    <!-- Section 2: 계산기 -->
    <section class="snap-start min-h-screen flex flex-col-reverse md:flex-row items-center justify-center bg-green-50 py-20 px-6 gap-10">
      <div class="text-left max-w-xl">
        <h2 class="text-3xl font-bold text-gray-800">
          복잡한 <span class="text-green-500">부동산 계산</span><br />을 쉽고 정확하게
        </h2>
        <p class="text-gray-700 mt-4">
          누구나 쉽게 부동산 금융을 계산할 수 있도록 설계된 스마트 계산기입니다.
        </p>
        <a href="#" class="text-green-600 mt-4 inline-block hover:underline" @click="$router.push('/calculator')">계산기 서비스 이용하기 →</a>
      </div>
      <div class="bg-white p-6 rounded-xl shadow w-full md:w-96">
        <h3 class="text-green-600 text-lg font-semibold mb-4">📊 스마트 부동산 계산기</h3>
        <ul class="text-sm space-y-3 text-gray-800">
          <li>✅ <strong>중개보수 계산기:</strong> 지역과 유형에 맞춰 중개비 계산</li>
          <li>✅ <strong>대출이자 계산기:</strong> 금리와 상환방식을 비교</li>
          <li>✅ <strong>일반 계산기:</strong> 유지비용 및 세금까지 계산</li>
        </ul>
      </div>

    </section>

    <!-- Section 3: 커뮤니티 -->
    <section class="snap-start min-h-screen flex flex-col md:flex-row items-center justify-center bg-blue-50 py-20 px-6 gap-10">
      <div class="bg-white p-6 rounded-xl shadow w-full md:w-96">
        <h3 class="text-blue-600 text-lg font-semibold mb-4">👥 부동산 커뮤니티</h3>
        <ul class="text-sm space-y-3 text-gray-800">
          <li>💬 <strong>실시간 부동산 정보:</strong> 최신 트렌드와 이슈</li>
          <li>💬 <strong>경험 공유 플랫폼:</strong> 지역 정보, 거래 후기 공유</li>
          <li>💬 <strong>전문가 상담:</strong> 질문/답변을 통한 전문 정보 획득</li>
        </ul>
      </div>
      <div class="text-left max-w-xl">
        <h2 class="text-3xl font-bold text-gray-800">
          <span class="text-blue-600">부동산 지식</span>을<br />함께 나누는 공간
        </h2>
        <p class="text-gray-700 mt-4">
          실제 경험을 바탕으로 한 다양한 관점의 정보와 인사이트를 공유하고 소통할 수 있는 커뮤니티입니다.
        </p>
        <a href="#" class="text-blue-600 mt-4 inline-block hover:underline" @click="$router.push('/community')">커뮤니티 참여하기 →</a>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
};
</script>

<style scoped>
/* Tailwind 기반이므로 별도 스타일은 필요 없음 */
</style>
