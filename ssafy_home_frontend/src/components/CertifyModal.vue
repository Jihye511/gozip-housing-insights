<template>
  <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-xl relative">
      <button class="absolute top-2 right-3 text-gray-500" @click="$emit('close')">✕</button>
      <h2 class="text-lg font-semibold mb-4">실거주 인증 요청</h2>

      <p class="mb-2">아파트 ID: <strong>{{ aptSeq }}</strong></p>

      <input
        type="file"
        accept="application/pdf,image/*"
        @change="onFileChange"
        class="w-full mb-4"
      />

      <button
        class="bg-green-600 text-white px-4 py-2 rounded disabled:opacity-50"
        :disabled="!file"
        @click="submitRequest"
      >
        요청 보내기
      </button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from '@/axios/axios'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

export default {
  name: 'CertifyModal',
  props: {
    aptSeq: { type: String, required: true }
  },
  setup(props, { emit }) {
    const file = ref(null)
    const userStore = useUserStore()
    const router = useRouter()

    const onFileChange = (e) => {
      const selected = e.target.files[0]
      if (selected) file.value = selected
    }

    const submitRequest = async () => {
      if (!userStore.user) {
        alert('로그인이 필요합니다.')
        emit('close')
        return router.push('/login')
      }
      try {
        const formData = new FormData()
        formData.append('apt_seq', props.aptSeq)
        formData.append('file', file.value)

        await axios.post(
          '/certifications',
          formData,
          { withCredentials: true, headers: { 'Content-Type': 'multipart/form-data' } }
        )
        alert('인증 요청이 전송되었습니다.')
        emit('close')
      } catch (err) {
        console.error('인증 요청 실패:', err)
        alert('인증 요청 중 오류가 발생했습니다.')
      }
    }

    return { file, onFileChange, submitRequest }
  }
}
</script>

<style scoped>
</style>
