<template>
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40"
    v-if="visible"
  >
    <div class="bg-white p-6 rounded-lg w-full max-w-md h-[90vh] flex flex-col">
      <!-- 헤더 -->
      <div class="flex justify-between items-center mb-2 shrink-0">
        <h2 class="text-lg font-semibold text-green-600">AI 추천 체크리스트</h2>
        <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">✕</button>
      </div>

      <p class="text-sm text-gray-600 mb-2 shrink-0">
        원하는 조건을 선택하시면 AI가 최적의 부동산을 추천해드립니다.
      </p>

      <!-- 스크롤 가능한 영역 -->
      <div class="overflow-y-auto pr-1 grow" style="max-height: calc(90vh - 160px)">
        <!-- 시도 선택 -->
        <div class="mb-2">
          <label class="text-sm font-medium"> 시도 선택 <span class="text-red-500">*</span> </label>
          <select v-model="sido" @change="selectSido(sido)" class="w-full border p-2 rounded mt-1">
            <option value="">시도 선택</option>
            <option v-for="option in sidoList" :key="option.code" :value="option.code">
              {{ option.name }}
            </option>
          </select>
        </div>

        <!-- 구군 선택 -->
        <div class="mb-2" v-if="gugunList.length">
          <label class="text-sm font-medium"> 구군 선택 <span class="text-red-500">*</span> </label>
          <select
            v-model="gugun"
            @change="selectGugun(gugun)"
            class="w-full border p-2 rounded mt-1"
          >
            <option value="">구 선택</option>
            <option v-for="option in gugunList" :key="option.code" :value="option.code">
              {{ option.name }}
            </option>
          </select>
        </div>

        <!-- 동 선택 -->
        <div class="mb-4" v-if="dongList.length">
          <label class="text-sm font-medium"> 동 선택 <span class="text-red-500">*</span> </label>
          <select v-model="dong" @change="selectDong(dong)" class="w-full border p-2 rounded mt-1">
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
            <div>
              <label
                ><input type="checkbox" value="역세권" v-model="env" /> 역세권 (도보 5분
                이내)</label
              >
              <label class="ml-4"
                ><input type="checkbox" value="학교 근처" v-model="env" /> 학교 근처 (도보 10분
                이내)</label
              >
            </div>
            <div>
              <label
                ><input type="checkbox" value="조용한 주거지" v-model="env" /> 소음이 적은
                환경</label
              >
              <label class="ml-4"
                ><input type="checkbox" value="보안 우수" v-model="env" /> 보안 시설 우수</label
              >
            </div>
            <div>
              <label><input type="checkbox" value="신축" v-model="env" /> 신축 (5년 이내)</label>
            </div>
          </div>
        </div>

        <!-- 목적 -->
        <div class="mb-4">
          <h3 class="text-sm font-bold text-gray-700">목적</h3>
          <div class="space-y-1 text-sm text-gray-700">
            <label><input type="checkbox" value="투자" v-model="purpose" /> 투자용</label>
            <label class="ml-10"
              ><input type="checkbox" value="실거주" v-model="purpose" /> 실거주용</label
            >
          </div>
        </div>

        <!-- 예산 -->
        <div class="mb-4">
          <label class="text-sm font-semibold block mb-1">예산</label>
          <select v-model="budget" class="w-full border p-2 rounded">
            <option value="1억 이하">1억 이하</option>
            <option value="1억 ~ 5억">1억 ~ 5억</option>
            <option value="5억 ~ 10억">5억 ~ 10억</option>
            <option value="10억 이상">10억 이상</option>
          </select>
        </div>

        <!-- 평수 -->
        <div class="mb-4">
          <label class="text-sm font-semibold block mb-1">평수</label>
          <select v-model="area" class="w-full border p-2 rounded">
            <option value="소형 (20평 이하)">20평 이하</option>
            <option value="중형 (20~30평)">20~30평</option>
            <option value="대형 (30평 이상)">30평 이상</option>
          </select>
        </div>
        <!-- 로딩 중 표시 -->
        <div v-if="isLoading" class="flex items-center justify-center text-gray-500 my-4">
          <svg
            class="animate-spin h-5 w-5 mr-2 text-green-600"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              class="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              stroke-width="4"
            ></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
          </svg>
          AI 추천 결과를 생성 중입니다...
        </div>
        <!-- 추천 결과 -->
        <div
          v-if="responseData"
          ref="resultSection"
          class="mt-4 border-t pt-4 text-sm text-gray-700"
        >
          <h3 class="text-sm font-bold text-green-600 mb-2">AI 추천 결과</h3>
          <pre class="bg-gray-100 p-3 rounded whitespace-pre-wrap">{{ responseData }}</pre>

          <div class="flex justify-end mt-2">
            <button class="bg-blue-600 text-white px-4 py-2 rounded" @click="confirmRecommendation">
              추천 결과 상세 정보 보기
            </button>
          </div>
        </div>
      </div>

      <!-- 하단 버튼 고정 -->
      <div class="mt-3 flex justify-end gap-2 shrink-0">
        <button @click="$emit('close')" class="border px-4 py-2 rounded">취소</button>
        <button class="bg-green-600 text-white px-4 py-2 rounded" @click="submit">
          AI 추천받기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchRegionList } from '@/utils/regionApi'
