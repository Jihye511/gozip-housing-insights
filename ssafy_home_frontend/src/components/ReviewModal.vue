<template>
  <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-xl relative">
      <button class="absolute top-2 right-3 text-gray-500" @click="$emit('close')">✕</button>
      <h2 class="text-lg font-semibold mb-4">리뷰 작성</h2>

      <textarea
        v-model="review.content"
        rows="3"
        class="w-full p-2 border rounded mb-2"
        placeholder="거주 후기를 입력하세요"
      />

      <select v-model="review.score" class="w-full p-2 border rounded mb-2">
        <option disabled value="">평점 선택</option>
        <option v-for="n in 5" :key="n" :value="n">{{ n }}점</option>
      </select>
      <input type="file" @change="onFileChange" class="mb-2" />

      <button
        class="w-full bg-green-600 text-white p-2 rounded"
        :disabled="!canSubmit"
        @click="submit"
      >
        등록
      </button>
    </div>
  </div>
</template>

<script>
import axios from '@/axios/axios'
export default {
  props: {
    aptId: String,
  },
  data() {
    return {
      review: {
        content: '',
        score: '',
        image_file: '',
      },
    }
  },
  computed: {
    canSubmit() {
      return this.review.content && this.review.score
    },
  },
  methods: {
    onFileChange(e) {
      const file = e.target.files[0]
      if (!file) return
      const reader = new FileReader()
      reader.onload = () => {
        this.review.image_file = reader.result
        // "data:image/jpeg;base64,...." 형태
      }
      reader.readAsDataURL(file)
    },
    async submit() {
      this.review.apt_id = this.aptId
      try {
        await axios.post('/reviews', this.review)
        this.$emit('submitted')
        this.$emit('close')
      } catch (err) {
        alert('리뷰 등록에 실패했습니다.')
      }
    },
  },
}
</script>
