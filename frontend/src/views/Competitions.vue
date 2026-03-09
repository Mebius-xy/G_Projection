<template>
  <div class="pb-10">
    <!-- 顶部搜索与分类栏 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-xl shadow-sm border border-gray-100">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center text-xl border border-blue-100 shadow-inner">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">竞赛大厅</h2>
          <p class="text-xs text-gray-500 mt-0.5">发现并报名参与全校各类学科竞赛</p>
        </div>
      </div>

      <!-- 搜索与分类（智能漏斗） -->
      <div class="flex gap-3">
        <div class="relative">
          <svg class="absolute left-3 top-2.5 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
          <input v-model="searchQuery" type="text" placeholder="搜索竞赛名称..." class="pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-300 w-64 bg-gray-50 text-sm transition">
        </div>
        <select v-model="selectedCategory" class="px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-300 bg-gray-50 cursor-pointer text-sm transition">
          <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
        </select>
      </div>
    </div>

    <!-- ================== 🌟 核心亮点：骨架屏加载动画 (Skeleton Screen) 🌟 ================== -->
    <div v-if="isLoading" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
      <div v-for="i in 6" :key="i" class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden flex flex-col h-[260px]">
        <div class="h-2 w-full bg-gray-200 animate-pulse"></div>
        <div class="p-6 flex-1 flex flex-col">
          <div class="flex justify-between items-start mb-4">
            <div class="w-16 h-6 bg-gray-200 rounded-full animate-pulse"></div>
            <div class="w-12 h-4 bg-gray-100 rounded-full animate-pulse"></div>
          </div>
          <div class="w-3/4 h-6 bg-gray-200 rounded mb-3 animate-pulse"></div>
          <div class="w-full h-4 bg-gray-100 rounded mb-2 animate-pulse"></div>
          <div class="w-5/6 h-4 bg-gray-100 rounded mb-6 animate-pulse"></div>
          <div class="w-1/2 h-8 bg-gray-50 rounded mb-5 animate-pulse"></div>
          <div class="mt-auto w-full h-11 bg-gray-200 rounded-lg animate-pulse"></div>
        </div>
      </div>
    </div>

    <!-- 无数据提示 -->
    <div v-else-if="filteredCompetitions.length === 0" class="text-center py-24 bg-white rounded-2xl shadow-sm border border-gray-100">
      <svg class="w-20 h-20 mx-auto mb-4 text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path></svg>
      <p class="text-gray-500 text-lg font-medium">没有找到符合条件的竞赛</p>
    </div>

    <!-- 真实竞赛卡片列表 -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
      <div v-for="comp in filteredCompetitions" :key="comp.id" class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden transform hover:-translate-y-1 hover:shadow-lg transition duration-300 flex flex-col group">
        <div class="h-2 w-full transition-colors duration-500" :class="getCategoryColor(comp.category)"></div>

        <div class="p-6 flex-1 flex flex-col relative">
          <div class="flex justify-between items-start mb-4">
            <span class="px-3 py-1 text-[11px] rounded-full font-bold border tracking-wider" :class="getCategoryBadgeClass(comp.category)">
              {{ comp.category }}
            </span>
            <span v-if="comp.status === 1" class="text-green-500 text-xs font-bold flex items-center"><span class="w-1.5 h-1.5 rounded-full bg-green-500 mr-1.5 animate-pulse"></span>报名中</span>
            <span v-else class="text-gray-400 text-xs font-bold flex items-center"><span class="w-1.5 h-1.5 rounded-full bg-gray-400 mr-1.5"></span>已截止</span>
          </div>

          <h3 class="text-lg font-bold text-gray-800 mb-2 leading-tight group-hover:text-blue-600 transition">{{ comp.title }}</h3>
          <p class="text-sm text-gray-500 mb-4 flex-1 line-clamp-3 leading-relaxed">{{ comp.description }}</p>

          <div class="text-sm text-gray-600 mb-5 bg-gray-50 p-2.5 rounded-lg flex items-center border border-gray-100">
            <svg class="w-4 h-4 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
            截止日期: <span class="font-bold ml-1 font-mono">{{ comp.deadline }}</span>
          </div>

          <!-- 🌟 核心修改：动态识别按钮状态 (已报名/待审核/直接通过) 🌟 -->
          <template v-if="currentUser.role === 'student'">

            <button v-if="myAppStatusMap[comp.id] === 'pending'" disabled class="w-full py-2.5 rounded-lg font-bold flex items-center justify-center gap-2 shadow-inner bg-yellow-50 text-yellow-600 border border-yellow-200 cursor-wait">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
              资格审核中...
            </button>

            <button v-else-if="myAppStatusMap[comp.id] === 'approved'" disabled class="w-full py-2.5 rounded-lg font-bold flex items-center justify-center gap-2 shadow-inner bg-green-50 text-green-600 border border-green-200">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
              报名成功 (已获参赛资格)
            </button>

            <button v-else-if="myAppStatusMap[comp.id] === 'rejected'" disabled class="w-full py-2.5 rounded-lg font-bold flex items-center justify-center gap-2 shadow-inner bg-red-50 text-red-600 border border-red-200">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
              报名被驳回
            </button>

            <button v-else @click="openApplyModal(comp)" :disabled="comp.status === 0" class="w-full py-2.5 rounded-lg font-bold transition flex items-center justify-center gap-2 shadow-sm" :class="comp.status === 1 ? 'bg-blue-600 hover:bg-blue-700 text-white' : 'bg-gray-100 text-gray-400 cursor-not-allowed'">
              <svg v-if="comp.status === 1" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
              {{ comp.status === 1 ? '立即报名' : '报名已结束' }}
            </button>
          </template>

          <template v-else>
            <button disabled class="w-full py-2.5 rounded-lg font-bold transition flex items-center justify-center gap-2 bg-gray-50 text-gray-400 cursor-not-allowed border border-gray-100">
              仅限学生操作
            </button>
          </template>
        </div>
      </div>
    </div>

    <!-- 报名弹窗 (支持免检表单) -->
    <div v-if="showApplyModal" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm flex items-center justify-center z-[100]">
      <div class="bg-white rounded-2xl shadow-2xl w-[550px] max-h-[90vh] flex flex-col overflow-hidden animate-fade-in-up border border-gray-100">

        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800">
            竞赛报名 <span class="text-blue-600 ml-1 text-sm font-normal">({{ currentApplyingComp.category }})</span>
          </h3>
          <button @click="showApplyModal = false" class="text-gray-400 hover:text-red-500 transition hover:bg-red-50 w-8 h-8 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <div class="p-6 overflow-y-auto flex-1">
          <div class="mb-5 bg-blue-50 p-4 rounded-xl text-sm border border-blue-100 text-blue-800 shadow-inner">
            <strong class="flex items-center gap-1.5"><svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>正在报名：</strong>
            <span class="mt-1 block font-bold text-lg text-blue-900">{{ currentApplyingComp.title }}</span>
          </div>

          <!-- 🌟 新增：如果是免检通道，给个帅气的提示，并且隐藏后面的填写项 🌟 -->
          <div v-if="currentApplyingComp.needReview === 0" class="mb-5 bg-green-50 border border-green-200 rounded-xl p-5 text-center">
            <div class="w-12 h-12 bg-green-100 text-green-500 rounded-full flex items-center justify-center mx-auto mb-3">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            </div>
            <h4 class="font-bold text-green-800 text-lg mb-1">免审核报名通道</h4>
            <p class="text-green-600 text-sm">该竞赛无需提交作品和证明材料，确认报名形式后即可直接获得参赛资格！</p>
          </div>

          <div class="mb-5 bg-gray-50 p-5 rounded-xl border border-gray-200">
            <label class="block text-gray-700 text-sm font-bold mb-3">请选择报名形式 <span class="text-red-500">*</span></label>
            <div class="flex gap-6">
              <label class="cursor-pointer flex items-center gap-2 group">
                <input type="radio" v-model="applyForm.applyType" value="individual" class="text-blue-600 focus:ring-blue-500 w-4 h-4">
                <span class="text-sm font-medium group-hover:text-blue-600 transition">个人独立报名</span>
              </label>
              <label class="cursor-pointer flex items-center gap-2 group">
                <input type="radio" v-model="applyForm.applyType" value="team" class="text-blue-600 focus:ring-blue-500 w-4 h-4">
                <span class="text-sm font-medium text-purple-600 group-hover:text-purple-800 transition">以队伍名义报名 (我是队长)</span>
              </label>
            </div>

            <div v-if="applyForm.applyType === 'team'" class="mt-4 animate-fade-in-up border-t border-gray-200 pt-4">
              <div v-if="myCaptainTeams.length > 0">
                <label class="block text-gray-600 text-xs mb-1.5 font-bold uppercase tracking-wider">选择出战队伍</label>
                <select v-model="applyForm.selectedTeamId" class="w-full border border-gray-300 rounded-lg p-2.5 text-sm shadow-sm focus:border-purple-500 focus:ring-2 focus:ring-purple-200 outline-none bg-white cursor-pointer">
                  <option disabled value="">-- 请选择你要带领的队伍 --</option>
                  <option v-for="team in myCaptainTeams" :key="team.id" :value="team.id">{{ team.name }} ({{ team.memberCount }}人)</option>
                </select>
              </div>
              <div v-else class="text-xs text-red-500 flex items-center bg-red-50 p-2 rounded border border-red-100">
                <svg class="w-3.5 h-3.5 mr-1 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path></svg>
                您目前还没有担任队长的队伍，请先去个人中心创建队伍。
              </div>
            </div>
          </div>

          <!-- 🌟 如果需要审核，才显示这些繁琐的表单 🌟 -->
          <template v-if="currentApplyingComp.needReview !== 0">
            <div class="mb-5">
              <label class="block text-gray-700 text-sm font-bold mb-2">参赛宣言 / 个人优势简述 <span class="text-red-500">*</span></label>
              <textarea v-model="applyForm.statement" class="shadow-sm border border-gray-200 rounded-xl w-full py-3 px-4 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition leading-relaxed" rows="3" placeholder="简单说说你为什么参加这个比赛..."></textarea>
            </div>

            <div v-if="currentApplyingComp.category === '数学类'" class="mb-5 p-5 bg-blue-50/50 rounded-xl border border-blue-100">
              <h4 class="font-bold text-blue-800 mb-3 text-sm flex items-center">
                <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path></svg>
                数学类竞赛补充信息
              </h4>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-gray-700 text-xs font-bold mb-1">所在班级 <span class="text-red-500">*</span></label>
                  <input v-model="applyForm.className" type="text" class="shadow-sm border border-gray-200 rounded-lg w-full py-2 px-3 text-sm focus:ring-2 focus:ring-blue-300 outline-none" placeholder="例如：计科2204班">
                </div>
                <div>
                  <label class="block text-gray-700 text-xs font-bold mb-1">高数/线代平均成绩 <span class="text-red-500">*</span></label>
                  <input v-model="applyForm.mathScore" type="number" class="shadow-sm border border-gray-200 rounded-lg w-full py-2 px-3 text-sm focus:ring-2 focus:ring-blue-300 outline-none" placeholder="0-100">
                </div>
              </div>
            </div>

            <div v-if="currentApplyingComp.category === '计算机类'" class="mb-5 p-5 bg-gray-50 rounded-xl border border-gray-200">
              <h4 class="font-bold text-gray-800 mb-3 text-sm flex items-center">
                <svg class="w-4 h-4 mr-1.5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4"></path></svg>
                源代码提交 <span class="text-red-500 ml-1">*</span>
              </h4>
              <label class="block text-gray-500 text-xs mb-3">请上传您的项目源代码 (仅限 .zip, .rar)</label>
              <input type="file" @change="handleFileUpload" accept=".zip,.rar" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-bold file:bg-white file:border file:border-gray-200 file:text-gray-700 hover:file:bg-gray-50 cursor-pointer transition"/>
              <div v-if="isUploading" class="mt-3 flex items-center text-xs text-blue-600 font-bold bg-blue-50 px-3 py-1.5 rounded w-max">
                <svg class="animate-spin h-3.5 w-3.5 mr-1.5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                文件拼命搬运中...
              </div>
              <p v-if="applyForm.fileUrl" class="text-xs text-green-600 mt-3 font-bold flex items-center"><svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>附件已安全到达仓库</p>
            </div>

            <div v-if="currentApplyingComp.category === '艺术类'" class="mb-5 p-5 bg-pink-50 rounded-xl border border-pink-100">
              <h4 class="font-bold text-pink-800 mb-3 text-sm flex items-center">
                <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01"></path></svg>
                艺术作品上传 <span class="text-red-500 ml-1">*</span>
              </h4>
              <label class="block text-pink-600/70 text-xs mb-3">请上传设计图或视频作品</label>
              <input type="file" @change="handleFileUpload" accept="image/*,video/mp4" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-bold file:bg-white file:text-pink-700 hover:file:bg-pink-50 cursor-pointer transition shadow-sm"/>
              <div v-if="isUploading" class="mt-3 flex items-center text-xs text-pink-600 font-bold bg-pink-100 px-3 py-1.5 rounded w-max">
                <svg class="animate-spin h-3.5 w-3.5 mr-1.5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                艺术作品搬运中...
              </div>
              <p v-if="applyForm.fileUrl" class="text-xs text-green-600 mt-3 font-bold flex items-center"><svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>作品已就绪</p>
            </div>
          </template>
        </div>

        <div class="p-5 border-t border-gray-100 bg-gray-50 flex justify-end gap-3 shrink-0">
          <button @click="showApplyModal = false" class="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 rounded-xl hover:bg-gray-100 transition shadow-sm font-bold text-sm">取消操作</button>
          <button @click="submitApplication" :disabled="isSubmitting" class="px-6 py-2.5 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition shadow-md font-bold flex items-center gap-2 text-sm disabled:opacity-50">
            <svg v-if="isSubmitting" class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
            {{ currentApplyingComp.needReview === 0 ? '一键确认报名' : '提交审核材料' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'
import { toast } from '../utils/toast'

const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

const competitions = ref([])
const myApplications = ref([])
const isLoading = ref(true)
const isSubmitting = ref(false)
const myCaptainTeams = ref([])

const searchQuery = ref('')
const selectedCategory = ref('全部')
const categories = ['全部', '数学类', '计算机类', '艺术类', '人文学科类', '其他']

const showApplyModal = ref(false)
const currentApplyingComp = ref(null)
const isUploading = ref(false)
const applyForm = reactive({
  applyType: 'individual', selectedTeamId: '', statement: '', className: '', mathScore: '', fileUrl: ''
})

// 并发请求：获取系统所有比赛 + 自己报名的比赛记录
const fetchInitialData = async () => {
  try {
    const reqs = [axios.get('http://localhost:8080/api/competition/list')]
    if (currentUser.value.role === 'student' && currentUser.value.id) {
      reqs.push(axios.get(`http://localhost:8080/api/application/my?studentId=${currentUser.value.id}`))
    }

    const responses = await Promise.all(reqs)
    competitions.value = responses[0].data
    if (responses[1]) {
      myApplications.value = responses[1].data
    }
  } catch (error) {
    toast.error("数据加载失败，请检查网络！")
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  setTimeout(fetchInitialData, 600) // 让骨架屏加载稍微展示一会，体验更好
})

// 🌟 核心：制作一个字典，方便在大厅里快速识别该比赛我到底报没报，状态是啥
const myAppStatusMap = computed(() => {
  const map = {}
  myApplications.value.forEach(app => {
    map[app.compId] = app.status
  })
  return map
})

const filteredCompetitions = computed(() => {
  return competitions.value.filter(comp => {
    const matchCategory = selectedCategory.value === '全部' || comp.category === selectedCategory.value
    const keyword = searchQuery.value.toLowerCase()
    const matchSearch = comp.title.toLowerCase().includes(keyword) || (comp.description && comp.description.toLowerCase().includes(keyword))
    return matchCategory && matchSearch
  })
})

const getCategoryColor = (category) => {
  const map = { '数学类': 'bg-blue-500', '计算机类': 'bg-indigo-500', '艺术类': 'bg-pink-500', '人文学科类': 'bg-yellow-500' }
  return map[category] || 'bg-gray-400'
}
const getCategoryBadgeClass = (category) => {
  const map = { '数学类': 'bg-blue-50 text-blue-700 border-blue-200', '计算机类': 'bg-indigo-50 text-indigo-700 border-indigo-200', '艺术类': 'bg-pink-50 text-pink-700 border-pink-200', '人文学科类': 'bg-yellow-50 text-yellow-700 border-yellow-200' }
  return map[category] || 'bg-gray-50 text-gray-600 border-gray-200'
}

const handleFileUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  isUploading.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.success) {
      applyForm.fileUrl = res.data.url
      toast.success("文件上传成功！")
    } else { toast.error("上传失败：" + res.data.message) }
  } catch (error) {
    toast.error("文件服务器异常！如果是大文件请确认 Spring Boot 配置是否正确。")
  } finally { isUploading.value = false }
}

