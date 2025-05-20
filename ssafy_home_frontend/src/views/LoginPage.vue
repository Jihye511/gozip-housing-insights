<template>
  <div class="min-h-screen bg-gray-50">

    <main class="flex justify-center items-center py-10">
      <div class="w-full max-w-sm bg-white p-8 rounded shadow">
        <h2 class="text-center text-2xl font-bold mb-2">로그인</h2>
        <p class="text-center text-gray-500 mb-6">계정 정보를 입력하여 로그인하세요</p>
        <form class="space-y-4">
          <div>
            <label class="block mb-1 text-sm font-medium">아이디</label>
            <input type="text" placeholder="아이디를 입력하세요" class="w-full border p-2 rounded" />
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">비밀번호</label>
            <div class="flex justify-between items-center">
              <input type="password" placeholder="비밀번호를 입력하세요" class="w-full border p-2 rounded" />
              <a href="#" class="ml-2 text-blue-600 text-sm hover:underline">비밀번호 찾기</a>
            </div>
          </div>
          <div class="flex items-center space-x-2">
            <input id="remember" type="checkbox" class="border rounded" />
            <label for="remember" class="text-sm">아이디 기억하기</label>
          </div>
          <button type="submit" class="w-full bg-black text-white py-2 rounded">로그인</button>
        </form>
        <p class="text-center text-sm text-gray-600 mt-4">
          계정이 없으신가요? <a href="#" class="text-blue-600 hover:underline">회원가입</a>
        </p>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: 'LoginPage',
};
</script>

<style scoped>
</style>
