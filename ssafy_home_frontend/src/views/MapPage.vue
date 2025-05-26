<template>
  <div class="h-screen bg-gray-100 flex">
    <!-- Sidebar -->
    <aside class="w-64 bg-white border-r p-4 flex flex-col">
      <router-link to="/" class="flex items-center space-x-2 hover:opacity-80">
        <img src="@/assets/gozip_logo.png" alt="로고" class="h-6 w-auto" />
        <h2 class="text-xl font-bold text-green-600">검색</h2>
      </router-link>

      <!-- 지역 검색 -->
<section class="mb-6">
  <h3 class="text-lg font-semibold text-gray-700 mb-3">📍 지역 검색</h3>
  <div class="space-y-3">
    <select
      v-model="sido"
      @change="fetchGugun"
      class="w-full border-gray-200 rounded-lg p-3 shadow-sm focus:ring-2 focus:ring-green-500"
    >
      <option value="">시 선택</option>
      <option
        v-for="option in sidoList"
        :key="option.code"
        :value="option.code"
      >
        {{ option.name }}
      </option>
    </select>

    <select
      v-model="gugun"
      @change="fetchDong"
      class="w-full border-gray-200 rounded-lg p-3 shadow-sm focus:ring-2 focus:ring-green-500"
    >
      <option value="">구 선택</option>
      <option
        v-for="option in gugunList"
        :key="option.code"
        :value="option.code"
      >
        {{ option.name }}
      </option>
    </select>

    <select
      v-model="dong"
      class="w-full border-gray-200 rounded-lg p-3 shadow-sm focus:ring-2 focus:ring-green-500"
    >
      <option value="">동 선택</option>
      <option
        v-for="option in dongList"
        :key="option.code"
        :value="option.code"
      >
        {{ option.name }}
      </option>
    </select>

    <button
      @click="searchByRegion"
      class="w-full bg-green-600 hover:bg-green-700 text-white font-medium rounded-lg py-3 transition"
    >
      검색
    </button>
  </div>
</section>

      <!-- 아파트명 검색 -->
      <div>
        <label class="block text-sm font-medium mb-1">아파트로 검색</label>
        <div class="flex items-center gap-2">
          <input
            class="flex-1 border p-2 rounded"
            placeholder="아파트명을 입력하세요"
            v-model="aptName"
          />
          <button class="bg-green-600 text-white px-3 py-2 rounded" @click="searchByAptName">
            검색
          </button>
        </div>
      </div>

      <!-- 검색 결과 목록 -->

      <!-- 상승/하락 Top3 표시 -->
      <!-- ✅ 검색 결과 및 Top3 통합 스크롤 영역 -->
      <div class="overflow-y-auto space-y-4" style="max-height: calc(100vh - 280px)">
        <!-- 상승/하락 Top3 표시 -->
        <div
          v-if="showRankingPanel && ranking.length"
          class="bg-white border rounded p-3 shadow-sm"
        >
          <h3 class="text-center font-bold text-green-700 mb-3">📊 시세 변화 TOP 3</h3>

          <!-- 상승 Top 3 -->
          <div>
            <h4 class="text-sm font-semibold text-green-600 mb-2">📈 상승률 Top 3</h4>
            <ul class="space-y-2">
              <li
                v-for="apt in topUp"
                :key="'up-' + apt.aptSeq"
                class="border p-2 rounded hover:bg-gray-100 cursor-pointer"
                @click="
                  moveToApt(
                    {
                      apt_seq: apt.aptSeq,
                      apt_nm: apt.aptName,
                      road_nm: apt.address,
                      road_nm_bonbun: '',
                      latitude: apt.latitude,
                      longitude: apt.longitude,
                      dealList: [], // 빈 리스트라도 명시해주면 에러 방지
                    },
                    false,
                  )
                "
              >
                <p class="font-semibold">{{ apt.aptName }}</p>
                <p class="text-xs text-gray-500">{{ apt.address }}</p>
                <p class="text-green-600 text-sm">증감률: {{ apt.rateChange.toFixed(2) }}%</p>
              </li>
            </ul>
          </div>

          <hr class="my-3 border-gray-300" />

          <!-- 하락 Top 3 -->
          <div>
            <h4 class="text-sm font-semibold text-red-600 mb-2">📉 하락률 Top 3</h4>
            <ul class="space-y-2">
              <li
                v-for="apt in topDown"
                :key="'down-' + apt.aptSeq"
                class="border p-2 rounded hover:bg-gray-100 cursor-pointer"
                @click="
                  moveToApt(
                    {
                      apt_seq: apt.aptSeq,
                      apt_nm: apt.aptName,
                      road_nm: apt.address,
                      road_nm_bonbun: '',
                      latitude: apt.latitude,
                      longitude: apt.longitude,
                    },
                    false,
                  )
                "
              >
                <p class="font-semibold">{{ apt.aptName }}</p>
                <p class="text-xs text-gray-500">{{ apt.address }}</p>
                <p class="text-red-600 text-sm">증감률: {{ apt.rateChange.toFixed(2) }}%</p>
              </li>
            </ul>
          </div>
        </div>

        <!-- 🔍 검색 결과 목록 -->
        <div>
          <h3 class="font-bold text-gray-700 mb-2">검색 결과</h3>
          <ul class="space-y-2">
            <li
              v-for="apt in aptList"
              :key="apt.apt_seq"
              class="border p-2 rounded hover:bg-gray-100 cursor-pointer"
              @click="moveToApt(apt)"
            >
              <p class="font-semibold">{{ apt.apt_nm }}</p>
              <p class="text-xs text-gray-500">{{ apt.road_nm }} {{ apt.road_nm_bonbun }}</p>
              <p class="text-green-600 text-sm">
                <span v-if="apt.dealList && apt.dealList.length">{{
                  formatPrice(apt.dealList[0].deal_amount)
                }}</span>
                <span v-else>정보 없음</span>
              </p>
            </li>
          </ul>
        </div>
      </div>
    </aside>

    <!-- 상세 정보 패널 -->