import axios from 'axios'

let globalRequestId = 0

export default {
  props: {
    visible: Boolean,
  },
  emits: ['close', 'submit', 'confirm'],
  data() {
    return {
      sido: '',
      gugun: '',
      dong: '',
      sidoName: '',
      gugunName: '',
      dongName: '',
      sidoList: [],
      gugunList: [],
      dongList: [],
      env: [],
      purpose: [],
      budget: '',
      area: '',
      responseData: null,
      currentRequestId: 0,

      isloading: false,
    }
  },
  mounted() {
    this.fetchSido()
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.resetForm()
      } else {
        // 모달이 닫혔을 때 현재 요청 ID를 초기화
        this.currentRequestId = -1
      }
    },
  },
  methods: {
    async fetchSido() {
      const data = await fetchRegionList('*00000000')
      this.sidoList = data.regcodes.map((r) => ({ code: r.code, name: r.name }))
    },
    async selectSido(code) {
      this.sido = code
      this.sidoName = this.sidoList.find((r) => r.code === code)?.name || ''

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
      this.gugunName = this.gugunList.find((r) => r.code === code)?.name || ''

      this.dong = ''
      const pattern = code.substr(0, 5) + '*'
      const data = await fetchRegionList(pattern)
      this.dongList = data.regcodes.map((r) => {
        const nameParts = r.name.split(' ')
        return { code: r.code, name: nameParts[2] || nameParts[3] }
      })
    },
    async selectDong(code) {
      this.dongName = this.dongList.find((r) => r.code === code)?.name || ''
    },

    async submit() {
      this.responseData = null // 기존 응답 초기화
      this.isLoading = true // 로딩 시작

      const payload = {
        dongCode: this.dong,
        sido: this.sidoName,
        gugun: this.gugunName,
        dong: this.dongName,
        env: this.env,
        purpose: this.purpose.join(', '),
        budget: this.budget,
        area: this.area,
      }

      const requestId = ++globalRequestId
      this.currentRequestId = requestId

      try {
        const response = await axios.post('http://localhost:8080/api/recommend', payload)

        // 응답이 내 요청 시점의 ID와 같을 때만 반영
        if (this.currentRequestId === requestId) {
          this.responseData = response.data

          this.$nextTick(() => {
            const el = this.$refs.resultSection
            if (el) el.scrollIntoView({ behavior: 'smooth' })
          })
        } else {
          console.warn('이전 요청 응답 도착 - 무시함')
        }
      } catch (error) {
        console.error('AI 추천 요청 실패:', error)
      } finally {
        this.isLoading = false // 로딩 종료
      }
    },
    resetForm() {
      this.sido = ''
      this.sidoName = ''
      this.gugun = ''
      this.gugunName = ''
      this.dong = ''
      this.dongName = ''
      this.gugunList = []
      this.dongList = []
      this.env = []
      this.purpose = []
      this.budget = ''
      this.area = ''
      this.responseData = null
    },
    confirmRecommendation() {
      const aptNames = this.extractAptNames(this.responseData)
      if (!aptNames.length) {
        alert('추천 결과에서 아파트 이름을 찾을 수 없습니다.')
        return
      }

      this.$emit('close')
      this.$emit('confirm', aptNames) // 부모에 추천 결과 전달

      axios
        .post(
          'http://localhost:8080/api/apt/aptseqList',
          { aptNames },
          {
            // apt_seq로 변환하는 백엔드 API
            headers: {}, // ✅ 인증 헤더 제거 (axios 인스턴스에 기본 설정된 경우)
          },
        )
        .then((res) => {
          const aptSeqList = res.data
          this.$emit('close') // 모달 닫고
          this.$router.push({
            name: 'MapPage',
            query: { aptSeqList: JSON.stringify(aptSeqList) },
          })
        })
        .catch((err) => {
          console.error('추천 결과 이동 중 오류 발생', err)
        })
    },
    extractAptNames(responseText) {
      const lines = responseText.split('\n')
      const aptNames = lines
        .map((line) => {
          const match = line.match(/\*\*(.+?)\*\*/) // ✅ "** ~ **" 안의 모든 텍스트
          return match ? match[1].trim() : null
        })
        .filter(Boolean)
      return aptNames
    },
  },
}
</script>

<style scoped></style>
