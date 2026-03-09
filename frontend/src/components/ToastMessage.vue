<template>
  <div class="fixed top-5 left-1/2 -translate-x-1/2 z-[9999] flex flex-col gap-2 pointer-events-none">
    <transition-group name="toast-slide">
      <div
          v-for="toast in toasts"
          :key="toast.id"
          class="flex items-center gap-3 px-5 py-3 rounded-xl shadow-xl min-w-[280px] max-w-md pointer-events-auto transform transition-all duration-300 backdrop-blur-md"
          :class="getToastStyle(toast.type)"
      >
        <div class="shrink-0 flex items-center justify-center p-1 rounded-full bg-white/20">
          <svg v-if="toast.type === 'success'" class="w-5 h-5 text-emerald-50" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
          <svg v-if="toast.type === 'error'" class="w-5 h-5 text-red-50" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          <svg v-if="toast.type === 'info'" class="w-5 h-5 text-blue-50" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        </div>
        <p class="font-medium text-sm text-white flex-1">{{ toast.message }}</p>
        <button @click="removeToast(toast.id)" class="shrink-0 text-white/50 hover:text-white transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
        </button>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const toasts = ref([])

// 这个方法会被暴露出去，给全站使用
const showToast = (message, type = 'info', duration = 3000) => {
  const id = Date.now() + Math.random().toString(36).substring(2, 9)
  toasts.value.push({ id, message, type })

  // 设定时间后自动消失
  if (duration > 0) {
    setTimeout(() => {
      removeToast(id)
    }, duration)
  }
}

const removeToast = (id) => {
  const index = toasts.value.findIndex(t => t.id === id)
  if (index > -1) {
    toasts.value.splice(index, 1)
  }
}

const getToastStyle = (type) => {
  switch (type) {
    case 'success': return 'bg-emerald-500/90 border border-emerald-400/30'
    case 'error': return 'bg-red-500/90 border border-red-400/30'
    case 'info':
    default: return 'bg-blue-600/90 border border-blue-500/30'
  }
}

// 暴露给外部调用的核心方法
defineExpose({
  showToast
})
</script>

<style scoped>
.toast-slide-enter-active,
.toast-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
.toast-slide-enter-from {
  opacity: 0;
  transform: translateY(-20px) scale(0.9);
}
.toast-slide-leave-to {
  opacity: 0;
  transform: translateY(-20px) scale(0.9);
}
</style>