<section
  v-if="aptDetailInfo"
  class="w-80 bg-white shadow-lg rounded-xl p-6 border-r overflow-y-auto"
>
  <h3 class="text-2xl font-bold text-gray-800 mb-3">
    {{ aptDetailInfo.aptName }}
  </h3>
  <p class="text-sm text-gray-500 mb-6">
    {{ aptDetailInfo.address }}
  </p>

  <div class="space-y-6">
    <!-- 가격 및 기본 정보 -->
    <div class="bg-gray-50 rounded-lg p-4">
      <h4 class="text-gray-700 font-semibold mb-2">가격 및 정보</h4>
      <p class="text-gray-600">
        <span v-if="selectedApt.dealList?.length">
          {{ formatPrice(selectedApt.dealList[0].deal_amount) }} •
        </span>
        {{ selectedApt.area || '84m²' }} • 아파트
      </p>
    </div>

    <!-- 평수 선택 -->
    <div v-if="areaList.length">
      <label class="block text-gray-700 font-medium mb-2">
        평수 선택
      </label>
      <select
        v-model="selectedArea"
        @change="fetchYearlyPrices"
        class="w-full border-gray-200 rounded-lg p-3 shadow-sm focus:ring-2 focus:ring-green-500"
      >
        <option
          v-for="item in areaList"
          :key="item.area"
          :value="item.area"
        >
          {{ item.area }}㎡
        </option>
      </select>
    </div>

    <!-- 선택 평수 평균가 -->
    <div v-if="selectedAvgPrice" class="text-gray-700">
      <p class="text-sm">
        선택한 평수의 평균 매매가:
        <span class="font-semibold text-green-600">
          {{ formatPrice(selectedAvgPrice.toString()) }}
        </span>
      </p>
    </div>

    <!-- 년도별 평균 매매가 -->
    <div v-if="yearlyPrices.length">
      <h4 class="text-gray-700 font-semibold mb-2">
        년도별 평균 매매가
      </h4>
      <ul class="space-y-1 text-sm text-gray-600">
        <li v-for="(item, idx) in yearlyPrices" :key="idx">
          {{ item.year }}년 — {{ formatPrice(item.avgPrice.toString()) }}
        </li>
      </ul>
    </div>

    <!-- 시세 그래프 -->
    <div>
      <h4 class="text-gray-700 font-semibold mb-2">시세 그래프</h4>
      <div class="bg-gray-100 rounded-lg p-4">
        <AptPriceChart
          v-if="yearlyPrices.length"
          :yearlyPrices="yearlyPrices"
          :key="selectedApt?.apt_seq + selectedArea"
        />
        <p v-else class="text-center text-gray-400 py-8">
          시세 데이터가 없습니다.
        </p>
      </div>
    </div>

    <!-- 거주자 리뷰 -->
    <div>
      <h4 class="text-gray-700 font-semibold mb-2">거주자 리뷰</h4>
      <button
        @click="checkCertification()"
        class="mb-4 bg-green-600 hover:bg-green-700 text-white font-medium rounded-lg px-4 py-2 transition"
      >
        리뷰 작성
      </button>
      <ul class="space-y-4">
        <li
          v-for="review in reviews"
          :key="review.review_id"
          class="border-b pb-4"
        >
          <p class="text-gray-800 text-sm mb-2">
            “{{ review.content }}” — {{ review.userName }} |
            <span class="font-semibold">{{ review.score }}점</span>
          </p>
          <img
            v-if="review.image_file"
            :src="review.image_file"
            alt="리뷰 이미지"
            class="w-36 h-36 object-cover rounded-lg mb-2"
          />
          <button
            v-if="review.user_id === userStore.userId"
            @click="deleteReview(review.review_id)"
            class="text-red-500 text-xs hover:underline"
          >
            삭제
          </button>
        </li>
      </ul>
    </div>
  </div>
