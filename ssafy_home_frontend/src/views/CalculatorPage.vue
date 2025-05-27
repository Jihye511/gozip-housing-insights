<template>
  <div class="min-h-screen bg-gray-100">
    <main class="max-w-3xl mx-auto py-10">
      <h2 class="text-2xl font-bold mb-6 text-center">부동산 계산기</h2>
      <div class="flex justify-center mb-6">
        <div class="bg-white shadow rounded-lg w-full p-6">
          <div class="flex justify-center mb-4 space-x-2 text-sm">
            <button @click="currentTab = 'commission'" :class="tabClass('commission')">중개보수 계산기</button>
            <button @click="currentTab = 'loan'" :class="tabClass('loan')">대출이자 계산기</button>
            <button @click="currentTab = 'basic'" :class="tabClass('basic')">일반 계산기</button>
          </div>

          <!-- 중개보수 계산기 -->
          <div v-if="currentTab === 'commission'">
            <h3 class="text-lg font-bold mb-4">중개보수 계산기</h3>

            <!-- 거래 유형, 거래 대상, 거래 지역 -->
            <div class="grid grid-cols-3 gap-4 mb-4">
              <div>
                <p class="text-sm font-medium mb-2">거래 유형</p>
                <div class="space-x-2">
                  <button @click="dealType='매매'" :class="dealType==='매매'?activeBtn:inactiveBtn">매매</button>
                  <button @click="dealType='전세'" :class="dealType==='전세'?activeBtn:inactiveBtn">전세</button>
                  <button @click="dealType='월세'" :class="dealType==='월세'?activeBtn:inactiveBtn">월세</button>
                </div>
              </div>

              <div>
                <p class="text-sm font-medium mb-2">거래 대상</p>
                <div class="space-x-2">
                  <button @click="propertyType='주택'" :class="propertyType==='주택'?activeBtn:inactiveBtn">주택</button>
                  <button @click="propertyType='오피스텔'" :class="propertyType==='오피스텔'?activeBtn:inactiveBtn">오피스텔</button>
                  <button @click="propertyType='기타'" :class="propertyType==='기타'?activeBtn:inactiveBtn">기타</button>
                </div>
              </div>

              <div>
                <label class="text-sm font-medium mb-1 block">거래 지역</label>
                <select v-model="region" class="w-full border p-2 rounded">
                  <option value="">선택</option>
                  <option v-for="r in regions" :key="r" :value="r">{{ r }}</option>
                </select>
              </div>
            </div>

            <!-- 보증금 입력 -->
            <div class="mb-4">
              <label class="text-sm font-medium block mb-1">보증금 (₩)</label>
              <div class="flex items-center space-x-2">
                <input v-model.number="deposit" type="number" class="border p-2 rounded w-full" placeholder="예: 2000000" />
                <span>원</span>
              </div>
              <div class="mt-2 space-x-1 text-xs">
                <button @click="addDeposit(100000000)" class="px-2 py-1 border rounded">+억</button>
                <button @click="addDeposit(10000000)" class="px-2 py-1 border rounded">+천만</button>
                <button @click="addDeposit(1000000)" class="px-2 py-1 border rounded">+백만</button>
                <button @click="addDeposit(100000)" class="px-2 py-1 border rounded">+십만</button>
                <button @click="addDeposit(10000)" class="px-2 py-1 border rounded">+만</button>
                <button @click="deposit=0" class="px-2 py-1 border rounded text-red-600">C</button>
              </div>
            </div>

            <!-- 월세 입력 (월세 거래 시) -->
            <div v-if="dealType==='월세'" class="mb-4">
              <label class="text-sm font-medium block mb-1">월세 (₩)</label>
              <div class="flex items-center space-x-2">
                <input v-model.number="rent" type="number" class="border p-2 rounded w-full" placeholder="예: 300000" />
                <span>원</span>
              </div>
              <div class="mt-2 space-x-1 text-xs">
                <button @click="addRent(1000000)" class="px-2 py-1 border rounded">+백만</button>
                <button @click="addRent(100000)" class="px-2 py-1 border rounded">+십만</button>
                <button @click="addRent(10000)" class="px-2 py-1 border rounded">+만</button>
                <button @click="rent=0" class="px-2 py-1 border rounded text-red-600">C</button>
              </div>
            </div>

            <!-- 계산 버튼 -->
            <button @click="calculateCommission" class="w-full bg-green-600 text-white py-2 rounded">수수료 계산하기</button>

            <!-- 결과 표시 -->
            <div v-if="resultCalculated" class="mt-6 bg-gray-50 p-4 rounded">
              <p class="text-sm">거래금액: {{ formattedTransactionAmount }}원</p>
              <p class="text-sm">상한요율: {{ (commissionRate * 100).toFixed(2) }}%</p>
              <p class="text-sm">중개 수수료: {{ formattedCommission }}원</p>
              <p class="text-sm">부가세 (10%): {{ formattedTax }}원</p>
              <p class="font-semibold mt-2">총 수수료: {{ formattedTotal }}원</p>
            </div>
          </div>

