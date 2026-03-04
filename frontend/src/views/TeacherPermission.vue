<template>
  <div class="space-y-6">
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="p-5 border-b bg-gray-50 flex items-center gap-2">
        <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"></path></svg>
        <h2 class="text-lg font-bold text-gray-800">数据权限申请</h2>
      </div>

      <div class="p-6">
        <div class="bg-blue-50 p-4 rounded-lg text-sm text-blue-800 border border-blue-100 mb-6 flex items-start">
          <svg class="w-5 h-5 mr-2 shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
          <p>为保护学生隐私，教师如需查看或修改特定班级的学生信息（如录入竞赛基础分），需向系统管理员提交权限申请并附带证明材料。</p>
        </div>

        <form @submit.prevent="submitApplication" class="max-w-3xl space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-gray-700 font-bold mb-2 text-sm">申请访问的班级名称 <span class="text-red-500">*</span></label>
              <input v-model="permForm.targetClass" type="text" required class="shadow-sm border rounded-lg w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 focus:outline-none" placeholder="需与系统内班级名完全一致，如：计科22级4班">
            </div>
          </div>
          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">申请理由及证明说明 <span class="text-red-500">*</span></label>
            <textarea v-model="permForm.reason" required rows="3" class="shadow-sm border rounded-lg w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 focus:outline-none" placeholder="例如：本人担任该班级《Java程序设计》课程授课教师，需按教学计划录入期末竞赛加分。"></textarea>
          </div>
          <button type="submit" :disabled="isApplying" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-6 rounded-lg transition shadow-sm flex items-center gap-2 disabled:bg-blue-300">
            <svg v-if="!isApplying" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
            <svg v-else class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            {{ isApplying ? '提交中...' : '提交申请' }}
          </button>
        </form>
      </div>
    </div>

    <!-- 已授权班级学生管理 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="p-5 border-b bg-gray-50 flex items-center gap-2">
        <svg class="w-5 h-5 text-emerald-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
        <h2 class="text-lg font-bold text-gray-800">已授权班级学生管理</h2>
      </div>

      <div class="p-6">
        <div v-if="approvedClasses.length === 0" class="text-center py-12 text-gray-500">
          <svg class="w-12 h-12 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path></svg>
          <p class="text-lg font-medium">您当前没有任何班级的数据访问权限</p>
          <p class="text-sm mt-1">请在上方提交申请，或等待管理员审批您已提交的申请。</p>
        </div>

        <div v-else class="space-y-6">
          <div class="flex items-center gap-4 bg-emerald-50 p-4 rounded-lg border border-emerald-100">
            <span class="font-bold text-emerald-800">当前管理的班级：</span>
            <select v-model="selectedClass" @change="fetchStudents" class="shadow-sm border border-emerald-200 rounded-lg py-1.5 px-3 text-emerald-700 bg-white focus:outline-none focus:ring-2 focus:ring-emerald-300 font-medium cursor-pointer">
              <option v-for="cls in approvedClasses" :key="cls" :value="cls">{{ cls }}</option>
            </select>
          </div>

          <div class="border rounded-lg overflow-hidden">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
              <tr>
                <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">学号</th>
                <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">姓名</th>
                <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">班级</th>
                <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">竞赛基础分</th>
                <th class="py-3 px-4 text-center text-xs font-bold text-gray-500 uppercase">操作</th>
              </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="student in currentStudents" :key="student.id" class="hover:bg-gray-50 transition">
                <td class="py-3 px-4 text-sm font-medium text-gray-900">{{ student.id }}</td>
                <td class="py-3 px-4 text-sm">{{ student.name }}</td>
                <td class="py-3 px-4 text-sm text-gray-500">{{ student.className }}</td>
                <td class="py-3 px-4 text-sm">
                  <input v-if="editingId === student.id" v-model="tempScore" type="number" class="border border-blue-300 rounded px-2 py-1 w-20 focus:ring focus:border-blue-400 outline-none">
                  <span v-else class="font-bold" :class="student.baseScore > 0 ? 'text-green-600' : 'text-gray-400'">{{ student.baseScore || 0 }}</span>
                </td>
                <td class="py-3 px-4 text-center">
                  <div v-if="editingId === student.id" class="flex justify-center gap-3">
                    <button @click="saveScore(student)" class="text-green-500 hover:text-green-700 transition"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg></button>
                    <button @click="editingId = null" class="text-gray-400 hover:text-gray-600 transition"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg></button>
                  </div>
                  <button v-else @click="startEdit(student)" class="text-blue-500 hover:text-blue-700 text-sm font-medium transition flex items-center justify-center gap-1 mx-auto">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg> 录入分数
                  </button>
                </td>
              </tr>
              <tr v-if="currentStudents.length === 0">
                <td colspan="5" class="py-8 text-center text-gray-500">该班级暂无学生数据。</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 我的申请历史记录 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
        <div class="flex items-center gap-2">
          <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
          <h2 class="text-lg font-bold text-gray-800">我的申请记录</h2>
        </div>
        <button @click="fetchMyApplications" class="text-sm text-blue-500 hover:text-blue-700 transition flex items-center">
          <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path></svg>
          刷新状态
        </button>
      </div>

      <div class="p-0">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-white">
          <tr>
            <th class="py-3 px-6 text-left text-xs font-bold text-gray-500 uppercase">申请班级</th>
            <th class="py-3 px-6 text-left text-xs font-bold text-gray-500 uppercase">理由</th>
            <th class="py-3 px-6 text-left text-xs font-bold text-gray-500 uppercase">申请时间</th>
            <th class="py-3 px-6 text-left text-xs font-bold text-gray-500 uppercase">状态</th>
          </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
          <tr v-for="app in myApplications" :key="app.id" class="hover:bg-gray-50">
            <td class="py-3 px-6 text-sm font-bold text-gray-800">{{ app.targetClass }}</td>
            <td class="py-3 px-6 text-sm text-gray-500 max-w-xs truncate" :title="app.reason">{{ app.reason }}</td>
            <td class="py-3 px-6 text-sm text-gray-500">{{ app.applyTime.replace('T', ' ').substring(0, 16) }}</td>
            <td class="py-3 px-6">
              <span class="px-2.5 py-1 text-xs font-bold rounded-full" :class="getStatusClass(app.status)">{{ getStatusText(app.status) }}</span>
            </td>
          </tr>
          <tr v-if="myApplications.length === 0">
            <td colspan="4" class="py-6 text-center text-gray-500 text-sm">暂无申请记录</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'