</section>


    <!-- 지도 -->
<div class="flex-1 bg-gray-50 relative">
  <div id="map" class="absolute inset-0 z-0"></div>

  <div
    class="absolute top-6 right-6 z-50
           bg-white bg-opacity-90 backdrop-blur-md
           rounded-xl p-4 shadow-lg w-64"
  >
    <h4 class="text-sm font-semibold text-gray-700 mb-3">비교 연도 선택</h4>

    <div class="flex items-center space-x-2 mb-4">
      <select
        v-model="fromYear"
        class="flex-1 border-gray-200 rounded-lg p-2 shadow-sm focus:ring-2 focus:ring-green-500"
      >
        <option v-for="y in yearOptions" :key="y" :value="y">
          {{ y }}년
        </option>
      </select>
      <span class="text-gray-500 font-semibold">→</span>
      <select
        v-model="toYear"
        class="flex-1 border-gray-200 rounded-lg p-2 shadow-sm focus:ring-2 focus:ring-green-500"
      >
        <option v-for="y in yearOptions" :key="y" :value="y">
          {{ y }}년
        </option>
      </select>
    </div>

    <div class="flex space-x-2">
      <button
        @click="selectOverlay('RECTANGLE')"
        class="flex-1 border-2 border-green-500 text-green-500
               rounded-lg py-2 font-medium
               hover:bg-green-50 transition"
      >
        범위 탐색
      </button>
      <button
        @click="onCompleteDrawing"
        class="flex-1 bg-green-600 text-white
               rounded-lg py-2 font-medium
               hover:bg-green-700 transition"
      >
        완료
      </button>
    </div>
  </div>
</div>
    <!--  오른쪽 하단 고정 버튼 -->
<div>
  <button
    @click="showModal = true"
    class="fixed bottom-8 right-8
           bg-gradient-to-br from-green-400 to-green-600
           hover:from-green-500 hover:to-green-700
           text-white text-2xl
           p-5
           rounded-full
           shadow-2xl
           transform transition duration-300
           hover:scale-110
           focus:outline-none focus:ring-4 focus:ring-green-300
           animate-pulse"
  >
    💡 AI 추천
  </button>
