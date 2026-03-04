<template>
  <div>
    <!-- 顶部搜索与分类栏 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center text-xl">
          <!-- 原生SVG: 列表图标 -->
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">竞赛大厅</h2>
          <p class="text-xs text-gray-500">发现并报名参与全校各类学科竞赛</p>
        </div>
      </div>

      <!-- 搜索与分类（智能漏斗） -->
      <div class="flex gap-3">
        <div class="relative">
          <!-- 原生SVG: 搜索图标 -->
          <svg class="absolute left-3 top-2.5 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
          <input v-model="searchQuery" type="text" placeholder="搜索竞赛名称..." class="pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring focus:border-blue-300 w-64 bg-gray-50 text-sm">
        </div>
        <select v-model="selectedCategory" class="px-4 py-2 border rounded-lg focus:outline-none focus:ring focus:border-blue-300 bg-gray-50 cursor-pointer text-sm">
          <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
        </select>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="isLoading" class="text-center py-20 text-blue-500">
      <svg class="animate-spin h-10 w-10 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      <p class="text-sm font-bold">正在从数据库拉取竞赛信息...</p>
    </div>

    <!-- 无数据提示 -->
    <div v-else-if="filteredCompetitions.length === 0" class="text-center py-20 bg-white rounded-lg shadow-sm border border-gray-100">
      <!-- 原生SVG: 空盒子图标 -->
      <svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path></svg>
      <p class="text-gray-500 text-lg">没有找到符合条件的竞赛</p>
    </div>

    <!-- 竞赛卡片列表 -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
      <div v-for="comp in filteredCompetitions" :key="comp.id" class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden transform hover:-translate-y-1 hover:shadow-md transition duration-300 flex flex-col">
        <!-- 卡片头部背景条 -->
        <div class="h-2 w-full" :class="getCategoryColor(comp.category)"></div>

        <div class="p-6 flex-1 flex flex-col">
          <div class="flex justify-between items-start mb-4">
            <span class="px-3 py-1 text-xs rounded-full font-semibold border" :class="getCategoryBadgeClass(comp.category)">
              {{ comp.category }}
            </span>
            <!-- 状态标签 -->
            <span v-if="comp.status === 1" class="text-green-500 text-xs font-bold flex items-center"><span class="w-1.5 h-1.5 rounded-full bg-green-500 mr-1.5"></span>报名中</span>
            <span v-else class="text-gray-400 text-xs font-bold flex items-center"><span class="w-1.5 h-1.5 rounded-full bg-gray-400 mr-1.5"></span>已结束</span>
          </div>

          <h3 class="text-lg font-bold text-gray-800 mb-2 leading-tight">{{ comp.title }}</h3>
          <p class="text-sm text-gray-500 mb-4 flex-1 line-clamp-3">{{ comp.description }}</p>

          <div class="text-sm text-gray-600 mb-5 bg-gray-50 p-2 rounded flex items-center">
            <!-- 原生SVG: 日历图标 -->
            <svg class="w-4 h-4 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
            截止日期: <span class="font-bold ml-1">{{ comp.deadline }}</span>
          </div>

          <!-- 🌟 权限判断：如果不是学生，按钮置灰且提示“仅限学生报名” 🌟 -->
          <template v-if="currentUser.role === 'student'">
            <button @click="openApplyModal(comp)" :disabled="comp.status === 0" class="w-full py-2.5 rounded-lg font-bold transition flex items-center justify-center gap-2" :class="comp.status === 1 ? 'bg-blue-500 hover:bg-blue-600 text-white shadow-sm' : 'bg-gray-100 text-gray-400 cursor-not-allowed'">
              <!-- 原生SVG: 纸飞机 -->
              <svg v-if="comp.status === 1" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
              {{ comp.status === 1 ? '立即报名' : '已截止' }}
            </button>
          </template>
          <template v-else>
            <button disabled class="w-full py-2.5 rounded-lg font-bold transition flex items-center justify-center gap-2 bg-gray-100 text-gray-400 cursor-not-allowed shadow-inner">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path></svg>
              仅限学生身份报名
            </button>
          </template>

        </div>
      </div>
    </div>

    <!-- 报名弹窗 (变形金刚表单) -->
    <div v-if="showApplyModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-2xl w-[550px] max-h-[90vh] flex flex-col overflow-hidden animate-fade-in-up">

        <!-- 弹窗标题栏 -->
        <div class="p-5 border-b flex justify-between items-center bg-gray-50">
          <h3 class="text-lg font-bold text-gray-800">
            竞赛报名 <span class="text-blue-600 ml-1 text-sm font-normal">({{ currentApplyingComp.category }})</span>
          </h3>
          <button @click="showApplyModal = false" class="text-gray-400 hover:text-red-500 transition">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <!-- 弹窗滚动内容区 -->
        <div class="p-6 overflow-y-auto flex-1">
          <div class="mb-5 bg-blue-50 p-3 rounded-lg text-sm border-l-4 border-blue-500 text-blue-800">
            <strong>正在报名：</strong> {{ currentApplyingComp.title }}
          </div>

          <!-- 🌟 选择报名形式 (个人 / 队伍) 🌟 -->
          <div class="mb-5 bg-gray-50 p-4 rounded-lg border border-gray-200">
            <label class="block text-gray-700 text-sm font-bold mb-3">请选择报名形式 <span class="text-red-500">*</span></label>
            <div class="flex gap-4">
              <label class="cursor-pointer flex items-center gap-2">
                <input type="radio" v-model="applyForm.applyType" value="individual" class="text-blue-600 focus:ring-blue-500 w-4 h-4">
                <span class="text-sm font-medium">个人独立报名</span>
              </label>
              <label class="cursor-pointer flex items-center gap-2">
                <input type="radio" v-model="applyForm.applyType" value="team" class="text-blue-600 focus:ring-blue-500 w-4 h-4">
                <span class="text-sm font-medium text-purple-600">以队伍名义报名 (我是队长)</span>
              </label>
            </div>

            <!-- 如果选择了队伍报名，且有队伍，则显示下拉框 -->
            <div v-if="applyForm.applyType === 'team'" class="mt-4 animate-fade-in-up">
              <div v-if="myCaptainTeams.length > 0">
                <label class="block text-gray-600 text-xs mb-1">选择出战队伍</label>
                <select v-model="applyForm.selectedTeamId" class="w-full border border-gray-300 rounded-lg p-2.5 text-sm shadow-sm focus:border-blue-500 focus:ring-blue-500 outline-none bg-white">
                  <option disabled value="">-- 请选择你要带领的队伍 --</option>
                  <option v-for="team in myCaptainTeams" :key="team.id" :value="team.id">{{ team.name }} ({{ team.memberCount }}人)</option>
                </select>
              </div>
              <div v-else class="text-xs text-red-500 mt-2 flex items-center">
                <svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path></svg>
                您目前还没有担任队长的队伍，请先去个人中心创建队伍。
              </div>
            </div>
          </div>

          <!-- 【通用字段】参赛宣言 -->
          <div class="mb-5">
            <label class="block text-gray-700 text-sm font-bold mb-2">参赛宣言 / 个人优势简述 <span class="text-red-500">*</span></label>
            <textarea v-model="applyForm.statement" class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition" rows="3" placeholder="简单说说你为什么参加这个比赛..."></textarea>
          </div>

          <!-- 【动态字段：数学类专属】 -->
          <div v-if="currentApplyingComp.category === '数学类'" class="mb-5 p-4 bg-blue-50/50 rounded-lg border border-blue-100">
            <h4 class="font-bold text-blue-800 mb-3 text-sm flex items-center">
              <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path></svg>
              数学类竞赛补充信息
            </h4>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-gray-700 text-xs font-bold mb-1">所在班级 <span class="text-red-500">*</span></label>
                <input v-model="applyForm.className" type="text" class="shadow-sm border rounded w-full py-2 px-3 text-sm focus:ring-2 focus:ring-blue-300 outline-none" placeholder="例如：计科2204班">
              </div>
              <div>
                <label class="block text-gray-700 text-xs font-bold mb-1">高数/线代平均成绩 <span class="text-red-500">*</span></label>
                <input v-model="applyForm.mathScore" type="number" class="shadow-sm border rounded w-full py-2 px-3 text-sm focus:ring-2 focus:ring-blue-300 outline-none" placeholder="0-100">
              </div>
            </div>
          </div>

          <!-- 【🌟 真实文件上传：计算机类专属】 -->
          <div v-if="currentApplyingComp.category === '计算机类'" class="mb-5 p-4 bg-gray-50 rounded-lg border border-gray-200">
            <h4 class="font-bold text-gray-800 mb-3 text-sm flex items-center">
              <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4"></path></svg>
              源代码提交 <span class="text-red-500 ml-1">*</span>
            </h4>
            <label class="block text-gray-700 text-xs mb-2">请上传您的项目源代码 (仅限 .zip, .rar)</label>
            <!-- 绑定 @change 事件触发文件上传 -->
            <input type="file" @change="handleFileUpload" accept=".zip,.rar" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-gray-200 file:text-gray-700 hover:file:bg-gray-300 cursor-pointer"/>
            <p v-if="isUploading" class="text-xs text-blue-500 mt-2 animate-pulse">文件拼命上传中...</p>
            <p v-if="applyForm.fileUrl" class="text-xs text-green-600 mt-2 font-bold flex items-center"><svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>文件已就绪</p>
          </div>

          <!-- 【🌟 真实文件上传：艺术类专属】 -->
          <div v-if="currentApplyingComp.category === '艺术类'" class="mb-5 p-4 bg-pink-50 rounded-lg border border-pink-100">
            <h4 class="font-bold text-pink-800 mb-3 text-sm flex items-center">
              <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01"></path></svg>
              艺术作品上传 <span class="text-red-500 ml-1">*</span>
            </h4>
            <label class="block text-gray-700 text-xs mb-2">请上传设计图或视频作品 (支持 .jpg, .png, .mp4)</label>
            <!-- 绑定 @change 事件触发文件上传 -->
            <input type="file" @change="handleFileUpload" accept="image/*,video/mp4" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-pink-100 file:text-pink-700 hover:file:bg-pink-200 cursor-pointer"/>
            <p v-if="isUploading" class="text-xs text-pink-500 mt-2 animate-pulse">作品搬运中...</p>
            <p v-if="applyForm.fileUrl" class="text-xs text-green-600 mt-2 font-bold flex items-center"><svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>作品已就绪</p>
          </div>
        </div>

        <!-- 弹窗底部操作区 -->
        <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="showApplyModal = false" class="px-5 py-2.5 bg-white border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition shadow-sm font-medium">取消</button>
          <button @click="submitApplication" class="px-5 py-2.5 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition shadow-md font-medium flex items-center gap-2">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
            确认提交报名
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'

