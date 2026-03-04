<template>
  <div class="max-w-6xl mx-auto space-y-6 font-sans">
    <!-- 页面标题 -->
    <div class="flex justify-between items-center bg-white p-5 rounded-2xl shadow-sm border border-gray-100">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center text-xl shadow-inner border border-blue-100">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">个人中心</h2>
          <p class="text-sm text-gray-500 mt-0.5">管理您的个人资料、社交好友与参赛队伍</p>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- 左侧：个人资料展示卡 -->
      <div class="lg:col-span-1 space-y-6">
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden relative">
          <div class="h-24 bg-gradient-to-r from-blue-500 to-indigo-600"></div>

          <div class="px-6 pb-6 relative">
            <div class="absolute -top-12 left-6 p-1 bg-white rounded-full shadow-md">
              <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${currentUser.id}&backgroundColor=e2e8f0`" class="w-20 h-20 rounded-full border border-gray-200">
            </div>

            <div class="pt-12">
              <h1 class="text-2xl font-bold text-gray-800">{{ currentUser.name }}</h1>
              <p class="text-xs text-gray-500 mt-1 font-mono">ID: {{ currentUser.id }}</p>

              <div class="mt-4 space-y-2">
                <p class="text-sm text-gray-600 flex items-center gap-2">
                  <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path></svg>
                  {{ currentUser.className || '系统运营中心' }}
                </p>
                <p class="text-sm text-gray-600 flex items-center gap-2">
                  <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path></svg>
                  身份: {{ currentUser.role === 'student' ? '学生' : (currentUser.role === 'teacher' ? '教职工' : '管理员') }}
                </p>
                <p v-if="currentUser.role === 'student'" class="text-sm text-emerald-600 font-bold flex items-center gap-2">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path></svg>
                  竞赛积分: {{ currentUser.baseScore || 0 }}
                </p>
              </div>

              <div class="mt-6 p-4 bg-gray-50 rounded-xl border border-gray-100 relative">
                <svg class="absolute top-2 left-2 w-4 h-4 text-gray-300" fill="currentColor" viewBox="0 0 32 32"><path d="M10 12c-2.2 0-4-1.8-4-4s1.8-4 4-4 4 1.8 4 4c0 3.3-2.7 6-6 6h-2v-2h2zm14 0c-2.2 0-4-1.8-4-4s1.8-4 4-4 4 1.8 4 4c0 3.3-2.7 6-6 6h-2v-2h2z"></path></svg>
                <p class="text-sm text-gray-600 italic pl-5 pt-1 leading-relaxed">
                  {{ currentUser.signature || '这个人很懒，什么都没留下...' }}
                </p>
              </div>

              <button @click="openEditModal" class="w-full mt-6 bg-blue-50 text-blue-600 hover:bg-blue-600 hover:text-white font-bold py-2.5 rounded-xl transition-all duration-300 flex items-center justify-center gap-2 border border-blue-100">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
                编辑个人资料
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：社交与业务展示区 -->
      <div class="lg:col-span-2 bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden flex flex-col">
        <!-- 标签导航 -->
        <div class="flex border-b border-gray-100 bg-gray-50/80 px-2 shrink-0">
          <button @click="activeTab = 'friends'" :class="activeTab === 'friends' ? 'border-b-2 border-blue-500 text-blue-600 font-bold bg-white' : 'text-gray-500 hover:text-gray-800'" class="px-6 py-4 text-sm transition relative">
            我的通讯录 ({{ friendsList.length }})
            <span v-if="friendRequests.length > 0" class="absolute top-3 right-2 w-2 h-2 bg-red-500 rounded-full animate-pulse"></span>
          </button>
          <button @click="activeTab = 'teams'" :class="activeTab === 'teams' ? 'border-b-2 border-blue-500 text-blue-600 font-bold bg-white' : 'text-gray-500 hover:text-gray-800'" class="px-6 py-4 text-sm transition">
            我的队伍 ({{ myTeams.length }})
          </button>
        </div>

        <div class="p-6 flex-1 overflow-y-auto">
          <!-- 🌟 通讯录面板 🌟 -->
          <div v-if="activeTab === 'friends'" class="space-y-6">

            <!-- 新好友申请区块 -->
            <div v-if="friendRequests.length > 0" class="bg-yellow-50/50 p-4 rounded-xl border border-yellow-100">
              <h4 class="text-xs font-bold text-yellow-800 uppercase tracking-wider mb-3 flex items-center">
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path></svg>
                新好友申请 ({{ friendRequests.length }})
              </h4>
              <div class="space-y-2">
                <div v-for="req in friendRequests" :key="req.requestId" class="flex justify-between items-center bg-white p-3 rounded-lg border border-yellow-200/50 shadow-sm hover:shadow-md transition">
                  <div class="flex items-center gap-3">
                    <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${req.requesterId}&backgroundColor=e2e8f0`" class="w-10 h-10 rounded-full border border-gray-100">
                    <div>
                      <p class="font-bold text-gray-800 text-sm">{{ req.requesterName }} <span class="text-xs font-normal text-gray-500 ml-1">请求加为好友</span></p>
                      <p class="text-[11px] text-gray-400 mt-0.5">{{ req.className || '无班级信息' }}</p>
                    </div>
                  </div>
                  <button @click="acceptFriend(req.requestId)" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-1.5 rounded-lg text-xs font-bold transition shadow-sm">同意</button>
                </div>
              </div>
            </div>

            <!-- 我的好友列表 -->
            <div>
              <h4 class="text-xs font-bold text-gray-400 uppercase tracking-wider mb-3">全部好友</h4>
              <div v-if="friendsList.length === 0" class="text-center py-12 text-gray-400">
                <svg class="w-16 h-16 mx-auto mb-3 text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
                <p class="text-sm font-medium">通讯录空空如也，去论坛认识些新朋友吧！</p>
              </div>

              <div v-else class="grid grid-cols-1 sm:grid-cols-2 gap-3">
                <div v-for="friend in friendsList" :key="friend.id" class="flex items-center justify-between p-3 hover:bg-gray-50 rounded-xl border border-gray-100 transition group cursor-pointer" @click="$router.push(`/user/${friend.id}`)">
                  <div class="flex items-center gap-3 overflow-hidden">
                    <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${friend.id}&backgroundColor=e2e8f0`" class="w-10 h-10 rounded-full border border-gray-200 shadow-sm bg-white shrink-0">
                    <div class="overflow-hidden">
                      <p class="font-bold text-gray-800 text-sm group-hover:text-blue-600 transition">{{ friend.name }}</p>
                      <p class="text-xs text-gray-500 truncate">{{ friend.signature || friend.className || '这人很懒' }}</p>
                    </div>
                  </div>
                  <button @click.stop class="text-gray-400 hover:text-blue-500 p-2 rounded-full transition bg-white border border-gray-100 shadow-sm shrink-0">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 🌟 队伍管理面板 🌟 -->
          <div v-if="activeTab === 'teams'" class="space-y-6">
            <div class="flex justify-between items-center mb-4">
              <h4 class="text-sm font-bold text-gray-800">我所在的大巴车</h4>
              <button @click="showCreateTeam = true" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-bold shadow-sm flex items-center gap-2 transition">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path></svg> 创建新队伍
              </button>
            </div>

            <div v-if="myTeams.length === 0" class="text-center py-16 text-gray-400 border border-dashed rounded-xl">
              <svg class="w-16 h-16 mx-auto mb-3 text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path></svg>
              <p class="text-sm font-medium">你还没有加入任何队伍，自己当队长建一个吧！</p>
            </div>

            <!-- 队伍卡片 -->
            <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div v-for="team in myTeams" :key="team.id" class="bg-gradient-to-br from-white to-blue-50/30 p-5 rounded-2xl border border-blue-100 shadow-sm relative overflow-hidden group flex flex-col justify-between h-full min-h-[160px]">
                <div class="absolute -right-6 -bottom-6 opacity-10 group-hover:scale-110 transition duration-500">
                  <svg class="w-32 h-32 text-blue-600" fill="currentColor" viewBox="0 0 24 24"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"></path></svg>
                </div>

                <div class="relative z-10">
                  <div class="flex justify-between items-start mb-2">
                    <h3 class="text-lg font-black text-gray-800 tracking-tight pr-2">{{ team.name }}</h3>
                    <span v-if="team.isCaptain" class="bg-yellow-100 text-yellow-700 text-[10px] font-bold px-2 py-1 rounded border border-yellow-200 whitespace-nowrap">队长是我</span>
                    <span v-else class="bg-gray-100 text-gray-500 text-[10px] font-bold px-2 py-1 rounded border border-gray-200 whitespace-nowrap">队员</span>
                  </div>
                  <p class="text-xs text-gray-500 mb-5 line-clamp-1 italic">"{{ team.slogan || '这支队伍很低调...' }}"</p>
                </div>

                <div class="relative z-10 flex justify-between items-center border-t border-blue-100/50 pt-3 mt-auto">
                  <span class="text-sm font-bold text-blue-600 flex items-center gap-1">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857"></path></svg>
                    已上车: {{ team.memberCount }} 人
                  </span>

                  <!-- 🌟 核心修复：队长解散与队员退出机制 🌟 -->
                  <div v-if="team.isCaptain" class="flex gap-2">
                    <button @click="handleDismissTeam(team.id)" class="text-xs bg-white text-red-500 border border-red-200 hover:bg-red-50 px-2 py-1.5 rounded-lg font-bold transition shadow-sm" title="解散队伍">
                      解散车队
                    </button>
                    <button @click="openInviteModal(team.id)" class="text-xs bg-white text-blue-600 border border-blue-200 hover:bg-blue-50 px-2 py-1.5 rounded-lg font-bold transition shadow-sm" title="邀请好友">
                      + 邀请
                    </button>
                  </div>
                  <div v-else>
                    <button @click="handleLeaveTeam(team.id)" class="text-xs bg-white text-gray-500 border border-gray-200 hover:bg-gray-100 px-3 py-1.5 rounded-lg font-bold transition shadow-sm">
                      跳车 (退出)
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <!-- ================= 各种弹窗组件 ================= -->

    <!-- 1. 修改资料精装弹窗 -->
    <div v-if="showEditModal" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm flex items-center justify-center z-[100]">
      <div class="bg-white rounded-2xl shadow-2xl w-[450px] overflow-hidden animate-fade-in-up border border-gray-100">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
            编辑个人资料
          </h3>
          <button @click="showEditModal = false" class="text-gray-400 hover:text-red-500 transition w-8 h-8 flex items-center justify-center rounded-full hover:bg-red-50">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>
        <form @submit.prevent="submitEdit" class="p-6 space-y-5">
          <div>
            <label class="block text-gray-700 text-sm font-bold mb-2">姓名 / 昵称 <span class="text-red-500">*</span></label>
            <input v-model="editForm.name" type="text" required class="shadow-sm border border-gray-200 rounded-lg w-full py-2.5 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition">
          </div>
          <div>
            <label class="block text-gray-700 text-sm font-bold mb-2">个性签名</label>
            <textarea v-model="editForm.signature" rows="3" class="shadow-sm border border-gray-200 rounded-lg w-full py-2.5 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition" placeholder="写点什么来彰显个性吧..."></textarea>
          </div>
          <div class="flex justify-end gap-3 pt-4">
            <button type="button" @click="showEditModal = false" class="px-5 py-2.5 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium">取消</button>
            <button type="submit" :disabled="isSubmitting" class="px-5 py-2.5 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition shadow-md font-bold flex items-center gap-2 disabled:bg-blue-400">
              <svg v-if="isSubmitting" class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
              保存修改
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 2. 创建队伍弹窗 -->
    <div v-if="showCreateTeam" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm flex items-center justify-center z-[100]">
      <div class="bg-white rounded-2xl w-[400px] overflow-hidden shadow-2xl animate-fade-in-up border border-gray-100">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path></svg>
            建立新队伍
          </h3>
          <button @click="showCreateTeam = false" class="text-gray-400 hover:text-red-500 transition w-8 h-8 flex items-center justify-center rounded-full hover:bg-red-50">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-gray-700 text-xs font-bold mb-2 uppercase">队伍名称 <span class="text-red-500">*</span></label>
            <input v-model="newTeam.name" placeholder="起个霸气的队名" class="w-full border border-gray-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-blue-300 transition">
          </div>
          <div>
            <label class="block text-gray-700 text-xs font-bold mb-2 uppercase">队伍口号 (选填)</label>
            <input v-model="newTeam.slogan" placeholder="比如：蓝桥杯必胜！" class="w-full border border-gray-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-blue-300 transition">
          </div>
          <button @click="submitCreateTeam" :disabled="!newTeam.name.trim()" class="w-full bg-blue-600 text-white font-bold py-3 mt-2 rounded-xl hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed shadow-md">
            确定建队并上车
          </button>
        </div>
      </div>
    </div>

    <!-- 3. 邀请好友上车弹窗 -->
    <div v-if="inviteTeamId" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm flex items-center justify-center z-[100]">
      <div class="bg-white rounded-2xl w-[450px] overflow-hidden shadow-2xl animate-fade-in-up border border-gray-100">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"></path></svg>
            邀请通讯录好友上车
          </h3>
          <button @click="inviteTeamId = null" class="text-gray-400 hover:text-red-500 transition w-8 h-8 flex items-center justify-center rounded-full hover:bg-red-50">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <div class="p-4 max-h-[400px] overflow-y-auto">
          <div v-if="friendsList.length === 0" class="text-center py-10 text-gray-400">
            <p class="text-sm">你的通讯录空空如也，先去加点好友吧！</p>
          </div>
          <div v-else class="space-y-2">
            <div v-for="friend in friendsList" :key="friend.id" class="flex justify-between items-center p-3 border border-gray-100 rounded-xl hover:bg-gray-50 transition">
              <div class="flex items-center gap-3">
                <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${friend.id}&backgroundColor=e2e8f0`" class="w-10 h-10 rounded-full border border-gray-200 bg-white">
                <div>
                  <span class="text-sm font-bold text-gray-800 block">{{ friend.name }}</span>
                  <span class="text-[10px] text-gray-500">{{ friend.className || '神秘好友' }}</span>
                </div>
              </div>
              <button @click="sendInvite(friend.id)" class="text-xs bg-blue-50 text-blue-600 border border-blue-100 px-4 py-2 rounded-lg font-bold hover:bg-blue-600 hover:text-white transition shadow-sm">
                发送邀请
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// ================== 基础状态 ==================
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

