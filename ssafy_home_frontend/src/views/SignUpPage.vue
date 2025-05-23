<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
        <div class="w-full max-w-md bg-white p-6 rounded shadow">
            <h1 class="text-2xl font-bold mb-6 text-center">회원가입</h1>
            <form @submit.prevent="onSubmit" class="space-y-4">
                <input v-model="form.user_id" placeholder="아이디" required class="w-full border p-2 rounded" />
                <input v-model="form.pw" type="password" placeholder="비밀번호" required
                    class="w-full border p-2 rounded" />
                <input v-model="form.email" type="email" placeholder="이메일" required class="w-full border p-2 rounded" />
                <input v-model="form.name" placeholder="이름" class="w-full border p-2 rounded" />
                <input v-model="form.phone" placeholder="휴대폰" class="w-full border p-2 rounded" />
                <button type="submit" class="w-full bg-green-600 text-white py-2 rounded">가입하기</button>
            </form>
            <p class="mt-4 text-center text-sm">
                이미 계정이 있으신가요?
                <router-link to="/login" class="text-green-600 underline">로그인</router-link>
            </p>
        </div>
    </div>
</template>

<script>
import axios from '@/axios/axios'
import { useRouter } from 'vue-router'
import { reactive } from 'vue'

export default {
    name: 'SignUpPage',
    setup() {
        const router = useRouter()
        const form = reactive({
            user_id: '',
            pw: '',
            email: '',
            name: '',
            phone: '',
        })

        const onSubmit = async () => {
            try {
                await axios.post('/auth/register', form)
                alert('회원가입 성공! 로그인 페이지로 이동합니다.')
                router.push('/login')
            } catch (err) {
                alert(err.response?.data || '회원가입에 실패했습니다.')
            }
        }

        return { form, onSubmit }
    },
}
</script>
