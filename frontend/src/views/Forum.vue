<template>
  <div>
    <!-- 头部横幅与操作区 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <div class="flex items-center gap-2">
        <div class="w-10 h-10 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center text-xl">
          <i class="fas fa-comments"></i>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">交流讨论区</h2>
          <p class="text-xs text-gray-500">组队招募、赛事答疑、经验分享，畅所欲言</p>
        </div>
      </div>

      <button @click="showAddModal = true" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-lg shadow-sm transition flex items-center gap-2">
        <i class="fas fa-edit"></i> 发起新讨论
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="isLoading" class="text-center py-20 text-blue-500">
      <i class="fas fa-spinner fa-spin text-4xl mb-4"></i>
    </div>

    <!-- 暂无帖子 -->
    <div v-else-if="posts.length === 0" class="text-center py-20 bg-white rounded-xl shadow-sm border border-gray-100">
      <i class="far fa-comment-dots text-6xl text-gray-300 mb-4"></i>
      <p class="text-gray-500 text-lg">当前还没有任何讨论，快来抢个沙发吧！</p>
    </div>

    <!-- 帖子列表 -->
    <div v-else class="space-y-4">
      <div v-for="post in posts" :key="post.id" class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition duration-300 relative overflow-hidden group">
        <!-- 左侧彩色条装饰 (根据标签变换颜色) -->
        <div class="absolute left-0 top-0 bottom-0 w-1" :class="getTagColor(post.tag)"></div>

        <div class="flex justify-between items-start">
          <div class="flex-1">
            <h3 class="font-bold text-lg text-gray-800 group-hover:text-blue-600 transition">{{ post.title }}</h3>
            <p class="text-gray-600 text-sm mt-2 line-clamp-2 leading-relaxed">{{ post.content }}</p>

            <div class="flex items-center gap-4 mt-4 text-xs text-gray-500">
              <span class="flex items-center gap-1.5 font-medium" :class="getRoleTextColor(post.authorRole)">
                <i :class="getRoleIcon(post.authorRole)"></i> {{ post.authorName }}
              </span>
              <span class="flex items-center gap-1"><i class="far fa-clock"></i> {{ post.createTime.replace('T', ' ').substring(0, 16) }}</span>
            </div>
          </div>
          <span class="px-3 py-1 bg-gray-50 text-gray-600 text-xs rounded border border-gray-200 shadow-sm ml-4 whitespace-nowrap">{{ post.tag }}</span>
        </div>
      </div>
    </div>

    <!-- 发帖弹窗 -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-2xl w-[600px] overflow-hidden animate-fade-in-up">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800"><i class="fas fa-pen-nib text-blue-500 mr-2"></i>发起新讨论</h3>
          <button @click="showAddModal = false" class="text-gray-400 hover:text-red-500"><i class="fas fa-times text-xl"></i></button>
        </div>

        <form @submit.prevent="submitPost" class="p-6 space-y-4">
          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">讨论标题 <span class="text-red-500">*</span></label>
            <input v-model="form.title" type="text" required class="shadow-sm border rounded w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none" placeholder="一句话概括你的核心主题">
          </div>

          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">讨论标签 <span class="text-red-500">*</span></label>
            <div class="flex gap-3">
              <label v-for="t in availableTags" :key="t" class="cursor-pointer">
                <input type="radio" v-model="form.tag" :value="t" class="sr-only peer">
                <div class="px-4 py-1.5 rounded-full border border-gray-200 text-sm font-medium text-gray-600 peer-checked:bg-blue-500 peer-checked:text-white peer-checked:border-blue-500 transition shadow-sm">
                  {{ t }}
                </div>
              </label>
            </div>
          </div>

          <div>
            <label class="block text-gray-700 font-bold mb-2 text-sm">详细内容 <span class="text-red-500">*</span></label>
            <textarea v-model="form.content" required rows="5" class="shadow-sm border rounded w-full py-2 px-3 text-gray-700 focus:ring-2 focus:ring-blue-300 outline-none leading-relaxed" placeholder="详细描述你想分享的经验、或者想寻找什么样的队友..."></textarea>
          </div>

          <div class="flex justify-end gap-3 pt-4">
            <button type="button" @click="showAddModal = false" class="px-5 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition">取消</button>
            <button type="submit" :disabled="isSubmitting" class="px-5 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition flex items-center gap-2">
              <i class="fas fa-spinner fa-spin" v-if="isSubmitting"></i>
              <i class="fas fa-paper-plane" v-else></i> 发布讨论
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

const userStr = localStorage.getItem('currentUser')
const currentUser = userStr ? JSON.parse(userStr) : null

const posts = ref([])
const isLoading = ref(true)

const fetchPosts = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/forum/list')
    posts.value = res.data
  } catch (error) {
    alert("拉取帖子列表失败！")
  } finally {
    isLoading.value = false
  }
}

onMounted(() => fetchPosts())

// =========== 发帖逻辑 ===========
const showAddModal = ref(false)
const isSubmitting = ref(false)
const availableTags = ['经验分享', '组队招募', '赛事答疑', '资源求助', '闲聊灌水']

const form = reactive({ title: '', tag: '经验分享', content: '' })

const submitPost = async () => {
  if (!currentUser) {
    alert("登录信息丢失，请重新登录！")
    return
  }

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
      fetchPosts() // 刷新列表，新帖子就会出现在最上面
    }
  } catch (error) {
    alert("发帖失败，请检查网络！")
  } finally {
    isSubmitting.value = false
  }
}

// =========== 样式辅助工具 ===========
const getTagColor = (tag) => {
  const map = { '经验分享': 'bg-green-500', '组队招募': 'bg-blue-500', '赛事答疑': 'bg-purple-500', '资源求助': 'bg-yellow-500', '闲聊灌水': 'bg-gray-400' }
  return map[tag] || 'bg-blue-300'
}
const getRoleIcon = (role) => {
  const map = { 'student': 'fas fa-user-graduate', 'teacher': 'fas fa-chalkboard-teacher', 'admin': 'fas fa-user-shield' }
  return map[role] || 'fas fa-user'
}
const getRoleTextColor = (role) => {
  const map = { 'student': 'text-blue-600', 'teacher': 'text-emerald-600', 'admin': 'text-purple-600' }
  return map[role] || 'text-gray-500'
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
</style>