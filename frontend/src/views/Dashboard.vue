<template>
  <div class="font-sans pb-10">

    <!-- 大厂级轮播图 (Carousel) -->
    <div class="relative w-full h-[280px] rounded-2xl overflow-hidden shadow-[0_8px_30px_rgba(0,0,0,0.08)] mb-8 group" @mouseenter="pauseCarousel" @mouseleave="startCarousel">
      <div
          v-for="(banner, index) in banners"
          :key="banner.id"
          class="absolute inset-0 w-full h-full transition-opacity duration-1000 ease-in-out flex justify-between items-center p-10 text-white cursor-pointer"
          :class="[banner.color, activeIndex === index ? 'opacity-100 z-10' : 'opacity-0 z-0']"
          @click="$router.push(banner.link)"
      >
        <div class="absolute -top-32 -right-10 w-96 h-96 bg-white opacity-10 rounded-full blur-3xl"></div>
        <div class="absolute -bottom-20 -left-20 w-64 h-64 bg-black opacity-10 rounded-full blur-3xl"></div>

        <div class="relative z-20 max-w-2xl transform transition-transform duration-700" :class="activeIndex === index ? 'translate-x-0' : '-translate-x-10'">
          <span class="inline-block px-3 py-1 mb-4 text-xs font-bold rounded-full bg-white/20 backdrop-blur-md border border-white/30 tracking-wider">
            {{ banner.tag }}
          </span>
          <h2 class="text-4xl font-extrabold mb-4 tracking-tight leading-tight">{{ banner.title }}</h2>
          <p class="text-white/90 text-base font-medium mb-6 line-clamp-2">{{ banner.subtitle }}</p>
          <button class="bg-white text-gray-900 px-6 py-2.5 rounded-lg font-bold shadow-lg hover:shadow-xl hover:scale-105 transition-all flex items-center gap-2">
            {{ banner.btnText }}
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"></path></svg>
          </button>
        </div>

        <div class="hidden md:block relative z-20 transform transition-transform duration-1000" :class="activeIndex === index ? 'scale-100 opacity-100' : 'scale-50 opacity-0'">
          <svg class="w-40 h-40 text-white opacity-20 drop-shadow-2xl" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-html="banner.svgPath"></svg>
        </div>
      </div>

      <div class="absolute bottom-4 left-1/2 -translate-x-1/2 z-30 flex gap-2">
        <button
            v-for="(banner, index) in banners" :key="'dot-'+banner.id"
            @click.stop="activeIndex = index"
            class="h-2 rounded-full transition-all duration-300"
            :class="activeIndex === index ? 'w-8 bg-white' : 'w-2 bg-white/40 hover:bg-white/70'"
        ></button>
      </div>
    </div>

    <!-- 核心指标卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div @click="$router.push('/competitions')" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 relative overflow-hidden group hover:shadow-md transition-all duration-300 cursor-pointer hover:-translate-y-1">
        <div class="absolute -right-4 -bottom-4 w-24 h-24 bg-blue-50 rounded-full opacity-50 group-hover:scale-150 transition-transform duration-500 ease-out"></div>
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wider mb-1 group-hover:text-blue-600 transition">全校竞赛总数</p>
            <div class="flex items-baseline gap-2">
              <h3 class="text-4xl font-black text-gray-800">{{ stats.totalComps }}</h3>
              <span class="text-xs text-green-500 font-bold flex items-center bg-green-50 px-1.5 py-0.5 rounded">
                <svg class="w-3 h-3 mr-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path></svg> 最新
              </span>
            </div>
          </div>
          <div class="w-14 h-14 bg-blue-100 text-blue-600 rounded-2xl flex items-center justify-center shadow-inner border border-blue-50">
            <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path></svg>
          </div>
        </div>
      </div>

      <div @click="goToMyBusiness" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 relative overflow-hidden group hover:shadow-md transition-all duration-300 cursor-pointer hover:-translate-y-1">
        <div class="absolute -right-4 -bottom-4 w-24 h-24 bg-emerald-50 rounded-full opacity-50 group-hover:scale-150 transition-transform duration-500 ease-out"></div>
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wider mb-1 group-hover:text-emerald-600 transition">
              {{ currentUser.role === 'teacher' ? '待审核报名表' : (currentUser.role === 'admin' ? '系统总用户数' : '我参与的竞赛') }}
            </p>
            <h3 class="text-4xl font-black text-gray-800">{{ stats.actionCount }}</h3>
          </div>
          <div class="w-14 h-14 bg-emerald-100 text-emerald-600 rounded-2xl flex items-center justify-center shadow-inner border border-emerald-50">
            <svg v-if="currentUser.role !== 'admin'" class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
            <svg v-else class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
          </div>
        </div>
      </div>

      <div @click="$router.push('/forum')" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 relative overflow-hidden group hover:shadow-md transition-all duration-300 cursor-pointer hover:-translate-y-1">
        <div class="absolute -right-4 -bottom-4 w-24 h-24 bg-purple-50 rounded-full opacity-50 group-hover:scale-150 transition-transform duration-500 ease-out"></div>
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wider mb-1 group-hover:text-purple-600 transition">全站讨论热帖</p>
            <h3 class="text-4xl font-black text-gray-800">{{ stats.forumCount }}</h3>
          </div>
          <div class="w-14 h-14 bg-purple-100 text-purple-600 rounded-2xl flex items-center justify-center shadow-inner border border-purple-50">
            <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path></svg>
          </div>
        </div>
      </div>
    </div>

    <!-- 数据大屏 与 动态公告栏 混排 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">

      <!-- 🌟 核心升级：系统动态公告栏 🌟 -->
      <div class="lg:col-span-1 bg-white rounded-2xl shadow-sm border border-gray-100 flex flex-col overflow-hidden">
        <div class="p-5 border-b border-gray-100 bg-blue-50/30 flex justify-between items-center">
          <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5.882V19.24a1.76 1.76 0 01-3.417.592l-2.147-6.15M18 13a3 3 0 100-6M5.436 13.683A4.001 4.001 0 017 6h1.832c4.1 0 7.625-1.234 9.168-3v14c-1.543-1.766-5.067-3-9.168-3H7a3.988 3.988 0 01-1.564-.317z"></path></svg>
            校园竞赛通知栏
          </h3>
          <!-- 只有管理员才能发公告 -->
          <button v-if="currentUser.role === 'admin'" @click="showAddNoticeModal = true" class="text-xs bg-blue-600 hover:bg-blue-700 text-white font-bold px-3 py-1.5 rounded-lg shadow-sm transition">
            + 发布通知
          </button>
        </div>

        <div class="flex-1 overflow-y-auto p-2 min-h-[300px] max-h-[400px]">
          <div v-if="notices.length === 0" class="text-center py-20 text-gray-400 text-sm">
            暂无最新公告
          </div>
          <div v-else class="space-y-1">
            <!-- 公告列表项 -->
            <div v-for="notice in notices" :key="notice.id" @click="viewNotice(notice)" class="p-3 hover:bg-gray-50 rounded-xl cursor-pointer transition group border border-transparent hover:border-gray-100">
              <div class="flex justify-between items-start mb-1">
                <span class="px-2 py-0.5 text-[10px] font-bold rounded shrink-0" :class="getNoticeBadgeClass(notice.type)">{{ notice.type }}</span>
                <!-- 🌟 修复 1：加上安全剪刀 `?.`，防止后端传空时间导致白屏 -->
                <span class="text-[10px] text-gray-400 ml-2 whitespace-nowrap">{{ notice.publishTime?.substring(5, 10) || '最新' }}</span>
              </div>
              <h4 class="text-sm font-bold text-gray-700 group-hover:text-blue-600 transition truncate" :title="notice.title">{{ notice.title }}</h4>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧两个图表被压到了占 2 列 -->
      <div class="lg:col-span-2 grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 flex flex-col">
          <h3 class="text-base font-bold text-gray-800 flex items-center gap-2 mb-2">
            <div class="w-1.5 h-4 bg-emerald-500 rounded-full"></div> 班级参赛热度图
          </h3>
          <div id="pieChartContainer" class="w-full flex-1 min-h-[250px]"></div>
        </div>

        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 flex flex-col">
          <h3 class="text-base font-bold text-gray-800 flex items-center gap-2 mb-2">
            <div class="w-1.5 h-4 bg-purple-500 rounded-full"></div> 平台近期访问趋势
          </h3>
          <div id="lineChartContainer" class="w-full flex-1 min-h-[250px]"></div>
        </div>
      </div>
    </div>

    <!-- ================= 🌟 弹窗：查看公告详情 🌟 ================= -->
    <div v-if="currentViewingNotice" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm flex items-center justify-center z-[100]">
      <div class="bg-white rounded-2xl shadow-2xl w-[600px] max-h-[85vh] flex flex-col overflow-hidden animate-fade-in-up border border-gray-100">
        <!-- 弹窗花纹头部 -->
        <div class="p-6 border-b relative overflow-hidden" :class="getNoticeHeaderClass(currentViewingNotice.type)">
          <div class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10"></div>
          <div class="relative z-10">
            <span class="inline-block px-3 py-1 bg-white/20 backdrop-blur-md rounded-full text-xs font-bold text-white mb-3">
              {{ currentViewingNotice.type }}
            </span>
            <h2 class="text-xl font-extrabold text-white leading-snug">{{ currentViewingNotice.title }}</h2>
            <p class="text-white/80 text-xs mt-2 flex items-center gap-1.5">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
              <!-- 🌟 修复 2：弹窗里的时间也加上三元判断防雷 -->
              发布时间：{{ currentViewingNotice.publishTime ? currentViewingNotice.publishTime.replace('T', ' ').substring(0, 16) : '系统发布' }}
            </p>
          </div>
        </div>

        <div class="p-8 overflow-y-auto flex-1 bg-gray-50/30">
          <!-- 💡 whitespace-pre-wrap 是重点，它能保留数据库里存的回车换行符，让文字分段显示 -->
          <div class="text-gray-700 text-sm leading-relaxed whitespace-pre-wrap font-medium">
            {{ currentViewingNotice.content }}
          </div>
        </div>

        <div class="p-4 border-t border-gray-100 bg-white flex justify-between items-center shrink-0">
          <button v-if="currentUser.role === 'admin'" @click="handleDeleteNotice(currentViewingNotice.id)" class="text-xs text-red-500 hover:text-red-700 font-bold px-3 py-2 rounded hover:bg-red-50 transition">
            删除此公告
          </button>
          <span v-else></span>

          <button @click="currentViewingNotice = null" class="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl shadow-sm font-bold transition">
            我知道了
          </button>
        </div>
      </div>
    </div>

    <!-- ================= 🌟 弹窗：管理员发布新公告 🌟 ================= -->
    <div v-if="showAddNoticeModal" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm flex items-center justify-center z-[100]">
      <div class="bg-white rounded-2xl shadow-2xl w-[500px] overflow-hidden animate-fade-in-up border border-gray-100">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800">用大喇叭发布全校通知</h3>
          <button @click="showAddNoticeModal = false" class="text-gray-400 hover:text-red-500 transition">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>
        <form @submit.prevent="submitAddNotice" class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">通知标题</label>
            <input v-model="newNotice.title" required type="text" class="w-full border border-gray-200 rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-blue-300" placeholder="例如：【考试通知】软件杯决赛安排">
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">通知类型</label>
            <select v-model="newNotice.type" class="w-full border border-gray-200 rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-blue-300 bg-white">
              <option>报名通知</option>
              <option>考试通知</option>
              <option>成绩公示</option>
              <option>普通通知</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">详细内容 (时间、地点、规则等)</label>
            <textarea v-model="newNotice.content" required rows="6" class="w-full border border-gray-200 rounded-lg p-3 outline-none focus:ring-2 focus:ring-blue-300 leading-relaxed" placeholder="请输入通知的正文内容..."></textarea>
          </div>
          <div class="pt-3">
            <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-xl shadow-md transition">
              立即推送给全校
            </button>
          </div>
        </form>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import * as echarts from 'echarts'
