import { createRouter, createWebHistory } from 'vue-router'

// 导入两个最核心的外壳和门面
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login' // 根目录默认跳转到登录
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/change-password',
      name: 'changePassword',
      component: () => import('../views/ChangePassword.vue')
    },

    // ================= 核心业务区域 (被 Layout 相框包裹) =================
    {
      path: '/',
      component: Layout,
      children: [
        // 1. 任何人登录后都能看的公共区域
        { path: 'dashboard', name: 'dashboard', component: () => import('../views/Dashboard.vue') },
        { path: 'forum', name: 'forum', component: () => import('../views/Forum.vue') },
        { path: 'study', name: 'study', component: () => import('../views/StudyZone.vue') },
        { path: 'profile', name: 'profile', component: () => import('../views/Profile.vue') },
        { path: 'settings', name: 'settings', component: () => import('../views/Settings.vue') },
        { path: 'user/:id', name: 'publicProfile', component: () => import('../views/PublicProfile.vue') },

        // 🌟 修复：将竞赛大厅从“仅限学生”挪到公共区域，让老师也能看 🌟
        { path: 'competitions', name: 'competitions', component: () => import('../views/Competitions.vue') },

        // 2. 学生专属区域
        {
          path: 'my-apps',
          name: 'myApps',
          component: () => import('../views/MyApplications.vue'),
          meta: { roles: ['student'] }
        },

        // 3. 教师专属区域
        {
          path: 'review',
          name: 'review',
          component: () => import('../views/Review.vue'),
          meta: { roles: ['teacher'] }
        },
        {
          path: 'teacher-perm',
          name: 'teacherPerm',
          component: () => import('../views/TeacherPermission.vue'),
          meta: { roles: ['teacher'] }
        },

        // 4. 管理员专属区域
        {
          path: 'admin-comps',
          name: 'adminComps',
          component: () => import('../views/AdminCompetitions.vue'),
          meta: { roles: ['admin'] }
        },
        {
          path: 'admin-users',
          name: 'adminUsers',
          component: () => import('../views/AdminUsers.vue'),
          meta: { roles: ['admin'] }
        },
        {
          path: 'admin-approvals',
          name: 'adminApprovals',
          component: () => import('../views/AdminApprovals.vue'),
          meta: { roles: ['admin'] }
        }
      ]
    },

    // 捕获所有瞎敲的乱码网址，统统丢回主页
    {
      path: '/:pathMatch(.*)*',
      redirect: '/dashboard'
    }
  ]
})

// ================= 🌟 核心安保系统：全局前置守卫 🌟 =================
router.beforeEach((to, from, next) => {
  // 1. 如果用户要去登录页，直接放行
  if (to.path === '/login') {
    return next();
  }

  // 2. 检查有没有房卡 (有没有登录过)
  const userStr = localStorage.getItem('currentUser');
  if (!userStr) {
    alert('非法访问：请先登录系统！');
    return next('/login');
  }

  const currentUser = JSON.parse(userStr);

  // 3. 强制改密拦截
  if (currentUser.isFirstLogin && to.path !== '/change-password') {
    alert('安全警告：请先完成初始密码修改！');
    return next('/change-password');
  }

  // 4. 🌟 权限越界拦截 (管理员绿灯特权) 🌟
  // 如果这个页面设置了门禁名单 (to.meta.roles)
  // 并且当前用户不在名单里 (!to.meta.roles.includes(currentUser.role))
  // 并且当前用户【不是管理员】 (currentUser.role !== 'admin')
  if (to.meta.roles && !to.meta.roles.includes(currentUser.role) && currentUser.role !== 'admin') {
    alert(`越权操作拦截：您的身份 (${currentUser.role}) 无权访问此页面！`);
    return next('/dashboard');
  }

  // 安检全部通过，放行
  next();
})

export default router