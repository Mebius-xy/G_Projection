<template>
  <div>
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-purple-100 text-purple-600 rounded-full flex items-center justify-center text-xl">
          <i class="fas fa-stamp"></i>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">数据权限审批中心</h2>
          <p class="text-xs text-gray-500">审批教师申请访问特定班级数据的请求</p>
        </div>
      </div>
    </div>

    <div v-if="isLoading" class="text-center py-20 text-purple-500">
      <i class="fas fa-spinner fa-spin text-4xl mb-4"></i>
    </div>

    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">申请教师</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">目标班级 (请求范围)</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">申请理由与说明</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">申请时间</th>
          <th class="py-3 px-4 text-center text-xs font-bold text-gray-500 uppercase tracking-wider">状态/操作</th>
        </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
        <tr v-for="app in approvals" :key="app.id" class="hover:bg-gray-50 transition">
          <td class="py-3 px-4 text-sm font-medium text-gray-900"><i class="fas fa-chalkboard-teacher text-gray-400 mr-1"></i> {{ app.teacherName }}</td>
          <td class="py-3 px-4 text-sm font-bold text-blue-600">{{ app.targetClass }}</td>
          <td class="py-3 px-4 text-sm text-gray-500 max-w-xs truncate" :title="app.reason">{{ app.reason }}</td>
          <td class="py-3 px-4 text-sm text-gray-500">{{ app.applyTime.replace('T', ' ').substring(0, 16) }}</td>
          <td class="py-3 px-4 text-center">
            <div v-if="app.status === 'pending'" class="flex justify-center gap-2">
              <button @click="process(app.id, 'approved')" class="bg-green-500 hover:bg-green-600 text-white px-3 py-1.5 rounded shadow-sm text-xs font-bold transition">批准授权</button>
              <button @click="process(app.id, 'rejected')" class="bg-red-500 hover:bg-red-600 text-white px-3 py-1.5 rounded shadow-sm text-xs font-bold transition">驳回</button>
            </div>
            <span v-else class="px-3 py-1 text-xs font-bold rounded-full" :class="app.status === 'approved' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'">
                {{ app.status === 'approved' ? '已批准' : '已驳回' }}
              </span>
          </td>
        </tr>
        <tr v-if="approvals.length === 0">
          <td colspan="5" class="py-12 text-center text-gray-500">当前没有需要审批的权限请求。</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const approvals = ref([])
const isLoading = ref(true)

const fetchApprovals = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/permission/all')
    approvals.value = res.data
  } catch (error) {
    alert("拉取审批列表失败！")
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchApprovals()
})

const process = async (id, status) => {
  if(!confirm(`确定要 ${status === 'approved' ? '批准' : '驳回'} 这个数据访问请求吗？`)) return

  try {
    const res = await axios.post('http://localhost:8080/api/permission/process', { id, status })
    if (res.data.success) {
      fetchApprovals() // 重新刷新列表，状态就变了
    }
  } catch (error) {
    alert("操作失败！")
  }
}
</script>