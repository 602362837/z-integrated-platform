<template>
  <div class="app-user-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应用用户管理</span>
          <el-space>
            <el-select v-model="filterAppId" placeholder="请选择应用" clearable @change="loadData" style="width: 200px">
              <el-option v-for="app in appList" :key="app.id" :label="app.name" :value="app.id" />
            </el-select>
            <el-button type="primary" @click="handleSync" :disabled="!filterAppId">同步用户</el-button>
            <el-button type="success" @click="handleMapping">映射管理</el-button>
          </el-space>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="appName" label="应用名称" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="platformUserId" label="平台用户ID" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.platformUserId" type="success" size="small">已映射</el-tag>
            <el-tag v-else type="info" size="small">未映射</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleMappingUser(row)">映射</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 映射对话框 -->
    <el-dialog v-model="mappingDialogVisible" title="用户映射" width="500px">
      <el-form label-width="100px">
        <el-form-item label="应用用户">
          <el-input :value="mappingForm.appUsername" disabled />
        </el-form-item>
        <el-form-item label="选择平台用户">
          <el-select v-model="mappingForm.platformUserId" placeholder="请选择平台用户" filterable style="width: 100%">
            <el-option v-for="user in platformUsers" :key="user.id" :label="user.nickname" :value="user.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="mappingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMappingSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 映射管理对话框 -->
    <el-dialog v-model="mappingListDialogVisible" title="映射管理" width="800px">
      <el-table :data="mappingList" border v-loading="mappingLoading">
        <el-table-column prop="appUserUsername" label="应用用户" />
        <el-table-column prop="platformUserNickname" label="平台用户" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleUnmapping(row)">解除映射</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAppUserList, deleteAppUser, syncAppUsers, getAppUserMapping, updateAppUserMapping } from '../api/appUser'
import { getAppList } from '../api/app'
import { getPlatformUserList } from '../api/platformUser'

const searchForm = ref({ username: '' })
const tableData = ref([])
const loading = ref(false)
const pagination = ref({ page: 1, pageSize: 10, total: 0 })
const filterAppId = ref(null)
const appList = ref([])

// 映射相关
const mappingDialogVisible = ref(false)
const mappingListDialogVisible = ref(false)
const mappingLoading = ref(false)
const mappingForm = ref({ appUserId: null, appUsername: '', platformUserId: null })
const platformUsers = ref([])
const mappingList = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAppUserList({
      page: pagination.value.page,
      pageSize: pagination.value.pageSize,
      username: searchForm.value.username,
      appId: filterAppId.value
    })
    tableData.value = res.data?.list || []
    pagination.value.total = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const loadAppList = async () => {
  try {
    const res = await getAppList({ page: 1, pageSize: 100 })
    appList.value = res.data?.list || []
  } catch (e) {
    console.error(e)
  }
}

const loadPlatformUsers = async () => {
  try {
    const res = await getPlatformUserList({ page: 1, pageSize: 100 })
    platformUsers.value = res.data?.list || []
  } catch (e) {
    console.error(e)
  }
}

const handleSync = async () => {
  if (!filterAppId.value) {
    ElMessage.warning('请先选择应用')
    return
  }
  try {
    await syncAppUsers(filterAppId.value)
    ElMessage.success('同步成功')
    loadData()
  } catch (e) {
    ElMessage.error('同步失败')
  }
}

const handleMapping = async () => {
  mappingListDialogVisible.value = true
  mappingLoading.value = true
  try {
    const res = await getAppUserMapping({ appId: filterAppId.value })
    mappingList.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    mappingLoading.value = false
  }
}

const handleMappingUser = (row) => {
  mappingForm.value = { appUserId: row.id, appUsername: row.username, platformUserId: row.platformUserId }
  loadPlatformUsers()
  mappingDialogVisible.value = true
}

const handleMappingSubmit = async () => {
  try {
    await updateAppUserMapping({
      appUserId: mappingForm.value.appUserId,
      platformUserId: mappingForm.value.platformUserId
    })
    ElMessage.success('映射成功')
    mappingDialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('映射失败')
  }
}

const handleUnmapping = async (row) => {
  try {
    await ElMessageBox.confirm('确定要解除映射吗？', '提示', { type: 'warning' })
    await updateAppUserMapping({
      appUserId: row.appUserId,
      platformUserId: null
    })
    ElMessage.success('解除成功')
    handleMapping()
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('解除失败')
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该应用用户吗？', '提示', { type: 'warning' })
    await deleteAppUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleReset = () => {
  searchForm.value = { username: '' }
  pagination.value.page = 1
  loadData()
}

onMounted(() => {
  loadAppList()
  loadData()
})
</script>

<style scoped>
.app-user-container {
  padding: 0;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search-form {
  margin-bottom: 15px;
}
</style>