// 获取当前登录用户（防止报错，赋予默认值）
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

// 状态变量
const competitions = ref([])
const isLoading = ref(true)
const myCaptainTeams = ref([]) // 存放我当队长的队伍

// 搜索与分类状态
const searchQuery = ref('')
const selectedCategory = ref('全部')
const categories = ['全部', '数学类', '计算机类', '艺术类', '人文学科类', '其他']

// 报名表单相关
const showApplyModal = ref(false)
const currentApplyingComp = ref(null)
const isUploading = ref(false) // 🌟 上传文件加载动画状态
const applyForm = reactive({
  applyType: 'individual', // 'individual' 或 'team'
  selectedTeamId: '',      // 选中的队伍ID
  statement: '',
  className: '',
  mathScore: '',
  fileUrl: '' // 🌟 新增：用来存后端返回的文件访问地址（提货单）
})

/**
 * 页面一加载，就呼叫传菜员去后厨拿菜单
 */
onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/competition/list')
    competitions.value = res.data
  } catch (error) {
    console.error("获取竞赛列表失败:", error)
  } finally {
    isLoading.value = false
  }
})

// 计算属性：智能漏斗过滤器
const filteredCompetitions = computed(() => {
  return competitions.value.filter(comp => {
    // 1. 分类匹配
    const matchCategory = selectedCategory.value === '全部' || comp.category === selectedCategory.value
    // 2. 关键词匹配 (搜索标题和描述)
    const keyword = searchQuery.value.toLowerCase()
    const matchSearch = comp.title.toLowerCase().includes(keyword) || comp.description.toLowerCase().includes(keyword)

    return matchCategory && matchSearch
  })
})

