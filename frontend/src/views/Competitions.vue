<template>
  <div>
    <!-- 顶部搜索与分类栏 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center text-xl">
          <i class="fas fa-list"></i>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">竞赛大厅</h2>
          <p class="text-xs text-gray-500">发现并报名参与全校各类学科竞赛</p>
        </div>
      </div>

      <!-- 搜索与分类（智能漏斗） -->
      <div class="flex gap-3">
        <div class="relative">
          <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
          <input v-model="searchQuery" type="text" placeholder="搜索竞赛名称..." class="pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring focus:border-blue-300 w-64 bg-gray-50">
        </div>
        <select v-model="selectedCategory" class="px-4 py-2 border rounded-lg focus:outline-none focus:ring focus:border-blue-300 bg-gray-50 cursor-pointer">
          <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
        </select>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="isLoading" class="text-center py-20 text-blue-500">
      <i class="fas fa-spinner fa-spin text-4xl mb-4"></i>
      <p>正在从数据库拉取竞赛信息...</p>
    </div>

    <!-- 无数据提示 -->
    <div v-else-if="filteredCompetitions.length === 0" class="text-center py-20 bg-white rounded-lg shadow-sm border border-gray-100">
      <i class="fas fa-box-open text-6xl text-gray-300 mb-4"></i>
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
            <span v-if="comp.status === 1" class="text-green-500 text-xs font-bold flex items-center"><i class="fas fa-circle text-[8px] mr-1"></i>报名中</span>
            <span v-else class="text-gray-400 text-xs font-bold flex items-center"><i class="fas fa-circle text-[8px] mr-1"></i>已结束</span>
          </div>

          <h3 class="text-lg font-bold text-gray-800 mb-2 leading-tight">{{ comp.title }}</h3>
          <p class="text-sm text-gray-500 mb-4 flex-1 line-clamp-3">{{ comp.description }}</p>

          <div class="text-sm text-gray-600 mb-5 bg-gray-50 p-2 rounded flex items-center">
            <i class="far fa-calendar-alt mr-2 text-blue-500"></i> 截止日期: <span class="font-bold ml-1">{{ comp.deadline }}</span>
          </div>

          <button @click="openApplyModal(comp)" :disabled="comp.status === 0" class="w-full py-2.5 rounded-lg font-bold transition flex items-center justify-center gap-2" :class="comp.status === 1 ? 'bg-blue-500 hover:bg-blue-600 text-white shadow-sm' : 'bg-gray-100 text-gray-400 cursor-not-allowed'">
            <i class="fas fa-paper-plane"></i> {{ comp.status === 1 ? '立即报名' : '已截止' }}
          </button>
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
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>

        <!-- 弹窗滚动内容区 -->
        <div class="p-6 overflow-y-auto flex-1">
          <div class="mb-5 bg-blue-50 p-3 rounded-lg text-sm border-l-4 border-blue-500 text-blue-800">
            <strong>正在报名：</strong> {{ currentApplyingComp.title }}
          </div>

          <!-- 【通用字段】参赛宣言 -->
          <div class="mb-5">
            <label class="block text-gray-700 text-sm font-bold mb-2">参赛宣言 / 个人优势简述 <span class="text-red-500">*</span></label>
            <textarea v-model="applyForm.statement" class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition" rows="3" placeholder="简单说说你为什么参加这个比赛..."></textarea>
          </div>

          <!-- 【动态字段：数学类专属】 -->
          <div v-if="currentApplyingComp.category === '数学类'" class="mb-5 p-4 bg-blue-50/50 rounded-lg border border-blue-100">
            <h4 class="font-bold text-blue-800 mb-3 text-sm"><i class="fas fa-calculator mr-1"></i> 数学类竞赛补充信息</h4>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-gray-700 text-xs font-bold mb-1">所在班级 <span class="text-red-500">*</span></label>
                <input v-model="applyForm.className" type="text" class="shadow-sm border rounded w-full py-2 px-3 text-sm focus:ring-2 focus:ring-blue-300" placeholder="例如：计科2204班">
              </div>
              <div>
                <label class="block text-gray-700 text-xs font-bold mb-1">高数/线代平均成绩 <span class="text-red-500">*</span></label>
                <input v-model="applyForm.mathScore" type="number" class="shadow-sm border rounded w-full py-2 px-3 text-sm focus:ring-2 focus:ring-blue-300" placeholder="0-100">
              </div>
            </div>
          </div>

          <!-- 【动态字段：计算机类专属】 -->
          <div v-if="currentApplyingComp.category === '计算机类'" class="mb-5 p-4 bg-gray-50 rounded-lg border border-gray-200">
            <h4 class="font-bold text-gray-800 mb-3 text-sm"><i class="fas fa-code mr-1"></i> 源代码提交</h4>
            <label class="block text-gray-700 text-xs mb-2">请上传您的项目源代码 (仅限 .zip, .rar)</label>
            <input type="file" accept=".zip,.rar" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-gray-200 file:text-gray-700 hover:file:bg-gray-300 cursor-pointer"/>
          </div>

          <!-- 【动态字段：艺术类专属】 -->
          <div v-if="currentApplyingComp.category === '艺术类'" class="mb-5 p-4 bg-pink-50 rounded-lg border border-pink-100">
            <h4 class="font-bold text-pink-800 mb-3 text-sm"><i class="fas fa-palette mr-1"></i> 艺术作品上传</h4>
            <label class="block text-gray-700 text-xs mb-2">请上传设计图或视频作品 (支持 .jpg, .png, .mp4)</label>
            <input type="file" accept="image/*,video/mp4" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-pink-100 file:text-pink-700 hover:file:bg-pink-200 cursor-pointer"/>
          </div>
        </div>

        <!-- 弹窗底部操作区 -->
        <div class="p-5 border-t bg-gray-50 flex justify-end gap-3">
          <button @click="showApplyModal = false" class="px-5 py-2.5 bg-white border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition shadow-sm font-medium">取消</button>
          <button @click="submitApplication" class="px-5 py-2.5 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition shadow-md font-medium flex items-center gap-2">
            <i class="fas fa-check"></i> 确认提交报名
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'

