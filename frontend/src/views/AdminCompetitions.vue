<template>
  <div>
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-purple-100 text-purple-600 rounded-full flex items-center justify-center text-xl">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">竞赛项目管理</h2>
          <p class="text-xs text-gray-500">发布新竞赛，修改或下架历史竞赛</p>
        </div>
      </div>
      <button @click="openModal('add')" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-lg shadow-sm transition flex items-center gap-2">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path></svg> 发布新竞赛
      </button>
    </div>

    <div v-if="isLoading" class="text-center py-20 text-purple-500">
      <svg class="animate-spin h-10 w-10 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
    </div>

    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">竞赛名称</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">学科分类</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">截止日期</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase">状态</th>
          <th class="py-3 px-4 text-center text-xs font-bold text-gray-500 uppercase">操作</th>
        </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
        <tr v-for="comp in competitions" :key="comp.id" class="hover:bg-gray-50 transition">
          <td class="py-3 px-4 text-sm font-bold text-gray-800">{{ comp.title }}</td>
          <td class="py-3 px-4 text-sm"><span class="bg-gray-100 text-gray-600 px-2 py-1 rounded text-xs">{{ comp.category }}</span></td>
          <td class="py-3 px-4 text-sm text-gray-500 flex items-center mt-1">
            <svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
            {{ comp.deadline }}
          </td>
          <td class="py-3 px-4">
            <span v-if="comp.status === 1" class="text-green-500 text-xs font-bold px-2 py-1 bg-green-50 border border-green-200 rounded-full flex items-center w-max"><span class="w-1.5 h-1.5 bg-green-500 rounded-full mr-1.5"></span>进行中</span>
            <span v-else class="text-gray-400 text-xs font-bold px-2 py-1 bg-gray-50 border border-gray-200 rounded-full flex items-center w-max"><span class="w-1.5 h-1.5 bg-gray-400 rounded-full mr-1.5"></span>已结束</span>
          </td>
          <td class="py-3 px-4 text-center">
            <button @click="openModal('edit', comp)" class="text-blue-500 hover:text-blue-700 transition mr-3" title="编辑">
              <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
            </button>
            <button @click="toggleStatus(comp)" class="text-yellow-500 hover:text-yellow-700 transition mr-3" :title="comp.status === 1 ? '提前结束' : '重新开启'">
              <svg v-if="comp.status === 1" class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 9v6m4-6v6m7-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
              <svg v-else class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            </button>
            <button @click="deleteComp(comp.id)" class="text-red-500 hover:text-red-700 transition" title="删除">
              <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
            </button>
          </td>
        </tr>
        <tr v-if="competitions.length === 0">
          <td colspan="5" class="py-12 text-center text-gray-500">暂无竞赛数据</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-2xl w-[550px] overflow-hidden animate-fade-in-up">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800">{{ modalMode === 'add' ? '发布新竞赛' : '编辑竞赛信息' }}</h3>
          <button @click="showModal = false" class="text-gray-400 hover:text-red-500">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <form @submit.prevent="submitForm" class="p-6 space-y-4">
          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">竞赛名称 <span class="text-red-500">*</span></label>
            <input v-model="form.title" type="text" required class="shadow-sm border rounded w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none">
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-gray-700 font-bold mb-2 text-sm">学科分类 <span class="text-red-500">*</span></label>
              <select v-model="form.category" class="shadow-sm border rounded w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none bg-white">
                <option value="数学类">数学类</option>
                <option value="计算机类">计算机类</option>
                <option value="艺术类">艺术类</option>
                <option value="人文学科类">人文学科类</option>
                <option value="其他">其他</option>
              </select>
            </div>
            <div>
              <label class="block text-gray-700 font-bold mb-2 text-sm">报名截止日期 <span class="text-red-500">*</span></label>
              <input v-model="form.deadline" type="date" required class="shadow-sm border rounded w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none">
            </div>
          </div>

          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">竞赛描述与要求 <span class="text-red-500">*</span></label>
            <textarea v-model="form.description" required rows="4" class="shadow-sm border rounded w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none" placeholder="描述越详细越好..."></textarea>
          </div>

          <div class="flex justify-end gap-3 pt-4">
            <button type="button" @click="showModal = false" class="px-5 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition">取消</button>
            <button type="submit" :disabled="isSubmitting" class="px-5 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition flex items-center gap-2">
              <svg v-if="isSubmitting" class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-3m-1 4l-3 3m0 0l-3-3m3 3V4"></path></svg>
              {{ modalMode === 'add' ? '确认发布' : '保存修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

const competitions = ref([])
const isLoading = ref(true)

const fetchCompetitions = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/competition/list')
    competitions.value = res.data
  } catch (error) {
    alert("拉取竞赛列表失败！")
  } finally {
    isLoading.value = false
  }
}

onMounted(() => fetchCompetitions())

const showModal = ref(false)
const modalMode = ref('add')
const isSubmitting = ref(false)

const form = reactive({ id: null, title: '', category: '计算机类', description: '', deadline: '' })

const openModal = (mode, comp = null) => {
  modalMode.value = mode
  if (mode === 'edit' && comp) {
    form.id = comp.id
    form.title = comp.title
    form.category = comp.category
    form.description = comp.description
    form.deadline = comp.deadline
  } else {
    form.id = null
    form.title = ''
    form.category = '计算机类'
    form.description = ''
    form.deadline = ''
  }
  showModal.value = true
}

const submitForm = async () => {
  isSubmitting.value = true
  try {
    const url = modalMode.value === 'add' ? 'http://localhost:8080/api/competition/add' : 'http://localhost:8080/api/competition/update'
    const res = await axios.post(url, form)
    if (res.data.success) {
      showModal.value = false
      fetchCompetitions()
    }
  } catch (error) {
    alert("保存失败！")
  } finally {
    isSubmitting.value = false
  }
}

const toggleStatus = async (comp) => {
  const newStatus = comp.status === 1 ? 0 : 1
  if (!confirm(`确定要将该竞赛标记为“${newStatus === 1 ? '进行中' : '已结束'}”吗？`)) return
  try {
    await axios.post('http://localhost:8080/api/competition/update', { ...comp, status: newStatus })
    fetchCompetitions()
  } catch (e) {
    alert("状态更新失败")
  }
}

const deleteComp = async (id) => {
  if (!confirm('警告：确定要彻底删除该竞赛吗？此操作不可恢复！')) return
  try {
    await axios.post(`http://localhost:8080/api/competition/delete/${id}`)
    fetchCompetitions()
  } catch (e) {
    alert("删除失败")
  }
}
</script>

<style scoped>
@keyframes fadeInUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
.animate-fade-in-up { animation: fadeInUp 0.3s ease-out forwards; }
</style>