</div>

    <!-- AI 추천 결과 영역 -->
    <div
      v-if="responseData"
      id="ai-result-section"
      class="mt-6 bg-white p-4 rounded shadow border w-full max-w-2xl mx-auto"
    >
      <h3 class="text-lg font-semibold text-green-700 mb-2">📌 AI 추천 결과</h3>
      <pre class="text-sm whitespace-pre-wrap">{{ responseData }}</pre>
    </div>
    <!-- 모달 컴포넌트 연결 -->
    <AIRecommendationModal
      :visible="showModal"
      @close="showModal = false"
      @confirm="handleConfirmedRecommendations"
    />

    <!-- <AIRecommendationModal
      :visible="showModal"
      @close="showModal = false"
      @submit="handleAISubmit"
    /> -->

    <!-- 리뷰 모달 -->
    <ReviewModal
      v-if="showReviewModal"
      :aptId="selectedApt?.apt_seq.toString()"
      @close="showReviewModal = false"
      @submitted="moveToApt(selectedApt)"
    />

    <!-- 인증 모달 -->
    <CertifyModal
      v-if="showCertifyModal"
      :aptSeq="selectedApt?.apt_seq.toString()"
      @close="showCertifyModal = false"
    />
  </div>
</template>

<script>
import { fetchRegionList } from '@/utils/regionApi'
import axios from '@/axios/axios'
import AptPriceChart from '@/components/AptPriceChart.vue'
import AIRecommendationModal from '@/components/AIRecommendationModal.vue'
import ReviewModal from '@/components/ReviewModal.vue'
import CertifyModal from '@/components/CertifyModal.vue'
import { useUserStore } from '@/stores/user'
import up1 from '@/assets/up1.png'
import up2 from '@/assets/up2.png'
import up3 from '@/assets/up3.png'
import down1 from '@/assets/down1.png'
import down2 from '@/assets/down2.png'
import down3 from '@/assets/down3.png'

