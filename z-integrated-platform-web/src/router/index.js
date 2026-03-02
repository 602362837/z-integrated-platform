import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue')
  },
  {
    path: '/app',
    name: 'App',
    component: () => import('../views/App.vue')
  },
  {
    path: '/org',
    name: 'Org',
    component: () => import('../views/Org.vue')
  },
  {
    path: '/platform-user',
    name: 'PlatformUser',
    component: () => import('../views/PlatformUser.vue')
  },
  {
    path: '/app-user',
    name: 'AppUser',
    component: () => import('../views/AppUser.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
