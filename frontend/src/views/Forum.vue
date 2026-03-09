<template>
  <div class="relative min-h-full font-sans pb-10">
    <!-- 头部横幅与操作区 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-xl shadow-sm border border-gray-100">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center text-xl border border-blue-100">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">交流讨论区</h2>
          <p class="text-sm text-gray-500 mt-0.5">组队招募、赛事答疑、经验分享，畅所欲言</p>
        </div>
      </div>

      <button @click="showAddModal = true" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2.5 px-5 rounded-lg shadow-md transition-all flex items-center gap-2 transform hover:-translate-y-0.5">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
        发起新讨论
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="isLoading" class="text-center py-32 text-blue-500 flex flex-col items-center">
      <svg class="animate-spin h-10 w-10 text-blue-500 mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      <span class="font-bold tracking-widest text-sm">加载讨论列表中...</span>
    </div>

    <!-- 暂无帖子 -->
    <div v-else-if="posts.length === 0" class="text-center py-24 bg-white rounded-2xl shadow-sm border border-gray-100">
      <svg class="w-20 h-20 text-gray-200 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
      <p class="text-gray-500 text-lg font-medium">当前还没有任何讨论，快来抢个沙发吧！</p>
    </div>

    <!-- 帖子瀑布流列表 -->
    <div v-else class="space-y-4">
      <div
          v-for="post in posts" :key="post.id"
          @click="openPostDetail(post)"
          class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition duration-300 relative overflow-hidden cursor-pointer group flex justify-between items-start"
      >
        <!-- 左侧彩色装饰条 -->
        <div class="absolute left-0 top-0 bottom-0 w-1.5 transition-colors" :class="getTagColor(post.tag)"></div>

        <!-- 左侧帖子内容主体 -->
        <div class="flex-1 pr-6 pl-2">
          <h3 class="font-bold text-lg text-gray-800 group-hover:text-blue-600 transition">{{ post.title }}</h3>
          <p class="text-gray-600 text-sm mt-2 line-clamp-2 leading-relaxed">{{ post.content }}</p>

          <div class="flex items-center gap-5 mt-4 text-xs text-gray-500">
            <span class="flex items-center gap-1.5 font-medium" :class="getRoleTextColor(post.authorRole)">
              <!-- 🌟 替换为本地头像生成器 -->
              <img :src="generateAvatar(post.authorName)" class="w-5 h-5 rounded-full border border-gray-200">
              {{ post.authorName }}
            </span>
            <span class="flex items-center gap-1">
              <svg class="w-3.5 h-3.5 opacity-70" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
              {{ post.createTime.replace('T', ' ').substring(0, 16) }}
            </span>
            <span class="flex items-center gap-1 text-blue-500 font-bold bg-blue-50 px-2 py-0.5 rounded-full">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"></path></svg>
              {{ post.commentCount || 0 }}
            </span>
          </div>
        </div>

        <!-- 右侧：标签与特权删帖按钮 -->
        <div class="flex flex-col items-end gap-3 shrink-0">
          <span class="px-3 py-1 bg-gray-50 text-gray-600 text-xs font-bold rounded-md border border-gray-200 shadow-sm whitespace-nowrap">{{ post.tag }}</span>

          <!-- 🌟 核心修复：加上 post.authorName === currentUser.name 的双重比对，并换成醒目红色 🌟 -->
          <button
              @click.stop="handleDeletePost(post.id)"
              v-if="currentUser && (String(currentUser.id) === String(post.authorId) || currentUser.name === post.authorName || currentUser.role === 'admin')"
              class="text-red-500 hover:text-red-700 bg-red-50 hover:bg-red-100 px-3 py-2 rounded-lg transition-colors border border-red-100 flex items-center gap-1 shadow-sm"
              title="永久删除此贴"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
            <span class="text-xs font-bold">删除</span>
          </button>
        </div>
      </div>
    </div>

    <!-- ========================================================= -->
    <!-- 🌟 高级交互：右侧滑出的帖子详情抽屉 (Drawer) 🌟 -->
    <!-- ========================================================= -->
    <div v-if="showDetailDrawer" class="fixed inset-0 z-[110] flex justify-end">
      <!-- 黑色半透明背景遮罩 -->
      <div class="absolute inset-0 bg-gray-900/40 backdrop-blur-sm transition-opacity" @click="closeDrawer"></div>

      <!-- 抽屉本体 -->
      <div class="bg-white w-full max-w-2xl h-full shadow-2xl flex flex-col transform transition-transform animate-slide-in-right relative z-10">

        <!-- 抽屉头部：标题栏 -->
        <div class="px-6 py-4 border-b border-gray-100 flex justify-between items-center bg-gray-50/80 shrink-0">
          <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wider flex items-center gap-2">
            <svg class="w-4 h-4 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
            讨论详情
          </h3>
          <button @click="closeDrawer" class="text-gray-400 hover:text-red-500 transition bg-white w-8 h-8 rounded-full shadow-sm border border-gray-100 flex items-center justify-center">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <!-- 抽屉中部：滚动内容区 (原贴 + 评论列表) -->
        <div class="flex-1 overflow-y-auto bg-gray-50/30 p-6 scroll-smooth">

          <!-- 原贴区块 (楼主) -->
          <div v-if="currentPost" class="bg-white p-6 rounded-2xl shadow-[0_2px_12px_rgba(0,0,0,0.04)] border border-gray-100 mb-8 relative">
            <span class="absolute top-4 right-4 text-[10px] font-bold px-2 py-1 rounded bg-blue-50 text-blue-600 border border-blue-100">楼主 (原贴)</span>
            <h2 class="text-2xl font-bold text-gray-900 mb-4 pr-16">{{ currentPost.title }}</h2>

            <div class="flex items-center gap-3 mb-6">
              <!-- 🌟 替换为本地头像生成器 -->
              <img :src="generateAvatar(currentPost.authorName)" class="w-10 h-10 rounded-full shadow-sm border border-gray-200">
              <div>
                <p class="font-bold text-sm text-gray-800" :class="getRoleTextColor(currentPost.authorRole)">{{ currentPost.authorName }}</p>
                <p class="text-xs text-gray-400">{{ currentPost.createTime.replace('T', ' ').substring(0, 16) }}</p>
              </div>
            </div>

            <div class="text-gray-700 leading-relaxed text-sm whitespace-pre-wrap bg-gray-50 p-4 rounded-xl border border-gray-100">
              {{ currentPost.content }}
            </div>
          </div>

          <div class="flex items-center justify-between mb-4 px-2">
            <h4 class="font-bold text-gray-800 text-lg">全部评论 ({{ comments.length }})</h4>
          </div>

          <!-- 评论列表区块 -->
          <div v-if="isCommentsLoading" class="text-center py-10 text-gray-400">
            <svg class="animate-spin h-6 w-6 mx-auto mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            <p class="text-xs">正在搬运数据...</p>
          </div>

          <div v-else-if="comments.length === 0" class="text-center py-12 bg-white rounded-xl border border-dashed border-gray-200">
            <p class="text-gray-400 text-sm">还没有人回复，快来发表你的高见吧！</p>
          </div>

          <div v-else class="space-y-4">
            <div v-for="(comment, index) in comments" :key="comment.id" class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex gap-4 animate-fade-in-up">
              <!-- 🌟 替换为本地头像生成器 -->
              <img :src="generateAvatar(comment.authorName)" class="w-10 h-10 rounded-full border border-gray-200 shrink-0">

              <div class="flex-1">
                <div class="flex justify-between items-center mb-1">
                  <span class="font-bold text-sm text-gray-800" :class="getRoleTextColor(comment.authorRole)">{{ comment.authorName }}</span>
                  <span class="text-xs font-bold text-gray-400 bg-gray-100 px-2 py-0.5 rounded">#{{ index + 1 }} 楼</span>
                </div>
                <p class="text-xs text-gray-400 mb-3">{{ comment.createTime.replace('T', ' ').substring(0, 16) }}</p>
                <div class="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">
                  {{ comment.content }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 抽屉底部：盖楼输入区 -->
        <div class="p-5 border-t border-gray-200 bg-white shrink-0">
          <form @submit.prevent="submitComment" class="flex gap-3">
            <!-- 🌟 替换为本地头像生成器 -->
            <img :src="generateAvatar(currentUser?.name)" class="w-10 h-10 rounded-full border border-gray-200 hidden sm:block">
            <div class="flex-1 relative">
              <input
                  v-model="newCommentText"
                  type="text"
                  required
                  class="w-full bg-gray-50 border border-gray-200 text-sm rounded-xl pl-4 pr-24 py-3 focus:bg-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                  placeholder="写下你的评论..."
              >
              <button
                  type="submit"
                  :disabled="isCommentSubmitting || !newCommentText.trim()"
                  class="absolute right-1.5 top-1.5 bottom-1.5 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold px-4 rounded-lg transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center shadow-sm"
              >
                <svg v-if="isCommentSubmitting" class="animate-spin h-3.5 w-3.5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                <span v-else>发送</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- ========================================================= -->
    <!-- 发帖弹窗 -->
    <!-- ========================================================= -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-[120]">
      <div class="bg-white rounded-2xl shadow-2xl w-[600px] overflow-hidden animate-fade-in-up border border-gray-100">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800 flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"></path></svg>
            发起新讨论
          </h3>
          <button @click="showAddModal = false" class="text-gray-400 hover:text-red-500 transition">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
          </button>
        </div>

        <form @submit.prevent="submitPost" class="p-6 space-y-5">
          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">讨论标题 <span class="text-red-500">*</span></label>
            <input v-model="form.title" type="text" required class="shadow-sm border border-gray-200 rounded-lg w-full py-2.5 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none transition" placeholder="一句话概括你的核心主题">
          </div>

          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">讨论标签 <span class="text-red-500">*</span></label>
            <div class="flex flex-wrap gap-3">
              <label v-for="t in availableTags" :key="t" class="cursor-pointer">
                <input type="radio" v-model="form.tag" :value="t" class="sr-only peer">
                <div class="px-4 py-1.5 rounded-full border border-gray-200 text-sm font-medium text-gray-500 peer-checked:bg-blue-500 peer-checked:text-white peer-checked:border-blue-500 transition shadow-sm">
                  {{ t }}
                </div>
              </label>
            </div>
          </div>

          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">详细内容 <span class="text-red-500">*</span></label>
            <textarea v-model="form.content" required rows="5" class="shadow-sm border border-gray-200 rounded-lg w-full py-2.5 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none transition leading-relaxed" placeholder="详细描述你想分享的经验、或者想寻找什么样的队友..."></textarea>
          </div>

          <div class="flex justify-end gap-3 pt-2">
            <button type="button" @click="showAddModal = false" class="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 rounded-lg hover:bg-gray-50 transition shadow-sm font-medium">取消</button>
            <button type="submit" :disabled="isSubmitting" class="px-5 py-2.5 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition flex items-center gap-2 font-bold shadow-md disabled:bg-blue-400">
              <svg v-if="isSubmitting" class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
              发布讨论
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
// 🌟 引入刚刚写好的本地纯色头像生成器
import { generateAvatar } from '../utils/avatar'

const userStr = localStorage.getItem('currentUser')
const currentUser = userStr ? JSON.parse(userStr) : null

// =============== 1. 帖子列表逻辑 ===============
const posts = ref([])
const isLoading = ref(true)

const fetchPosts = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/forum/list')
    posts.value = res.data
  } catch (error) {
    alert("拉取帖子列表失败！请确认后端已启动。")
  } finally {
    isLoading.value = false
  }
}

