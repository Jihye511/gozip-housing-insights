<template>
  <div class="h-screen bg-gray-100 flex">
    <!-- Sidebar -->
    <aside class="w-64 bg-white border-r p-4 flex flex-col">
      <router-link to="/" class="flex items-center space-x-2 hover:opacity-80">
        <img src="@/assets/gozip_logo.png" alt="로고" class="h-6 w-auto" />
        <h2 class="text-xl font-bold text-green-600">검색</h2>
      </router-link>

      <!-- 지역 검색 -->
      <div>
        <label class="block text-sm font-medium mb-1">지역으로 검색</label>
        <select class="w-full border p-2 rounded mb-2" v-model="sido" @change="fetchGugun">
          <option value="">시 선택</option>
          <option v-for="option in sidoList" :key="option.code" :value="option.code">
            {{ option.name }}
          </option>
        </select>
        <select class="w-full border p-2 rounded mb-2" v-model="gugun" @change="fetchDong">
          <option value="">구 선택</option>
          <option v-for="option in gugunList" :key="option.code" :value="option.code">
            {{ option.name }}
          </option>
        </select>
        <select class="w-full border p-2 rounded mb-2" v-model="dong">
          <option value="">동 선택</option>
          <option v-for="option in dongList" :key="option.code" :value="option.code">
            {{ option.name }}
          </option>
        </select>
        <button class="w-full bg-green-600 text-white p-2 rounded" @click="searchByRegion">
          검색
        </button>
      </div>

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
      <div class="overflow-y-auto mt-4" style="max-height: calc(100vh - 300px)">
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
    </aside>

    <!-- 상세 정보 패널 -->
    <div class="w-[360px] bg-white p-4 border-r" v-if="aptDetailInfo">
      <h2 class="text-xl font-bold mb-2">{{ aptDetailInfo.aptName }}</h2>
      <p class="text-gray-600 text-sm mb-4">{{ aptDetailInfo.address }}</p>
      <div class="space-y-2">
        <div>
          <h4 class="font-semibold text-sm text-gray-700">가격 및 정보</h4>
          <p class="text-sm">
            <span v-if="selectedApt.dealList?.length"
              >{{ formatPrice(selectedApt.dealList[0].deal_amount) }} •</span
            >
            {{ selectedApt.area || '84m²' }} • 아파트
          </p>
        </div>

        <div v-if="areaList.length">
          <label class="text-sm font-semibold">평수 선택</label>
          <select
            v-model="selectedArea"
            @change="fetchYearlyPrices"
            class="w-full p-2 border rounded mt-1"
          >
            <option v-for="item in areaList" :key="item.area" :value="item.area">
              {{ item.area }}㎡
            </option>
          </select>
        </div>

        <div v-if="selectedAvgPrice">
          <p class="text-sm mt-2 text-gray-700">
            선택한 평수의 평균 매매가:
            <span class="font-semibold text-green-600">{{
              formatPrice(selectedAvgPrice.toString())
            }}</span>
          </p>
        </div>

        <div v-if="yearlyPrices.length">
          <h4 class="font-semibold text-sm text-gray-700 mt-3">년도별 평균 매매가</h4>
          <ul class="text-sm text-gray-700 space-y-1">
            <li v-for="(item, idx) in yearlyPrices" :key="idx">
              {{ item.year }}년 - {{ formatPrice(item.avgPrice.toString()) }}
            </li>
          </ul>
        </div>

        <div>
          <h4 class="font-semibold text-sm text-gray-700">시세 그래프</h4>
          <div class="bg-gray-100 rounded p-2">
            <AptPriceChart
              v-if="yearlyPrices.length"
              :key="selectedApt?.apt_seq + selectedArea"
              :yearlyPrices="yearlyPrices"
            />
            <p v-else class="text-gray-400 text-center py-6">시세 데이터가 없습니다.</p>
          </div>
        </div>

        <div>
          <h4 class="font-semibold text-sm text-gray-700">거주자 리뷰</h4>
          <button
            @click="checkCertification()"
            class="text-sm text-white bg-green-600 px-3 py-1 rounded mb-2"
          >
            리뷰 작성
          </button>
          <ul class="text-sm text-gray-700 space-y-1">
            <li
              v-for="review in reviews"
              :key="review.review_id"
              class="flex flex-col gap-2 border-b pb-2"
            >
              <p>"{{ review.content }}" — {{ review.userName }} | {{ review.score }}점</p>
              <img
                v-if="review.image_file"
                :src="review.image_file"
                alt="리뷰 이미지"
                class="w-32 h-32 object-cover rounded"
              />
              <button
                v-if="review.user_id === userStore.userId"
                @click="deleteReview(review.review_id)"
                class="self-end text-xs text-red-500 hover:underline"
              >
                삭제
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 지도 -->
    <div class="flex-1 bg-gray-50 relative">
      <div id="map" class="absolute inset-0 z-0"></div>
      <div class="absolute top-4 right-4 z-50 space-x-2">
        <button @click="selectOverlay('RECTANGLE')" class="px-2 py-1 bg-white rounded">
          사각형
        </button>
        <!-- 새로 추가하는 완료 버튼 -->
        <button @click="onCompleteDrawing" class="px-2 py-1 bg-green-600 text-white rounded">
          완료
        </button>
      </div>
    </div>

    <!--  오른쪽 하단 고정 버튼 -->
    <div>
      <button
        class="fixed bottom-20 right-10 bg-green-600 text-white text-3xl px-20 py-20 rounded-full shadow-lg hover:bg-green-700"
        @click="showModal = true"
      >
        AI 추천!
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
import rankPins from '@/assets/rankpins.png'

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
    }
  },
  computed: {
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
        const aptInfoList = []

        for (const name of aptNames) {
          const res = await axios.get('/apt/search', {
            params: { aptName: name },
          })

          // 여러 개일 수 있으므로 배열 합치기
          aptInfoList.push(...res.data)
        }

        this.aptList = aptInfoList

        await this.fetchDealsForAptList()
        if (this.aptList.length > 0) {
          this.moveToApt(this.aptList[0])
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

    async fetchDealsForAptList() {
      const updatedList = await Promise.all(
        this.aptList.map(async (apt) => {
          try {
            const res = await fetch(`/api/apt/${apt.apt_seq}/deals`)
            const deals = await res.json()
            return {
              ...apt,
              dealList: deals,
            }
          } catch (e) {
            console.error(`거래 정보 불러오기 실패: ${apt.apt_nm}`, e)
            return {
              ...apt,
              dealList: [],
            }
          }
        }),
      )
      this.aptList = updatedList
      if (this.mapReady) this.drawMarkers()
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
    onCompleteDrawing() {
      // 1) 완성된 도형 정보 가져와서 처리
      const data = this.drawingManager.getData?.()
      const rects = data?.[kakao.maps.drawing.OverlayType.RECTANGLE] || []
      if (rects.length > 0) {
        rects.forEach((r) => {
          const sw = new kakao.maps.LatLng(r.sPoint.y, r.sPoint.x)
          const ne = new kakao.maps.LatLng(r.ePoint.y, r.ePoint.x)
          const params = {
            swLat: sw.getLat(),
            swLng: sw.getLng(),
            neLat: ne.getLat(),
            neLng: ne.getLng(),
          }
          this.fetchRanking(params)
        })
      } else {
        console.log('[onCompleteDrawing] 사각형 없음')
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
    async moveToApt(apt) {
      if (!apt.latitude || !apt.longitude) return
      const latLng = new kakao.maps.LatLng(parseFloat(apt.latitude), parseFloat(apt.longitude))
      this.map.setCenter(latLng)
      this.map.setLevel(2)
      this.selectedApt = apt
      try {
        const res = await axios.get(`/apt/${apt.apt_seq}/info`)
        this.aptDetailInfo = res.data
      } catch (err) {
        console.error('아파트 정보 조회 실패:', err)
        this.aptDetailInfo = null
      }
      try {
        const res = await axios.get(`/reviews/${apt.apt_seq}`)
        this.reviews = res.data
      } catch (err) {
        console.error('리뷰 불러오기 실패:', err)
        this.reviews = []
      }
      try {
        const res = await axios.get(`/apt/${apt.apt_seq}/avg-prices`)
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
      if (!this.dong) return
      try {
        const response = await fetch(`/api/apt/search?dongCode=${this.dong}`)
        const data = await response.json()
        this.aptList = data
        await this.fetchDealsForAptList()
        if (this.aptList.length > 0) this.moveToApt(this.aptList[0])
      } catch (error) {
        console.error('지역 검색 실패:', error)
      }
    },
    async searchByAptName() {
      if (!this.aptName.trim()) return
      try {
        const response = await fetch(`/api/apt/search?aptName=${this.aptName}`)
        const data = await response.json()
        this.aptList = data
        await this.fetchDealsForAptList()
        if (this.aptList.length > 0) this.moveToApt(this.aptList[0])
      } catch (error) {
        console.error('아파트명 검색 실패:', error)
      }
    },
    async fetchDealsForAptList() {
      const updatedList = await Promise.all(
        this.aptList.map(async (apt) => {
          try {
            const res = await fetch(`/api/apt/${apt.apt_seq}/deals`)
            const deals = await res.json()
            return { ...apt, dealList: deals }
          } catch (e) {
            console.error(`거래 정보 실패: ${apt.apt_nm}`, e)
            return { ...apt, dealList: [] }
          }
        }),
      )
      this.aptList = updatedList
      if (this.mapReady) {
        this.drawMarkers()
      } else {
        const check = setInterval(() => {
          if (this.mapReady) {
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
      // 사각형 모드 진입
      this.drawingManager.cancel()
      this.drawingManager.select(kakao.maps.drawing.OverlayType[type])
    },
    /** 랭킹 API 호출 */
    /** 사각형 경계(swLat,swLng,neLat,neLng) 기반 랭킹 API 호출 */
    async fetchRanking({ swLat, swLng, neLat, neLng }) {
      try {
        const res = await axios.get('/apt/ranking', {
          params: { swLat, swLng, neLat, neLng },
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

      const SPRITE_URL = rankPins
      const ICON_SIZE = new kakao.maps.Size(64, 64)
      const SPRITE_SIZE = new kakao.maps.Size(192, 128)

      this.ranking.forEach((item) => {
        const lat = parseFloat(item.latitude)
        const lng = parseFloat(item.longitude)
        if (!lat || !lng) return

        // 기본 origin
        let spriteOrigin = new kakao.maps.Point(0, 0)
        let labelHtml = `#`

        const upIdx = topUp.findIndex((x) => x.apt_seq === item.apt_seq)
        if (upIdx !== -1) {
          spriteOrigin = new kakao.maps.Point(64 * upIdx, 0)
          labelHtml = `▲${upIdx + 1}`
        }

        const dnIdx = topDown.findIndex((x) => x.apt_seq === item.apt_seq)
        if (dnIdx !== -1) {
          spriteOrigin = new kakao.maps.Point(64 * dnIdx, 64)
          labelHtml = `▼${dnIdx + 1}`
        }

        const markerImage = new kakao.maps.MarkerImage(SPRITE_URL, ICON_SIZE, {
          spriteSize: SPRITE_SIZE,
          spriteOrigin,
          offset: new kakao.maps.Point(ICON_SIZE.width/2, ICON_SIZE.height),
        })

        // 레이블 옵션을 생성자에 포함
        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(lat, lng),
          map: this.map,
          image: markerImage,
          label: {
            content: `<div style="font-weight:bold; color:#333">${labelHtml}</div>`,
            anchor: new kakao.maps.Point(12, 44),
          },
        })

        const iw = new kakao.maps.InfoWindow({
          content: `
        <div style="padding:6px;font-size:12px;">
          <strong>${item.aptName}</strong><br/>
          증감률: ${item.rateChange}%
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