import { toast } from '../utils/toast'

const router = useRouter()
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

const stats = reactive({ totalComps: 0, actionCount: 0, forumCount: 0 })

// ================= 新增：公告栏核心逻辑 =================
const notices = ref([])
const currentViewingNotice = ref(null)
const showAddNoticeModal = ref(false)
const newNotice = reactive({ title: '', type: '报名通知', content: '' })

const fetchNotices = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/notice/list')
    notices.value = res.data
  } catch (error) {
    console.error("加载公告失败", error)
  }
}

const viewNotice = (notice) => {
  currentViewingNotice.value = notice
}

const submitAddNotice = async () => {
  try {
    const res = await axios.post('http://localhost:8080/api/notice/add', newNotice)
    if (res.data.success) {
      toast.success(res.data.message)
      showAddNoticeModal.value = false
      newNotice.title = ''
      newNotice.content = ''
      fetchNotices() // 刷新公告列表
    }
  } catch (e) {
    toast.error("发布失败，请检查网络！")
  }
}

const handleDeleteNotice = async (id) => {
  if (!confirm("确定要永久删除这条全校公告吗？")) return
  try {
    const res = await axios.post(`http://localhost:8080/api/notice/delete/${id}`)
    if (res.data.success) {
      toast.success("公告已撤回")
      currentViewingNotice.value = null
      fetchNotices()
    }
  } catch (e) {
    toast.error("删除失败")
  }
}