export default {
  name: 'MapPage',
  components: {
    AptPriceChart,
    AIRecommendationModal,
    ReviewModal,
    CertifyModal,
  },
  setup() {
    const userStore = useUserStore()
    return { userStore }
  },
  data() {
    return {
      mapReady: false,
      sido: '',
      gugun: '',
      dong: '',
      aptName: '',
      aptList: [],
      sidoList: [],
      gugunList: [],
      dongList: [],
      map: null,
      markers: [],
      selectedApt: null,
      reviews: [],
      aptDetailInfo: null,

      areaList: [], //평수 리스트
      selectedArea: '', //선택된 평수
      yearlyPrices: [], //연도별 가격
      showModal: false,
      responseData: null,
      showReviewModal: false,
      showCertifyModal: false,

      ranking: [], // 백에서 받아온 랭킹 리스트
      rankingMarkers: [], // 지도 위에 표시된 랭킹 마커들

      yearOptions: Array.from({ length: 2025 - 2011 + 1 }, (_, i) => 2011 + i),
      fromYear: 2024,
      toYear: 2025,

      originalMapLevel: null,
      originalMapCenter: null,

      showRankingPanel: false, // ← 사각형 검색 여부
    }
  },
  computed: {
    topUp() {
      return [...this.ranking].sort((a, b) => b.rateChange - a.rateChange).slice(0, 3)
    },
    topDown() {
      return [...this.ranking].sort((a, b) => a.rateChange - b.rateChange).slice(0, 3)
    },
    selectedAvgPrice() {
      const match = this.areaList.find((item) => item.area === this.selectedArea)
      return match ? match.avgPrice : null
    },
  },
  mounted() {
    this.loadKakaoMapScript()
    this.fetchSido()
  },
  methods: {
    clearMarkers() {
      this.markers.forEach((marker) => marker.setMap(null))
      this.markers = []
    },
    initDrawingManager() {
      const options = {
        map: this.map,
        drawingControl: false,
        drawingControlOptions: {
          position: kakao.maps.ControlPosition.TOP_RIGHT,
          drawingModes: [kakao.maps.drawing.OverlayType.RECTANGLE],
        },
        guideTooltip: ['draw', 'drag', 'edit'],
        rectangleOptions: {
          /* … */
        },
      }

      // (혹시 남아있는 매니저가 있으면 제거)
      if (this.drawingManager) {
        kakao.maps.event.removeListener(
          this.drawingManager,
          'overlaycomplete',
          this.onCompleteDrawing,
        )
        this.drawingManager.setMap(null)
      }

      this.drawingManager = new kakao.maps.drawing.DrawingManager(options)
      kakao.maps.event.addListener(this.drawingManager, 'overlaycomplete', this.onCompleteDrawing)
    },
    async handleConfirmedRecommendations(aptNames) {
      console.log('🧩 받은 추천 아파트 이름들:', aptNames)

      try {
        this.showRankingPanel = false // ✅ AI 추천이므로 Top3 비활성화
        this.clearRankingMarkers() // ✅ 혹시 이전에 그려진 마커가 있다면 제거
        const aptInfoList = []

        for (const name of aptNames) {
          const res = await axios.get('/apt/search', {
            params: { aptName: name },
          })

          // 여러 개일 수 있으므로 배열 합치기
          aptInfoList.push(...res.data)
        }

        // 👇 기존 aptList 완전히 대체 (섞이지 않게)
        this.aptList = aptInfoList

        await this.fetchDealsForAptList(true) // draw = true 설정
        if (this.aptList.length > 0) {
          await this.moveToApt(this.aptList[0])
        }
      } catch (error) {
        console.error('추천 결과 이동 중 오류 발생', error)
        alert('추천 결과를 불러오는 중 오류가 발생했습니다.')
      }
    },

    extractAptNames(responseText) {
      const matches = [...responseText.matchAll(/\*\*(.+?)\*\*/g)]
      return matches.map((match) => match[1].trim())
    },

    handleAISubmit(region) {
      this.showAiModal = false
      console.log('AI 추천 요청 받은 지역:', region)
      // TODO: 여기서 AI 추천 API 호출
      this.responseData = data
      this.$nextTick(() => {
        const el = document.getElementById('ai-result-section')
        if (el) el.scrollIntoView({ behavior: 'smooth' })
      })
    },
    loadKakaoMapScript() {
      if (window.kakao && window.kakao.maps) {
        this.initMap()
        return
      }
      const script = document.createElement('script')
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_APP_KEY}&autoload=false&libraries=drawing`
      // initMap 내부의 this 를 Vue 컴포넌트로 유지
      script.onload = () => kakao.maps.load(() => this.initMap())
      document.head.appendChild(script)
    },
    initMap() {
      console.log('[initMap] this =', this)
      const container = document.getElementById('map')
      this.map = new kakao.maps.Map(container, {
        center: new kakao.maps.LatLng(37.5665, 126.978),
        level: 3,
      })
      this.mapReady = true

      this.initDrawingManager()
      // 변경: getData()로 완성된 모든 도형을 가져와 RECTANGLE만 골라 fetchRanking
    },
    async onCompleteDrawing() {
      // 1) 완성된 도형 정보 가져와서 처리
      const data = this.drawingManager.getData?.()
      const rects = data?.[kakao.maps.drawing.OverlayType.RECTANGLE] || []
      if (!rects.length) {
        console.log('[onCompleteDrawing] 사각형 없음')
      } else {
        // 🔽 여기 넣기
        this.originalMapLevel = this.map.getLevel()
        this.originalMapCenter = this.map.getCenter()
        for (const r of rects) {
          const sw = new kakao.maps.LatLng(r.sPoint.y, r.sPoint.x)
          const ne = new kakao.maps.LatLng(r.ePoint.y, r.ePoint.x)
          console.log('SW:', sw.getLat(), sw.getLng())
          console.log('NE:', ne.getLat(), ne.getLng())
          const params = {
            swLat: sw.getLat(),
            swLng: sw.getLng(),
            neLat: ne.getLat(),
            neLng: ne.getLng(),
          }
          // 2) 랭킹 데이터 fetch (await)
          await this.fetchRanking(params)
          console.log('랭킹 데이터:', this.ranking)
        }
      }
      // 3) RankingDto → aptList 변환
      this.aptList = this.ranking.map((r) => ({
        apt_seq: r.aptSeq,
        apt_nm: r.aptName,
        road_nm: r.address, // 기존 API 에선 road_nm, road_nm_bonbun 따로지만 생략 가능
        road_nm_bonbun: '',
        latitude: r.latitude,
        longitude: r.longitude,
      }))
      console.log('변환된 aptList:', this.aptList)

      // 4) 거래 정보 채워주고, 첫 항목으로 자동 이동
      this.fetchDealsForAptList(false)
      console.log('거래 정보 채워진 aptList:', this.aptList)
      if (this.aptList.length) {
        this.moveToApt(this.aptList[0], true)
        // ✅ 복원
        if (this.originalMapCenter && this.originalMapLevel !== null) {
          this.map.setCenter(this.originalMapCenter)
          this.map.setLevel(this.originalMapLevel)
        }
      }
      // 2) 매니저 클리어 + 리스너 해제
      this.drawingManager.clear() // 그린 도형만 삭제
      kakao.maps.event.removeListener(
        this.drawingManager,
        'overlaycomplete',
        this.onCompleteDrawing,
      )
      this.drawingManager = null

      // 3) 필요하면 다시 초기화
      this.$nextTick(() => {
        this.initDrawingManager()
      })
      this.showRankingPanel = true
    },
    drawMarkers() {
      if (!this.map) return
      this.markers.forEach((marker) => marker.setMap(null))
      this.markers = []
      this.aptList.forEach((apt) => {
        if (!apt.latitude || !apt.longitude) return
        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(parseFloat(apt.latitude), parseFloat(apt.longitude)),
          map: this.map,
          title: apt.apt_nm,
        })
        this.markers.push(marker)
      })
    },
    async fetchYearlyPrices() {
      if (!this.selectedApt || !this.selectedArea) return
      try {
        const res = await axios.get(`/apt/${this.selectedApt.apt_seq}/priceByYear`, {
          params: { area: this.selectedArea },
        })
        this.yearlyPrices = res.data
      } catch (err) {
        console.error('연도별 매매가 조회 실패:', err)
        this.yearlyPrices = []
      }
    },
    async moveToApt(apt, keepView = false) {
      // Top3 또는 검색결과 어떤 형태든 통일
      const normalizedApt = {
        apt_seq: apt.apt_seq || apt.aptSeq,
        apt_nm: apt.apt_nm || apt.aptName,
        road_nm: apt.road_nm || apt.address,
        road_nm_bonbun: apt.road_nm_bonbun || '',
        latitude: apt.latitude,
        longitude: apt.longitude,
      }

      if (!normalizedApt.latitude || !normalizedApt.longitude) return

      const latLng = new kakao.maps.LatLng(
        parseFloat(normalizedApt.latitude),
        parseFloat(normalizedApt.longitude),
      )
      if (!keepView) {
        this.map.setCenter(latLng)
        this.map.setLevel(2)
      }

      this.selectedApt = normalizedApt
      try {
        const res = await axios.get(`/apt/${normalizedApt.apt_seq}/info`)
        this.aptDetailInfo = res.data
      } catch (err) {
        console.error('아파트 정보 조회 실패:', err)
        this.aptDetailInfo = null
      }
      try {
        const res = await axios.get(`/reviews/${normalizedApt.apt_seq}`)
        this.reviews = res.data
      } catch (err) {
        if (err.response && err.response.status === 404) {
          // 리뷰 없음 → 에러 아님
          this.reviews = []
        } else {
          // 진짜 에러만 콘솔 출력
          // console.error('리뷰 불러오기 실패:', err)
          this.reviews = []
        }
      }

      try {
        const res = await axios.get(`/apt/${normalizedApt.apt_seq}/avg-prices`)
        this.areaList = res.data
        if (res.data.length > 0) {
          this.selectedArea = res.data[0].area
          await this.fetchYearlyPrices()
        }
      } catch (err) {
        console.error('평수 목록 불러오기 실패:', err)
        this.areaList = []
      }
    },
    async deleteReview(reviewId) {
      if (!confirm('이 리뷰를 삭제하시겠습니까?')) return
      try {
        await axios.delete(`/reviews/${reviewId}`)
        alert('리뷰가 삭제되었습니다.')
        await this.moveToApt(this.selectedApt)
      } catch (err) {
        console.error('리뷰 삭제 실패:', err)
        alert(err.response?.data || '삭제 권한이 없습니다.')
      }
    },
    formatPrice(priceStr) {
      const raw = parseInt(priceStr.replace(/[^0-9]/g, ''))
      if (isNaN(raw)) return '정보 없음'
      const eok = Math.floor(raw / 10000)
      const cheon = Math.floor((raw % 10000) / 1000)
      if (eok > 0 && cheon > 0) return `${eok}억 ${cheon * 1000}만원`
      if (eok > 0) return `${eok}억`
      return `${raw.toLocaleString()}만원`
    },
    async fetchSido() {
      const data = await fetchRegionList('*00000000')
      this.sidoList = data.regcodes.map((r) => ({ code: r.code, name: r.name }))
    },
    async fetchGugun() {
      if (!this.sido) return
      const pattern = this.sido.substr(0, 2) + '*00000'
      const data = await fetchRegionList(pattern)
      this.gugunList = data.regcodes.map((r) => {
        const nameParts = r.name.split(' ')
        return { code: r.code, name: nameParts[1] }
      })
    },
    async fetchDong() {
      if (!this.gugun) return
      const pattern = this.gugun.substr(0, 5) + '*'
      const data = await fetchRegionList(pattern)
      this.dongList = data.regcodes.map((r) => {
        const nameParts = r.name.split(' ')
        return { code: r.code, name: nameParts[2] || nameParts[3] }
      })
    },
    async searchByRegion() {
      this.showRankingPanel = false
      this.clearRankingMarkers() // ✅ 추가
      if (!this.dong) return
      try {
        const response = await fetch(`/api/apt/search?dongCode=${this.dong}`)
        const data = await response.json()
        this.aptList = data
        await this.fetchDealsForAptList(true)
        if (this.aptList.length > 0) this.moveToApt(this.aptList[0])
      } catch (error) {
        console.error('지역 검색 실패:', error)
      }
    },
    async searchByAptName() {
      this.showRankingPanel = false
      this.clearRankingMarkers() // ✅ 추가
      if (!this.aptName.trim()) return
      try {
        const response = await fetch(`/api/apt/search?aptName=${this.aptName}`)
        const data = await response.json()
        this.aptList = data
        await this.fetchDealsForAptList(true)
        if (this.aptList.length > 0) this.moveToApt(this.aptList[0])
      } catch (error) {
        console.error('아파트명 검색 실패:', error)
      }
    },
    async fetchDealsForAptList(draw = false) {
      console.log('fetchDealsForAptList 시작, aptList:', this.aptList)
      const updatedList = await Promise.all(
        this.aptList.map(async (apt) => {
          try {
            const res = await fetch(`/api/apt/${apt.apt_seq}/deals`)
            const deals = await res.json()
            console.log(`  ← ${apt.apt_seq} 거래 조회 완료, count:`, deals.length)
            return { ...apt, dealList: deals }
          } catch (e) {
            console.error(`거래 정보 실패: ${apt.apt_nm}`, e)
            return { ...apt, dealList: [] }
          }
        }),
      )
      this.aptList = updatedList
      console.log('fetchDealsForAptList 완료, updated aptList:', this.aptList)
      // ✅ draw false면 마커 생략
      if (!draw) return

      if (this.mapReady || this.ranking.length <= 0) {
        this.drawMarkers()
      } else {
        const check = setInterval(() => {
          if (this.mapReady || this.ranking.length <= 0) {
            this.drawMarkers()
            clearInterval(check)
          }
        }, 100)
      }
    },
    async checkCertification() {
      // 1) 로그인 체크
      if (!this.userStore.user) {
        alert('로그인이 필요합니다.')
        return
      }

      // 2) 인증 상태 API 호출
      try {
        const res = await axios.get('/certifications/status', {
          params: { aptSeq: this.selectedApt.apt_seq },
          withCredentials: true,
        })
        const { approval } = res.data

        // 3) 승인 상태일 때만 모달 열기
        if (approval === 'APPROVED') {
          this.showReviewModal = true
        } else {
          alert('리뷰를 작성하려면 실거주 인증이 필요합니다. 인증 페이지로 이동합니다.')
          this.showCertifyModal = true
        }
      } catch (err) {
        // 404 (요청 없음) 등 모든 실패 케이스
        alert('실거주 인증이 필요합니다. 인증 페이지로 이동합니다.')
        this.showCertifyModal = true
      }
    },
    selectOverlay(type) {
      this.clearMarkers()
      this.clearRankingMarkers()
      // 사각형 모드 진입
      this.drawingManager.cancel()
      this.drawingManager.select(kakao.maps.drawing.OverlayType[type])
    },
    /** 랭킹 API 호출 */
    /** 사각형 경계(swLat,swLng,neLat,neLng) 기반 랭킹 API 호출 */
    async fetchRanking({ swLat, swLng, neLat, neLng }) {
      try {
        // 유저가 선택한 연도(예시: data()에 바인딩된 fromYear/toYear)
        const fromYear = this.fromYear
        const toYear = this.toYear

        const res = await axios.get('/apt/ranking', {
          params: {
            swLat,
            swLng,
            neLat,
            neLng,
            fromYear,
            toYear,
          },
          withCredentials: true,
        })
        this.ranking = res.data
        this.drawRankingMarkers()
      } catch (err) {
        console.error('랭킹 조회 실패', err)
      }
    },

    /** 기존 매물 마커 지우기 */
    clearRankingMarkers() {
      this.rankingMarkers.forEach((m) => m.setMap(null))
      this.rankingMarkers = []
    },

    /** 랭킹 리스트 기반으로 마커를 찍고, InfoWindow로 순위·증감률 표시 */
    drawRankingMarkers() {
      this.clearRankingMarkers()
      if (!this.ranking.length) return

      const topUp = [...this.ranking].sort((a, b) => b.rateChange - a.rateChange).slice(0, 3)
      const topDown = [...this.ranking].sort((a, b) => a.rateChange - b.rateChange).slice(0, 3)

      console.log(
        '상위 3개:',
        topUp.map((x) => x.aptSeq),
      )
      console.log(
        '하위 3개:',
        topDown.map((x) => x.aptSeq),
      )

      const upIcons = [up1, up2, up3]
      const downIcons = [down1, down2, down3]

      this.ranking.forEach((item) => {
        const lat = parseFloat(item.latitude)
        const lng = parseFloat(item.longitude)
        if (!lat || !lng) return

        // 실제 필드 이름으로 비교!
        const upIdx = topUp.findIndex((x) => String(x.aptSeq) === String(item.aptSeq))
        const dnIdx =
          upIdx === -1 ? topDown.findIndex((x) => String(x.aptSeq) === String(item.aptSeq)) : -1

        const markerOptions = {
          position: new kakao.maps.LatLng(lat, lng),
          map: this.map,
        }

        if (upIdx !== -1) {
          markerOptions.image = new kakao.maps.MarkerImage(
            upIcons[upIdx],
            new kakao.maps.Size(48, 48),
            { offset: new kakao.maps.Point(24, 48) },
          )
        } else if (dnIdx !== -1) {
          markerOptions.image = new kakao.maps.MarkerImage(
            downIcons[dnIdx],
            new kakao.maps.Size(48, 48),
            { offset: new kakao.maps.Point(24, 48) },
          )
        }
        // else: 기본 마커

        const marker = new kakao.maps.Marker(markerOptions)

        const iw = new kakao.maps.InfoWindow({
          content: `
        <div style="padding:6px;font-size:12px;">
          <strong>${item.aptName}</strong><br/>
          증감률: ${item.rateChange.toFixed(2)}%
        </div>`,
        })
        kakao.maps.event.addListener(marker, 'click', () => iw.open(this.map, marker))
        this.rankingMarkers.push(marker)
      })
    },
  },
}
</script>

<style scoped></style>
