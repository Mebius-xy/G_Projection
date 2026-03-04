<template>
  <div class="font-sans pb-10">

    <!-- ================= 🌟 大厂级轮播图 (Carousel) 🌟 ================= -->
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

      <!-- 指示器 -->
      <div class="absolute bottom-4 left-1/2 -translate-x-1/2 z-30 flex gap-2">
        <button
            v-for="(banner, index) in banners" :key="'dot-'+banner.id"
            @click.stop="activeIndex = index"
            class="h-2 rounded-full transition-all duration-300"
            :class="activeIndex === index ? 'w-8 bg-white' : 'w-2 bg-white/40 hover:bg-white/70'"
        ></button>
      </div>
    </div>

    <!-- ================= 🌟 修复：贴上跳转符咒的核心指标卡片 🌟 ================= -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <!-- 卡片1：点击去竞赛大厅 -->
      <div @click="$router.push('/competitions')" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 relative overflow-hidden group hover:shadow-md transition-all duration-300 cursor-pointer hover:-translate-y-1">
        <div class="absolute -right-4 -bottom-4 w-24 h-24 bg-blue-50 rounded-full opacity-50 group-hover:scale-150 transition-transform duration-500 ease-out"></div>
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wider mb-1">全校竞赛总数</p>
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

      <!-- 卡片2：点击去对应的业务页面 (动态判断身份) -->
      <div @click="goToMyBusiness" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 relative overflow-hidden group hover:shadow-md transition-all duration-300 cursor-pointer hover:-translate-y-1">
        <div class="absolute -right-4 -bottom-4 w-24 h-24 bg-emerald-50 rounded-full opacity-50 group-hover:scale-150 transition-transform duration-500 ease-out"></div>
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wider mb-1">
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

      <!-- 卡片3：点击去讨论区 -->
      <div @click="$router.push('/forum')" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 relative overflow-hidden group hover:shadow-md transition-all duration-300 cursor-pointer hover:-translate-y-1">
        <div class="absolute -right-4 -bottom-4 w-24 h-24 bg-purple-50 rounded-full opacity-50 group-hover:scale-150 transition-transform duration-500 ease-out"></div>
        <div class="flex justify-between items-center relative z-10">
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wider mb-1">全站讨论热帖</p>
            <h3 class="text-4xl font-black text-gray-800">{{ stats.forumCount }}</h3>
          </div>
          <div class="w-14 h-14 bg-purple-100 text-purple-600 rounded-2xl flex items-center justify-center shadow-inner border border-purple-50">
            <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path></svg>
          </div>
        </div>
      </div>
    </div>

    <!-- ================= 数据图表大屏核心区 ================= -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
            <div class="w-2 h-6 bg-blue-500 rounded-full"></div>
            全校学生班级分布
          </h3>
        </div>
        <div id="pieChartContainer" class="w-full h-[300px]"></div>
      </div>

      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
            <div class="w-2 h-6 bg-emerald-500 rounded-full"></div>
            近期系统访问活跃度趋势
          </h3>
        </div>
        <div id="lineChartContainer" class="w-full h-[300px]"></div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import * as echarts from 'echarts'

const router = useRouter()
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

const stats = reactive({ totalComps: 0, actionCount: 0, forumCount: 0 })

// 🌟 根据身份动态跳转对应业务页面
const goToMyBusiness = () => {
  if (currentUser.value.role === 'student') router.push('/my-apps')
  else if (currentUser.value.role === 'teacher') router.push('/review')
  else router.push('/admin-users')
}

// ================= 🌟 轮播图核心逻辑 🌟 =================
const activeIndex = ref(0)
let carouselTimer = null

const banners = [
  {
    id: 1,
    tag: '🏆 顶级赛事',
    title: '第十七届蓝桥杯全国专业人才大赛',
    subtitle: '校内选拔赛报名通道现已全面开启，算法、开发、设计多赛道并进，即刻备战！',
    btnText: '立即查看竞赛',
    link: '/competitions',
    color: 'bg-gradient-to-r from-blue-600 to-indigo-800',
    svgPath: '<path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path>'
  },
  {
    id: 2,
    tag: '⚡ 效率跃升',
    title: '全新 CIMS 聚合搜索重磅上线',
    subtitle: '打破数据孤岛，支持一键同时检索全校用户、竞赛规程及讨论热帖，找资料快人一步。',
    btnText: '体验全局搜索',
    link: '/dashboard',
    color: 'bg-gradient-to-r from-purple-600 to-pink-600',
    svgPath: '<path d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path><path d="M10 7v3m0 0v3m0-3h3m-3 0H7"></path>'
  },
  {
    id: 3,
    tag: '🤝 社交大厅',
    title: '校园专属组队与交流空间开放',
    subtitle: '缺大腿带飞？遇到技术瓶颈？一键发布招募贴，打破学院壁垒，连接志同道合的竞赛战友。',
    btnText: '进入讨论区',
    link: '/forum',
    color: 'bg-gradient-to-r from-emerald-500 to-teal-700',
    svgPath: '<path d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>'
  }
]

const nextSlide = () => { activeIndex.value = (activeIndex.value + 1) % banners.length }
const startCarousel = () => {
  clearInterval(carouselTimer)
  carouselTimer = setInterval(nextSlide, 4000)
}
const pauseCarousel = () => clearInterval(carouselTimer)

onUnmounted(() => clearInterval(carouselTimer))

// ================= 画图与数据统计逻辑 =================
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
          legend: { top: 'bottom', textStyle: { fontSize: 10 } },
          series: [{
            name: '班级分布', type: 'pie', radius: ['40%', '70%'],
            itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
            label: { show: false }, emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
            data: res.data.pieData
          }]
        })

        lineChart.setOption({
          tooltip: { trigger: 'axis' },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', boundaryGap: false, data: res.data.lineXData, axisLine: { lineStyle: { color: '#E5E7EB' } } },
          yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: '#F3F4F6', type: 'dashed' } } },
          series: [{
            name: '访问量', type: 'line', smooth: true, lineStyle: { width: 3, color: '#10B981' }, showSymbol: false,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(16, 185, 129, 0.4)' }, { offset: 1, color: 'rgba(16, 185, 129, 0.05)' }
              ])
            },
            data: res.data.lineYData
          }]
        })

        window.addEventListener('resize', () => { pieChart.resize(); lineChart.resize() })
      }
    }
  } catch (error) {
    console.error("图表数据获取失败", error)
  }
}

onMounted(async () => {
  startCarousel()

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
    }
    else if (currentUser.value.role === 'teacher') {
      const appRes = await axios.get('http://localhost:8080/api/application/all')
      stats.actionCount = appRes.data.filter(app => app.status === 'pending').length || 0
    }
    else if (currentUser.value.role === 'admin') {
      const userRes = await axios.get('http://localhost:8080/api/user/page?pageSize=1')
      stats.actionCount = userRes.data.total || 0
    }

    nextTick(() => { initCharts() })
  } catch (error) {
    console.error("加载数据失败:", error)
  }
})
</script>