onMounted(() => fetchPosts())

// 🌟 新增：特权删帖逻辑
const handleDeletePost = async (postId) => {
  if (!confirm("确定要永久删除这篇帖子及所有评论吗？操作不可逆！")) return
  try {
    // 调用后端的删帖接口，带上操作人的身份进行双重校验
    const res = await axios.post(`http://localhost:8080/api/forum/delete/${postId}?userId=${currentUser.id}&role=${currentUser.role}`)
    if (res.data.success) {
      fetchPosts() // 重新拉取列表，被删的帖子就会立刻消失
    } else {
      alert(res.data.message)
    }
  } catch (error) {
    alert("删除失败，网络错误或后端服务未响应！")
  }
}

// =============== 2. 抽屉与评论逻辑 ===============
const showDetailDrawer = ref(false)
const currentPost = ref(null)
const comments = ref([])
const isCommentsLoading = ref(false)
const newCommentText = ref('')
const isCommentSubmitting = ref(false)

const openPostDetail = async (post) => {
  currentPost.value = post
  showDetailDrawer.value = true
  isCommentsLoading.value = true
  comments.value = []

  try {
    const res = await axios.get(`http://localhost:8080/api/forum/comments/${post.id}`)
    comments.value = res.data
  } catch (error) {
    console.error("拉取评论失败", error)
  } finally {
    isCommentsLoading.value = false
  }
}

