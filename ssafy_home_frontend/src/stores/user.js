import { defineStore } from 'pinia';

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
        } else {
          this.user = null;
        }
      } catch (e) {
        this.user = null;
      }
    },
    logout() {
      fetch('http://localhost:8080/api/user/logout', {
        method: 'POST',
        credentials: 'include',
      })
        .catch((e) => console.error('Logout failed', e))
        .finally(async() => {
          this.user = null;
          window.location.href = '/'; // 또는 router.push("/")
        });
    },
  },
});
