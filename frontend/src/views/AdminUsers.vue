<template>
  <div class="space-y-6 font-sans">
    <!-- 头部横幅 -->
    <div class="flex justify-between items-center bg-white p-5 rounded-2xl shadow-[0_2px_10px_rgba(0,0,0,0.02)] border border-gray-100">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 bg-purple-50 text-purple-600 rounded-xl flex items-center justify-center text-2xl shadow-inner border border-purple-100">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">用户数据管理中心</h2>
          <p class="text-sm text-gray-500 mt-1">管理全校学生与教职工账号，支持海量数据分页查询。</p>
        </div>
      </div>
      <span class="bg-red-50 border border-red-100 text-red-600 text-xs font-bold px-3 py-1.5 rounded-lg flex items-center shadow-sm">
        <svg class="w-3.5 h-3.5 mr-1" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M10 1.944A11.954 11.954 0 012.166 5C2.056 5.649 2 6.319 2 7c0 5.225 3.34 9.67 8 11.317C14.66 16.67 18 12.225 18 7c0-.682-.057-1.35-.166-2.001A11.954 11.954 0 0110 1.944zM11 14a1 1 0 11-2 0 1 1 0 012 0zm0-7a1 1 0 10-2 0v3a1 1 0 102 0V7z" clip-rule="evenodd"></path></svg>
        超级权限
      </span>
    </div>

    <div class="bg-white rounded-2xl shadow-[0_2px_10px_rgba(0,0,0,0.02)] border border-gray-100 overflow-hidden flex flex-col">

      <!-- 工具栏：分表标签页与操作 -->
      <div class="p-5 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
        <!-- 💡 角色分表筛选器 -->
        <div class="flex bg-white p-1.5 rounded-lg border border-gray-200 shadow-sm gap-1">
          <button @click="changeRoleFilter('')" :class="activeRole === '' ? 'bg-blue-50 text-blue-700 shadow-sm font-bold border border-blue-100' : 'text-gray-500 hover:bg-gray-50 border border-transparent'" class="px-5 py-1.5 text-sm rounded-md transition-all">全校总库</button>
          <button @click="changeRoleFilter('student')" :class="activeRole === 'student' ? 'bg-blue-50 text-blue-700 shadow-sm font-bold border border-blue-100' : 'text-gray-500 hover:bg-gray-50 border border-transparent'" class="px-5 py-1.5 text-sm rounded-md transition-all">学生库</button>
          <button @click="changeRoleFilter('teacher')" :class="activeRole === 'teacher' ? 'bg-blue-50 text-blue-700 shadow-sm font-bold border border-blue-100' : 'text-gray-500 hover:bg-gray-50 border border-transparent'" class="px-5 py-1.5 text-sm rounded-md transition-all">教师库</button>
        </div>

        <div class="text-sm text-gray-500 font-medium bg-white px-4 py-2 rounded-lg border border-gray-100 shadow-sm">
          共查询到 <span class="text-blue-600 font-bold mx-1 text-base">{{ totalRecords }}</span> 条数据
        </div>
      </div>

      <!-- 数据表格区域 -->
      <div class="relative min-h-[450px]">
        <!-- 加载遮罩层 -->
        <div v-if="isLoading" class="absolute inset-0 bg-white/70 backdrop-blur-[2px] z-10 flex flex-col items-center justify-center text-blue-600">
          <svg class="animate-spin h-10 w-10 mb-3" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
          <span class="font-bold tracking-widest text-sm">拉取数据中...</span>
        </div>

        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
          <tr>
            <th class="py-4 px-6 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">身份标识 (ID)</th>
            <th class="py-4 px-6 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">姓名</th>
            <th class="py-4 px-6 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">角色组</th>
            <th class="py-4 px-6 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">组织架构 (班级/部门)</th>
            <th class="py-4 px-6 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">账户安全状态</th>
          </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-100">
          <tr v-for="user in tableData" :key="user.id" class="hover:bg-blue-50/40 transition-colors">
            <td class="py-4 px-6 whitespace-nowrap text-sm font-bold text-gray-800 font-mono">{{ user.id }}</td>
            <td class="py-4 px-6 whitespace-nowrap text-sm text-gray-700 font-bold">{{ user.name }}</td>
            <td class="py-4 px-6 whitespace-nowrap">
                <span class="px-3 py-1 text-xs font-bold rounded-md border" :class="getRoleBadgeClass(user.role)">
                  {{ getRoleText(user.role) }}
                </span>
            </td>
            <td class="py-4 px-6 whitespace-nowrap text-sm text-gray-500">{{ user.className || '--' }}</td>
            <td class="py-4 px-6 whitespace-nowrap text-sm">
              <div v-if="user.isFirstLogin" class="flex items-center text-yellow-700 bg-yellow-50 px-2.5 py-1 rounded-md w-fit border border-yellow-200">
                <svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path></svg>
                <span class="font-bold text-xs">需改密</span>
              </div>
              <div v-else class="flex items-center text-green-600 font-medium">
                <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                正常
              </div>
            </td>
          </tr>
          <!-- 骨架占位，如果数据为空 -->
          <tr v-if="tableData.length === 0 && !isLoading">
            <td colspan="5" class="py-20 text-center text-gray-400">
              <svg class="w-16 h-16 mx-auto mb-4 text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path></svg>
              <p class="text-base font-medium">暂无相关数据</p>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- 💡 底部精装分页器 💡 -->
      <div class="p-4 border-t border-gray-100 bg-gray-50 flex items-center justify-between">
        <div class="text-sm text-gray-500 bg-white px-3 py-1.5 rounded-lg border border-gray-200 shadow-sm">
          当前第 <span class="font-bold text-blue-600">{{ currentPage }}</span> 页 / 共 <span class="font-bold text-gray-800">{{ totalPages }}</span> 页
        </div>

        <div class="flex gap-2">
          <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1 || isLoading" class="px-4 py-2 bg-white border border-gray-200 rounded-lg text-sm font-bold text-gray-600 hover:bg-blue-50 hover:text-blue-600 hover:border-blue-200 transition-all disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:border-gray-200 disabled:hover:text-gray-600 flex items-center shadow-sm">
            <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path></svg> 上一页
          </button>

          <!-- 页码直达选单 -->
          <select v-model="currentPage" @change="fetchData" class="px-4 py-2 border border-gray-200 rounded-lg text-sm font-bold text-gray-700 focus:ring-2 focus:ring-blue-500 outline-none bg-white cursor-pointer shadow-sm">
            <option v-for="p in totalPages" :key="p" :value="p">第 {{ p }} 页</option>
          </select>

          <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages || isLoading" class="px-4 py-2 bg-white border border-gray-200 rounded-lg text-sm font-bold text-gray-600 hover:bg-blue-50 hover:text-blue-600 hover:border-blue-200 transition-all disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:border-gray-200 disabled:hover:text-gray-600 flex items-center shadow-sm">
            下一页 <svg class="w-4 h-4 ml-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path></svg>
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import axios from 'axios'