const closeDrawer = () => {
  showDetailDrawer.value = false
  setTimeout(() => {
    currentPost.value = null
    comments.value = []
  }, 300)
}

const submitComment = async () => {
  if (!currentUser) return

  isCommentSubmitting.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/forum/addComment', {
      postId: currentPost.value.id,
      authorId: currentUser.id,
      content: newCommentText.value
    })

    if (res.data.success) {
      newCommentText.value = ''
      const commentsRes = await axios.get(`http://localhost:8080/api/forum/comments/${currentPost.value.id}`)
      comments.value = commentsRes.data
      fetchPosts()
    }
  } catch (error) {
    alert("发送失败！")
  } finally {
    isCommentSubmitting.value = false
  }
}

// =============== 3. 发帖表单逻辑 ===============
const showAddModal = ref(false)
const isSubmitting = ref(false)
const availableTags = ['经验分享', '组队招募', '赛事答疑', '资源求助', '闲聊灌水']
const form = reactive({ title: '', tag: '经验分享', content: '' })

const submitPost = async () => {
  if (!currentUser) return
  isSubmitting.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/forum/add', {
      authorId: currentUser.id,
      title: form.title,
      tag: form.tag,
      content: form.content
    })
    if (res.data.success) {
      showAddModal.value = false
      form.title = ''
      form.content = ''
      fetchPosts()
    }
  } catch (error) {
    alert("发帖失败！")
  } finally {
    isSubmitting.value = false
  }
}

const getTagColor = (tag) => {
  const map = { '经验分享': 'bg-green-500', '组队招募': 'bg-blue-500', '赛事答疑': 'bg-purple-500', '资源求助': 'bg-yellow-500', '闲聊灌水': 'bg-gray-400' }
  return map[tag] || 'bg-blue-300'
}
const getRoleTextColor = (role) => {
  const map = { 'student': 'text-blue-600', 'teacher': 'text-emerald-600', 'admin': 'text-purple-600' }
  return map[role] || 'text-gray-700'
}
</script>

<style scoped>
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up { animation: fadeInUp 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards; }

@keyframes slideInRight {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}
.animate-slide-in-right { animation: slideInRight 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards; }

.overflow-y-auto::-webkit-scrollbar { width: 6px; }
.overflow-y-auto::-webkit-scrollbar-thumb { background-color: #cbd5e1; border-radius: 4px; }
</style>