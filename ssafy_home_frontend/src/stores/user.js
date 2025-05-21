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
      document.cookie = 'Authorization=; Max-Age=0; path=/';
      this.user = null;
      window.location.reload();
    },
  },
});