// 样式小工具
const getCategoryColor = (category) => {
  const map = { '数学类': 'bg-blue-500', '计算机类': 'bg-indigo-500', '艺术类': 'bg-pink-500', '人文学科类': 'bg-yellow-500' }
  return map[category] || 'bg-gray-400'
}
const getCategoryBadgeClass = (category) => {
  const map = { '数学类': 'bg-blue-50 text-blue-600 border-blue-200', '计算机类': 'bg-indigo-50 text-indigo-600 border-indigo-200', '艺术类': 'bg-pink-50 text-pink-600 border-pink-200', '人文学科类': 'bg-yellow-50 text-yellow-600 border-yellow-200' }
  return map[category] || 'bg-gray-50 text-gray-600 border-gray-200'
}

// 🌟 真实文件上传核心逻辑 🌟
const handleFileUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 创建一个 FormData 对象（相当于一个虚拟的包裹，用来装文件）
  const formData = new FormData()
  formData.append('file', file)

  isUploading.value = true
  try {
    // 把包裹发给后端的 FileController
    const res = await axios.post('http://localhost:8080/api/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    if (res.data.success) {
      // 成功后，把后端返回的文件网址存起来，一会和表单一起提交！
      applyForm.fileUrl = res.data.url
    } else {
      alert("上传失败：" + res.data.message)
    }
  } catch (error) {
    alert("文件服务器异常，请检查后端是否正常启动！")
  } finally {
    isUploading.value = false
  }
}

