import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
  }),
  actions: {
    async fetchUser() {
      try {
        const res = await fetch('http://localhost:8080/api/user/mypage', {
          credentials: 'include',
        });

        if (res.status === 200) {
          this.user = await res.json();
        } else if (res.status === 401) {
          this.user = null;
          // console.warn('로그인되지 않은 사용자입니다.');
        } else {
          this.user = null;
          console.warn('예상치 못한 응답 상태:', res.status);
        }
      } catch (e) {
        console.error('요청 실패 (네트워크 또는 서버 문제):', e);
        this.user = null;
      }
    },

    logout() {
      fetch('http://localhost:8080/api/user/logout', {
        method: 'POST',
        credentials: 'include',
      })
        .then(() => {
          this.user = null;
          // window.location.href = '/';
        })
        .catch((e) => console.error('Logout failed', e));
    },
  },
});