const openApplyModal = async (comp) => {
  currentApplyingComp.value = comp
  applyForm.applyType = 'individual'
  applyForm.selectedTeamId = ''
  applyForm.statement = ''
  applyForm.className = ''
  applyForm.mathScore = ''
  applyForm.fileUrl = ''

  if (currentUser.value.role === 'student' && currentUser.value.id) {
    try {
      const res = await axios.get(`http://localhost:8080/api/team/my?userId=${currentUser.value.id}`)
      myCaptainTeams.value = res.data.filter(t => t.isCaptain)
    } catch (e) {}
  }
  showApplyModal.value = true
}

const submitApplication = async () => {
  if (applyForm.applyType === 'team' && !applyForm.selectedTeamId) {
    toast.error("队长大人，请选择你要带领出战的队伍！")
    return
  }

  // 如果需要审核，才强制校验宣言和附件
  if (currentApplyingComp.value.needReview !== 0) {
    if (!applyForm.statement) {
      toast.error("参赛宣言总得写点什么吧~")
      return
    }
    if ((currentApplyingComp.value.category === '计算机类' || currentApplyingComp.value.category === '艺术类') && !applyForm.fileUrl) {
      toast.error("这个比赛必须要交附件作品哦，快去上传！")
      return
    }
  }

  if (!currentUser.value.id) return

  let finalStatement = applyForm.statement || "免审核直接报名"
  if (currentApplyingComp.value.needReview !== 0 && currentApplyingComp.value.category === '数学类') {
    finalStatement += `\n【附加信息】班级：${applyForm.className}，高数/线代均分：${applyForm.mathScore}`
  }

  isSubmitting.value = true
  try {
    const response = await axios.post('http://localhost:8080/api/application/submit', {
      studentId: currentUser.value.id,
      compId: currentApplyingComp.value.id,
      teamId: applyForm.applyType === 'team' ? applyForm.selectedTeamId : null,
      statement: finalStatement,
      fileUrl: applyForm.fileUrl
    })

    if (response.data.success) {
      toast.success(currentApplyingComp.value.needReview === 0 ? `报名成功！您已获得【${currentApplyingComp.value.title}】的参赛资格！` : `申请提交成功！请耐心等待教师审核。`)
      showApplyModal.value = false
      fetchInitialData() // 🌟 重新拉取数据，让按钮状态立刻更新！
    } else {
      toast.error(response.data.message)
    }
  } catch (error) {
    toast.error("网络连接失败，报名表没发出去！")
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
@keyframes fadeInUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
.animate-fade-in-up { animation: fadeInUp 0.3s ease-out forwards; }
</style>