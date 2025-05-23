<template>
  <div class="min-h-screen bg-gray-100 p-6">
    <main class="max-w-6xl mx-auto">
      <h2 class="text-2xl font-bold mb-6">관리자 페이지</h2>

      <!-- 인증 요청 관리 -->
      <div class="bg-white shadow rounded-lg p-6 mb-8">
        <h3 class="text-lg font-bold mb-4">아파트 실거주 인증 요청</h3>
        <div v-if="certRequests.length">
          <div class="overflow-x-auto">
            <table class="w-full table-auto text-sm text-left">
              <thead class="bg-gray-100">
                <tr>
                  <th class="px-4 py-2">요청 ID</th>
                  <th class="px-4 py-2">회원 ID</th>
                  <th class="px-4 py-2">아파트 ID</th>
                  <th class="px-4 py-2">상태</th>
                  <th class="px-4 py-2">관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="req in certRequests" :key="req.certification_id" class="border-b">
                  <td>{{ req.certification_id }}</td>
                  <td>{{ req.user_id }}</td>
                  <td>{{ req.apt_seq }}</td>
                  <td>{{ req.approval }}</td>
                  <td class="px-4 py-2 space-x-2">
                    <button
                      @click="approveCert(req.certification_id)"
                      class="text-green-600 hover:underline"
                    >
                      승인
                    </button>
                    <button
                      @click="rejectCert(req.certification_id)"
                      class="text-red-600 hover:underline"
                    >
                      거부
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div v-else class="text-gray-500">인증 요청이 없습니다.</div>
      </div>

      <!-- 회원 목록 관리 -->
      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-lg font-bold mb-4">회원 목록</h3>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="회원 검색 (이름, 아이디, 이메일)"
          class="w-full border p-2 mb-4 rounded"
        />

        <div class="overflow-x-auto">
          <table class="w-full table-auto text-sm text-left">
            <thead class="bg-gray-100">
              <tr>
                <th class="px-4 py-2">회원 ID</th>
                <th class="px-4 py-2">이름</th>
                <th class="px-4 py-2">이메일</th>
                <th class="px-4 py-2">권한</th>
                <th class="px-4 py-2">가입일</th>
                <th class="px-4 py-2">관리</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in filteredUsers" :key="user.user_id" class="border-b">
                <td class="px-4 py-2">{{ user.user_id }}</td>
                <td class="px-4 py-2">{{ user.name }}</td>
                <td class="px-4 py-2">{{ user.email }}</td>
                <td class="px-4 py-2">{{ user.role }}</td>
                <td class="px-4 py-2">{{ user.c_date }}</td>
                <td class="px-4 py-2">
                  <button @click="deleteUser(user.user_id)" class="text-red-500 hover:underline">
                    삭제
                  </button>
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
import axios from '@/axios/axios'
import { ref, computed, onMounted } from 'vue'

export default {
  name: 'AdminPage',
  setup() {
    const certRequests = ref([])
    const users = ref([])
    const searchQuery = ref('')

    // 인증 요청 불러오기
    const fetchCertRequests = async () => {
      try {
        const res = await axios.get('/admin/certifications', { withCredentials: true })
        certRequests.value = res.data
      } catch (err) {
        console.error('인증 요청 조회 실패:', err)
      }
    }

    // 회원 목록 불러오기
    const fetchUsers = async () => {
      try {
        const res = await axios.get('user/all', { withCredentials: true })
        console.log('>>> 서버가 준 users', res.data)
        users.value = res.data
        console.log('>>> users.value ▶', users.value)
      } catch (err) {
        console.error('회원 목록 조회 실패:', err)
      }
    }

    // 인증 승인/거부
    const approveCert = async (id) => {
      await axios.post(`/admin/certifications/${id}/approve`, null, { withCredentials: true })
      fetchCertRequests()
    }
    const rejectCert = async (id) => {
      await axios.post(`/admin/certifications/${id}/reject`, null, { withCredentials: true })
      fetchCertRequests()
    }

    // 회원 삭제
    const deleteUser = async (userId) => {
      if (!confirm('정말 이 회원을 삭제하시겠습니까?')) return
      await axios.delete(`/user/${userId}`, { withCredentials: true })
      fetchUsers()
    }

    const filteredUsers = computed(() => {
      // 검색어에서 불필요한 앞뒤 공백 제거
      const q = searchQuery.value.trim().toLowerCase()

      // 검색어가 비어 있으면 users 전체 반환
      if (!q) return users.value

      // 아니라면 user_id, name, email 중 하나라도 일치하는 것만 필터링
      return users.value.filter(
        (u) =>
          u.user_id.toLowerCase().includes(q) ||
          u.name.toLowerCase().includes(q) ||
          u.email.toLowerCase().includes(q),
      )
    })

    onMounted(() => {
      fetchCertRequests()
      fetchUsers()
    })

    return {
      certRequests,
      users,
      searchQuery,
      filteredUsers,
      approveCert,
      rejectCert,
      deleteUser,
    }
  },
}
</script>

<style scoped></style>
