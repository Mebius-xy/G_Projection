<template>
  <div class="font-sans h-full flex flex-col">
    <!-- 顶部霸气 Banner -->
    <div class="bg-gradient-to-r from-blue-900 via-indigo-800 to-purple-900 rounded-2xl p-8 mb-6 text-white shadow-lg relative overflow-hidden flex-shrink-0">
      <div class="relative z-10 w-full md:w-2/3">
        <span class="bg-yellow-400 text-yellow-900 text-xs font-bold px-2 py-1 rounded mb-3 inline-block">🚀 全新上线</span>
        <h1 class="text-3xl font-bold mb-2">CIMS 学习资源与真题库</h1>
        <p class="text-blue-200 text-sm leading-relaxed">汇聚蓝桥杯、ACM历年真题，收集全国大学生艺术展演优秀作品集。<br>在这里，为你的下一次竞赛蓄力。</p>
      </div>
      <svg class="absolute -right-10 -top-10 w-64 h-64 text-white opacity-10" fill="currentColor" viewBox="0 0 20 20"><path d="M10.394 2.08a1 1 0 00-.788 0l-7 3a1 1 0 000 1.84L5.25 8.051a.999.999 0 01.356-.257l4-1.714a1 1 0 11.788 1.838L7.667 9.088l1.94.831a1 1 0 00.787 0l7-3a1 1 0 000-1.838l-7-3zM3.31 9.397L5 10.12v4.102a8.969 8.969 0 00-1.05-.174 1 1 0 01-.89-.89 11.115 11.115 0 01.25-3.762zM9.3 16.573A9.026 9.026 0 007 14.935v-3.957l1.818.78a3 3 0 002.364 0l5.508-2.361a11.026 11.026 0 01.25 3.762 1 1 0 01-.89.89 8.968 8.968 0 00-5.35 2.524 1 1 0 01-1.4 0z"></path></svg>
    </div>

    <!-- 主体布局 -->
    <div class="flex flex-col md:flex-row gap-6 flex-1 overflow-hidden">
      <!-- 左侧分类导航 -->
      <div class="w-full md:w-48 bg-white rounded-xl shadow-sm border border-gray-100 p-3 flex-shrink-0 flex flex-col h-auto md:h-full">
        <h3 class="text-xs font-bold text-gray-400 uppercase tracking-wider mb-3 px-3">资源分类</h3>
        <nav class="space-y-1 flex-1 overflow-y-auto">
          <a v-for="nav in navs" :key="nav.id" @click="activeNav = nav.id" :class="activeNav === nav.id ? 'bg-blue-50 text-blue-600 font-bold' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'" class="block px-4 py-2.5 rounded-lg cursor-pointer transition text-sm">
            {{ nav.name }}
          </a>
        </nav>
      </div>

      <!-- 右侧内容流 -->
      <div class="flex-1 bg-white rounded-xl shadow-sm border border-gray-100 p-6 overflow-y-auto relative">
        <div class="flex justify-between items-center mb-6 border-b border-gray-100 pb-4">
          <h2 class="text-xl font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path></svg>
            {{ currentNavName }} 排行榜
          </h2>
          <span class="text-xs font-bold text-gray-400 bg-gray-100 px-3 py-1 rounded-full">按全网阅读量降序</span>
        </div>

        <div v-if="activeNav !== 'art'">
          <div v-if="isLoading" class="text-center py-10 text-blue-500">
            <svg class="animate-spin h-8 w-8 mx-auto mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            <p class="text-sm">正在加载云端题库...</p>
          </div>
          <div v-else-if="realQuestions.length === 0" class="text-center py-10 text-gray-400">
            <p>题库暂无数据，请等待爬虫抓取更新...</p>
          </div>

          <!-- 🌟 动态排行榜渲染 🌟 -->
          <div v-else class="space-y-4">
            <!-- v-for 里的 index 是从 0 开始的数组索引 -->
            <div v-for="(q, index) in realQuestions" :key="q.id" class="p-5 border border-gray-100 rounded-xl hover:shadow-md transition cursor-pointer group flex gap-5 items-start">

              <!-- 排名数字徽章 (前三名特殊颜色) -->
              <div class="w-10 h-10 shrink-0 rounded-xl flex items-center justify-center font-black text-lg shadow-sm border"
                   :class="index === 0 ? 'bg-gradient-to-br from-yellow-200 to-yellow-400 text-yellow-800 border-yellow-300' :
                           (index === 1 ? 'bg-gradient-to-br from-gray-200 to-gray-300 text-gray-700 border-gray-300' :
                           (index === 2 ? 'bg-gradient-to-br from-orange-200 to-orange-300 text-orange-800 border-orange-300' : 'bg-gray-50 text-gray-400 border-gray-100'))">
                {{ index + 1 }}
              </div>

              <!-- 题目内容 -->
              <div class="flex-1">
                <div class="flex justify-between items-start">
                  <h3 class="text-lg font-bold text-gray-800 group-hover:text-blue-600 transition">{{ q.title }}</h3>
                  <span class="px-2.5 py-1 rounded text-xs font-bold whitespace-nowrap border" :class="getDifficultyClass(q.difficulty)">
                    {{ q.difficulty }}
                  </span>
                </div>
                <p class="text-sm text-gray-500 mt-2 line-clamp-2 leading-relaxed">{{ q.description }}</p>
                <div class="flex gap-3 mt-4 text-xs">
                  <span class="bg-gray-100 text-gray-600 px-2 py-1 rounded font-mono border border-gray-200">{{ q.tag }}</span>
                  <span class="text-gray-500 flex items-center bg-blue-50 px-2 py-1 rounded text-blue-600 font-bold border border-blue-100">
                    <svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path></svg>
                    {{ q.views }} 次热度
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 艺术展 (保持不变) -->
        <div v-else class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
          <div v-for="art in mockArts" :key="art.id" class="border border-gray-100 rounded-xl overflow-hidden hover:shadow-lg transition cursor-pointer group">
            <div class="h-40 bg-gray-200 overflow-hidden relative">
              <img :src="art.img" alt="作品封面" class="w-full h-full object-cover group-hover:scale-105 transition duration-500">
              <div class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition flex items-center justify-center">
                <span class="text-white font-bold tracking-widest border-2 border-white px-4 py-1 rounded">查看作品</span>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-bold text-gray-800 truncate">{{ art.title }}</h3>
              <p class="text-xs text-gray-500 mt-1">作者：{{ art.author }}</p>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const navs = [
  { id: 'algo', name: '算法真题榜 (爬虫)' },
  { id: 'math', name: '数学建模优秀库' },
  { id: 'dev', name: '开发实战项目' },
  { id: 'art', name: '文创/设计作品展' }
]
const activeNav = ref('algo')
const isLoading = ref(true)

const currentNavName = computed(() => {
  return navs.find(n => n.id === activeNav.value)?.name || '未知'
})

const realQuestions = ref([])

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/study/questions')
    realQuestions.value = res.data
  } catch (error) {
    console.error("题库获取失败:", error)
  } finally {
    isLoading.value = false
  }
})

const getDifficultyClass = (diff) => {
  const map = {
    '困难': 'text-red-600 bg-red-50 border-red-200',
    '中等': 'text-yellow-700 bg-yellow-50 border-yellow-200',
    '简单': 'text-green-600 bg-green-50 border-green-200'
  }
  return map[diff] || 'text-gray-500 bg-gray-50 border-gray-100'
}

const mockArts = [
  {id: 1, title: '《未来城市》3D概念设计', author: '数字媒体22级 张三', img: 'https://picsum.photos/seed/city/400/300'},
  {id: 2, title: '《寻根》校园微电影分镜', author: '编导23级 李四', img: 'https://picsum.photos/seed/movie/400/300'},
  {id: 3, title: '系统UI重构交互稿', author: '设计21级 王五', img: 'https://picsum.photos/seed/ui/400/300'}
]
</script>