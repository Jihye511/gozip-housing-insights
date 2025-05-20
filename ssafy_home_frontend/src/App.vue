<template>
  <div>
    <HeaderComponent v-if="!noHeaderRoutes.includes($route.path)" />
    <router-view />
  </div>
</template>

<script>
import HeaderComponent from '@/components/HeaderComponent.vue';

export default {
  components: { HeaderComponent },
  computed: {
    noHeaderRoutes() {
      return ['/map', '/admin']; // 여기에 헤더 숨기고 싶은 경로 추가
    },
  },
};
</script>
