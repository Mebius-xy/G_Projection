import { createApp, ref } from 'vue'
import ToastMessage from '../components/ToastMessage.vue'

// 1. 创建一个游离于主 Vue 实例之外的独立实例
const toastContainer = document.createElement('div')
document.body.appendChild(toastContainer)

const toastApp = createApp(ToastMessage)
const toastInstance = toastApp.mount(toastContainer)

// 2. 导出快捷方法供全局页面使用
export const toast = {
    success: (msg, duration) => toastInstance.showToast(msg, 'success', duration),
    error: (msg, duration) => toastInstance.showToast(msg, 'error', duration),
    info: (msg, duration) => toastInstance.showToast(msg, 'info', duration)
}