<!-- 대출이자 계산기 -->
          <div v-if="currentTab === 'loan'">
            <h3 class="text-lg font-bold mb-4">대출이자 계산기</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

              <div>
                <label class="text-sm font-medium block mb-1">대출 금액 (₩)</label>
                <input v-model.number="loanAmount" type="number" placeholder="예: 100000000" class="border p-2 rounded w-full" />
              </div>

              <div>
                <label class="text-sm font-medium block mb-1">연 이자율 (%)</label>
                <input v-model.number="loanRate" type="number" step="0.01" placeholder="예: 3.5" class="border p-2 rounded w-full" />
              </div>

              <div>
                <label class="text-sm font-medium block mb-1">대출 기간 (년)</label>
                <input v-model.number="loanTerm" type="number" placeholder="예: 20" class="border p-2 rounded w-full" />
              </div>

              <div>
                <label class="text-sm font-medium block mb-1">상환 방식</label>
                <select v-model="repaymentType" class="border p-2 rounded w-full">
                  <option value="equal">원리금균등상환</option>
                  <option value="principal">원금균등상환</option>
                  <option value="maturity">만기일시상환</option>
                </select>
              </div>
            </div>

            <button @click="calcLoan" class="w-full bg-green-600 text-white py-2 rounded">계산하기</button>
            <p class="mt-4 text-blue-600 font-bold text-center">월 상환금액: {{ monthlyPayment.toLocaleString() }}원</p>

            <p class="text-sm text-center mt-2 text-gray-700" v-if="loanAmount && loanRate && loanTerm">
              계산식:
              <span v-if="repaymentType === 'equal'">
                {{ loanAmount.toLocaleString() }} × 월이자율 ÷ [1 - (1 + 월이자율)<sup>-{{ loanTerm * 12 }}</sup>]
              </span>
              <span v-else-if="repaymentType === 'principal'">
                월 원금 + 최초 이자 = {{ (loanAmount / (loanTerm * 12)).toLocaleString() }} + {{ (loanAmount * (loanRate / 100 / 12)).toLocaleString() }}
              </span>
              <span v-else>
                월 이자 = {{ loanAmount.toLocaleString() }} × {{ (loanRate / 100 / 12).toFixed(5) }}
              </span>
            </p>

            <p class="text-xs text-gray-500 text-center mt-2">* 선택된 상환방식 기준</p>
          </div>

          <!-- 일반 계산기 -->
          <div v-if="currentTab === 'basic'">
            <h3 class="text-lg font-bold mb-4">일반 계산기</h3>
            <div class="grid grid-cols-4 gap-2 w-64 mx-auto">
              <button @click="append('7')" class="btn">7</button>
              <button @click="append('8')" class="btn">8</button>
              <button @click="append('9')" class="btn">9</button>
              <button @click="append('/')" class="btn">/</button>
              <button @click="append('4')" class="btn">4</button>
              <button @click="append('5')" class="btn">5</button>
              <button @click="append('6')" class="btn">6</button>
              <button @click="append('*')" class="btn">×</button>
              <button @click="append('1')" class="btn">1</button>
              <button @click="append('2')" class="btn">2</button>
              <button @click="append('3')" class="btn">3</button>
              <button @click="append('-')" class="btn">-</button>
              <button @click="append('0')" class="btn">0</button>
              <button @click="append('.')" class="btn">.</button>
              <button @click="calculate" class="btn">=</button>
              <button @click="append('+')" class="btn">+</button>
              <button @click="clear" class="col-span-4 bg-red-500 text-white rounded py-2 mt-2">C</button>
            </div>
            <p class="text-center text-xl mt-4 font-bold">{{ calcInput || 0 }}</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'commission',
      // 중개보수
      dealType: '매매',
      propertyType: '주택',
      region: '',
      regions: ['서울', '부산', '대전', '대구', '인천', '광주', '울산', '세종', '경기도', '강원'],
      deposit: 0,
      rent: 0,
      commissionRate: 0,
      commissionLimit: 0,
      commission: 0,
      tax: 0,
      total: 0,
      resultCalculated: false,
      activeBtn: 'px-3 py-1 bg-green-600 text-white rounded',
      inactiveBtn: 'px-3 py-1 border rounded text-gray-600 hover:bg-gray-100',
      // 대출
      loanAmount: 0,
      loanRate: 0,
      loanTerm: 0,
      repaymentType: 'equal',
      monthlyPayment: 0,
      // 일반 계산기
      calcInput: ''
    };
  },
  computed: {
    formattedTransactionAmount() {
      const amt = this.dealType === '월세'
        ? this.deposit + this.rent * 70
        : this.deposit;
      return amt.toLocaleString();
    },
    formattedCommission() {
      return this.commission.toLocaleString();
    },
    formattedTax() {
      return this.tax.toLocaleString();
    },
    formattedTotal() {
      return this.total.toLocaleString();
    }
  },
  methods: {
    tabClass(tab) {
      return `px-4 py-1 rounded ${this.currentTab === tab ? 'bg-gray-200 font-semibold' : 'text-gray-600 hover:underline'}`;
    },
    // 중개보수 계산
    addDeposit(amount) {
      this.deposit += amount;
    },
    addRent(amount) {
      this.rent += amount;
    },
    calculateCommission() {
      const amt = this.dealType === '월세'
        ? this.deposit + this.rent * 70
        : this.deposit;
      let table;
      if (this.dealType === '매매') {
        table = [
          { max: 50000000, rate: 0.006, limit: 250000 },
          { max: 200000000, rate: 0.005, limit: 800000 },
          { max: 900000000, rate: 0.004, limit: Infinity },
          { max: 1200000000, rate: 0.005, limit: Infinity },
          { max: 1500000000, rate: 0.006, limit: Infinity },
          { max: Infinity, rate: 0.007, limit: Infinity }
        ];
      } else if (this.dealType === '전세') {
        table = [
          { max: 50000000, rate: 0.005, limit: 200000 },
          { max: 100000000, rate: 0.004, limit: 300000 },
          { max: 600000000, rate: 0.003, limit: Infinity },
          { max: 1200000000, rate: 0.004, limit: Infinity },
          { max: 1500000000, rate: 0.005, limit: Infinity },
          { max: Infinity, rate: 0.006, limit: Infinity }
        ];
      } else {
        table = [
          { max: 50000000, rate: 0.005, limit: 200000 },
          { max: 100000000, rate: 0.004, limit: 300000 },
          { max: 600000000, rate: 0.003, limit: Infinity },
          { max: 1200000000, rate: 0.004, limit: Infinity },
          { max: 1500000000, rate: 0.005, limit: Infinity },
          { max: Infinity, rate: 0.006, limit: Infinity }
        ];
      }
      const tier = table.find(t => amt <= t.max);
      this.commissionRate = tier.rate;
      this.commissionLimit = tier.limit;
      this.commission = Math.min(Math.round(amt * tier.rate), tier.limit);
      this.tax = Math.round(this.commission * 0.1);
      this.total = this.commission + this.tax;
      this.resultCalculated = true;
    },
    // 대출이자 계산 (기존)
    calcLoan() {
      const r = this.loanRate / 100 / 12;
      const n = this.loanTerm * 12;
      if (this.repaymentType === 'equal') {
        if (r > 0) {
          this.monthlyPayment = Math.round(
            (this.loanAmount * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)
          );
        } else {
          this.monthlyPayment = Math.round(this.loanAmount / n);
        }
      } else if (this.repaymentType === 'principal') {
        const monthlyPrincipal = this.loanAmount / n;
        const firstMonthInterest = this.loanAmount * r;
        this.monthlyPayment = Math.round(monthlyPrincipal + firstMonthInterest);
      } else if (this.repaymentType === 'maturity') {
        this.monthlyPayment = Math.round(this.loanAmount * r);
      }
    },
    // 일반 계산기
    append(char) {
      this.calcInput += char;
    },
    calculate() {
      try {
        const result = eval(this.calcInput);
        this.calcInput = (!isNaN(result) && isFinite(result)) ? result.toString() : '오류';
      } catch {
        this.calcInput = '오류';
      }
    },
    clear() {
      this.calcInput = '';
    }
  }
};
</script>

<style scoped>
.btn {
  @apply bg-white border p-2 rounded text-center font-medium hover:bg-gray-100;
}
</style>
