<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#409eff"><App /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.appCount }}</div>
              <div class="stat-label">应用数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#67c23a"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.platformUserCount }}</div>
              <div class="stat-label">平台用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#e6a23c"><UserFilled /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.appUserCount }}</div>
              <div class="stat-label">应用用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#f56c6c"><OfficeBuilding /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.orgCount }}</div>
              <div class="stat-label">组织数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="quick-actions" style="margin-top: 20px">
      <template #header>
        <span>快捷操作</span>
      </template>
      <el-space wrap>
        <el-button type="primary" @click="$router.push('/app')">应用管理</el-button>
        <el-button type="success" @click="$router.push('/org')">组织管理</el-button>
        <el-button type="warning" @click="$router.push('/platform-user')">平台用户</el-button>
        <el-button type="info" @click="$router.push('/app-user')">应用用户</el-button>
      </el-space>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAppStats } from '../api/app'
import { getPlatformUserStats } from '../api/platformUser'
import { getOrgTree } from '../api/org'
import { getAppUserList } from '../api/appUser'

const stats = ref({
  appCount: 0,
  platformUserCount: 0,
  appUserCount: 0,
  orgCount: 0
})

const loadStats = async () => {
  try {
    const [appData, userData, orgData, appUserData] = await Promise.all([
      getAppStats().catch(() => ({ data: { count: 0 } })),
      getPlatformUserStats().catch(() => ({ data: { count: 0 } })),
      getOrgTree().catch(() => ({ data: [] })),
      getAppUserList({ page: 1, pageSize: 1 }).catch(() => ({ data: { total: 0 } }))
    ])
    stats.value = {
      appCount: appData.data?.count || 0,
      platformUserCount: userData.data?.count || 0,
      appUserCount: appUserData.data?.total || 0,
      orgCount: orgData.data?.length || 0
    }
  } catch (e) {
    console.error('Failed to load stats:', e)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}
.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}
.stat-card:hover {
  transform: translateY(-5px);
}
.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}
.stat-info {
  flex: 1;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
.quick-actions {
  margin-top: 20px;
}
</style>
