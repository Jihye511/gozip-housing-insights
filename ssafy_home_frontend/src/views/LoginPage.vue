<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md bg-white p-6 rounded shadow">
      <h1 class="text-2xl font-bold mb-6 text-center">로그인</h1>

      <!-- 일반 로그인 폼 -->
      <form @submit.prevent="onSubmit" class="space-y-4">
        <div>
          <label class="block text-sm mb-1">아이디</label>
          <input
            v-model="username"
            type="text"
            class="w-full border rounded p-2"
            required
          />
        </div>
        <div>
          <label class="block text-sm mb-1">비밀번호</label>
          <input
            v-model="password"
            type="password"
            class="w-full border rounded p-2"
            required
          />
        </div>
        <button
          type="submit"
          class="w-full bg-green-600 text-white py-2 rounded"
        >
          로그인
        </button>
      </form>
      <div class="mt-4 text-center">
        <span class="text-sm text-gray-600">계정이 없으신가요?</span>
        <!-- 회원가입 페이지로 이동 -->
        <router-link
          to="/signup"
          class="ml-2 text-sm text-green-600 hover:underline"
        >
          회원가입
        </router-link>
      </div>
      <div class="my-6 text-center text-gray-500">또는</div>

      <!-- 네이버 OAuth 버튼 -->
      <button
        @click="onNaverLogin"
        class="w-full bg-[#03C75A] text-white py-2 rounded flex items-center justify-center"
      >
        <!-- <img src="@/assets/naver_logo.png" alt="네이버 로고" class="h-5 mr-2" /> -->
        네이버로 로그인
      </button>
    </div>
  </div>
</template>

<script>
import axios from '@/axios/axios'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {ref} from 'vue'

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const username = ref('')
    const password = ref('')

    const onSubmit = async () => {
      try {
        await axios.post('/auth/login', {
          username: username.value,
          password: password.value
        })
        // 로그인 성공하면 유저 정보 불러오기
        await userStore.fetchUser()
        router.push('/')  // 원하는 페이지로 이동
      } catch (err) {
        alert('로그인에 실패했습니다.')
        console.error(err)
      }
    }

    const onNaverLogin = () => {
      window.location.href = 'http://localhost:8080/oauth2/authorization/naver'
    }

    return { username, password, onSubmit, onNaverLogin }
  }
}
</script>

<style scoped>
/* 필요하면 스타일 추가 */
</style>
