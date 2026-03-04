<template>
  <div class="flex h-screen overflow-hidden bg-[#f4f5f7] font-sans">
    <!-- 左侧侧边栏 (极简白色系，无边界设计) -->
    <aside class="w-64 bg-white border-r border-gray-200 flex flex-col transition-all duration-300 z-20 shadow-[2px_0_8px_rgba(0,0,0,0.02)]">
      <div class="h-16 flex items-center px-6 border-b border-gray-100 bg-white">
        <svg class="w-7 h-7 text-blue-600 mr-2.5" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M16 2L2 9.5V22.5L16 30L30 22.5V9.5L16 2Z" fill="currentColor" fill-opacity="0.15" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
          <path d="M21 11.5C19.5 9.5 17 8.5 14 9.5C11 10.5 9 13.5 9 16C9 18.5 11 21.5 14 22.5C17 23.5 19.5 22.5 21 20.5" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <span class="font-bold text-xl text-gray-800 tracking-wider font-mono">CIMS</span>
      </div>

      <nav class="flex-1 py-4 px-3 space-y-1 overflow-y-auto">
        <!-- 🌟 公共菜单区域 (所有身份可见) 🌟 -->
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

        <!-- 🌟 将竞赛大厅放到公共区域，让所有身份都能查看 🌟 -->
        <router-link to="/competitions" class="flex items-center px-4 py-3 rounded-lg cursor-pointer transition-colors group mt-1" active-class="bg-blue-50 text-blue-600 font-bold" :class="$route.path === '/competitions' ? '' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'">
          <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"></path></svg>
          <span>竞赛大厅</span>
        </router-link>

        <div class="pt-5 pb-2 px-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">核心业务</div>

        <!-- 【学生专属区域】 -->
        <template v-if="currentUser.role === 'student'">
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

      <!-- 顶栏 (精简白，悬浮头像下拉菜单) -->
      <header class="h-16 bg-white border-b border-gray-200 shadow-sm z-10 px-8 flex justify-between items-center shrink-0">

        <!-- 左侧搜索区 (聚合搜索) -->
        <div class="flex items-center gap-8 flex-1 relative">
          <h2 class="text-lg font-bold text-gray-800 whitespace-nowrap w-32 hidden md:block">{{ getPageTitle }}</h2>
          <div class="relative w-full max-w-xl hidden md:block">
            <div class="relative flex items-center transition-all duration-200 z-[101]" :class="showSearchDropdown ? 'bg-white rounded-t-3xl shadow-sm border-t border-x border-gray-200' : 'bg-gray-100 rounded-full border border-transparent'">
              <svg class="absolute left-4 w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
              <input v-model="searchQuery" @input="handleSearchInput" @focus="handleSearchInput" @blur="hideSearchDropdownDelay" type="text" placeholder="全局搜索：用户、竞赛、经验帖子..." class="w-full bg-transparent text-sm font-medium pl-11 pr-4 py-2.5 outline-none">
            </div>

            <!-- 下拉聚合结果面板 -->
            <div v-if="showSearchDropdown" class="absolute top-[98%] left-0 w-full bg-white rounded-b-3xl shadow-lg border-b border-x border-gray-200 z-[100] pb-2 max-h-[450px] overflow-y-auto">
              <div class="w-[92%] mx-auto h-[1px] bg-gray-100 mb-1"></div>

              <div v-if="isSearching" class="p-6 text-center text-blue-500">
                <svg class="animate-spin h-6 w-6 mx-auto mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                <p class="text-xs font-bold">全网检索中...</p>
              </div>

              <template v-else>
                <!-- 区块 1：找人 -->
                <div v-if="searchResults.users.length > 0" class="mb-2">
                  <div class="px-4 py-2 flex items-center gap-1.5 text-xs font-bold text-gray-400 uppercase tracking-wider bg-gray-50/50">
                    <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg> 相关用户
                  </div>
                  <ul class="px-2 space-y-0.5">
                    <li v-for="user in searchResults.users" :key="user.id">
                      <a @click.prevent="goTo('/user/' + user.id)" class="flex items-center gap-3 p-2 hover:bg-gray-50 rounded-xl cursor-pointer transition">
                        <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${user.id}&backgroundColor=e2e8f0`" class="w-8 h-8 rounded-full border border-gray-200 shadow-sm bg-white">
                        <div class="flex-1 overflow-hidden">
                          <h4 class="text-sm font-bold text-gray-800">{{ user.name }}</h4>
                          <p class="text-[10px] text-gray-500 truncate">{{ user.id }} <span class="mx-0.5">·</span> {{ user.className || '教职工/管理中心' }}</p>
                        </div>
                      </a>
                    </li>
                  </ul>
                </div>

                <!-- 区块 2：找比赛 -->
                <div v-if="searchResults.comps.length > 0" class="mb-2">
                  <div class="px-4 py-2 flex items-center gap-1.5 text-xs font-bold text-gray-400 uppercase tracking-wider bg-gray-50/50">
                    <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path></svg> 竞赛直达
                  </div>
                  <ul class="px-2 space-y-0.5">
                    <li v-for="comp in searchResults.comps" :key="'comp-'+comp.id">
                      <a @click.prevent="goTo('/competitions')" class="block p-2.5 hover:bg-gray-50 rounded-xl cursor-pointer transition">
                        <h4 class="text-sm font-bold text-blue-600 truncate">{{ comp.title }}</h4>
                        <p class="text-[11px] text-gray-500 truncate mt-0.5">{{ comp.description }}</p>
                      </a>
                    </li>
                  </ul>
                </div>

                <!-- 区块 3：找帖子 -->
                <div v-if="searchResults.posts.length > 0">
                  <div class="px-4 py-2 flex items-center gap-1.5 text-xs font-bold text-gray-400 uppercase tracking-wider bg-gray-50/50">
                    <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path></svg> 讨论热帖
                  </div>
                  <ul class="px-2 pb-2 space-y-0.5">
                    <li v-for="post in searchResults.posts" :key="'post-'+post.id">
                      <a @click.prevent="goTo('/forum')" class="block p-2.5 hover:bg-gray-50 rounded-xl cursor-pointer transition border-l-2 border-transparent hover:border-green-400">
                        <h4 class="text-sm font-bold text-gray-800 truncate">{{ post.title }}</h4>
                        <p class="text-[11px] text-gray-500 truncate mt-0.5">{{ post.content }}</p>
                      </a>
                    </li>
                  </ul>
                </div>

                <div v-if="searchQuery && searchResults.users.length === 0 && searchResults.comps.length === 0 && searchResults.posts.length === 0" class="p-6 text-center text-gray-500">
                  <svg class="w-8 h-8 text-gray-300 mx-auto mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM13 10H7"></path></svg>
                  <p class="text-sm font-medium">全网未找到与 "{{ searchQuery }}" 相关的内容</p>
                </div>
              </template>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-6">
          <!-- 全局通知小铃铛 -->
          <div class="relative flex items-center h-full" @mouseleave="showNotifs = false">
            <div @mouseenter="showNotifs = true" class="relative cursor-pointer p-2 rounded-full hover:bg-gray-100 transition">
              <svg class="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path></svg>
              <!-- 动态红点 -->
              <span v-if="unreadCount > 0" class="absolute top-1 right-1 bg-red-500 text-white text-[9px] font-bold px-1.5 py-0.5 rounded-full shadow-sm animate-bounce">{{ unreadCount }}</span>
            </div>

            <!-- 通知下拉面板 -->
            <div v-if="showNotifs" class="absolute right-0 top-[80%] mt-2 w-80 bg-white rounded-2xl shadow-2xl border border-gray-100 overflow-hidden z-50 animate-fade-in-up">
              <div class="px-4 py-3 border-b border-gray-50 flex justify-between items-center bg-gray-50/50">
                <span class="text-sm font-bold text-gray-800">系统通知</span>
                <span class="text-xs text-blue-500 cursor-pointer hover:underline" @click="loadNotifs">刷新</span>
              </div>
              <div class="max-h-[300px] overflow-y-auto">
                <div v-if="notifs.length === 0" class="p-6 text-center text-gray-400 text-sm">暂无新通知</div>
                <div v-else class="divide-y divide-gray-50">
                  <div v-for="n in notifs" :key="n.id" class="p-4 hover:bg-gray-50 transition" :class="n.isRead === 1 ? 'opacity-60' : 'bg-blue-50/20'">
                    <p class="text-xs font-bold mb-1" :class="n.isRead === 1 ? 'text-gray-600' : 'text-blue-600'">{{ n.title }}</p>
                    <p class="text-xs text-gray-500 leading-relaxed">{{ n.content }}</p>
                    <!-- 队伍邀请的处理按钮 -->
                    <div v-if="n.isRead === 0 && n.type === 'team_invite'" class="mt-3 flex gap-2">
                      <button @click="handleInvite(n, 'accept')" class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white text-xs rounded shadow-sm transition">同意上车</button>
                      <button @click="handleInvite(n, 'reject')" class="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-600 text-xs rounded transition">残忍拒绝</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 头像与下拉菜单 -->
          <div class="relative group cursor-pointer h-full flex items-center">
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
            <div class="absolute right-0 top-[90%] pt-2 w-48 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
              <div class="bg-white rounded-xl shadow-xl border border-gray-100 overflow-hidden transform origin-top-right">
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
        </div>
      </header>

      <!-- 🌟 中间大屏幕渲染区域 🌟 -->
      <div class="flex-1 overflow-y-auto p-8 scroll-smooth relative z-0">
        <router-view :key="$route.fullPath"></router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

// 从本地存储获取用户信息，控制侧边栏渲染
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

// ================= 🌟 聚合搜索逻辑 🌟 =================
const searchQuery = ref('')
const showSearchDropdown = ref(false)
const isSearching = ref(false)
let searchTimeout = null
const searchResults = ref({ users: [], comps: [], posts: [] })

const handleSearchInput = () => {
  if (!searchQuery.value.trim()) {
    searchResults.value = { users: [], comps: [], posts: [] }
    showSearchDropdown.value = false
    isSearching.value = false
    return
  }

  showSearchDropdown.value = true
  isSearching.value = true
  clearTimeout(searchTimeout)

  // 防抖处理：用户停止打字 400 毫秒后，再去后台搜，防止请求过于频繁
  searchTimeout = setTimeout(async () => {
    const keyword = searchQuery.value.trim().toLowerCase()
    try {
      // 一次性发出三个请求去不同部门搜人、搜比赛、搜帖子
      const [userRes, compRes, forumRes] = await Promise.all([
        axios.get(`http://localhost:8080/api/user/search?keyword=${keyword}`),
        axios.get(`http://localhost:8080/api/competition/list`),
        axios.get(`http://localhost:8080/api/forum/list`)
      ])

      searchResults.value.users = userRes.data.success ? userRes.data.data.slice(0, 3) : []
      searchResults.value.comps = compRes.data.filter(c =>
          c.title.toLowerCase().includes(keyword) || (c.description && c.description.toLowerCase().includes(keyword))
      ).slice(0, 3)
      searchResults.value.posts = forumRes.data.filter(p =>
          p.title.toLowerCase().includes(keyword) || (p.content && p.content.toLowerCase().includes(keyword))
      ).slice(0, 3)

    } catch (error) {
      console.error("全局聚合搜索失败", error)
    } finally {
      isSearching.value = false
    }
  }, 400)
}

