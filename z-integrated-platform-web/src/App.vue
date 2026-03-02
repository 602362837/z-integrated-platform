<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)

const menuItems = [
  { path: '/dashboard', title: '仪表盘', icon: 'DataLine' },
  { path: '/app', title: '应用管理', icon: 'App' },
  { path: '/org', title: '组织管理', icon: 'OfficeBuilding' },
  { path: '/platform-user', title: '平台用户', icon: 'User' },
  { path: '/app-user', title: '应用用户', icon: 'UserFilled' }
]

const handleMenuSelect = (path) => {
  router.push(path)
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="aside">
      <div class="logo">
        <h3 v-if="!isCollapse">综合管理平台</h3>
        <span v-else>平台</span>
      </div>
      <el-menu
        :default-active="route.path"
        :collapse="isCollapse"
        router
        class="menu"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-button text @click="isCollapse = !isCollapse">
          <el-icon :size="20"><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
        </el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}
.aside {
  background: #304156;
  transition: width 0.3s;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  border-bottom: 1px solid #404854;
}
.logo h3 {
  margin: 0;
  font-size: 16px;
}
.menu {
  border-right: none;
  background: #304156;
}
.menu :deep(.el-menu-item) {
  color: #bfcbd9;
}
.menu :deep(.el-menu-item:hover),
.menu :deep(.el-menu-item.is-active) {
  background: #263445;
  color: #409eff;
}
.header {
  background: #fff;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #e6e6e6;
}
.main {
  background: #f5f7fa;
  padding: 20px;
}
</style>
