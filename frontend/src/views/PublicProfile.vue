<template>
  <div class="font-sans pb-10">
    <!-- 顶部绝美渐变横幅 -->
    <div class="h-48 bg-gradient-to-r from-indigo-500 via-blue-600 to-purple-600 rounded-2xl relative overflow-hidden shadow-sm">
      <div class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10"></div>
    </div>

    <div class="max-w-4xl mx-auto px-4 sm:px-6 -mt-20 relative z-10">
      <div v-if="isLoading" class="bg-white rounded-2xl shadow-xl p-10 text-center text-blue-500">
        <svg class="animate-spin h-10 w-10 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
        <p class="font-bold tracking-widest text-sm">正在加载用户档案...</p>
      </div>

      <div v-else-if="!userInfo" class="bg-white rounded-2xl shadow-xl p-16 text-center text-gray-500 border border-gray-100">
        <svg class="w-20 h-20 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        <h2 class="text-2xl font-bold text-gray-800">查无此人</h2>
        <p class="mt-2">该用户可能已被注销或涉嫌违规被封禁</p>
        <button @click="$router.back()" class="mt-6 text-blue-600 font-bold hover:underline">返回上一页</button>
      </div>

      <!-- 核心用户资料展示区 -->
      <div v-else class="bg-white rounded-2xl shadow-xl border border-gray-100 overflow-hidden">
        <div class="p-8 sm:p-10 flex flex-col sm:flex-row gap-8 items-start sm:items-end border-b border-gray-50">

          <!-- 大头像 -->
          <div class="p-1.5 bg-white rounded-full shadow-lg border border-gray-100 -mt-24 sm:-mt-16 shrink-0">
            <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${userInfo.id}&backgroundColor=e2e8f0`" class="w-32 h-32 rounded-full border border-gray-200 bg-gray-50">
          </div>

          <div class="flex-1 w-full">
            <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div>
                <h1 class="text-3xl font-black text-gray-900 flex items-center gap-3">
                  {{ userInfo.name }}
                  <span class="px-2.5 py-0.5 text-xs font-bold rounded-full border" :class="userInfo.role === 'teacher' ? 'bg-emerald-50 text-emerald-600 border-emerald-200' : (userInfo.role === 'admin' ? 'bg-purple-50 text-purple-600 border-purple-200' : 'bg-blue-50 text-blue-600 border-blue-200')">
                    {{ getRoleText(userInfo.role) }}
                  </span>
                </h1>
                <p class="text-sm text-gray-500 mt-2 font-mono flex items-center gap-2">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V8a2 2 0 00-2-2h-5m-4 0V5a2 2 0 114 0v1m-4 0a2 2 0 104 0m-5 8a2 2 0 100-4 2 2 0 000 4zm0 0c1.306 0 2.417.835 2.83 2M9 14a3.001 3.001 0 00-2.83 2M15 11h3m-3 4h3"></path></svg>
                  {{ userInfo.id }} <span class="mx-1">|</span> {{ userInfo.className || '教职工/管理中心' }}
                </p>
              </div>

              <!-- 🌟 社交互动操作按钮 🌟 -->
              <div v-if="friendStatus !== 'self'" class="flex gap-3">
                <button class="bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 px-4 py-2.5 rounded-xl font-bold transition shadow-sm text-sm flex items-center gap-2">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
                  私信
                </button>

                <button v-if="friendStatus === 'loading'" disabled class="bg-gray-100 text-gray-400 px-4 py-2.5 rounded-xl font-bold shadow-sm text-sm flex items-center gap-2">
                  <svg class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg> 验证中
                </button>

                <button v-else-if="friendStatus === 'none'" @click="sendFriendRequest" class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2.5 rounded-xl font-bold transition shadow-sm text-sm flex items-center gap-2">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"></path></svg> 加为好友
                </button>

                <button v-else-if="friendStatus === 'sent'" disabled class="bg-gray-100 text-gray-500 border border-gray-200 px-5 py-2.5 rounded-xl font-bold shadow-sm text-sm flex items-center gap-2 cursor-not-allowed">
                  <svg class="w-4 h-4 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg> 已发送申请
                </button>

                <button v-else-if="friendStatus === 'friends'" disabled class="bg-emerald-50 text-emerald-600 border border-emerald-200 px-5 py-2.5 rounded-xl font-bold shadow-sm text-sm flex items-center gap-2 cursor-not-allowed">
                  <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20"><path d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM17 6a3 3 0 11-6 0 3 3 0 016 0zM12.93 17c.046-.327.07-.66.07-1a6.97 6.97 0 00-1.5-4.33A5 5 0 0119 16v1h-6.07zM6 11a5 5 0 015 5v1H1v-1a5 5 0 015-5z"></path></svg> 我们是好友
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="p-8 bg-gray-50/50">
          <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wider mb-4">关于Ta</h3>
          <div class="bg-white p-5 rounded-xl border border-gray-100 shadow-sm relative">
            <svg class="absolute top-3 left-3 w-5 h-5 text-gray-200" fill="currentColor" viewBox="0 0 32 32"><path d="M10 12c-2.2 0-4-1.8-4-4s1.8-4 4-4 4 1.8 4 4c0 3.3-2.7 6-6 6h-2v-2h2zm14 0c-2.2 0-4-1.8-4-4s1.8-4 4-4 4 1.8 4 4c0 3.3-2.7 6-6 6h-2v-2h2z"></path></svg>
            <p class="text-gray-700 leading-relaxed pl-8">
              {{ userInfo.signature || 'Ta很神秘，没有留下任何个性签名...' }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const userInfo = ref(null)
const isLoading = ref(true)

// 获取当前登录用户
const userStr = localStorage.getItem('currentUser')
const currentUser = userStr ? JSON.parse(userStr) : null

// 好友状态：loading, none, sent, received, friends, self
const friendStatus = ref('loading')

const getRoleText = (role) => {
  const map = { 'student': '学生', 'teacher': '教职工', 'admin': '管理员' }
  return map[role] || '未知'
}

onMounted(async () => {
  const targetId = route.params.id

  try {
    const res = await axios.get(`http://localhost:8080/api/user/publicInfo/${targetId}`)
    if (res.data.success) {
      userInfo.value = res.data.data

      // 如果不是看自己，就去查一下好友状态
      if (currentUser && currentUser.id !== targetId) {
        checkFriendStatus(targetId)
      } else {
        friendStatus.value = 'self' // 是自己看自己
      }
    }
  } catch (error) {
    console.error("加载公共主页失败", error)
  } finally {
    isLoading.value = false
  }
})

const checkFriendStatus = async (targetId) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/friend/check?userId=${currentUser.id}&targetId=${targetId}`)
    friendStatus.value = res.data.status
  } catch (e) {
    friendStatus.value = 'none'
  }
}

const sendFriendRequest = async () => {
  if (!currentUser) return alert("请先登录")
  friendStatus.value = 'loading'
  try {
    const res = await axios.post('http://localhost:8080/api/friend/add', {
      userId: currentUser.id,
      targetId: userInfo.value.id
    })
    if (res.data.success) {
      friendStatus.value = 'sent'
    }
  } catch (error) {
    alert("发送申请失败")
    friendStatus.value = 'none'
  }
}
</script>