// 状态变量
const competitions = ref([]) // 存放从后端拿到的原数据
const isLoading = ref(true)

// 搜索与分类状态
const searchQuery = ref('')
const selectedCategory = ref('全部')
const categories = ['全部', '数学类', '计算机类', '艺术类', '人文学科类', '其他']

// 报名表单相关
const showApplyModal = ref(false)
const currentApplyingComp = ref(null)
const applyForm = reactive({
  statement: '',
  className: '',
  mathScore: ''
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
    alert("无法连接到后端，请确保 Spring Boot 正在运行！")
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

// 打开报名弹窗
const openApplyModal = (comp) => {
  currentApplyingComp.value = comp
  applyForm.statement = ''
  applyForm.className = ''
  applyForm.mathScore = ''
  showApplyModal.value = true
}

// 提交报名 (联调真实后端)
const submitApplication = async () => {
  if (!applyForm.statement) {
    alert("请填写参赛宣言！")
    return
  }

  // 1. 获取当前登录用户的信息（我们需要知道是谁报的表）
  const userStr = localStorage.getItem('currentUser')
  const currentUser = userStr ? JSON.parse(userStr) : null
  if (!currentUser) {
    alert("登录信息已过期，请重新登录！")
    return
  }

  // 2. 拼装要提交给后端的特殊字段（比如数学类要填的成绩，我们把它拼进 statement 字段里发给后端）
  let finalStatement = applyForm.statement
  if (currentApplyingComp.value.category === '数学类') {
    finalStatement += `\n【附加信息】班级：${applyForm.className}，高数/线代均分：${applyForm.mathScore}`
  }

  // 3. 呼叫传菜员发快递！
  try {
    const response = await axios.post('http://localhost:8080/api/application/submit', {
      studentId: currentUser.id,
      compId: currentApplyingComp.value.id,
      statement: finalStatement
      // fileUrl 之后如果做文件上传可以加这里
    })

    if (response.data.success) {
      alert(`报名成功！\n您已成功提交对【${currentApplyingComp.value.title}】的报名。`)
      showApplyModal.value = false // 关掉弹窗
    } else {
      alert(response.data.message)
    }
  } catch (error) {
    console.error("提交报名失败:", error)
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