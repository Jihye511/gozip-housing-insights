<template>
  <div class="min-h-screen bg-gray-100">

    <main class="max-w-6xl mx-auto py-10">
      <h2 class="text-2xl font-bold mb-6">관리자 페이지</h2>

      <div class="bg-white shadow rounded-lg p-6 mb-8">
        <h3 class="text-lg font-bold mb-4">회원 목록</h3>
        <input type="text" placeholder="회원 검색 (이름, 아이디, 닉네임, 이메일)" class="w-full border p-2 mb-4 rounded" />

        <div class="overflow-x-auto">
          <table class="w-full table-auto text-sm text-left">
            <thead class="bg-gray-100">
              <tr>
                <th class="px-4 py-2">ID</th>
                <th class="px-4 py-2">이름</th>
                <th class="px-4 py-2">아이디</th>
                <th class="px-4 py-2">닉네임</th>
                <th class="px-4 py-2">이메일</th>
                <th class="px-4 py-2">권한</th>
                <th class="px-4 py-2">거주지 인증</th>
                <th class="px-4 py-2">가입일</th>
                <th class="px-4 py-2">관리</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id" class="border-b">
                <td class="px-4 py-2">{{ user.id }}</td>
                <td class="px-4 py-2">{{ user.name }}</td>
                <td class="px-4 py-2">{{ user.username }}</td>
                <td class="px-4 py-2">{{ user.nickname }}</td>
                <td class="px-4 py-2">{{ user.email }}</td>
                <td class="px-4 py-2">{{ user.role }}</td>
                <td class="px-4 py-2">
                  <span
                    :class="{
                      'text-red-500': user.status === '미인증' || user.status === '거부됨',
                      'text-green-500': user.status === '승인됨',
                      'text-yellow-500': user.status === '승인 대기중',
                    }"
                  >{{ user.status }}</span>
                </td>
                <td class="px-4 py-2">{{ user.joined }}</td>
                <td class="px-4 py-2">
                  <button class="text-red-500 hover:underline">삭제</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-lg font-bold mb-4">게시글 관리</h3>
        <input type="text" placeholder="게시글 검색 (제목, 작성자, 카테고리)" class="w-full border p-2 mb-4 rounded" />

        <div class="overflow-x-auto">
          <table class="w-full table-auto text-sm text-left">
            <thead class="bg-gray-100">
              <tr>
                <th class="px-4 py-2">ID</th>
                <th class="px-4 py-2">제목</th>
                <th class="px-4 py-2">작성자</th>
                <th class="px-4 py-2">카테고리</th>
                <th class="px-4 py-2">작성일</th>
                <th class="px-4 py-2">댓글</th>
                <th class="px-4 py-2">관리</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="post in posts" :key="post.id" class="border-b">
                <td class="px-4 py-2">{{ post.id }}</td>
                <td class="px-4 py-2">{{ post.title }}</td>
                <td class="px-4 py-2">{{ post.author }}</td>
                <td class="px-4 py-2">{{ post.category }}</td>
                <td class="px-4 py-2">{{ post.date }}</td>
                <td class="px-4 py-2">{{ post.comments }}</td>
                <td class="px-4 py-2">
                  <button class="text-red-500 hover:underline">삭제</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>



<script>

export default {
  data() {
    return {
      users: [
        { id: 1, name: '김민수', username: 'user1', nickname: '부동산왕', email: 'user1@example.com', role: '일반회원', status: '미인증', joined: '2023-01-15' },
        { id: 2, name: '이지은', username: 'user2', nickname: '집을찾는중', email: 'user2@example.com', role: '일반회원', status: '승인됨', joined: '2023-02-20' },
        { id: 3, name: '박준호', username: 'user3', nickname: '전세꾼', email: 'user3@example.com', role: '일반회원', status: '승인됨', joined: '2023-03-05' },
        { id: 4, name: '관리자', username: 'admin1', nickname: '어드민', email: 'admin@example.com', role: '관리자', status: '승인됨', joined: '2022-12-01' },
        { id: 5, name: '최유진', username: 'user4', nickname: '월세쪽', email: 'user4@example.com', role: '일반회원', status: '거부됨', joined: '2023-04-12' },
        { id: 6, name: '정서연', username: 'user5', nickname: '부동산전문가', email: 'user5@example.com', role: '일반회원', status: '승인 대기중', joined: '2023-05-22' },
      ],
      posts: [
        { id: 1, title: '강남 아파트 시세가 또 올랐네요', author: '부동산전문가', category: '시장동향', date: '2023-05-15', comments: 13 },
        { id: 3, title: '신혼부부 내집마련 어떻게 하셨나요?', author: '예비신랑', category: '질문', date: '2023-05-08', comments: 28 },
        { id: 4, title: '정부 부동산 정책 새로 발표됐네요', author: '정책연구가', category: '정책토론', date: '2023-05-05', comments: 45 },
        { id: 5, title: '역세권 vs 학세권 어떤 것이 더 좋을까요?', author: '초보투자자', category: '질문', date: '2023-05-03', comments: 23 },
      ]
    };
  }
};
</script>

<style scoped>
</style>