// 默认停在队伍页方便测试
const activeTab = ref('teams')

// ================== 修改资料逻辑 ==================
const showEditModal = ref(false)
const isSubmitting = ref(false)
const editForm = reactive({ name: '', signature: '' })

const openEditModal = () => {
  editForm.name = currentUser.value.name
  editForm.signature = currentUser.value.signature || ''
  showEditModal.value = true
}

const submitEdit = async () => {
  isSubmitting.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/user/updateProfile', {
      id: currentUser.value.id,
      name: editForm.name,
      signature: editForm.signature
    })

    if (res.data.success) {
      const updatedUser = res.data.data
      localStorage.setItem('currentUser', JSON.stringify(updatedUser))
      currentUser.value = updatedUser
      showEditModal.value = false
    } else {
      alert("修改失败：" + res.data.message)
    }
  } catch (error) {
    alert("网络异常，无法保存修改！")
  } finally {
    isSubmitting.value = false
  }
}

// ================== 社交与队伍拉取逻辑 ==================
const friendsList = ref([])
const friendRequests = ref([])
const myTeams = ref([])

const loadData = async () => {
  if (!currentUser.value.id) return
  try {
    const [listRes, reqRes, teamRes] = await Promise.all([
      axios.get(`http://localhost:8080/api/friend/list?userId=${currentUser.value.id}`),
      axios.get(`http://localhost:8080/api/friend/requests?userId=${currentUser.value.id}`),
      axios.get(`http://localhost:8080/api/team/my?userId=${currentUser.value.id}`)
    ])
    friendsList.value = listRes.data
    friendRequests.value = reqRes.data
    myTeams.value = teamRes.data
  } catch (error) {
    console.error("加载个人中心数据失败", error)
  }
}

