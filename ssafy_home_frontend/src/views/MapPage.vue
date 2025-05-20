<template>
  <div class="min-h-screen bg-gray-100 flex">
    <!-- Sidebar -->
    <aside class="w-64 bg-white border-r p-4 space-y-4">
      <router-link to="/" class="flex items-center space-x-2 hover:opacity-80">
        <img src="@/assets/gozip_logo.png" alt="로고" class="h-6 w-auto" />
        <h2 class="text-xl font-bold text-green-600">검색</h2>
      </router-link>
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
      <div>
        <h3 class="font-bold text-gray-700 mt-4 mb-2">검색 결과</h3>
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
              <span v-if="apt.dealList && apt.dealList.length">
                {{ formatPrice(apt.dealList[0].deal_amount) }}
              </span>
              <span v-else>정보 없음</span>
            </p>
          </li>
        </ul>
      </div>
    </aside>

    <!-- Info Panel -->
    <div class="w-[360px] bg-white p-4 border-r" v-if="selectedApt">
      <h2 class="text-xl font-bold mb-2">{{ selectedApt.apt_nm }}</h2>
      <p class="text-gray-600 text-sm mb-4">
        {{ selectedApt.road_nm }} {{ selectedApt.road_nm_bonbun }}
      </p>

      <div class="space-y-2">
        <div>
          <h4 class="font-semibold text-sm text-gray-700">가격 및 정보</h4>
          <p class="text-sm">
            <span v-if="selectedApt.dealList?.length">
              {{ formatPrice(selectedApt.dealList[0].deal_amount) }} •
            </span>
            {{ selectedApt.area || '84m²' }} • 아파트
          </p>
        </div>

        <div>
          <h4 class="font-semibold text-sm text-gray-700">시세 그래프</h4>
          <div class="h-24 bg-gray-100 rounded flex items-center justify-center text-gray-400">
            시세 그래프 영역
          </div>
        </div>

        <div>
          <h4 class="font-semibold text-sm text-gray-700">거주자 리뷰</h4>
          <ul class="text-sm text-gray-700 space-y-1">
            <li v-if="reviews.length === 0" class="text-gray-400">리뷰가 없습니다.</li>
            <li v-for="(review, index) in reviews" :key="index">
              "{{ review.content }}" — {{ review.writer }} | {{ review.date }}
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Map 영역 -->
    <div class="flex-1 bg-gray-50 relative">
      <div id="map" class="absolute inset-0 z-0"></div>
    </div>
  </div>
</template>

<script>
import { fetchRegionList } from '@/utils/regionApi'
import axios from '@/axios/axios' // ✅ src/axios/axios.js 기준의 절대 경로

export default {
  name: 'MapPage',
  data() {
    return {
      // 기존 상태 유지
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
      // ✅ 새로 추가
      selectedApt: null,
      reviews: [],
    }
  },

  mounted() {
    this.loadKakaoMapScript()
    this.fetchSido()
  },
  methods: {
    loadKakaoMapScript() {
      if (window.kakao && window.kakao.maps) {
        this.initMap()
        return
      }
      const script = document.createElement('script')
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_APP_KEY}&autoload=false`
      script.onload = () => kakao.maps.load(this.initMap)
      document.head.appendChild(script)
    },
    initMap() {
      const container = document.getElementById('map')
      this.map = new kakao.maps.Map(container, {
        center: new kakao.maps.LatLng(37.5665, 126.978),
        level: 3,
      })
      this.mapReady = true
      // this.drawMarkers();
    },
    drawMarkers() {
      if (!this.map) {
        console.warn('지도 객체가 아직 초기화되지 않았습니다.')
        return
      }
      this.markers.forEach((marker) => marker.setMap(null))
      this.markers = []

      this.aptList.forEach((apt) => {
        if (!apt.latitude || !apt.latitude) {
          console.warn('위치 정보 누락:', apt)
          return
        }
        console.log(typeof apt.latitude, apt.latitude)

        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(parseFloat(apt.latitude), parseFloat(apt.longitude)),

          map: this.map,
          title: apt.apt_nm,
        })
        this.markers.push(marker)
      })
    },

    async moveToApt(apt) {
      if (!apt.latitude || !apt.longitude) return

      // 지도 이동
      const latLng = new kakao.maps.LatLng(parseFloat(apt.latitude), parseFloat(apt.longitude))
      this.map.setCenter(latLng)
      this.map.setLevel(2)

      // ✅ 아파트 및 리뷰 설정
      this.selectedApt = apt

      try {
        const res = await axios.get(`/reviews/${apt.apt_seq}`)
        this.reviews = res.data
        console.log(res.data.apt_id)
      } catch (error) {
        console.error('리뷰 불러오기 실패:', error)
        this.reviews = []
      }
    },

    formatPrice(priceStr) {
      const raw = parseInt(priceStr.replace(/[^0-9]/g, '')) // "17500" → 17500 (만원 단위)

      if (isNaN(raw)) return '정보 없음'

      const eok = Math.floor(raw / 10000) // 억 단위
      const cheon = Math.floor((raw % 10000) / 1000) // 천 단위

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
        if (this.aptList.length > 0) {
          this.moveToApt(this.aptList[0])
        }
        // this.drawMarkers();
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
        // this.drawMarkers();
        if (this.aptList.length > 0) {
          this.moveToApt(this.aptList[0])
        }
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
      // this.drawMarkers();

      // ✅ map이 준비된 후에만 마커를 그림
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
  },
}
</script>

<style scoped></style>
