<template>
  <div>
    <!-- 头部横幅 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-3">
        <!-- 💡 修复：替换掉了出问题的 fas fa-history -->
        <div class="w-12 h-12 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center text-xl border border-blue-100">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">我的报名记录</h2>
          <p class="text-xs text-gray-500 mt-0.5">跟踪您的报名状态、查看教师反馈与成绩</p>
        </div>
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="isLoading" class="text-center py-20 text-blue-500">
      <svg class="animate-spin h-10 w-10 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      <p class="font-bold tracking-widest text-sm">正在拉取您的报名记录...</p>
    </div>

    <!-- 暂无记录 -->
    <div v-else-if="myApps.length === 0" class="text-center py-24 bg-white rounded-2xl shadow-sm border border-gray-100">
      <svg class="w-20 h-20 mx-auto mb-4 text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z"></path></svg>
      <p class="text-gray-500 text-lg font-medium">您还没有报名任何竞赛</p>
      <button @click="$router.push('/competitions')" class="mt-6 bg-blue-600 hover:bg-blue-700 text-white font-bold px-6 py-2.5 rounded-lg transition shadow-md">
        去竞赛大厅看看
      </button>
    </div>

    <!-- 报名记录列表 -->
    <div v-else class="space-y-4">
      <div v-for="app in myApps" :key="app.id" class="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 hover:shadow-md transition duration-300 relative overflow-hidden group">
        <!-- 左侧色彩条 -->
        <div class="absolute left-0 top-0 bottom-0 w-1.5 transition-colors" :class="getStatusBorderColor(app.status)"></div>

        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 pl-2">
          <!-- 左侧：比赛信息 -->
          <div class="flex-1">
            <h3 class="font-bold text-xl text-gray-800 flex items-center gap-2">
              {{ app.compTitle }}
              <!-- 🌟 队伍公章显示 🌟 -->
              <span v-if="app.teamName" class="px-2.5 py-0.5 bg-purple-50 text-purple-600 border border-purple-200 text-xs rounded-full flex items-center shadow-sm">
                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857"></path></svg>
                团队报名: {{ app.teamName }}
              </span>
            </h3>

            <div class="text-sm text-gray-500 mt-2 flex flex-wrap items-center gap-5">
              <span class="flex items-center gap-1.5"><svg class="w-4 h-4 opacity-70" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg> 提交于: {{ formatDate(app.submitTime) }}</span>
              <span class="flex items-center gap-1.5"><svg class="w-4 h-4 opacity-70" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"></path></svg> 申请编号: #{{ app.id }}</span>
            </div>
          </div>

          <!-- 右侧：状态角标 (通过原生 SVG 渲染图标) -->
          <div class="text-right flex-shrink-0">
            <span class="px-4 py-1.5 rounded-full text-sm font-bold shadow-sm flex items-center" :class="getStatusBadgeClass(app.status)">
               <svg v-if="app.status === 'pending'" class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
               <svg v-else-if="app.status === 'approved'" class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
               <svg v-else class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
               {{ getStatusText(app.status) }}
            </span>
          </div>
        </div>

        <!-- 底部：教师反馈 -->
        <div v-if="app.status !== 'pending'" class="mt-5 pt-4 border-t border-gray-100 bg-gray-50 p-5 rounded-xl ml-2">
          <div class="flex items-start gap-3">
            <div class="mt-0.5 bg-white p-1 rounded border border-gray-200 shadow-sm text-gray-400">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
            </div>
            <div class="flex-1">
              <p class="text-sm text-gray-700 leading-relaxed"><span class="font-bold text-gray-900">指导老师反馈：</span>{{ app.feedback || '老师很忙，没有留下评语~' }}</p>
              <div v-if="app.score !== null" class="mt-3 text-sm flex items-center">
                <span class="font-bold text-gray-700">竞赛得分 / 评级：</span>
                <span class="font-black text-2xl mx-1.5" :class="app.score >= 60 ? 'text-green-600' : 'text-red-500'">{{ app.score }}</span> 分
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const myApps = ref([])
const isLoading = ref(true)

onMounted(async () => {
  const userStr = localStorage.getItem('currentUser')
  const currentUser = userStr ? JSON.parse(userStr) : null

  if (currentUser) {
    try {
      // 呼叫传菜员，把自己的学号带过去查记录
      const res = await axios.get('http://localhost:8080/api/application/my', {
        params: { studentId: currentUser.id }
      })
      myApps.value = res.data
    } catch (error) {
      console.error("查询报名记录失败:", error)
      alert("无法获取报名记录，请检查后端！")
    } finally {
      isLoading.value = false
    }
  }
})

// 工具：时间格式化 (去掉多余的 T 和毫秒)
const formatDate = (dateString) => {
  if (!dateString) return ''
  return dateString.replace('T', ' ').substring(0, 16)
}

// 工具：状态转文字
const getStatusText = (status) => {
  const map = { 'pending': '待审核', 'approved': '报名成功/通过', 'rejected': '报名被驳回' }
  return map[status] || status
}

// 工具：状态转颜色标签
const getStatusBadgeClass = (status) => {
  const map = {
    'pending': 'bg-yellow-100 text-yellow-700 border border-yellow-200',
    'approved': 'bg-green-100 text-green-700 border border-green-200',
    'rejected': 'bg-red-100 text-red-700 border border-red-200'
  }
  return map[status] || 'bg-gray-100 text-gray-800'
}

// 工具：卡片左侧边框颜色
const getStatusBorderColor = (status) => {
  const map = { 'pending': 'border-yellow-400', 'approved': 'border-green-400', 'rejected': 'border-red-400' }
  return map[status] || 'border-gray-200'
}

// 工具：状态图标
const getStatusIcon = (status) => {
  const map = { 'pending': 'fas fa-hourglass-half', 'approved': 'fas fa-check-circle', 'rejected': 'fas fa-times-circle' }
  return map[status] || 'fas fa-info-circle'}
// (如果你之前的 script 里有 getStatusIcon 这个函数，现在可以删除了，因为我们直接在模板里用 v-if 渲染 SVG 了)
</script>