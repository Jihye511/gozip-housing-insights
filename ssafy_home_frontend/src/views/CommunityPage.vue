<template>
  <div class="min-h-screen bg-gray-100 relative">
    <main class="max-w-5xl mx-auto py-10">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold">부동산 커뮤니티</h2>
        <div class="flex items-center gap-2">
          <input
            type="text"
            v-model="searchKeyword"
            placeholder="검색어를 입력하세요"
            class="border p-2 rounded w-64"
          />

          <button @click="showWriteModal = true" class="bg-green-500 text-white px-4 py-2 rounded">글쓰기</button>
        </div>
      </div>

      <!-- ✅ 카테고리 필터 -->
      <div class="bg-white p-3 rounded mb-4 flex gap-4 text-sm font-medium text-gray-600 border">
        <button
          v-for="cat in categories"
          :key="cat"
          @click="selectedCategory = cat"
          :class="[
            'px-3 py-1 rounded-full',
            selectedCategory === cat
              ? 'bg-green-500 text-white'
              : 'hover:bg-gray-100'
          ]"
        >
          {{ cat }}
        </button>
      </div>

      <!-- ✅ 게시글 목록 -->
      <div class="space-y-4">
        <div
          v-for="post in filteredPosts"
          :key="post.board_id"
          class="relative bg-white shadow rounded p-4 cursor-pointer"
        >
          <div @click="openDetail(post)">
            <h3 class="font-semibold text-lg">{{ post.title }}</h3>
            <p class="text-sm text-gray-600">{{ post.content.slice(0, 60) }}...</p>
            <div class="flex justify-between text-xs text-gray-500 mt-2">
              <span>
                {{ post.user_id }} · {{ formatDate(post.created_at) }} · 💬 {{ post.comment_count }}
              </span>
              <span class="bg-gray-100 px-2 py-1 rounded-full text-xs text-gray-700">{{ post.category }}</span>
            </div>
          </div>

          <!-- ✅ 관리자만 삭제 가능 -->
          <button
            v-if="isAdmin"
            @click.stop="deletePost(post.board_id)"
            class="absolute top-2 right-2 text-sm text-red-500 hover:underline"
          >
            삭제
          </button>
        </div>

      </div>
    </main>

    <!-- 상세 모달 -->
    <div v-if="showDetailModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-2xl rounded-lg p-6 relative">
        <button @click="showDetailModal = false" class="absolute top-2 right-2 text-gray-500">✕</button>
        <!-- 제목 -->
        <h3 class="text-xl font-bold mb-2">{{ selectedPost?.title }}</h3>

        <!-- 작성자 · 날짜 · 카테고리 · 댓글수 -->
        <p class="text-sm text-gray-500 mb-1">
          {{ selectedPost?.user_id }} ·
          {{ formatDate(selectedPost?.created_at) }} ·
          {{ selectedPost?.category }} ·
          💬 {{ comments.length }}
        </p>

        <button
          v-if="String(selectedPost?.user_id) === String(currentUserId)"
          @click.stop="deletePost(selectedPost.board_id)"
          class="absolute top-12 right-2 text-sm text-red-500 hover:underline"
        >
          삭제
        </button>
        
        <div class="border-t pt-4">
          <h4 class="font-semibold mb-2">댓글</h4>
          <div v-for="comment in comments" :key="comment.comment_id" class="mb-2 text-sm text-gray-700 border-b pb-2">
            <div class="flex justify-between">
              <span class="font-medium">{{ comment.user_id }}</span>
              <span class="text-xs text-gray-400">{{ comment.created_at }}</span>
            </div>
            <p>{{ comment.content }}</p>
          </div>

          <div class="flex items-center gap-2 mt-2">
            <input v-model="commentContent" type="text" placeholder="댓글을 입력하세요..." class="w-full border p-2 rounded" />
            <button @click="submitComment" class="bg-black text-white px-4 py-1 rounded">등록</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 글쓰기 모달 -->
    <div v-if="showWriteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-lg rounded-lg p-6 relative">
        <button @click="showWriteModal = false" class="absolute top-2 right-2 text-gray-500">✕</button>
        <h3 class="text-xl font-bold mb-4">새 게시글 작성</h3>
        <form @submit.prevent="submitPost" class="space-y-4">
          <div>
            <label class="block mb-1 text-sm font-medium">카테고리</label>
            <select v-model="postCategory" class="w-full border p-2 rounded">
              <option>정보공유</option>
              <option>질문</option>
              <option>시장동향</option>
              <option>정책토론</option>
            </select>
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">제목</label>
            <input v-model="postTitle" type="text" class="w-full border p-2 rounded" placeholder="제목을 입력하세요" />
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">내용</label>
            <textarea v-model="postContent" class="w-full border p-2 rounded h-32" placeholder="내용을 입력하세요"></textarea>
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
import axios from '@/axios/axios';

