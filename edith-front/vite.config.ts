import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "tailwindcss";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [tailwindcss()],
    },
  },
  // server: {
  //   host: "0.0.0.0",
  //   port: 3000,
  //   proxy: {
  //     "/api": {
  //       target: "https://edith-ai.xyz:30443",
  //       changeOrigin: true,
  //       secure: true,
  //     },
  //   },
  // },
});