onMounted(() => {
  loadData()
})

// ================== 好友操作逻辑 ==================
const acceptFriend = async (requestId) => {
  try {
    const res = await axios.post('http://localhost:8080/api/friend/accept', { requestId })
    if (res.data.success) {
      loadData()
    }
  } catch (error) {
    alert("同意请求失败")
  }
}

// ================== 队伍操作逻辑 ==================
const showCreateTeam = ref(false)
const newTeam = reactive({ name: '', slogan: '' })
const inviteTeamId = ref(null)

const submitCreateTeam = async () => {
  try {
    const res = await axios.post('http://localhost:8080/api/team/create', {
      name: newTeam.name,
      slogan: newTeam.slogan,
      captainId: currentUser.value.id
    })
    if (res.data.success) {
      showCreateTeam.value = false
      newTeam.name = ''
      newTeam.slogan = ''
      loadData()
    }
  } catch (e) {
    alert("建队失败，请检查网络！")
  }
}

const openInviteModal = (teamId) => {
  inviteTeamId.value = teamId
}

const sendInvite = async (friendId) => {
  try {
    const res = await axios.post('http://localhost:8080/api/team/invite', {
      teamId: inviteTeamId.value,
      friendId: friendId,
      captainId: currentUser.value.id
    })
    if(res.data.success) {
      alert('邀请信已放入对方的右上角小铃铛！')
      inviteTeamId.value = null
    } else {
      alert(res.data.message)
    }
  } catch (e) {
    alert("发送邀请失败")
  }
}

// 🌟 新增：报废大巴车 (解散队伍)
const handleDismissTeam = async (teamId) => {
  if (!confirm("警告：解散车队将丢失所有乘客信息，确认解散吗？")) return
  try {
    const res = await axios.post(`http://localhost:8080/api/team/dismiss/${teamId}?userId=${currentUser.value.id}`)
    if(res.data.success) {
      loadData() // 刷新大巴车列表
    } else {
      alert(res.data.message)
    }
  } catch(e) {
    alert("操作失败！")
  }
}

// 🌟 新增：跳车 (退出队伍)
const handleLeaveTeam = async (teamId) => {
  if (!confirm("确认要退出这个队伍吗？")) return
  try {
    const res = await axios.post('http://localhost:8080/api/team/leave', {
      teamId: teamId,
      userId: currentUser.value.id
    })
    if(res.data.success) {
      loadData() // 刷新大巴车列表
    } else {
      alert(res.data.message)
    }
  } catch(e) {
    alert("操作失败！")
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

/* 美化滚动条 */
.overflow-y-auto::-webkit-scrollbar {
  width: 6px;
}
.overflow-y-auto::-webkit-scrollbar-thumb {
  background-color: #cbd5e1;
  border-radius: 4px;
}
</style>