// 样式小工具：给不同类型的公告上不同颜色的徽章
const getNoticeBadgeClass = (type) => {
  const map = {
    '报名通知': 'bg-blue-100 text-blue-700',
    '考试通知': 'bg-red-100 text-red-700',
    '成绩公示': 'bg-emerald-100 text-emerald-700',
    '普通通知': 'bg-gray-200 text-gray-700'
  }
  return map[type] || 'bg-gray-100 text-gray-600'
}
// 弹窗头部大背景色
const getNoticeHeaderClass = (type) => {
  const map = {
    '报名通知': 'bg-gradient-to-r from-blue-600 to-indigo-600',
    '考试通知': 'bg-gradient-to-r from-red-500 to-orange-600',
    '成绩公示': 'bg-gradient-to-r from-emerald-500 to-teal-600',
    '普通通知': 'bg-gradient-to-r from-gray-600 to-gray-800'
  }
  return map[type] || 'bg-gradient-to-r from-blue-600 to-blue-800'
}
// ========================================================


const goToMyBusiness = () => {
  if (currentUser.value.role === 'student') router.push('/my-apps')
  else if (currentUser.value.role === 'teacher') router.push('/review')
  else router.push('/admin-users')
}

// 轮播图逻辑
const activeIndex = ref(0)
let carouselTimer = null
const banners = [
  { id: 1, tag: '🏆 顶级赛事', title: '第十七届蓝桥杯全国专业人才大赛', subtitle: '校内选拔赛报名通道现已全面开启，即刻备战！', btnText: '立即查看竞赛', link: '/competitions', color: 'bg-gradient-to-r from-blue-600 to-indigo-800', svgPath: '<path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path>' },
  { id: 2, tag: '⚡ 效率跃升', title: '全新 CIMS 聚合搜索重磅上线', subtitle: '一键同时检索全校用户、竞赛规程及讨论热帖。', btnText: '体验全局搜索', link: '/dashboard', color: 'bg-gradient-to-r from-purple-600 to-pink-600', svgPath: '<path d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path><path d="M10 7v3m0 0v3m0-3h3m-3 0H7"></path>' },
  { id: 3, tag: '🤝 社交大厅', title: '校园专属组队与交流空间开放', subtitle: '一键发布招募贴，打破学院壁垒，连接志同道合的战友。', btnText: '进入讨论区', link: '/forum', color: 'bg-gradient-to-r from-emerald-500 to-teal-700', svgPath: '<path d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>' }
]

