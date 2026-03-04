<template>
  <div>
    <!-- 头部横幅与批量操作区 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center text-xl">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">报名审核与评分</h2>
          <p class="text-xs text-gray-500">查看全校报名记录，支持打分、下载附件与批量处理</p>
        </div>
      </div>

      <!-- 批量操作按钮（只有当选中了记录时才显示） -->
      <div class="flex gap-2 animate-fade-in" v-if="selectedApps.length > 0">
        <button @click="handleBulkReview('approved')" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg shadow-sm text-sm font-bold transition flex items-center gap-2">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
          批量通过 ({{ selectedApps.length }})
        </button>
        <button @click="handleBulkReview('rejected')" class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg shadow-sm text-sm font-bold transition flex items-center gap-2">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          批量驳回
        </button>
      </div>
    </div>

    <div v-if="isLoading" class="text-center py-20 text-emerald-500">
      <svg class="animate-spin h-10 w-10 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      <p>正在收作业（拉取报名表）...</p>
    </div>

    <!-- 报名列表表格 -->
    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
          <tr>
            <th class="py-3 px-4 text-left w-12">
              <input type="checkbox" @change="toggleSelectAll" :checked="isAllSelected" class="rounded text-emerald-500 focus:ring-emerald-400 w-4 h-4 cursor-pointer">
            </th>
            <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">学生姓名</th>
            <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">申报竞赛</th>
            <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">参赛说明/附加信息</th>
            <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">当前状态</th>
            <th class="py-3 px-4 text-center text-xs font-bold text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="app in allApplications" :key="app.id" class="hover:bg-gray-50 transition">
            <td class="py-3 px-4">
              <input type="checkbox" v-model="selectedApps" :value="app.id" class="rounded text-emerald-500 focus:ring-emerald-400 w-4 h-4 cursor-pointer">
            </td>
            <td class="py-3 px-4 font-medium text-gray-900">{{ app.studentName }}</td>
            <td class="py-3 px-4 text-sm text-gray-700 font-semibold">{{ app.compTitle }}</td>
            <td class="py-3 px-4 text-sm text-gray-500">
              <div class="flex items-center gap-2">
                <div class="truncate max-w-[200px]" :title="app.statement">{{ app.statement || '无' }}</div>
                <!-- 🌟 如果该学生传了附件，显示一个小小的曲别针图标 -->
                <span v-if="app.fileUrl" class="text-blue-500 flex items-center shrink-0" title="包含附件">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"></path></svg>
                  </span>
              </div>
            </td>
            <td class="py-3 px-4">
                <span class="px-3 py-1 text-xs font-bold rounded-full border" :class="getStatusBadgeClass(app.status)">
                  {{ getStatusText(app.status) }}
                </span>
            </td>
            <td class="py-3 px-4 text-center">
              <button v-if="app.status === 'pending'" @click="openReviewModal(app)" class="bg-emerald-500 hover:bg-emerald-600 text-white px-3 py-1.5 rounded text-sm font-medium transition shadow-sm">
                去审核
              </button>
              <button v-else @click="openReviewModal(app)" class="bg-gray-100 border border-gray-300 hover:bg-gray-200 text-gray-700 px-3 py-1.5 rounded text-sm font-medium transition">
                修改评分
              </button>
            </td>
          </tr>
          <tr v-if="allApplications.length === 0">
            <td colspan="6" class="py-12 text-center text-gray-500">当前没有需要审核的报名记录。</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 审核弹窗 (含附件下载) -->
    <div v-if="showReviewModal" class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-2xl w-[550px] flex flex-col overflow-hidden animate-fade-in-up border border-gray-100">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-emerald-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
            审核与评分
          </h3>
          <button @click="showReviewModal = false" class="text-gray-400 hover:text-red-500 transition w-8 h-8 flex items-center justify-center rounded-full hover:bg-red-50">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <div class="p-6">
          <!-- 报名表详情摘要 -->
          <div class="mb-5 text-sm bg-gray-50/80 p-4 rounded-xl border border-gray-200">
            <div class="space-y-2.5">
              <p class="flex items-start"><span class="text-gray-500 w-20 shrink-0">提交学生：</span> <span class="font-bold text-gray-800">{{ currentReviewingApp.studentName }}</span></p>
              <p class="flex items-start"><span class="text-gray-500 w-20 shrink-0">申报竞赛：</span> <span class="font-bold text-blue-600">{{ currentReviewingApp.compTitle }}</span></p>
              <div class="border-t border-gray-200/60 mt-2 pt-2">
                <span class="text-gray-500 block mb-1">参赛宣言 / 详细说明：</span>
                <p class="text-gray-700 whitespace-pre-wrap leading-relaxed">{{ currentReviewingApp.statement }}</p>
              </div>
            </div>

            <!-- 🌟 核心：显示附件下载区域 🌟 -->
            <div v-if="currentReviewingApp.fileUrl" class="mt-4 pt-4 border-t border-gray-200 border-dashed">
              <span class="text-gray-500 block mb-2 font-bold">作品附件已就绪：</span>
              <a :href="currentReviewingApp.fileUrl" target="_blank" class="inline-flex items-center justify-center gap-2 px-4 py-2.5 bg-blue-50 text-blue-700 hover:bg-blue-600 hover:text-white rounded-lg border border-blue-200 transition-all font-medium text-sm group shadow-sm">
                <svg class="w-4 h-4 group-hover:animate-bounce" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path></svg>
                点击下载 / 查看作品
              </a>
            </div>
          </div>

          <!-- 打分与反馈表单 -->
          <div class="space-y-4">
            <div>
              <label class="block text-gray-700 text-sm font-bold mb-2">审核结果 <span class="text-red-500">*</span></label>
              <select v-model="reviewForm.status" class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:ring-2 focus:ring-emerald-300 focus:outline-none bg-white">
                <option value="pending">待审核 (维持原状)</option>
                <option value="approved">通过报名 / 作品达标</option>
                <option value="rejected">驳回报名 / 直接淘汰</option>
              </select>
            </div>

            <div>
              <label class="block text-gray-700 text-sm font-bold mb-2">竞赛评分 (0-100分)</label>
              <input type="number" v-model="reviewForm.score" class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:ring-2 focus:ring-emerald-300 focus:outline-none" placeholder="选填，可输入分数">
            </div>

            <div>
              <label class="block text-gray-700 text-sm font-bold mb-2">教师评语 / 反馈建议</label>
              <textarea v-model="reviewForm.feedback" class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:ring-2 focus:ring-emerald-300 focus:outline-none leading-relaxed" rows="3" placeholder="写点鼓励或修改建议吧..."></textarea>
            </div>
          </div>
        </div>

        <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="showReviewModal = false" class="px-5 py-2.5 bg-white border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition shadow-sm font-medium">取消</button>
          <button @click="submitReview" :disabled="isSubmitting" class="px-5 py-2.5 bg-emerald-500 text-white rounded-lg hover:bg-emerald-600 transition shadow-md font-bold flex items-center gap-2 disabled:bg-emerald-300">
            <svg v-if="isSubmitting" class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
            保存审核
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'

