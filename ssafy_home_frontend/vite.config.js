import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        // target: 'http://localhost:8080', // 스프링 서버 주소
        // target: 'http://211.231.82.58:8080',
        target: 'http://192.168.205.52:8080',

        changeOrigin: true
      },
      '/uploads': {
        target: 'http://192.168.205.52:8080',
        changeOrigin: true,
      },
    }
  },
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
