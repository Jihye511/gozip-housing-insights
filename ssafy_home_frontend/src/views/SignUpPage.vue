<template>
    <div class="min-h-screen bg-gray-50">


        <main class="flex justify-center items-center py-10">
            <div class="w-full max-w-md bg-white p-8 rounded shadow">
                <h2 class="text-center text-2xl font-bold mb-4">회원가입</h2>
                <p class="text-center text-gray-500 mb-6">새 계정을 만들어 서비스를 이용하세요</p>
                <form class="space-y-4">
                    <input type="text" placeholder="사용할 아이디를 입력하세요" class="w-full border p-2 rounded" />
                    <input type="password" placeholder="비밀번호를 입력하세요" class="w-full border p-2 rounded" />
                    <input type="password" placeholder="비밀번호를 다시 입력하세요" class="w-full border p-2 rounded" />
                    <input type="email" placeholder="이메일 주소를 입력하세요" class="w-full border p-2 rounded" />
                    <input type="text" placeholder="사용할 닉네임을 입력하세요" class="w-full border p-2 rounded" />
                    <input type="text" placeholder="실명을 입력하세요" class="w-full border p-2 rounded" />
                    <button type="submit" class="w-full bg-black text-white py-2 rounded">회원가입</button>
                </form>
                <p class="text-center text-sm text-gray-600 mt-4">
                    이미 계정이 있으신가요? <a href="#" class="text-blue-600 hover:underline">로그인</a>
                </p>
            </div>
        </main>
    </div>
</template>

<script>
export default {
    name: 'SignUpPage',
}
</script>

<style scoped></style>
