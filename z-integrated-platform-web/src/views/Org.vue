<template>
  <div class="org-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>组织管理</span>
          <el-button type="primary" @click="handleAdd">新增组织</el-button>
        </div>
      </template>

      <el-tree
        :data="treeData"
        :props="treeProps"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        v-loading="loading"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ data.orgName }}</span>
            <span class="actions">
              <el-button link type="primary" size="small" @click="handleAddChild(data)">添加子级</el-button>
              <el-button link type="primary" size="small" @click="handleEdit(data)">编辑</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(data)">删除</el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="上级组织" v-if="form.parentId">
          <el-input :value="parentName" disabled />
        </el-form-item>
        <el-form-item label="组织名称" prop="orgName">
          <el-input v-model="form.orgName" placeholder="请输入组织名称" />
        </el-form-item>
        <el-form-item label="组织编码" prop="orgCode">
          <el-input v-model="form.orgCode" placeholder="请输入组织编码" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrgTree, createOrg, updateOrg, deleteOrg } from '../api/org'

const treeData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const parentName = ref('')

const treeProps = {
  children: 'children',
  label: 'orgName'
}

const form = ref({
  id: null,
  parentId: null,
  orgName: '',
  orgCode: '',
  sortOrder: 0,
  status: 1
})

const rules = {
  orgName: [{ required: true, message: '请输入组织名称', trigger: 'blur' }],
  orgCode: [{ required: true, message: '请输入组织编码', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getOrgTree()
    treeData.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const findNodeName = (data, id) => {
  for (const item of data) {
    if (item.id === id) return item.orgName
    if (item.children) {
      const found = findNodeName(item.children, id)
      if (found) return found
    }
  }
  return ''
}

const handleAdd = () => {
  form.value = { id: null, parentId: 0, orgName: '', orgCode: '', sortOrder: 0, status: 1 }
  parentName.value = '根组织'
  dialogTitle.value = '新增组织'
  dialogVisible.value = true
}

const handleAddChild = (data) => {
  form.value = { id: null, parentId: data.id, orgName: '', orgCode: '', sortOrder: 0, status: 1 }
  parentName.value = data.orgName
  dialogTitle.value = '新增子组织'
  dialogVisible.value = true
}

const handleEdit = (data) => {
  form.value = { ...data, parentId: data.parentId || null }
  parentName.value = data.parentId ? findNodeName(treeData.value, data.parentId) : '根组织'
  dialogTitle.value = '编辑组织'
  dialogVisible.value = true
}

const handleDelete = async (data) => {
  try {
    await ElMessageBox.confirm('确定要删除该组织吗？', '提示', { type: 'warning' })
    await deleteOrg(data.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  try {
    if (form.value.id) {
      await updateOrg(form.value)
      ElMessage.success('更新成功')
    } else {
      await createOrg(form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(form.value.id ? '更新失败' : '创建失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.org-container {
  padding: 0;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 20px;
}
.actions {
  display: flex;
  gap: 10px;
}
</style>