// 核心数据状态
const tableData = ref([])
const isLoading = ref(true)

// 分页与过滤状态
const currentPage = ref(1)
const pageSize = ref(8) // 每页显示8条，这样我们塞进去的20条测试数据正好能分3页
const totalPages = ref(1)
const totalRecords = ref(0)
const activeRole = ref('') // 空字符串代表全查

// 核心拉取数据方法：呼叫后厨的 /api/user/page
const fetchData = async () => {
  isLoading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/user/page', {
      params: {
        currentPage: currentPage.value,
        pageSize: pageSize.value,
        role: activeRole.value
      }
    })

    // 把后厨传回来的包装盒拆开
    tableData.value = res.data.records
    totalRecords.value = res.data.total
    totalPages.value = res.data.pages || 1
  } catch (error) {
    console.error("分页数据加载失败:", error)
    alert("网络连接失败，请确认后端的 Spring Boot 已重启并安装了分页插件！")
  } finally {
    isLoading.value = false
  }
}

// 页面加载拉取第一页
onMounted(() => {
  fetchData()
})

// 操作：切换分表标签 (点击 学生库/教师库 时触发)
const changeRoleFilter = (role) => {
  activeRole.value = role
  currentPage.value = 1 // 切分类时，必须强行回到第1页
  fetchData()
}

// 操作：点击上一页/下一页
const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage
    fetchData()
  }
}

// 样式小工具
const getRoleText = (role) => {
  const map = {'student': '学生', 'teacher': '教职工', 'admin': '系统管理员'}
  return map[role] || role
}
const getRoleBadgeClass = (role) => {
  const map = {
    'student': 'bg-blue-50 text-blue-700 border-blue-200',
    'teacher': 'bg-emerald-50 text-emerald-700 border-emerald-200',
    'admin': 'bg-purple-50 text-purple-700 border-purple-200'
  }
  return map[role] || 'bg-gray-100 text-gray-800 border-gray-200'
}
</script>