// 打开报名弹窗
const openApplyModal = async (comp) => {
  currentApplyingComp.value = comp
  applyForm.applyType = 'individual'
  applyForm.selectedTeamId = ''
  applyForm.statement = ''
  applyForm.className = ''
  applyForm.mathScore = ''
  applyForm.fileUrl = '' // 打开弹窗时清空之前的文件链接

  // 🌟 打开弹窗时，如果我是学生，顺便去查一下我当队长的队伍有哪些
  if (currentUser.value.role === 'student' && currentUser.value.id) {
    try {
      const res = await axios.get(`http://localhost:8080/api/team/my?userId=${currentUser.value.id}`)
      // 只过滤出 isCaptain 为 true 的队伍
      myCaptainTeams.value = res.data.filter(t => t.isCaptain)
    } catch (e) {
      console.log("获取队伍失败")
    }
  }

  showApplyModal.value = true
}

// 提交报名 (联调真实后端)
const submitApplication = async () => {
  if (applyForm.applyType === 'team' && !applyForm.selectedTeamId) {
    alert("请选择你要带领出战的队伍！")
    return
  }
  if (!applyForm.statement) {
    alert("请填写参赛宣言！")
    return
  }

  // 🌟 强校验：如果比赛分类要求传文件，但用户没传，就不让提交！
  if ((currentApplyingComp.value.category === '计算机类' || currentApplyingComp.value.category === '艺术类') && !applyForm.fileUrl) {
    alert("请先上传相关的作品或代码附件！")
    return
  }

  if (!currentUser.value.id) return

  let finalStatement = applyForm.statement
  if (currentApplyingComp.value.category === '数学类') {
    finalStatement += `\n【附加信息】班级：${applyForm.className}，高数/线代均分：${applyForm.mathScore}`
  }

  try {
    const response = await axios.post('http://localhost:8080/api/application/submit', {
      studentId: currentUser.value.id,
      compId: currentApplyingComp.value.id,
      teamId: applyForm.applyType === 'team' ? applyForm.selectedTeamId : null,
      statement: finalStatement,
      fileUrl: applyForm.fileUrl // 🌟 核心：把文件链接一并提交到报名表数据库中
    })

    if (response.data.success) {
      alert(`报名成功！\n您已成功提交对【${currentApplyingComp.value.title}】的报名。`)
      showApplyModal.value = false
    } else {
      alert(response.data.message)
    }
  } catch (error) {
    alert("网络连接失败，请检查后端！")
  }
}
</script>

<style scoped>
/* 弹窗从下往上浮现的动画 */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fadeInUp 0.3s ease-out forwards;
}
</style>