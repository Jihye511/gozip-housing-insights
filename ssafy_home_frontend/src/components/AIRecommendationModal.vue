<template>
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40"
    v-if="visible"
  >
    <div class="bg-white p-6 rounded-lg w-full max-w-md">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-lg font-semibold text-green-600">AI 추천 체크리스트</h2>
        <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">✕</button>
      </div>
      <p class="text-sm text-gray-600 mb-4">
        원하는 조건을 선택하시면 AI가 최적의 부동산을 추천해드립니다.
      </p>

     <!-- 시도 선택 -->
    <div class="mb-2">
    <label class="text-sm font-medium">시도 선택</label>
    <select v-model="sido" @change="selectSido(sido)" class="w-full border p-2 rounded mt-1">
        <option value="">시도 선택</option>
        <option v-for="option in sidoList" :key="option.code" :value="option.code">
        {{ option.name }}
        </option>
    </select>
    </div>

    <!-- 구군 선택 -->
    <div class="mb-2" v-if="gugunList.length">
    <label class="text-sm font-medium">구군 선택</label>
    <select v-model="gugun" @change="selectGugun(gugun)" class="w-full border p-2 rounded mt-1">
        <option value="">구 선택</option>
        <option v-for="option in gugunList" :key="option.code" :value="option.code">
        {{ option.name }}
        </option>
    </select>
    </div>

    <!-- 동 선택 -->
    <div class="mb-4" v-if="dongList.length">
    <label class="text-sm font-medium">동 선택</label>
    <select v-model="dong" class="w-full border p-2 rounded mt-1">
        <option value="">동 선택</option>
        <option v-for="option in dongList" :key="option.code" :value="option.code">
        {{ option.name }}
        </option>
    </select>
    </div>




      <!-- 주거 환경 -->
      <div class="mb-4">
        <h3 class="text-sm font-bold text-gray-700">주거 환경</h3>
        <div class="space-y-1 text-sm text-gray-700">
          <label><input type="checkbox" value="역세권" v-model="env" /> 역세권 (도보 5분 이내)</label>
          <label><input type="checkbox" value="학교 근처" v-model="env" /> 학교 근처 (도보 10분 이내)</label>
          <label><input type="checkbox" value="조용한 주거지" v-model="env" /> 소음이 적은 환경</label>
          <label><input type="checkbox" value="보안 우수" v-model="env" /> 보안 시설 우수</label>
          <label><input type="checkbox" value="신축" v-model="env" /> 신축 (5년 이내)</label>
        </div>
      </div>

      <!-- 목적 -->
      <div class="mb-4">
        <h3 class="text-sm font-bold text-gray-700">목적</h3>
        <div class="space-y-1 text-sm text-gray-700">
          <label><input type="checkbox" value="투자" v-model="purpose" /> 투자용</label>
          <label><input type="checkbox" value="실거주" v-model="purpose" /> 실거주용</label>
        </div>
      </div>

      <!-- 예산 & 평수 -->
      <div class="mb-4">
        <label class="text-sm font-semibold block mb-1">예산</label>
        <select v-model="budget" class="w-full border p-2 rounded">
          <option value="1억 이하">1억 이하</option>
          <option value="1억 ~ 5억">1억 ~ 5억</option>
          <option value="5억 ~ 10억">5억 ~ 10억</option>
          <option value="10억 이상">10억 이상</option>
        </select>
      </div>

      <div class="mb-4">
        <label class="text-sm font-semibold block mb-1">평수</label>
        <select v-model="area" class="w-full border p-2 rounded">
          <option value="소형 (20평 이하)">20평 이하</option>
          <option value="중형 (20~30평)">20~30평</option>
          <option value="대형 (30평 이상)">30평 이상</option>
        </select>
      </div>

      <!-- 버튼 -->
      <div class="flex justify-end gap-2">
        <button @click="$emit('close')" class="border px-4 py-2 rounded">취소</button>
        <button class="bg-green-600 text-white px-4 py-2 rounded" @click="submit">AI 추천받기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchRegionList } from '@/utils/regionApi'

export default {
  props: {
    visible: Boolean
  },
  emits: ['close', 'submit'],
  data() {
    return {
      sido: '',
      gugun: '',
      dong: '',
      sidoList: [],
      gugunList: [],
      dongList: [],
      env: [],
      purpose: [],
      budget: '',
      area: ''
    }
  },
  mounted() {
    this.fetchSido()
  },
  methods: {
    async fetchSido() {
      const data = await fetchRegionList('*00000000')
      this.sidoList = data.regcodes.map((r) => ({ code: r.code, name: r.name }))
    },
    async selectSido(code) {
      this.sido = code
      this.gugun = ''
      this.dong = ''
      const pattern = code.substr(0, 2) + '*00000'
      const data = await fetchRegionList(pattern)
      this.gugunList = data.regcodes.map((r) => {
        const nameParts = r.name.split(' ')
        return { code: r.code, name: nameParts[1] }
      })
    },
    async selectGugun(code) {
      this.gugun = code
      this.dong = ''
      const pattern = code.substr(0, 5) + '*'
      const data = await fetchRegionList(pattern)
      this.dongList = data.regcodes.map((r) => {
        const nameParts = r.name.split(' ')
        return { code: r.code, name: nameParts[2] || nameParts[3] }
      })
    },
    submit() {
      this.$emit('submit', {
        sido: this.sido,
        gugun: this.gugun,
        dong: this.dong,
        env: this.env,
        purpose: this.purpose,
        budget: this.budget,
        area: this.area
      })
    }
  }
}
</script>

<style scoped></style>
