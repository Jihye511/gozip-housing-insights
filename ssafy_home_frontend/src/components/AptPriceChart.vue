<template>
  <Line :data="chartData" :options="chartOptions" />
</template>

<script setup>
import { ref, watch } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Filler
} from 'chart.js'

// Chart.js 모듈 등록
ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale, Filler)

// Props 정의
const props = defineProps({
  yearlyPrices: {
    type: Array,
    required: true
  }
})

// chartData를 반응형으로 정의
const chartData = ref({
  labels: [],
  datasets: []
})

// options은 그냥 객체
const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false },
    tooltip: {
      callbacks: {
        title: (tooltipItems) => {
          const year = tooltipItems[0].label
          return `${year}년`
        },
        label: (tooltipItem) => {
          const value = tooltipItem.raw
          const eok = Math.floor(value / 10000)
          const man = Math.floor((value % 10000) / 1000)
          return eok > 0
            ? `${eok}억${man > 0 ? ` ${man * 1000}만원` : ''}`
            : `${value.toLocaleString()}만원`
        }
      }
    }
  },
  scales: {
    y: {
      ticks: {
        callback: (value) => `${value.toLocaleString()}`
      }
    }
  }
}


// yearlyPrices가 바뀔 때마다 chartData 갱신
watch(
  () => props.yearlyPrices,
  (newVal) => {
    const sorted = [...newVal].sort((a, b) => a.year - b.year)

    chartData.value = {
      labels: sorted.map(item => `${item.year}`),
      datasets: [
        {
          label: '년도별 평균 매매가',
          data: sorted.map(item => item.avgPrice),
          borderColor: '#10B981',
          backgroundColor: 'rgba(16,185,129,0.2)',
          tension: 0.4,
          fill: true,
          pointRadius: 0
        }
      ]
    }
  },
  { immediate: true }
)
</script>
