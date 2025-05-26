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

            <label class="text-sm font-medium block mb-1">거래 유형 선택</label>
            <select v-model="dealType" class="border p-2 rounded w-full mb-4">
              <option value="매매">매매</option>
              <option value="전세">전세</option>
              <option value="월세">월세</option>
            </select>

            <label class="text-sm font-medium block mb-1">거래 금액 입력 (₩)</label>
            <input v-model.number="price" type="number" placeholder="예: 300000000" class="border p-2 rounded w-full mb-4" />

            <button @click="calcCommission" class="w-full bg-black text-white py-2 rounded">계산하기</button>
            <p class="mt-4 text-blue-600 font-bold text-center">{{ commission.toLocaleString() }}원</p>
            <p class="text-sm text-center mt-2 text-gray-700" v-if="price > 0">
              계산식: {{ price.toLocaleString() }} × {{ (commissionRate * 100).toFixed(2) }}% = {{ (price * commissionRate).toLocaleString() }} (최대 {{ commissionLimit.toLocaleString() }}원 적용)
            </p>
            <p class="text-xs text-gray-500 text-center mt-2">* 법정 중개보수 상한 기준으로 계산됩니다.</p>
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

            <button @click="calcLoan" class="w-full bg-black text-white py-2 rounded mt-4">계산하기</button>
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
      price: 0,
      commission: 0,
      commissionRate: 0,
      commissionLimit: 1000000,
      // 대출
      loanAmount: 0,
      loanRate: 0,
      loanTerm: 0,
      repaymentType: 'equal',
      monthlyPayment: 0,
      // 계산기
      calcInput: '',
    };
  },
  methods: {
    tabClass(tab) {
      return `px-4 py-1 rounded ${this.currentTab === tab ? 'bg-gray-200 font-semibold' : 'text-gray-600 hover:underline'}`;
    },
    calcCommission() {
      let rate = 0.009;
      const amount = this.price;

      if (this.dealType === '매매') {
        if (amount <= 50000000) rate = 0.006;
        else if (amount <= 200000000) rate = 0.005;
        else if (amount <= 900000000) rate = 0.004;
        else rate = 0.009;
      } else if (this.dealType === '전세') {
        rate = 0.008;
      } else if (this.dealType === '월세') {
        rate = 0.008;
      }

      this.commissionRate = rate;
      this.commission = Math.min(amount * rate, this.commissionLimit);
    },
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
    append(char) {
      this.calcInput += char;
    },
    calculate() {
      try {
        const result = eval(this.calcInput);
        if (isNaN(result) || !isFinite(result)) {
          this.calcInput = '오류';
        } else {
          this.calcInput = result.toString();
        }
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
