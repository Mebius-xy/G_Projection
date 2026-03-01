<template>
  <div class="max-w-4xl mx-auto space-y-6 font-sans">
    <div class="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
      <h2 class="text-2xl font-bold text-gray-800 border-b border-gray-100 pb-4 mb-6">系统设置</h2>

      <div class="space-y-8">
        <!-- 账户安全 -->
        <div>
          <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wider mb-4">账户安全</h3>
          <div class="space-y-4">
            <div class="flex items-center justify-between p-4 bg-gray-50 rounded-xl border border-gray-100">
              <div>
                <p class="font-bold text-gray-800">登录密码</p>
                <p class="text-xs text-gray-500 mt-1">定期修改密码有助于保护账号安全</p>
              </div>
              <button class="px-4 py-2 bg-white border border-gray-200 rounded-lg text-sm font-medium hover:bg-gray-50 transition">修改密码</button>
            </div>
          </div>
        </div>

        <!-- 通知设置 -->
        <div>
          <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wider mb-4">消息与通知</h3>
          <div class="bg-white border border-gray-200 rounded-xl divide-y divide-gray-100">
            <div class="flex items-center justify-between p-4">
              <div>
                <p class="font-bold text-gray-800">竞赛截止提醒</p>
                <p class="text-xs text-gray-500 mt-0.5">当报名的竞赛即将截止时发送通知</p>
              </div>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" checked class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-500"></div>
              </label>
            </div>
            <div class="flex items-center justify-between p-4">
              <div>
                <p class="font-bold text-gray-800">成绩发布通知</p>
                <p class="text-xs text-gray-500 mt-0.5">指导教师完成评分后立即通知我</p>
              </div>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" checked class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-500"></div>
              </label>
            </div>
          </div>
        </div>

        <!-- 隐私设置 -->
        <div>
          <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wider mb-4">隐私与展示</h3>
          <div class="bg-white border border-gray-200 rounded-xl divide-y divide-gray-100">
            <div class="flex items-center justify-between p-4">
              <div>
                <p class="font-bold text-gray-800">允许其他人通过搜索找到我</p>
                <p class="text-xs text-gray-500 mt-0.5">关闭后，其他学生无法在添加好友时搜到你</p>
              </div>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" checked class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-500"></div>
              </label>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>