const userStr = localStorage.getItem('currentUser')
const currentUser = userStr ? JSON.parse(userStr) : null

const isApplying = ref(false)
const myApplications = ref([])
const currentStudents = ref([])

const permForm = reactive({ targetClass: '', reason: '' })

const editingId = ref(null)
const tempScore = ref(0)

const approvedClasses = computed(() => {
  return myApplications.value.filter(app => app.status === 'approved').map(app => app.targetClass)
})
const selectedClass = ref('')

const fetchMyApplications = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/permission/my', { params: { teacherId: currentUser.id } })
    myApplications.value = res.data
    if (approvedClasses.value.length > 0 && !selectedClass.value) {
      selectedClass.value = approvedClasses.value[0]
      fetchStudents()
    }
  } catch (error) {
    console.error("拉取申请记录失败", error)
  }
}

onMounted(() => { if (currentUser) fetchMyApplications() })

const submitApplication = async () => {
  isApplying.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/permission/submit', {
      teacherId: currentUser.id, targetClass: permForm.targetClass, reason: permForm.reason
    })
    if (res.data.success) {
      alert("申请提交成功！")
      permForm.targetClass = ''
      permForm.reason = ''
      fetchMyApplications()
    }
  } catch (error) {
    alert("网络连接失败，请检查后端！")
  } finally {
    isApplying.value = false
  }
}

const fetchStudents = async () => {
  if (!selectedClass.value) return
  try {
    const res = await axios.get('http://localhost:8080/api/user/byClass', { params: { className: selectedClass.value } })
    currentStudents.value = res.data
  } catch (error) {
    console.error("拉取学生名单失败", error)
  }
}

const startEdit = (student) => {
  editingId.value = student.id
  tempScore.value = student.baseScore || 0
}

const saveScore = async (student) => {
  try {
    const res = await axios.post('http://localhost:8080/api/user/updateScore', { studentId: student.id, score: tempScore.value })
    if (res.data.success) {
      student.baseScore = Number(tempScore.value)
      editingId.value = null
    } else {
      alert("保存失败")
    }
  } catch (error) {
    alert("网络错误")
  }
}

const getStatusText = (status) => {
  const map = { 'pending': '待审批', 'approved': '已授权', 'rejected': '已驳回' }
  return map[status] || status
}
const getStatusClass = (status) => {
  const map = { 'pending': 'bg-yellow-100 text-yellow-700', 'approved': 'bg-emerald-100 text-emerald-700', 'rejected': 'bg-red-100 text-red-700' }
  return map[status] || 'bg-gray-100 text-gray-600'
}
</script>