const nextSlide = () => { activeIndex.value = (activeIndex.value + 1) % banners.length }
const startCarousel = () => { clearInterval(carouselTimer); carouselTimer = setInterval(nextSlide, 4000) }
const pauseCarousel = () => clearInterval(carouselTimer)

onUnmounted(() => clearInterval(carouselTimer))

const initCharts = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/dashboard/chartsData')
    if (res.data.success) {
      const pieDom = document.getElementById('pieChartContainer')
      const lineDom = document.getElementById('lineChartContainer')

      if (pieDom && lineDom) {
        const pieChart = echarts.init(pieDom)
        const lineChart = echarts.init(lineDom)

        pieChart.setOption({
          tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
          series: [{
            name: '班级分布', type: 'pie', radius: ['40%', '80%'],
            itemStyle: { borderRadius: 5, borderColor: '#fff', borderWidth: 2 },
            label: { show: false },
            data: res.data.pieData
          }]
        })

        lineChart.setOption({
          tooltip: { trigger: 'axis' },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', boundaryGap: false, data: res.data.lineXData, axisLine: { lineStyle: { color: '#E5E7EB' } } },
          yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: '#F3F4F6', type: 'dashed' } } },
          series: [{
            name: '访问量', type: 'line', smooth: true, lineStyle: { width: 3, color: '#8B5CF6' }, showSymbol: false,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(139, 92, 246, 0.4)' }, { offset: 1, color: 'rgba(139, 92, 246, 0.05)' }
              ])
            },
            data: res.data.lineYData
          }]
        })

        window.addEventListener('resize', () => { pieChart.resize(); lineChart.resize() })
      }
    }
  } catch (error) {
    console.error("加载图表数据失败", error)
  }
}

onMounted(async () => {
  startCarousel()
  fetchNotices() // 🌟 加载系统公告

  try {
    const [compRes, forumRes] = await Promise.all([
      axios.get('http://localhost:8080/api/competition/list'),
      axios.get('http://localhost:8080/api/forum/list')
    ])
    stats.totalComps = compRes.data.length || 0
    stats.forumCount = forumRes.data.length || 0

    if (currentUser.value.role === 'student') {
      const appRes = await axios.get(`http://localhost:8080/api/application/my?studentId=${currentUser.value.id}`)
      stats.actionCount = appRes.data.length || 0
    } else if (currentUser.value.role === 'teacher') {
      const appRes = await axios.get('http://localhost:8080/api/application/all')
      stats.actionCount = appRes.data.filter(app => app.status === 'pending').length || 0
    } else {
      const userRes = await axios.get('http://localhost:8080/api/user/page?pageSize=1')
      stats.actionCount = userRes.data.total || 0
    }

    nextTick(() => { initCharts() })
  } catch (error) {
    console.error("加载面板数据失败", error)
  }
})
</script>

<style scoped>
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up { animation: fadeInUp 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards; }

/* 隐藏公告栏的滚动条让它更美观 */
.overflow-y-auto::-webkit-scrollbar {
  width: 4px;
}
.overflow-y-auto::-webkit-scrollbar-thumb {
  background-color: #e5e7eb;
  border-radius: 4px;
}
</style>