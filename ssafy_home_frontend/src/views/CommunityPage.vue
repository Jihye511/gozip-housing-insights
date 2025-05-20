<template>
  <div class="min-h-screen bg-gray-100 relative">


    <main class="max-w-5xl mx-auto py-10">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold">부동산 커뮤니티</h2>
        <div class="flex items-center gap-2">
          <input type="text" placeholder="검색어를 입력하세요" class="border p-2 rounded w-64" />
          <button @click="showWriteModal = true" class="bg-green-500 text-white px-4 py-2 rounded">글쓰기</button>
        </div>
      </div>

      <div class="bg-purple-50 p-4 rounded mb-4">
        <div class="flex gap-4 text-sm font-medium text-gray-600">
          <button class="hover:underline">전체</button>
          <button class="hover:underline">시장동향</button>
          <button class="hover:underline">정보공유</button>
          <button class="hover:underline">질문</button>
          <button class="hover:underline">정책토론</button>
        </div>
      </div>

      <div class="space-y-4">
        <div class="bg-white shadow rounded p-4 cursor-pointer" @click="showDetailModal = true">
          <h3 class="font-semibold text-lg">전세 계약시 주의사항 공유합니다</h3>
          <p class="text-sm text-gray-600">최근 전세 사기가 많아지고 있어 제가 알고 있는 주의사항을 공유합니다...</p>
          <div class="flex justify-between text-xs text-gray-500 mt-2">
            <span>계약인턴 · 2023-05-10</span>
            <span>정보공유 · 👁 32</span>
          </div>
        </div>
      </div>
    </main>

    <!-- 상세 모달 -->
    <div v-if="showDetailModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-2xl rounded-lg p-6 relative">
        <button @click="showDetailModal = false" class="absolute top-2 right-2 text-gray-500">✕</button>
        <h3 class="text-xl font-bold mb-2">전세 계약시 주의사항 공유합니다</h3>
        <p class="text-sm text-gray-500 mb-1">계약인턴 · 2023-05-10 · 정보공유 · 👁 32</p>
        <p class="mt-4 mb-6 text-gray-700">최근 전세 사기가 많아지고 있어 제가 알고 있는 주의사항을 공유합니다...</p>

        <div class="border-t pt-4">
          <h4 class="font-semibold mb-2">댓글</h4>
          <div class="mb-2 text-sm text-gray-700 border-b pb-2">
            <div class="flex justify-between">
              <span class="font-medium">댓글작성자</span>
              <span class="text-xs text-gray-400">2023-05-16</span>
            </div>
            <p>정말 도움이 되는 내용이에요! 감사합니다.</p>
          </div>
          <div class="mb-4 text-sm text-gray-700">
            <div class="flex justify-between">
              <span class="font-medium">또다른댓글작성자</span>
              <span class="text-xs text-gray-400">2023-05-15</span>
            </div>
            <p>저도 비슷한 경험이 있어요. 좋은 정보 공유 감사합니다.</p>
          </div>
          <div class="flex items-center gap-2">
            <input type="text" placeholder="댓글을 입력하세요..." class="w-full border p-2 rounded" />
            <button class="bg-black text-white px-4 py-1 rounded">등록</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 글쓰기 모달 -->
    <div v-if="showWriteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-lg rounded-lg p-6 relative">
        <button @click="showWriteModal = false" class="absolute top-2 right-2 text-gray-500">✕</button>
        <h3 class="text-xl font-bold mb-4">새 게시글 작성</h3>
        <form class="space-y-4">
          <div>
            <label class="block mb-1 text-sm font-medium">카테고리</label>
            <select class="w-full border p-2 rounded">
              <option>정보공유</option>
              <option>질문</option>
              <option>시장동향</option>
              <option>정책토론</option>
            </select>
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">제목</label>
            <input type="text" class="w-full border p-2 rounded" placeholder="제목을 입력하세요" />
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">내용</label>
            <textarea class="w-full border p-2 rounded h-32" placeholder="내용을 입력하세요"></textarea>
          </div>
          <div class="flex justify-end gap-2">
            <button type="button" @click="showWriteModal = false" class="px-4 py-2 border rounded">취소</button>
            <button type="submit" class="px-4 py-2 bg-green-500 text-white rounded">등록하기</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showDetailModal: false,
      showWriteModal: false,
    };
  }
};
</script>

<style scoped>
</style>
