<template>
  <div class="min-h-screen flex items-center justify-center bg-[#f0f2f5] p-4 relative font-sans">

    <!-- 充满科技感的背景网格装饰 -->
    <div class="absolute inset-0 bg-[linear-gradient(to_right,#80808012_1px,transparent_1px),linear-gradient(to_bottom,#80808012_1px,transparent_1px)] bg-[size:24px_24px]"></div>

    <!-- 登录大卡片 -->
    <div class="relative flex bg-white rounded-2xl shadow-[0_20px_60px_-15px_rgba(0,0,0,0.1)] overflow-hidden max-w-5xl w-full min-h-[600px] z-10 border border-gray-100">

      <!-- 左侧：品牌展示区 (深邃蓝渐变) -->
      <div class="hidden md:flex flex-col w-5/12 bg-gradient-to-br from-blue-700 via-blue-600 to-indigo-800 text-white p-12 relative overflow-hidden justify-between">

        <!-- 光晕装饰 -->
        <div class="absolute -top-32 -left-32 w-80 h-80 bg-white opacity-10 rounded-full blur-3xl"></div>
        <div class="absolute -bottom-32 -right-32 w-80 h-80 bg-indigo-400 opacity-20 rounded-full blur-3xl"></div>

        <div class="relative z-10">
          <!-- 💡 核心暗门：双击系统LOGO区域，开启管理员通道 -->
          <div @dblclick="unlockAdminMode" class="flex items-center gap-3 mb-10 cursor-pointer group" title="双击此处可唤出隐藏通道">
            <div class="w-12 h-12 bg-white rounded-xl flex items-center justify-center shadow-lg group-hover:scale-105 transition duration-300">
              <!-- 🌟 专属 CIMS 品牌 Logo (六边形科技风) 🌟 -->
              <svg class="w-8 h-8 text-blue-600" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M16 2L2 9.5V22.5L16 30L30 22.5V9.5L16 2Z" fill="currentColor" fill-opacity="0.1" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                <path d="M21 11.5C19.5 9.5 17 8.5 14 9.5C11 10.5 9 13.5 9 16C9 18.5 11 21.5 14 22.5C17 23.5 19.5 22.5 21 20.5" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <span class="text-3xl font-bold tracking-widest uppercase select-none font-mono">CIMS</span>
          </div>

          <!-- 扩大暗门触发区：大标题也可以双击触发 -->
          <h2 @dblclick="unlockAdminMode" class="text-4xl font-bold mb-6 leading-tight tracking-wide cursor-pointer select-none" title="双击此处也可唤出隐藏通道">
            大学生竞赛<br/>信息管理系统
          </h2>
          <div class="w-12 h-1 bg-yellow-400 mb-6 rounded-full"></div>
          <p class="text-blue-100 text-base leading-relaxed opacity-90">
            全方位汇聚学科竞赛资源<br/>
            致力于打造高效、公平、便捷的赛事体验。
          </p>
        </div>

        <div class="relative z-10">
          <p class="text-xs text-blue-200/60 font-mono tracking-wider">
            VERSION 1.0.0 <br/>
            © 2026 MEBIUS TECH. ALL RIGHTS RESERVED.
          </p>
        </div>
      </div>

      <!-- 右侧：表单交互区 -->
      <div class="w-full md:w-7/12 p-8 md:p-16 flex flex-col justify-center relative bg-white">

        <div class="mb-10">
          <h3 class="text-3xl font-bold text-gray-800 tracking-tight">欢迎登录</h3>
          <p class="text-sm text-gray-500 mt-2 font-medium">
            <span v-if="isAdminMode" class="text-purple-600 font-bold bg-purple-50 px-3 py-1.5 rounded border border-purple-100 flex items-center inline-flex">
              <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path></svg>
              系统管理员专属通道
            </span>
            <span v-else>请选择您的身份，进入系统办理业务</span>
          </p>
        </div>

        <!-- 身份切换标签页 -->
        <div v-if="!isAdminMode" class="flex mb-8 bg-gray-50/80 p-1.5 rounded-xl border border-gray-100">
          <button
              v-for="role in publicRoles"
              :key="role.value"
              @click="selectedRole = role.value"
              class="flex-1 py-3 text-sm font-bold transition-all duration-300 rounded-lg flex items-center justify-center gap-2"
              :class="selectedRole === role.value ? 'bg-white text-blue-600 shadow-sm border border-gray-100' : 'text-gray-500 hover:text-gray-700'"
          >
            <svg v-if="role.value==='student'" class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20"><path d="M10.394 2.08a1 1 0 00-.788 0l-7 3a1 1 0 000 1.84L5.25 8.051a.999.999 0 01.356-.257l4-1.714a1 1 0 11.788 1.838L7.667 9.088l1.94.831a1 1 0 00.787 0l7-3a1 1 0 000-1.838l-7-3zM3.31 9.397L5 10.12v4.102a8.969 8.969 0 00-1.05-.174 1 1 0 01-.89-.89 11.115 11.115 0 01.25-3.762zM9.3 16.573A9.026 9.026 0 007 14.935v-3.957l1.818.78a3 3 0 002.364 0l5.508-2.361a11.026 11.026 0 01.25 3.762 1 1 0 01-.89.89 8.968 8.968 0 00-5.35 2.524 1 1 0 01-1.4 0z"></path></svg>
            <svg v-if="role.value==='teacher'" class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20"><path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z"></path></svg>
            {{ role.label }}
          </button>
        </div>

        <!-- 表单区域 -->
        <form @submit.prevent="handleLogin" class="space-y-6">
          <div>
            <label class="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-2">登录账号</label>
            <div class="relative">
              <svg class="absolute left-4 top-4 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
              <input
                  v-model="loginForm.username"
                  type="text"
                  required
                  class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-base rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white block pl-12 p-3.5 transition-all outline-none"
                  :placeholder="currentRolePlaceholder"
              >
            </div>
          </div>

          <div>
            <div class="flex justify-between items-center mb-2">
              <label class="block text-xs font-bold text-gray-500 uppercase tracking-wider">登录密码</label>
              <!-- 呼出忘记密码弹窗 -->
              <a href="#" @click.prevent="showForgotModal = true" class="text-sm text-blue-600 hover:text-blue-800 font-semibold transition">忘记密码？</a>
            </div>
            <div class="relative">
              <svg class="absolute left-4 top-4 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path></svg>
              <input
                  v-model="loginForm.password"
                  :type="showPassword ? 'text' : 'password'"
                  required
                  class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-base rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white block pl-12 pr-12 p-3.5 transition-all outline-none"
                  placeholder="请输入密码"
              >
              <!-- 🌟 新增：密码可见性切换小眼睛 -->
              <button type="button" @click="showPassword = !showPassword" class="absolute right-4 top-3.5 text-gray-400 hover:text-blue-500 transition">
                <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.29 3.29m0 0a10.05 10.05 0 015.71-1.595c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0l-3.29-3.29"></path></svg>
                <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path></svg>
              </button>
            </div>
          </div>

          <button
              type="submit"
              :disabled="isLoading"
              class="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:ring-blue-200 font-bold rounded-xl text-base px-5 py-4 text-center transition-all shadow-[0_8px_20px_-6px_rgba(37,99,235,0.4)] mt-6 disabled:bg-blue-400 flex justify-center items-center"
          >
            <svg v-if="isLoading" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span>{{ isLoading ? '正在验证身份...' : '立即登录' }}</span>
          </button>

          <!-- 如果是在管理员模式，提供一个退出通道 -->
          <div v-if="isAdminMode" class="text-center mt-4">
            <a href="#" @click.prevent="exitAdminMode" class="text-sm text-gray-400 hover:text-gray-600 underline">返回普通通道</a>
          </div>
        </form>

      </div>
    </div>

    <!-- ================= 独立模块：忘记密码精装弹窗 ================= -->
    <div v-if="showForgotModal" class="fixed inset-0 z-50 flex items-center justify-center">
      <div class="absolute inset-0 bg-gray-900/40 backdrop-blur-sm" @click="showForgotModal = false"></div>

      <div class="bg-white rounded-2xl shadow-2xl w-[450px] relative z-10 overflow-hidden transform transition-all animate-fade-in-up">
        <div class="bg-blue-50 p-6 text-center border-b border-blue-100">
          <div class="w-16 h-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mx-auto mb-4 text-2xl shadow-sm border border-white">
            <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"></path></svg>
          </div>
          <h3 class="text-xl font-bold text-gray-800">密码重置向导</h3>
        </div>

        <div class="p-8 text-center">
          <p class="text-gray-600 leading-relaxed mb-6 text-sm">
            为了保障校园数据安全，本系统<strong class="text-red-500">不支持</strong>在线自助找回密码。<br/><br/>
            请携带您的<strong class="text-gray-800">学生证</strong>或<strong class="text-gray-800">教工卡</strong>，前往所在学院的教务办公室，由管理员核实身份后进行人工重置。
          </p>
          <button @click="showForgotModal = false" class="w-full bg-gray-100 hover:bg-gray-200 text-gray-800 font-bold py-3 px-4 rounded-xl transition">
            我知道了，返回登录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