const hideSearchDropdownDelay = () => {
  // 延迟关闭下拉框，防止用户点击结果时直接关掉
  setTimeout(() => { showSearchDropdown.value = false }, 200)
}

const goTo = (path) => {
  showSearchDropdown.value = false
  searchQuery.value = ''
  router.push(path)
}

// ================= 🌟 小铃铛通知逻辑 🌟 =================
const showNotifs = ref(false)
const notifs = ref([])
let notifTimer = null

// 计算红点数字
const unreadCount = computed(() => {
  return notifs.value.filter(n => n.isRead === 0).length
})

const loadNotifs = async () => {
  if (!currentUser.value.id) return
  try {
    const res = await axios.get(`http://localhost:8080/api/notification/list?userId=${currentUser.value.id}`)
    notifs.value = res.data
  } catch (error) {
    console.error("加载通知失败")
  }
}

// 点击通知里“同意/拒绝”队伍邀请
const handleInvite = async (notif, action) => {
  try {
    const res = await axios.post('http://localhost:8080/api/notification/handleInvite', {
      notifId: notif.id,
      teamId: notif.relatedId,
      userId: currentUser.value.id,
      action: action
    })
    if (res.data.success) {
      alert(res.data.message)
      loadNotifs() // 刷新通知列表，红点会消掉
    }
  } catch (e) {
    alert("操作失败")
  }
}

