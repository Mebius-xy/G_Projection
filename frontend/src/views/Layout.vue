<template>
  <div class="flex h-screen overflow-hidden bg-[#f4f5f7] font-sans">
    <!-- 左侧侧边栏 (极简白色系，无边界设计) -->
    <aside class="w-64 bg-white border-r border-gray-200 flex flex-col transition-all duration-300 z-20 shadow-[2px_0_8px_rgba(0,0,0,0.02)]">

      <!-- 顶部 Logo 区域 -->
      <div class="h-16 flex items-center px-6 border-b border-gray-100 bg-white">
        <!-- 🌟 专属 CIMS 品牌 Logo (小尺寸版) 🌟 -->
        <svg class="w-7 h-7 text-blue-600 mr-2.5" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M16 2L2 9.5V22.5L16 30L30 22.5V9.5L16 2Z" fill="currentColor" fill-opacity="0.15" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
          <path d="M21 11.5C19.5 9.5 17 8.5 14 9.5C11 10.5 9 13.5 9 16C9 18.5 11 21.5 14 22.5C17 23.5 19.5 22.5 21 20.5" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <span class="font-bold text-xl text-gray-800 tracking-wider font-mono">CIMS</span>
      </div>

      <!-- 动态菜单导航区 -->
      <nav class="flex-1 py-4 px-3 space-y-1 overflow-y-auto">
        <!-- 基础菜单 -->
        <router-link to="/dashboard" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group" active-class="bg-blue-50 text-blue-600 font-bold" :class="$route.path === '/dashboard' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
          <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path></svg>
          <span>系统首页</span>
        </router-link>

        <router-link to="/forum" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-blue-50 text-blue-600 font-bold" :class="$route.path === '/forum' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
          <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path></svg>
          <span>交流讨论区</span>
        </router-link>

        <router-link to="/study" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-blue-50 text-blue-600 font-bold" :class="$route.path === '/study' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
          <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
          <span>学习资源与真题库</span>
        </router-link>

        <div class="pt-5 pb-2 px-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">核心业务</div>

        <!-- 【学生专属区域】 -->
        <template v-if="currentUser.role === 'student'">
          <router-link to="/competitions" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group" active-class="bg-blue-50 text-blue-600 font-bold" :class="$route.path === '/competitions' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"></path></svg>
            <span>竞赛大厅</span>
          </router-link>
          <router-link to="/my-apps" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-blue-50 text-blue-600 font-bold" :class="$route.path === '/my-apps' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            <span>我的报名</span>
          </router-link>
        </template>

        <!-- 【教师专属区域】 -->
        <template v-if="currentUser.role === 'teacher'">
          <router-link to="/review" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group" active-class="bg-emerald-50 text-emerald-600 font-bold" :class="$route.path === '/review' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"></path></svg>
            <span>报名审核与评分</span>
          </router-link>
          <router-link to="/teacher-perm" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-emerald-50 text-emerald-600 font-bold" :class="$route.path === '/teacher-perm' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"></path></svg>
            <span>数据权限管理</span>
          </router-link>
        </template>

        <!-- 【管理员专属区域】 -->
        <template v-if="currentUser.role === 'admin'">
          <router-link to="/admin-comps" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group" active-class="bg-purple-50 text-purple-600 font-bold" :class="$route.path === '/admin-comps' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
            <span>竞赛项目发布</span>
          </router-link>

          <router-link to="/admin-users" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-purple-50 text-purple-600 font-bold" :class="$route.path === '/admin-users' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4"></path></svg>
            <span>全局用户信息表</span>
          </router-link>

          <router-link to="/admin-approvals" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-purple-50 text-purple-600 font-bold" :class="$route.path === '/admin-approvals' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path></svg>
            <span>权限审批中心</span>
          </router-link>
        </template>
      </nav>
    </aside>

    <!-- 右侧主体内容区 -->
    <main class="flex-1 flex flex-col h-screen overflow-hidden relative">

      <!-- 顶栏 (带个人中心下拉菜单) -->
      <header class="h-16 bg-white border-b border-gray-200 shadow-sm z-10 px-8 flex justify-between items-center shrink-0">
        <h2 class="text-lg font-bold text-gray-800">{{ getPageTitle }}</h2>

        <div class="flex items-center gap-6">
          <div class="relative group cursor-pointer">
            <div class="flex items-center gap-3">
              <div class="w-9 h-9 rounded-full bg-gray-100 text-gray-600 flex items-center justify-center text-lg border border-gray-200">
                {{ currentUser.role === 'student' ? '🎓' : (currentUser.role === 'teacher' ? '🏫' : '💻') }}
              </div>
              <div class="hidden md:block text-sm">
                <p class="font-bold text-gray-700 leading-none">{{ currentUser.name }}</p>
                <p class="text-gray-400 text-xs mt-1">{{ currentUser.className || (currentUser.role === 'teacher' ? '教师' : '管理员') }}</p>
              </div>
              <svg class="w-4 h-4 text-gray-400 ml-1 transition-transform group-hover:rotate-180" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
            </div>

            <!-- 下拉框本体 -->
            <div class="absolute right-0 mt-3 w-48 bg-white rounded-xl shadow-xl border border-gray-100 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 transform origin-top-right z-50">
              <div class="p-3 border-b border-gray-50">
                <p class="text-sm text-gray-800 font-bold">账号设置</p>
              </div>
              <div class="p-2 space-y-1">
                <router-link to="/profile" class="flex items-center px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 hover:text-blue-600 rounded-lg transition">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg> 个人中心
                </router-link>
                <router-link to="/settings" class="flex items-center px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 hover:text-blue-600 rounded-lg transition">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg> 系统设置
                </router-link>
                <div class="h-px bg-gray-100 my-1"></div>
                <a @click="logout" class="flex items-center px-3 py-2 text-sm text-red-500 hover:bg-red-50 hover:text-red-600 rounded-lg transition cursor-pointer">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg> 安全退出
                </a>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- 🌟 中间大屏幕渲染区域 🌟 -->
      <div class="flex-1 overflow-y-auto p-8 scroll-smooth">
        <router-view></router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

const getPageTitle = computed(() => {
  const pathMap = {
    '/dashboard': '系统首页',
    '/forum': '交流讨论区',
    '/study': '学习资源与真题库',
    '/profile': '个人资料中心',
    '/settings': '系统偏好设置',
    '/competitions': '竞赛大厅',
    '/my-apps': '我的报名记录',
    '/review': '报名审核与评分',
    '/teacher-perm': '数据权限与班级管理',
    '/admin-comps': '竞赛项目管理',
    '/admin-users': '全局用户信息表',
    '/admin-approvals': '数据权限审批中心'
  }
  return pathMap[route.path] || '竞赛管理平台'
})

const logout = () => {
  if(confirm('确定要退出当前账号吗？')) {
    localStorage.removeItem('currentUser')
    localStorage.removeItem('token')
    router.push('/login')
  }
}
</script>

<style scoped>
/* 隐藏侧边栏的滚动条，让视觉更干净 */
aside nav::-webkit-scrollbar { display: none; }
aside nav { -ms-overflow-style: none; scrollbar-width: none; }
</style>