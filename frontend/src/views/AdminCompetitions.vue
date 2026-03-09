<template>
  <div class="pb-10">
    <!-- 头部横幅 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-xl shadow-sm border border-gray-100">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 bg-purple-50 text-purple-600 rounded-xl flex items-center justify-center text-xl border border-purple-100">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">竞赛项目发布与管理</h2>
          <p class="text-sm text-gray-500 mt-0.5">面向全校发布各类学科竞赛、选拔赛与考试通知</p>
        </div>
      </div>
      <button @click="openModal()" class="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2.5 px-5 rounded-lg shadow-md transition-all flex items-center gap-2">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path></svg>
        发布新竞赛
      </button>
    </div>

    <!-- 列表 -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">竞赛名称</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">类别</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">报名模式</th>
          <th class="py-3 px-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">状态</th>
          <th class="py-3 px-4 text-right text-xs font-bold text-gray-500 uppercase tracking-wider">操作</th>
        </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
        <tr v-for="comp in competitions" :key="comp.id" class="hover:bg-gray-50">
          <td class="py-3 px-4 font-bold text-gray-800">{{ comp.title }}</td>
          <td class="py-3 px-4 text-sm text-gray-600">{{ comp.category }}</td>
          <td class="py-3 px-4">
            <span v-if="comp.needReview === 0" class="text-xs font-bold text-green-600 bg-green-50 px-2 py-1 rounded">直接报名</span>
            <span v-else class="text-xs font-bold text-yellow-600 bg-yellow-50 px-2 py-1 rounded">需教师审核</span>
          </td>
          <td class="py-3 px-4">
            <span v-if="comp.status === 1" class="text-green-500 font-bold text-xs">进行中</span>
            <span v-else class="text-gray-400 font-bold text-xs">已结束</span>
          </td>
          <td class="py-3 px-4 text-right">
            <button @click="toggleStatus(comp)" class="text-xs font-bold px-3 py-1.5 rounded transition" :class="comp.status === 1 ? 'bg-red-50 text-red-600 hover:bg-red-100' : 'bg-green-50 text-green-600 hover:bg-green-100'">
              {{ comp.status === 1 ? '截止报名' : '重新开启' }}
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 发布弹窗 -->
    <div v-if="showModal" class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl shadow-2xl w-[500px] overflow-hidden animate-fade-in-up">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="font-bold text-gray-800">发布新竞赛</h3>
        </div>
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">竞赛名称</label>
            <input v-model="form.title" class="w-full border rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-purple-300">
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-1">竞赛分类</label>
              <select v-model="form.category" class="w-full border rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-purple-300 bg-white">
                <option>数学类</option>
                <option>计算机类</option>
                <option>艺术类</option>
                <option>人文学科类</option>
                <option>其他</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-1">截止日期</label>
              <input type="date" v-model="form.deadline" class="w-full border rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-purple-300">
            </div>
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">详情描述</label>
            <textarea v-model="form.description" rows="3" class="w-full border rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-purple-300"></textarea>
          </div>

          <!-- 🌟 新增：报名模式选择 🌟 -->
          <div class="bg-purple-50 p-4 rounded-xl border border-purple-100">
            <label class="block text-sm font-bold text-purple-900 mb-2">设置报名/审核模式</label>
            <div class="space-y-2">
              <label class="flex items-center gap-2 cursor-pointer">
                <input type="radio" v-model="form.needReview" :value="1" class="text-purple-600 focus:ring-purple-500">
                <span class="text-sm font-medium text-gray-700">【选拔赛模式】学生需提交作品/宣言，教师审核后方可参赛</span>
              </label>
              <label class="flex items-center gap-2 cursor-pointer">
                <input type="radio" v-model="form.needReview" :value="0" class="text-purple-600 focus:ring-purple-500">
                <span class="text-sm font-medium text-gray-700">【免检通配模式】无需审核，无需提交作品，学生一键报名即成功</span>
              </label>
            </div>
          </div>

        </div>
        <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="showModal = false" class="px-5 py-2 bg-white border rounded-lg shadow-sm font-medium">取消</button>
          <button @click="submitForm" class="px-5 py-2 bg-purple-600 text-white rounded-lg shadow-md font-bold hover:bg-purple-700">确认发布</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { toast } from '../utils/toast'

const competitions = ref([])
const showModal = ref(false)
const form = reactive({ title: '', category: '计算机类', description: '', deadline: '', needReview: 1 })

const fetchComps = async () => {
  const res = await axios.get('http://localhost:8080/api/competition/list')
  competitions.value = res.data
}

onMounted(() => fetchComps())

const openModal = () => {
  form.title = ''
  form.description = ''
  form.deadline = ''
  form.needReview = 1 // 默认需要审核
  showModal.value = true
}

const submitForm = async () => {
  try {
    const res = await axios.post('http://localhost:8080/api/competition/add', {
      ...form, status: 1
    })
    if(res.data.success) {
      toast.success('发布成功！')
      showModal.value = false
      fetchComps()
    }
  } catch(e) { toast.error('发布失败') }
}

const toggleStatus = async (comp) => {
  try {
    const newStatus = comp.status === 1 ? 0 : 1
    await axios.post('http://localhost:8080/api/competition/update', { ...comp, status: newStatus })
    fetchComps()
  } catch(e) {}
}
</script>

<style scoped>
@keyframes fadeInUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
.animate-fade-in-up { animation: fadeInUp 0.3s ease-out forwards; }
</style>