onMounted(() => {
  loadNotifs()
  // 模拟轮询，每30秒去信箱看一眼有没有新信
  notifTimer = setInterval(loadNotifs, 30000)
})

onUnmounted(() => {
  clearInterval(notifTimer) // 页面销毁时清理定时器
})

// ================= 🌟 基础逻辑 🌟 =================
const getPageTitle = computed(() => {
  const pathMap = {
    '/dashboard': '系统首页',
    '/forum': '交流讨论区',
    '/study': '学习资源与真题库',
    '/competitions': '竞赛大厅',
    '/my-apps': '我的报名记录',
    '/review': '报名审核与评分',
    '/teacher-perm': '数据权限与班级管理',
    '/admin-comps': '竞赛项目管理',
    '/admin-users': '全局用户信息表',
    '/admin-approvals': '数据权限审批中心',
    '/profile': '个人中心',
    '/settings': '系统设置'
  }
  if (route.path.startsWith('/user/')) return '用户公开主页'
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
@keyframes fadeInUp { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
.animate-fade-in-up { animation: fadeInUp 0.2s ease-out forwards; }
/* 隐藏侧边栏滚动条，让 UI 更清爽 */
aside nav::-webkit-scrollbar { display: none; }
aside nav { -ms-overflow-style: none; scrollbar-width: none; }
</style>