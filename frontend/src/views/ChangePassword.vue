<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-md w-96 border-t-4 border-red-500">
      <div class="text-center mb-4">
        <div class="w-12 h-12 bg-red-100 text-red-500 rounded-full flex items-center justify-center mx-auto mb-2 text-xl">
          <i class="fas fa-shield-alt"></i>
        </div>
        <h2 class="text-xl font-bold text-gray-800">首次登录安全设置</h2>
        <p class="text-sm text-gray-500 mt-1">系统检测到您是首次登录，为保障账号安全，请修改初始密码。</p>
      </div>

      <form @submit.prevent="submitPasswordChange" class="space-y-4">
        <div>
          <label class="block text-gray-700 text-sm font-bold mb-2">新密码</label>
          <input v-model="pwdForm.newPwd" type="password" required class="shadow border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:ring focus:border-red-300" placeholder="建议包含字母和数字">
        </div>
        <div>
          <label class="block text-gray-700 text-sm font-bold mb-2">确认新密码</label>
          <input v-model="pwdForm.confirmPwd" type="password" required class="shadow border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:ring focus:border-red-300" placeholder="请再次输入新密码">
        </div>
        <button type="submit" :disabled="isLoading" class="w-full bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded transition">
          {{ isLoading ? '提交中...' : '确认修改并进入系统' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const isLoading = ref(false)
const pwdForm = reactive({ newPwd: '', confirmPwd: '' })

// 从 localStorage 里拿出刚才登录存进来的个人信息
const currentUserStr = localStorage.getItem('currentUser')
const currentUser = currentUserStr ? JSON.parse(currentUserStr) : null

const submitPasswordChange = async () => {
  if (!currentUser) {
    alert("登录信息丢失，请重新登录！")
    router.push('/login')
    return
  }

  if (pwdForm.newPwd !== pwdForm.confirmPwd) {
    alert("两次输入的新密码不一致！")
    return
  }

  isLoading.value = true
  try {
    // 呼叫传菜员，去我们刚刚在后端写的 /api/user/updatePassword 接口
    const response = await axios.post('http://localhost:8080/api/user/updatePassword', {
      userId: currentUser.id,
      newPassword: pwdForm.newPwd
    })

    if (response.data.success) {
      alert("密码修改成功！欢迎进入系统！")
      // 修改成功后，更新本地存的房卡，把 isFirstLogin 改成 false
      currentUser.isFirstLogin = false
      localStorage.setItem('currentUser', JSON.stringify(currentUser))

      // 正式放行去主页！
      router.push('/dashboard')
    } else {
      alert(response.data.message)
    }
  } catch (error) {
    console.error(error)
    alert("网络请求失败，请检查后端！")
  } finally {
    isLoading.value = false
  }
}
</script>