// 🌟 1. 引入我们的高级提示组件
import { toast } from '../utils/toast'

const router = useRouter()
const isLoading = ref(false)
const showForgotModal = ref(false)
const showPassword = ref(false)

const publicRoles = [
  { label: '学生通道', value: 'student' },
  { label: '教师通道', value: 'teacher' }
]

const selectedRole = ref('student')
const isAdminMode = ref(false)

// 触发暗门逻辑
const unlockAdminMode = () => {
  isAdminMode.value = true
  selectedRole.value = 'admin'
  loginForm.username = 'admin'
  loginForm.password = ''
}

const exitAdminMode = () => {
  isAdminMode.value = false
  selectedRole.value = 'student'
  loginForm.username = ''
}

const loginForm = reactive({ username: '', password: '' })

const currentRolePlaceholder = computed(() => {
  if (selectedRole.value === 'student') return '请输入 10 位学号'
  if (selectedRole.value === 'teacher') return '请输入教职工号'
  return '请输入管理员账号 (Admin)'
})

const handleLogin = async () => {
  isLoading.value = true
  try {
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      username: loginForm.username,
      password: loginForm.password,
      role: selectedRole.value
    })

    if (response.data.success) {
      localStorage.setItem('currentUser', JSON.stringify(response.data.data))
      localStorage.setItem('token', response.data.data.id)

      // 🌟 2. 替换掉原来的 alert
      toast.success('登录成功，欢迎回来！')

      if (response.data.data.isFirstLogin === true) {
        router.push('/change-password')
      } else {
        router.push('/dashboard')
      }
    } else {
      // 🌟 2. 替换掉原来的 alert
      toast.error(response.data.message)
    }
  } catch (error) {
    // 🌟 2. 替换掉原来的 alert
    toast.error('网络连接失败，请确认后端已启动！')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px) scale(0.95); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}
.animate-fade-in-up { animation: fadeInUp 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards; }
input:-webkit-autofill { -webkit-box-shadow: 0 0 0 1000px #f9fafb inset !important; }
input:focus:-webkit-autofill { -webkit-box-shadow: 0 0 0 1000px #ffffff inset !important; }
</style>