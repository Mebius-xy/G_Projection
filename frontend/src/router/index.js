import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import ChangePassword from '../views/ChangePassword.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // ================= 独立房间 (不带侧边栏和顶栏) =================
    {
      path: '/login',
      name: 'login',
      component: Login // 系统大门
    },
    {
      path: '/change-password',
      name: 'changePassword',
      component: ChangePassword // 强制改密室
    },

    // ================= 商场主体内部 (带侧边栏和顶栏) =================
    {
      path: '/',
      component: Layout, // 商场骨架外壳
      redirect: '/dashboard', // 一进商场，默认把视角切到首页
      children: [
        // 1. 公共区域
        {
          path: 'dashboard',
          name: 'dashboard',
          component: Dashboard
        },

        // 2. 学生区域
        {
          path: 'competitions',
          name: 'competitions',
          // 【懒加载技术】() => import(...) 意思是：
          // 只有当用户真的点击了这个菜单，系统才会去下载这个页面的代码。
          // 这样能让你的系统在第一次打开时速度飞快！
          component: () => import('../views/Competitions.vue')
        },
        {
          path: 'my-apps',
          name: 'myApps',
          component: () => import('../views/MyApplications.vue')
        },

        // 3. 教师区域
        {
          path: 'review',
          name: 'review',
          component: () => import('../views/Review.vue')
        },
        {
          path: 'teacher-perm',
          name: 'teacherPerm',
          component: () => import('../views/TeacherPermission.vue')
        },

        // 4. 管理员区域
        {
          path: 'admin-users',
          name: 'adminUsers',
          component: () => import('../views/AdminUsers.vue')
        },
        {
          path: 'admin-approvals',
          name: 'adminApprovals',
          component: () => import('../views/AdminApprovals.vue')
        },
        {
          path:'admin-comps',
          name: 'adminComps',
          component: () => import('../views/AdminCompetitions.vue')
        },
        {
          path:'forum',
          name: 'forum',
          component: () => import('../views/Forum.vue')
        },
        {
          path:'profile',
          name: 'profile',
          component: () => import('../views/Profile.vue')
        },
        {
          path:'settings',
          name: 'settings',
          component: () => import('../views/Settings.vue')
        },
        {
          path:'study',
          name: 'study',
          component: () => import('../views/StudyZone.vue')
        }
      ]
    }
  ]
})

/**
 * ================= 全局走廊保安 (路由守卫) =================
 * 每次你点链接、或者在地址栏敲回车，保安都会先拦住你，执行这个函数。
 * to: 你想去哪
 * from: 你从哪来
 * next: 决定你是通过、还是被踢走
 */
router.beforeEach((to, from, next) => {
  // 掏出口袋里的信息
  const token = localStorage.getItem('token')
  const userStr = localStorage.getItem('currentUser')

  // 场景 1：你想溜进内部房间，但你没带房卡 (未登录)
  if (to.name !== 'login' && !token) {
    next({ name: 'login' }) // 保安：回去登录！
    return
  }

  // 场景 2：你带了房卡 (已登录)
  if (token && userStr) {
    const user = JSON.parse(userStr)

    // 检查 2.1：你的卡上是不是贴着“首次登录”的红标？
    // 并且你现在想去的地方不是“改密室”？
    if (user.isFirstLogin === true && to.path !== '/change-password') {
      next('/change-password') // 保安：不改密码哪都不许去！强制押送！
      return
    }

    // 检查 2.2：你明明已经登录了，还在地址栏手敲 /login 想回登录页？
    if (to.name === 'login') {
      next('/dashboard') // 保安：你已经进来了，别去大门挤着，送你回大堂！
      return
    }
  }

  // 场景 3：一切合法，没有触发任何违规条件
  next() // 保安：请进！
})

export default router