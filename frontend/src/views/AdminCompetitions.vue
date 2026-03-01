<template>
  <div>
    <!-- 头部栏 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-purple-100 text-purple-600 rounded-full flex items-center justify-center text-xl">
          <i class="fas fa-cogs"></i>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">竞赛项目管理</h2>
          <p class="text-xs text-gray-500">发布新竞赛，修改或下架历史竞赛</p>
        </div>
      </div>
      <button @click="openModal('add')" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-lg shadow-sm transition flex items-center gap-2">
        <i class="fas fa-plus"></i> 发布新竞赛
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="isLoading" class="text-center py-20 text-purple-500">
      <i class="fas fa-spinner fa-spin text-4xl mb-4"></i>
    </div>

    <!-- 竞赛列表表格 -->
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
          <td class="py-3 px-4 text-sm text-gray-500"><i class="far fa-calendar-alt mr-1"></i>{{ comp.deadline }}</td>
          <td class="py-3 px-4">
            <span v-if="comp.status === 1" class="text-green-500 text-xs font-bold px-2 py-1 bg-green-50 border border-green-200 rounded-full"><i class="fas fa-circle text-[8px] mr-1"></i>进行中</span>
            <span v-else class="text-gray-400 text-xs font-bold px-2 py-1 bg-gray-50 border border-gray-200 rounded-full"><i class="fas fa-circle text-[8px] mr-1"></i>已结束</span>
          </td>
          <td class="py-3 px-4 text-center">
            <button @click="openModal('edit', comp)" class="text-blue-500 hover:text-blue-700 transition mr-3" title="编辑"><i class="fas fa-edit"></i></button>
            <button @click="toggleStatus(comp)" class="text-yellow-500 hover:text-yellow-700 transition mr-3" :title="comp.status === 1 ? '提前结束' : '重新开启'">
              <i :class="comp.status === 1 ? 'fas fa-pause-circle' : 'fas fa-play-circle'"></i>
            </button>
            <button @click="deleteComp(comp.id)" class="text-red-500 hover:text-red-700 transition" title="删除"><i class="fas fa-trash-alt"></i></button>
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
          <button @click="showModal = false" class="text-gray-400 hover:text-red-500"><i class="fas fa-times text-xl"></i></button>
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
              <i class="fas fa-spinner fa-spin" v-if="isSubmitting"></i>
              <i class="fas fa-save" v-else></i> {{ modalMode === 'add' ? '确认发布' : '保存修改' }}
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

// =========== 弹窗与表单逻辑 ===========
const showModal = ref(false)
const modalMode = ref('add') // 'add' 或 'edit'
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
    // 新增模式，清空表单
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
      fetchCompetitions() // 刷新列表
    }
  } catch (error) {
    alert("保存失败！")
  } finally {
    isSubmitting.value = false
  }
}

// =========== 快捷状态修改与删除 ===========
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
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fadeInUp 0.3s ease-out forwards;
}
</style>