const allApplications = ref([])
const isLoading = ref(true)
const isSubmitting = ref(false)

// 页面加载：去后厨把全校作业收上来
const fetchApplications = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/application/all')
    allApplications.value = res.data
  } catch (error) {
    console.error("获取记录失败:", error)
    alert("网络连接失败，请检查后端！")
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchApplications()
})

// ================== 单条审核逻辑 ==================
const showReviewModal = ref(false)
const currentReviewingApp = ref(null)
const reviewForm = reactive({
  id: null,
  status: 'pending',
  score: '',
  feedback: ''
})

const openReviewModal = (app) => {
  currentReviewingApp.value = app
  reviewForm.id = app.id
  reviewForm.status = app.status
  reviewForm.score = app.score !== null ? app.score : ''
  reviewForm.feedback = app.feedback || ''
  showReviewModal.value = true
}

const submitReview = async () => {
  isSubmitting.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/application/review', reviewForm)
    if (res.data.success) {
      showReviewModal.value = false
      fetchApplications() // 重新拉取最新的列表，刷新页面
    } else {
      alert(res.data.message)
    }
  } catch (error) {
    alert("保存失败，请检查网络连接！")
  } finally {
    isSubmitting.value = false
  }
}

// ================== 批量审核逻辑 ==================
const selectedApps = ref([])

const isAllSelected = computed(() => {
  return allApplications.value.length > 0 && selectedApps.value.length === allApplications.value.length
})

const toggleSelectAll = (event) => {
  if (event.target.checked) {
    selectedApps.value = allApplications.value.map(app => app.id)
  } else {
    selectedApps.value = []
  }
}

const handleBulkReview = async (targetStatus) => {
  const actionName = targetStatus === 'approved' ? '批量通过' : '批量驳回'
  if (!confirm(`确定要将这 ${selectedApps.value.length} 个报名记录标记为 "${actionName}" 吗？`)) {
    return
  }

  try {
    const res = await axios.post('http://localhost:8080/api/application/bulkReview', {
      ids: selectedApps.value,
      status: targetStatus
    })

    if (res.data.success) {
      selectedApps.value = []
      fetchApplications()
    }
  } catch (error) {
    alert("批量操作失败！")
  }
}

// 工具函数：状态转文字和颜色
const getStatusText = (status) => {
  const map = { 'pending': '待审核', 'approved': '已通过', 'rejected': '已驳回' }
  return map[status] || status
}
const getStatusBadgeClass = (status) => {
  const map = {
    'pending': 'bg-yellow-50 text-yellow-700 border-yellow-200',
    'approved': 'bg-green-50 text-green-700 border-green-200',
    'rejected': 'bg-red-50 text-red-700 border-red-200'
  }
  return map[status] || 'bg-gray-100 text-gray-800 border-gray-200'
}
</script>

<style scoped>
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fadeInUp 0.3s ease-out forwards;
}
</style>