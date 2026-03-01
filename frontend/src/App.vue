<template>
  <router-view></router-view>
</template>

<style>
/* 🌟 1. 彻底粉碎 Vite 默认的 body 魔法胶水 🌟 */
html, body {
  margin: 0 !important;
  padding: 0 !important;
  width: 100% !important;
  height: 100% !important;
  display: block !important; /* 核心绝杀：解除 flex 强行居中的魔咒 */
  background-color: #f4f6f9 !important; /* 统一底色 */
}

/* 🌟 2. 彻底释放 #app 的尺寸封印 🌟 */
#app {
  max-width: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
  width: 100% !important;
  height: 100% !important;
  display: block !important;
  text-align: left !important;
}
</style>