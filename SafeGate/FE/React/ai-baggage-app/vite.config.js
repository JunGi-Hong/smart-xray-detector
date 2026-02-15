import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      //로컬 환경에서 돌아갈 수 있게..
      // '/user'로 시작하는 요청이 오면 백엔드(8080)로 전달
      '/user': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    }
  }
})

