<template>
  <div>
    <!-- 头部横幅 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center text-xl">
          <i class="fas fa-history"></i>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">我的报名记录</h2>
          <p class="text-xs text-gray-500">跟踪您的报名状态、查看教师反馈与成绩</p>
        </div>
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="isLoading" class="text-center py-20 text-blue-500">
      <i class="fas fa-spinner fa-spin text-4xl mb-4"></i>
      <p>正在拉取您的报名记录...</p>
    </div>

    <!-- 暂无记录 -->
    <div v-else-if="myApps.length === 0" class="text-center py-20 bg-white rounded-lg shadow-sm border border-gray-100">
      <i class="fas fa-folder-open text-6xl text-gray-300 mb-4"></i>
      <p class="text-gray-500 text-lg">您还没有报名任何竞赛</p>
      <button @click="$router.push('/competitions')" class="mt-4 bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-lg transition shadow-sm">
        去大厅看看
      </button>
    </div>

    <!-- 报名记录列表 -->
    <div v-else class="space-y-4">
      <div v-for="app in myApps" :key="app.id" class="bg-white p-6 rounded-xl shadow-sm border-l-4 transition hover:shadow-md" :class="getStatusBorderColor(app.status)">
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">

          <!-- 左侧：比赛信息 -->
          <div class="flex-1">
            <h3 class="font-bold text-lg text-gray-800">{{ app.compTitle }}</h3>
            <div class="text-sm text-gray-500 mt-1 flex items-center gap-4">
              <span><i class="far fa-clock"></i> 提交于: {{ formatDate(app.submitTime) }}</span>
              <span><i class="fas fa-hashtag"></i> 申请编号: #{{ app.id }}</span>
            </div>
          </div>

          <!-- 右侧：状态角标 -->
          <div class="text-right flex-shrink-0">
            <span class="px-4 py-1.5 rounded-full text-sm font-bold shadow-sm" :class="getStatusBadgeClass(app.status)">
              <i :class="getStatusIcon(app.status)" class="mr-1"></i> {{ getStatusText(app.status) }}
            </span>
          </div>
        </div>

        <!-- 底部：教师反馈 (只有在老师处理过后才显示) -->
        <div v-if="app.status !== 'pending'" class="mt-4 pt-4 border-t border-gray-100 bg-gray-50 p-4 rounded-lg">
          <div class="flex items-start gap-2">
            <i class="fas fa-comment-dots text-gray-400 mt-1"></i>
            <div class="flex-1">
              <p class="text-sm text-gray-700"><span class="font-bold">指导老师反馈：</span>{{ app.feedback || '老师很忙，没有留下评语~' }}</p>
              <div v-if="app.score !== null" class="mt-2 text-sm">
                <span class="font-bold text-gray-700">竞赛得分/评级：</span>
                <span class="font-bold text-xl ml-1" :class="app.score >= 60 ? 'text-green-600' : 'text-red-500'">{{ app.score }}</span> 分
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
  return map[status] || 'fas fa-info-circle'
}
</script>