export default {
  data() {
    return {
      currentUserId: '',
      isAdmin: false, // 관리자 여부
      posts: [],
      selectedPost: null,
      selectedCategory: '전체',
      categories: ['전체', '시장동향', '정보공유', '질문', '정책토론'],
      showDetailModal: false,
      showWriteModal: false,
      postTitle: '',
      postContent: '',
      postCategory: '정보공유',
      comments: [],
      commentContent: '',
      searchKeyword: '',
    };
  },
  computed: {
    filteredPosts() {
      return this.posts
        .filter(post => {
          return this.selectedCategory === '전체' || post.category === this.selectedCategory;
        })
        .filter(post => {
          const keyword = this.searchKeyword.toLowerCase();
          return (
            post.title.toLowerCase().includes(keyword) ||
            post.content.toLowerCase().includes(keyword)
          );
        });
    },
    commentCount() {
      return this.comments.length;
    },
  },
  async mounted() {
    await this.fetchCurrentUser();
    await this.fetchPosts();
  },
  methods: {
    formatDate(datetimeStr) {
      if (!datetimeStr) return '';
      return datetimeStr.replace('T', ' ').slice(0, 16); // "2023-05-25 14:22"
    },
  
    async fetchCurrentUser() {
      try {
        const res = await axios.get('/user'); 
        console.log('🔥 전체 응답 데이터:', res.data);

        this.currentUserId = res.data.userId;
        this.isAdmin = res.data.role === 'ADMIN';

        console.log("currentUserId:", this.currentUserId);
        console.log("isAdmin:", this.isAdmin);
      } catch (e) {
        console.error('로그인 사용자 정보 불러오기 실패', e);
      }
    },

    async fetchPosts() {
      try {
        const res = await axios.get('/community');
        this.posts = res.data;
      } catch (e) {
        console.error('게시글 불러오기 실패', e);
      }
    },
    async deletePost(boardId) {
      if (!confirm('정말 삭제하시겠습니까?')) return;
      try {
        await axios.delete(`/community/${boardId}`);
        alert('삭제되었습니다');
        this.fetchPosts();
      } catch (e) {
        console.error('삭제 실패', e);
        alert('삭제에 실패했습니다.');
      }
    },
    openDetail(post) {
      this.selectedPost = post;
      this.showDetailModal = true;
      console.log('✅ post 클릭됨:', post);
      console.log('✅ selectedPost 저장됨:', this.selectedPost);
      this.fetchComments(post.board_id);
    },
    async fetchComments(boardId) {
      try {
        const res = await axios.get(`/community/comment/${boardId}`);
        this.comments = res.data;
      } catch (e) {
        console.error('댓글 불러오기 실패', e);
      }
    },
    async submitPost() {
      try {
        await axios.post('/community', {
          title: this.postTitle,
          content: this.postContent,
          category: this.postCategory,
        });
        this.showWriteModal = false;
        this.postTitle = '';
        this.postContent = '';
        this.fetchPosts();
      } catch (e) {
        console.error('게시글 등록 실패', e);
      }
    },
    async submitComment() {
      if (!this.commentContent.trim()) return;
      try {
        await axios.post('/community/comment', {
          board_id: this.selectedPost.board_id,
          content: this.commentContent,
        });
        this.commentContent = '';
        this.fetchComments(this.selectedPost.board_id);
      } catch (e) {
        console.error('댓글 등록 실패', e);
      }
    },
  },
};
</script>

<style scoped>
/* 필요 시 추가 스타일 작성 */
</style>
