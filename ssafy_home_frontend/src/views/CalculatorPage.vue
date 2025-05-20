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
            <input v-model.number="price" type="number" placeholder="매매가를 입력하세요" class="border p-2 rounded w-full mb-4" />
            <button @click="calcCommission" class="w-full bg-black text-white py-2 rounded">계산하기</button>
            <p class="mt-4 text-blue-600 font-bold text-center">{{ commission.toLocaleString() }}원</p>
            <p class="text-xs text-gray-500 text-center mt-2">* 계산된 금액은 법정 중개보수 요율에 따라 산출된값입니다.</p>
          </div>

          <!-- 대출이자 계산기 -->
          <div v-if="currentTab === 'loan'">
            <h3 class="text-lg font-bold mb-4">대출이자 계산기</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input v-model.number="loanAmount" type="number" placeholder="대출 금액 입력" class="border p-2 rounded" />
              <input v-model.number="loanRate" type="number" step="0.01" placeholder="연 이자율 (%)" class="border p-2 rounded" />
              <input v-model.number="loanTerm" type="number" placeholder="대출 기간 (년)" class="border p-2 rounded" />
            </div>
            <button @click="calcLoan" class="w-full bg-black text-white py-2 rounded mt-4">계산하기</button>
            <p class="mt-4 text-blue-600 font-bold text-center">월 상환금액: {{ monthlyPayment.toLocaleString() }}원</p>
            <p class="text-xs text-gray-500 text-center mt-2">* 균등상환 방식 기준</p>
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
              <button @click="calculate" class="btn bg-blue-500 text-white">=</button>
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
      price: 0,
      commission: 0,
      loanAmount: 0,
      loanRate: 0,
      loanTerm: 0,
      monthlyPayment: 0,
      calcInput: ''
    };
  },
  methods: {
    tabClass(tab) {
      return `px-4 py-1 rounded ${this.currentTab === tab ? 'bg-gray-200 font-semibold' : 'text-gray-600 hover:underline'}`;
    },
    calcCommission() {
      this.commission = this.price * 0.009; // 임의 중개 보수율
    },
    calcLoan() {
      const r = this.loanRate / 100 / 12;
      const n = this.loanTerm * 12;
      if (r > 0) {
        this.monthlyPayment = Math.round(
          (this.loanAmount * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)
        );
      } else {
        this.monthlyPayment = Math.round(this.loanAmount / n);
      }
    },
    append(char) {
      this.calcInput += char;
    },
    calculate() {
      try {
        this.calcInput = eval(this.calcInput).toString();
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
