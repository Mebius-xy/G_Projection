<template>
  <div>
    <!-- 头部横幅 -->
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-xl shadow-sm border border-gray-100">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 bg-purple-50 text-purple-600 rounded-xl flex items-center justify-center text-xl border border-purple-100">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800">全局用户信息表</h2>
          <p class="text-xs text-gray-500 mt-0.5">上帝视角：查看、检索与管理全站人员数据</p>
        </div>
      </div>

      <!-- 🌟 核心新增：导出 Excel 按钮 🌟 -->
      <button @click="exportToExcel" :disabled="isExporting" class="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2.5 px-5 rounded-lg shadow-md transition-all flex items-center gap-2 disabled:opacity-50">
        <svg v-if="isExporting" class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
        <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path></svg>
        {{ isExporting ? '正在打包下载...' : '一键导出 Excel 花名册' }}
      </button>
    </div>

    <!-- 搜索与筛选区 -->
    <div class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 mb-6 flex flex-wrap gap-4 items-center">
      <div class="relative flex-1 min-w-[200px]">
        <svg class="absolute left-3 top-3 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
        <input v-model="searchQuery" @input="fetchUsers" type="text" placeholder="按姓名、学号或班级模糊搜索..." class="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-purple-300 outline-none transition bg-gray-50 text-sm">
      </div>

      <select v-model="roleFilter" @change="fetchUsers" class="px-4 py-2 border border-gray-200 rounded-lg bg-gray-50 text-sm focus:ring-2 focus:ring-purple-300 outline-none cursor-pointer">
        <option value="">全部身份</option>
        <option value="student">仅看学生</option>
        <option value="teacher">仅看教师</option>
        <option value="admin">仅看管理员</option>
      </select>
    </div>

    <!-- 加载中 -->
    <div v-if="isLoading" class="text-center py-20 text-purple-500">
      <svg class="animate-spin h-8 w-8 mx-auto mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      <p class="text-sm font-bold">正在拉取全站数据...</p>
    </div>

    <!-- 用户数据表格 -->
    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm whitespace-nowrap">
          <thead class="bg-gray-50/80 text-gray-500 uppercase tracking-wider font-bold border-b border-gray-100">
          <tr>
            <th class="px-6 py-4">系统ID (学号/工号)</th>
            <th class="px-6 py-4">姓名</th>
            <th class="px-6 py-4">系统角色</th>
            <th class="px-6 py-4">班级 / 部门</th>
            <th class="px-6 py-4">首次登录</th>
            <th class="px-6 py-4">竞赛积分</th>
            <th class="px-6 py-4 text-center">操作</th>
          </tr>
          </thead>
          <tbody class="divide-y divide-gray-50">
          <tr v-if="users.length === 0">
            <td colspan="7" class="px-6 py-16 text-center text-gray-400">没有找到符合条件的用户</td>
          </tr>
          <tr v-for="u in users" :key="u.id" class="hover:bg-purple-50/30 transition">
            <td class="px-6 py-4 font-mono text-gray-600">{{ u.id }}</td>
            <td class="px-6 py-4 font-bold text-gray-800">
              <div class="flex items-center gap-3">
                <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${u.id}&backgroundColor=e2e8f0`" class="w-8 h-8 rounded-full border border-gray-200">
                {{ u.name }}
              </div>
            </td>
            <td class="px-6 py-4">
                <span class="px-2.5 py-1 rounded-full text-xs font-bold border" :class="getRoleStyle(u.role)">
                  {{ getRoleName(u.role) }}
                </span>
            </td>
            <td class="px-6 py-4 text-gray-600">{{ u.className || '--' }}</td>
            <td class="px-6 py-4">
                <span v-if="u.isFirstLogin === 1" class="text-red-500 font-bold bg-red-50 px-2 py-0.5 rounded text-xs flex items-center w-max">
                  <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path></svg>未激活
                </span>
              <span v-else class="text-gray-400 text-xs flex items-center"><svg class="w-3 h-3 mr-1 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>已激活</span>
            </td>
            <td class="px-6 py-4">
              <span v-if="u.role === 'student'" class="font-bold text-emerald-600 bg-emerald-50 px-2 py-1 rounded">{{ u.baseScore }} 分</span>
              <span v-else class="text-gray-300">-</span>
            </td>
            <td class="px-6 py-4 text-center">
              <button @click="$router.push(`/user/${u.id}`)" class="text-blue-500 hover:text-blue-700 font-bold text-sm bg-blue-50 hover:bg-blue-100 px-3 py-1.5 rounded transition">查看主页</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- 极其精简的分页控件 -->
      <div class="px-6 py-4 border-t border-gray-100 flex justify-between items-center bg-gray-50">
        <span class="text-sm text-gray-500">共检索到 <strong class="text-gray-800">{{ total }}</strong> 名用户</span>
        <div class="flex gap-2">
          <button @click="changePage(page - 1)" :disabled="page <= 1" class="px-3 py-1.5 border rounded bg-white text-sm disabled:opacity-50 hover:bg-gray-100 transition">上一页</button>
          <span class="px-4 py-1.5 text-sm font-bold text-gray-700">{{ page }} / {{ Math.ceil(total / pageSize) || 1 }}</span>
          <button @click="changePage(page + 1)" :disabled="page * pageSize >= total" class="px-3 py-1.5 border rounded bg-white text-sm disabled:opacity-50 hover:bg-gray-100 transition">下一页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const users = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const isLoading = ref(true)

const searchQuery = ref('')
const roleFilter = ref('')

// 抓取列表数据
const fetchUsers = async () => {
  isLoading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/user/page', {
      params: {
        page: page.value,
        pageSize: pageSize.value,
        keyword: searchQuery.value,
        role: roleFilter.value
      }
    })
    users.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error("拉取数据失败:", error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => fetchUsers())

const changePage = (p) => {
  page.value = p
  fetchUsers()
}

// ================== 🌟 核心：导出 Excel 逻辑 🌟 ==================
const isExporting = ref(false)

const exportToExcel = async () => {
  isExporting.value = true
  try {
    // 1. 发起请求，注意必须要加 responseType: 'blob'，否则接到的就是一堆乱码字符！
    const res = await axios.get('http://localhost:8080/api/user/export', {
      responseType: 'blob'
    })

    // 2. 把二进制流打包成一个 Blob 对象 (告诉浏览器这是个 Excel 文件)
    const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })

    // 3. 用魔法在浏览器里造一个临时的下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.style.display = 'none'
    link.href = url

    // 4. 给下载的文件起个名字
    link.setAttribute('download', `CIMS系统用户花名册_${new Date().getTime()}.xlsx`)

    // 5. 偷偷把它塞进页面，点击它，然后再销毁尸体！
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url) // 释放内存

  } catch (error) {
    alert("导出失败，请检查网络或联系管理员！")
  } finally {
    isExporting.value = false
  }
}

// 辅助样式工具
const getRoleName = (role) => {
  const map = { 'student': '学生', 'teacher': '教职工', 'admin': '超级管理员' }
  return map[role] || role
}
const getRoleStyle = (role) => {
  const map = {
    'student': 'bg-blue-50 text-blue-600 border-blue-200',
    'teacher': 'bg-emerald-50 text-emerald-600 border-emerald-200',
    'admin': 'bg-purple-50 text-purple-600 border-purple-200'
  }
  return map[role] || 'bg-gray-50 text-gray-600 border-gray-200'
}
</script>