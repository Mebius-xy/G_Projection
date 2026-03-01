<template>
  <div class="font-sans">
    <!-- 欢迎横幅 -->
    <div class="bg-gradient-to-r from-blue-600 to-indigo-700 rounded-xl shadow-lg p-8 mb-8 text-white flex justify-between items-center relative overflow-hidden">
      <!-- 装饰用光晕 -->
      <div class="absolute -top-20 -right-20 w-64 h-64 bg-white opacity-10 rounded-full blur-3xl"></div>

      <div class="relative z-10">
        <h2 class="text-3xl font-bold mb-2">欢迎回来，{{ currentUser.name }}！</h2>
        <p class="text-blue-100 opacity-90">今天是你在 CIMS 平台努力的又一天，祝你取得好成绩！</p>
      </div>
      <div class="hidden md:block relative z-10">
        <i class="fas fa-rocket text-6xl opacity-20"></i>
      </div>
    </div>

    <!-- 数据卡片区域 (动态数据加载) -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">

      <!-- 卡片1：竞赛总数 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 transform hover:-translate-y-1 transition duration-300 relative overflow-hidden">
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-medium mb-1">全校竞赛总数</p>
            <div class="flex items-baseline gap-2">
              <h3 class="text-3xl font-bold text-gray-800">{{ stats.totalComps }}</h3>
              <span class="text-xs text-green-500 font-bold"><i class="fas fa-arrow-up"></i> 最新</span>
            </div>
          </div>
          <div class="w-12 h-12 bg-blue-50 text-blue-600 rounded-full flex items-center justify-center text-2xl shadow-inner border border-blue-100">
            <i class="fas fa-trophy"></i>
          </div>
        </div>
      </div>

      <!-- 卡片2：我的报名 / 待审核 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 transform hover:-translate-y-1 transition duration-300">
        <div class="flex justify-between items-center">
          <div>
            <p class="text-gray-500 text-sm font-medium mb-1">
              {{ currentUser.role === 'teacher' ? '待审核报名表' : (currentUser.role === 'admin' ? '系统总用户数' : '我参与的竞赛') }}
            </p>
            <h3 class="text-3xl font-bold text-gray-800">{{ stats.actionCount }}</h3>
          </div>
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center text-2xl shadow-inner border border-emerald-100">
            <i class="fas fa-file-alt" v-if="currentUser.role !== 'admin'"></i>
            <i class="fas fa-users" v-else></i>
          </div>
        </div>
      </div>

      <!-- 卡片3：论坛动态 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 transform hover:-translate-y-1 transition duration-300">
        <div class="flex justify-between items-center">
          <div>
            <p class="text-gray-500 text-sm font-medium mb-1">全站讨论热帖</p>
            <h3 class="text-3xl font-bold text-gray-800">{{ stats.forumCount }}</h3>
          </div>
          <div class="w-12 h-12 bg-purple-50 text-purple-600 rounded-full flex items-center justify-center text-2xl shadow-inner border border-purple-100">
            <i class="fas fa-comments"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 动态通知区域 (直接抓取论坛的最新帖子当公告) -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-bold flex items-center text-gray-800">
          <i class="fas fa-bullhorn text-blue-500 mr-2"></i> 最新系统动态
        </h3>
        <button @click="$router.push('/forum')" class="text-sm text-blue-500 hover:text-blue-700 transition">查看更多 <i class="fas fa-angle-right"></i></button>
      </div>

      <div v-if="isLoading" class="text-center py-4 text-gray-400">
        <i class="fas fa-spinner fa-spin mr-2"></i> 正在加载动态...
      </div>

      <ul v-else class="space-y-4">
        <!-- 循环渲染最新的 3 条帖子 -->
        <li v-for="(notice, index) in recentNotices" :key="index" class="flex justify-between items-start text-sm border-b border-gray-50 pb-4 last:border-0 last:pb-0">
          <div class="flex items-start">
            <span class="inline-block px-2 py-1 text-xs rounded font-bold mr-3 mt-0.5" :class="index === 0 ? 'bg-red-100 text-red-600' : 'bg-blue-50 text-blue-600'">
              {{ index === 0 ? '置顶' : '最新' }}
            </span>
            <div>
              <span @click="$router.push('/forum')" class="text-gray-700 font-bold hover:text-blue-600 cursor-pointer transition text-base">{{ notice.title }}</span>
              <p class="text-gray-500 text-xs mt-1.5 flex items-center gap-3">
                <span><i class="fas fa-user text-gray-400"></i> {{ notice.authorName }}</span>
                <span class="bg-gray-100 px-1.5 rounded text-gray-500">{{ notice.tag }}</span>
              </p>
            </div>
          </div>
          <span class="text-gray-400 whitespace-nowrap ml-4 flex items-center">
            <i class="far fa-clock mr-1"></i> {{ notice.createTime.substring(5, 16).replace('T', ' ') }}
          </span>
        </li>
        <li v-if="recentNotices.length === 0" class="text-center text-gray-500 py-4">暂无任何系统动态。</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

// 1. 获取登录用户信息
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

// 2. 响应式状态数据
const isLoading = ref(true)
const stats = reactive({
  totalComps: 0,
  actionCount: 0, // 根据角色不同代表不同含义
  forumCount: 0
})
const recentNotices = ref([]) // 存放最新动态（用论坛帖子替代）

// 3. 页面加载时，并行拉取三个接口的数据
onMounted(async () => {
  try {
    // 技巧：使用 Promise.all 让三个请求同时发出去，速度更快！
    const [compRes, forumRes] = await Promise.all([
      axios.get('http://localhost:8080/api/competition/list'),
      axios.get('http://localhost:8080/api/forum/list')
    ])

    // 填充竞赛总数和论坛总数
    stats.totalComps = compRes.data.length || 0
    stats.forumCount = forumRes.data.length || 0

    // 取论坛前3条帖子作为 "最新动态"
    recentNotices.value = forumRes.data.slice(0, 3)

    // 根据角色拉取专门的数据
    if (currentUser.value.role === 'student') {
      const appRes = await axios.get('http://localhost:8080/api/application/my', {
        params: { studentId: currentUser.value.id }
      })
      stats.actionCount = appRes.data.length || 0
    }
    else if (currentUser.value.role === 'teacher') {
      const appRes = await axios.get('http://localhost:8080/api/application/all')
      // 老师只看“待审核”的数量
      stats.actionCount = appRes.data.filter(app => app.status === 'pending').length || 0
    }
    else if (currentUser.value.role === 'admin') {
      // 管理员看系统里一共多少人（分页接口获取 total）
      const userRes = await axios.get('http://localhost:8080/api/user/page?pageSize=1')
      stats.actionCount = userRes.data.total || 0
    }

  } catch (error) {
    console.error("加载首页动态数据失败:", error)
  } finally {
    isLoading.value = false
  }
})
</script>