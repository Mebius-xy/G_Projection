<template>
  <div class="max-w-6xl mx-auto space-y-6 font-sans pb-10">

    <!-- 顶部个人资料卡片 -->
    <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
      <!-- 渐变色背景 -->
      <div class="h-48 bg-gradient-to-r from-blue-400 via-indigo-500 to-purple-600 relative"></div>

      <div class="px-8 pb-8 relative flex justify-between items-end">
        <div class="flex items-end -mt-16 gap-6 relative z-10">
          <div class="relative group">
            <!-- 动态卡通头像：根据用户 ID 自动生成唯一头像 -->
            <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${currentUser.id}&backgroundColor=e2e8f0`" alt="Avatar" class="w-32 h-32 rounded-full border-4 border-white shadow-md bg-white">
          </div>
          <div class="pb-2">
            <h1 class="text-3xl font-bold text-gray-800">{{ currentUser.name }}</h1>
            <p class="text-gray-500 mt-1 flex items-center gap-2">
              <span class="bg-blue-50 text-blue-600 px-2 py-0.5 rounded text-xs font-bold">{{ currentUser.role === 'student' ? '学生' : (currentUser.role === 'teacher' ? '教师' : '管理员') }}</span>
              <span>{{ currentUser.className || '系统运营中心' }}</span>
              <span v-if="currentUser.role === 'student'" class="ml-2 text-xs font-bold text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded">
                竞赛积分: {{ currentUser.baseScore || 0 }}
              </span>
            </p>
          </div>
        </div>

        <div class="pb-2 flex gap-3">
          <button @click="openEditModal" class="bg-blue-500 hover:bg-blue-600 text-white px-5 py-2 rounded-lg font-medium transition shadow-sm flex items-center gap-2">
            <i class="fas fa-edit"></i> 编辑资料
          </button>
        </div>
      </div>
    </div>

    <!-- 底部内容区：分栏布局 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">

      <!-- 左侧边栏：关于我 & 数据 -->
      <div class="space-y-6">
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
          <h3 class="font-bold text-lg text-gray-800 mb-4 flex items-center gap-2">
            <i class="fas fa-quote-left text-blue-500"></i> 个性签名
          </h3>
          <p class="text-gray-600 text-sm italic leading-relaxed">
            "{{ currentUser.signature || '这个人很懒，什么都没留下...' }}"
          </p>

          <hr class="my-5 border-gray-100">

          <h3 class="font-bold text-lg text-gray-800 mb-4 flex items-center gap-2">
            <i class="fas fa-tags text-purple-500"></i> 我的标签
          </h3>
          <div class="flex flex-wrap gap-2">
            <span class="px-3 py-1 bg-gray-100 text-gray-600 text-xs rounded-full">新晋赛手</span>
            <span class="px-3 py-1 bg-blue-50 text-blue-600 text-xs rounded-full font-medium" v-if="currentUser.role === 'student'">寻找队伍中</span>
            <span class="px-3 py-1 bg-emerald-50 text-emerald-600 text-xs rounded-full font-medium" v-else>金牌导师</span>
          </div>
        </div>
      </div>

      <!-- 右侧主内容区：好友与队伍 (后续扩展社交功能的预留位) -->
      <div class="md:col-span-2 bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="flex border-b border-gray-100 bg-gray-50/50">
          <button @click="activeTab = 'friends'" :class="activeTab === 'friends' ? 'border-b-2 border-blue-500 text-blue-600 font-bold bg-white' : 'text-gray-500 hover:text-gray-700'" class="px-6 py-4 text-sm transition">
            我的好友 (1)
          </button>
          <button @click="activeTab = 'teams'" :class="activeTab === 'teams' ? 'border-b-2 border-blue-500 text-blue-600 font-bold bg-white' : 'text-gray-500 hover:text-gray-700'" class="px-6 py-4 text-sm transition">
            我的队伍 (0)
          </button>
        </div>

        <div class="p-6">
          <div v-if="activeTab === 'friends'" class="space-y-4">
            <div class="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg border border-gray-100 transition">
              <div class="flex items-center gap-3">
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=admin&backgroundColor=e2e8f0" class="w-10 h-10 rounded-full bg-gray-100" alt="admin">
                <div>
                  <p class="font-bold text-gray-800 text-sm">系统小助手</p>
                  <p class="text-xs text-gray-500">有任何问题随时反馈哦！</p>
                </div>
              </div>
              <button class="text-blue-500 hover:bg-blue-50 px-3 py-1.5 rounded text-xs font-medium transition border border-blue-100">发消息</button>
            </div>
            <p class="text-center text-gray-400 text-xs mt-6">添加好友与社交功能正在路上...</p>
          </div>

          <div v-if="activeTab === 'teams'" class="text-center py-10 text-gray-400">
            <i class="fas fa-users-slash text-4xl mb-3 text-gray-200"></i>
            <p>你还没有加入任何队伍，去论坛找找队友吧！</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 💡 修改资料弹窗 💡 -->
    <div v-if="showEditModal" class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl shadow-2xl w-[450px] overflow-hidden animate-fade-in-up">
        <div class="p-5 border-b bg-gray-50 flex justify-between items-center">
          <h3 class="text-lg font-bold text-gray-800">编辑个人资料</h3>
          <button @click="showEditModal = false" class="text-gray-400 hover:text-red-500 transition">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>

        <form @submit.prevent="submitEdit" class="p-6 space-y-5">
          <div>
            <label class="block text-gray-700 text-sm font-bold mb-2">姓名 / 昵称</label>
            <input v-model="editForm.name" type="text" required class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition">
          </div>

          <div>
            <label class="block text-gray-700 text-sm font-bold mb-2">个性签名</label>
            <textarea v-model="editForm.signature" rows="3" class="shadow-sm border rounded-lg w-full py-2.5 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-300 transition" placeholder="写点什么来彰显个性吧..."></textarea>
          </div>

          <div class="flex justify-end gap-3 pt-2">
            <button type="button" @click="showEditModal = false" class="px-5 py-2.5 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium">取消</button>
            <button type="submit" :disabled="isSubmitting" class="px-5 py-2.5 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition shadow-md font-medium flex items-center gap-2 disabled:bg-blue-300">
              <i class="fas fa-spinner fa-spin" v-if="isSubmitting"></i>
              <i class="fas fa-check" v-else></i> 保存修改
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import axios from 'axios'

// 1. 取出本地缓存的用户信息
const userStr = localStorage.getItem('currentUser')
const currentUser = ref(userStr ? JSON.parse(userStr) : { name: '游客', role: 'student' })

const activeTab = ref('friends')

// ======= 2. 修改资料逻辑 =======
const showEditModal = ref(false)
const isSubmitting = ref(false)

// 响应式的表单数据（魔法复写纸）
const editForm = reactive({ name: '', signature: '' })

// 打开弹窗时，把旧数据填进输入框
const openEditModal = () => {
  editForm.name = currentUser.value.name
  editForm.signature = currentUser.value.signature || ''
  showEditModal.value = true
}

// 提交表单
const submitEdit = async () => {
  isSubmitting.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/user/updateProfile', {
      id: currentUser.value.id,
      name: editForm.name,
      signature: editForm.signature
    })

    if (res.data.success) {
      // 🌟 核心：成功后，必须同步更新浏览器里的缓存数据
      const updatedUser = res.data.data
      localStorage.setItem('currentUser', JSON.stringify(updatedUser))
      currentUser.value = updatedUser // 触发Vue响应式更新视图，头像和名字瞬间改变

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
</script>

<style scoped>
/* 弹窗滑入动画 */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fadeInUp 0.3s ease-out forwards;
}
</style>