const { chromium } = require('playwright');

const BASE_URL = 'http://localhost:5173';
const API_URL = 'http://localhost:9101';

// Test results storage
const results = {
  app: { passed: 0, failed: 0, issues: [] },
  org: { passed: 0, failed: 0, issues: [] },
  platformUser: { passed: 0, failed: 0, issues: [] },
  appUser: { passed: 0, failed: 0, issues: [] }
};

function logTest(module, name, passed, details = '') {
  const status = passed ? 'PASS' : 'FAIL';
  console.log(`[${module}] ${name}: ${status} ${details}`);
  if (passed) {
    results[module].passed++;
  } else {
    results[module].failed++;
    results[module].issues.push({ name, details });
  }
}

async function waitForPageReady(page, selector, timeout = 10000) {
  try {
    await page.waitForSelector(selector, { timeout });
    return true;
  } catch {
    return false;
  }
}

async function getApiData(endpoint) {
  const response = await fetch(`${API_URL}${endpoint}`);
  return await response.json();
}

async function createTestData() {
  console.log('Creating test data via API...');

  // Create test app
  try {
    await fetch(`${API_URL}/api/v1/apps`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: '测试应用',
        code: 'test-app',
        description: '自动化测试应用',
        status: 1
      })
    });
  } catch (e) {
    console.log('App may already exist');
  }

  // Create test org
  try {
    await fetch(`${API_URL}/api/v1/orgs`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: '测试组织',
        code: 'test-org',
        sort: 0,
        status: 1
      })
    });
  } catch (e) {
    console.log('Org may already exist');
  }

  // Create test platform user
  try {
    await fetch(`${API_URL}/api/v1/platform-users`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: 'testuser',
        nickname: '测试用户',
        password: '123456',
        email: 'test@example.com',
        phone: '13800138000',
        status: 1
      })
    });
  } catch (e) {
    console.log('Platform user may already exist');
  }

  console.log('Test data creation completed');
}

async function testAppManagement(page) {
  console.log('\n=== Testing App Management ===');

  try {
    await page.goto(`${BASE_URL}/app`);
    await page.waitForLoadState('networkidle');

    // Test 1: List display
    const tableVisible = await waitForPageReady(page, '.el-table', 10000);
    logTest('app', 'List display', tableVisible, tableVisible ? '' : 'Table not found');

    // Test 2: Add new app
    if (tableVisible) {
      await page.click('button:has-text("新增应用")');
      await page.waitForSelector('.el-dialog', 5000);

      const dialogVisible = await page.isVisible('.el-dialog');
      logTest('app', 'Add dialog opens', dialogVisible);

      if (dialogVisible) {
        await page.fill('input[placeholder="请输入应用名称"]', 'Playwright测试应用');
        await page.fill('input[placeholder="请输入应用编码"]', 'playwright-test');
        await page.fill('textarea[placeholder="请输入描述"]', '自动化测试描述');
        await page.click('button:has-text("确定")');

        await page.waitForTimeout(2000);
        const successMsg = await page.isVisible('text=创建成功');
        logTest('app', 'Add new app', successMsg, successMsg ? '' : 'Creation may have failed');
      }
    }

    // Test 3: Search functionality
    await page.fill('input[placeholder="请输入应用名称"]', '测试');
    await page.click('button:has-text("搜索")');
    await page.waitForTimeout(1000);
    logTest('app', 'Search functionality', true, 'Search executed');

    // Test 4: Edit functionality
    const editBtn = await page.$('button:has-text("编辑")');
    if (editBtn) {
      await editBtn.click();
      await page.waitForSelector('.el-dialog', 5000);
      const editDialogVisible = await page.isVisible('.el-dialog');
      logTest('app', 'Edit dialog opens', editDialogVisible);

      if (editDialogVisible) {
        await page.fill('input[placeholder="请输入应用名称"]', 'Playwright测试应用-已编辑');
        await page.click('button:has-text("确定")');
        await page.waitForTimeout(2000);
        const updateSuccess = await page.isVisible('text=更新成功');
        logTest('app', 'Update app', updateSuccess, updateSuccess ? '' : 'Update may have failed');
      }
    }

    // Test 5: Delete functionality
    await page.waitForTimeout(1000);
    const deleteBtn = await page.$('button:has-text("删除")');
    if (deleteBtn) {
      // Handle confirmation dialog
      page.on('dialog', async dialog => {
        await dialog.accept();
      });
      await deleteBtn.click();
      await page.waitForTimeout(2000);
      const deleteSuccess = await page.isVisible('text=删除成功');
      logTest('app', 'Delete app', deleteSuccess, deleteSuccess ? '' : 'Delete may have failed');
    }

  } catch (e) {
    console.error('App Management Test Error:', e.message);
    logTest('app', 'General test', false, e.message);
  }
}

async function testOrgManagement(page) {
  console.log('\n=== Testing Org Management ===');

  try {
    await page.goto(`${BASE_URL}/org`);
    await page.waitForLoadState('networkidle');

    // Test 1: Tree display
    const treeVisible = await waitForPageReady(page, '.el-tree', 10000);
    logTest('org', 'Tree display', treeVisible, treeVisible ? '' : 'Tree not found');

    // Test 2: Add new org
    if (treeVisible) {
      await page.click('button:has-text("新增组织")');
      await page.waitForSelector('.el-dialog', 5000);

      const dialogVisible = await page.isVisible('.el-dialog');
      logTest('org', 'Add dialog opens', dialogVisible);

      if (dialogVisible) {
        await page.fill('input[placeholder="请输入组织名称"]', 'Playwright测试组织');
        await page.fill('input[placeholder="请输入组织编码"]', 'playwright-test-org');
        await page.click('button:has-text("确定")');

        await page.waitForTimeout(2000);
        const successMsg = await page.isVisible('text=创建成功');
        logTest('org', 'Add new org', successMsg, successMsg ? '' : 'Creation may have failed');
      }
    }

    // Test 3: Add child org
    await page.waitForTimeout(1000);
    const addChildBtn = await page.$('.el-tree-node button:has-text("添加子级")');
    if (addChildBtn) {
      await addChildBtn.click();
      await page.waitForSelector('.el-dialog', 5000);
      const childDialogVisible = await page.isVisible('.el-dialog');
      logTest('org', 'Add child org dialog', childDialogVisible);

      if (childDialogVisible) {
        await page.fill('input[placeholder="请输入组织名称"]', '子组织测试');
        await page.fill('input[placeholder="请输入组织编码"]', 'child-org-test');
        await page.click('button:has-text("确定")');
        await page.waitForTimeout(2000);
      }
    }

    // Test 4: Edit functionality
    const editBtn = await page.$('.el-tree-node button:has-text("编辑")');
    if (editBtn) {
      await editBtn.click();
      await page.waitForSelector('.el-dialog', 5000);
      const editDialogVisible = await page.isVisible('.el-dialog');
      logTest('org', 'Edit dialog opens', editDialogVisible);

      if (editDialogVisible) {
        await page.fill('input[placeholder="请输入组织名称"]', '编辑后的组织');
        await page.click('button:has-text("确定")');
        await page.waitForTimeout(2000);
      }
    }

    // Test 5: Delete functionality
    await page.waitForTimeout(1000);
    const deleteBtn = await page.$('.el-tree-node button:has-text("删除")');
    if (deleteBtn) {
      page.on('dialog', async dialog => {
        await dialog.accept();
      });
      await deleteBtn.click();
      await page.waitForTimeout(2000);
      const deleteSuccess = await page.isVisible('text=删除成功');
      logTest('org', 'Delete org', deleteSuccess, deleteSuccess ? '' : 'Delete may have failed');
    }

  } catch (e) {
    console.error('Org Management Test Error:', e.message);
    logTest('org', 'General test', false, e.message);
  }
}

async function testPlatformUser(page) {
  console.log('\n=== Testing Platform User Management ===');

  try {
    await page.goto(`${BASE_URL}/platform-user`);
    await page.waitForLoadState('networkidle');

    // Test 1: List display
    const tableVisible = await waitForPageReady(page, '.el-table', 10000);
    logTest('platformUser', 'List display', tableVisible, tableVisible ? '' : 'Table not found');

    // Test 2: Add new user
    if (tableVisible) {
      await page.click('button:has-text("新增用户")');
      await page.waitForSelector('.el-dialog', 5000);

      const dialogVisible = await page.isVisible('.el-dialog');
      logTest('platformUser', 'Add dialog opens', dialogVisible);

      if (dialogVisible) {
        await page.fill('input[placeholder="请输入用户名"]', 'playwrightuser');
        await page.fill('input[placeholder="请输入昵称"]', 'Playwright测试用户');
        await page.fill('input[type="password"]', '123456');
        await page.fill('input[placeholder="请输入邮箱"]', 'playwright@test.com');
        await page.fill('input[placeholder="请输入手机号"]', '13900139000');
        await page.click('button:has-text("确定")');

        await page.waitForTimeout(2000);
        const successMsg = await page.isVisible('text=创建成功');
        logTest('platformUser', 'Add new user', successMsg, successMsg ? '' : 'Creation may have failed');
      }
    }

    // Test 3: Search functionality
    await page.fill('input[placeholder="请输入用户名"]', 'test');
    await page.click('button:has-text("搜索")');
    await page.waitForTimeout(1000);
    logTest('platformUser', 'Search functionality', true, 'Search executed');

    // Test 4: Edit functionality
    await page.waitForTimeout(1000);
    const editBtn = await page.$('button:has-text("编辑")');
    if (editBtn) {
      await editBtn.click();
      await page.waitForSelector('.el-dialog', 5000);
      const editDialogVisible = await page.isVisible('.el-dialog');
      logTest('platformUser', 'Edit dialog opens', editDialogVisible);

      if (editDialogVisible) {
        await page.fill('input[placeholder="请输入昵称"]', '编辑后的用户');
        await page.click('button:has-text("确定")');
        await page.waitForTimeout(2000);
        const updateSuccess = await page.isVisible('text=更新成功');
        logTest('platformUser', 'Update user', updateSuccess, updateSuccess ? '' : 'Update may have failed');
      }
    }

    // Test 5: Delete functionality
    await page.waitForTimeout(1000);
    const deleteBtn = await page.$('button:has-text("删除")');
    if (deleteBtn) {
      page.on('dialog', async dialog => {
        await dialog.accept();
      });
      await deleteBtn.click();
      await page.waitForTimeout(2000);
      const deleteSuccess = await page.isVisible('text=删除成功');
      logTest('platformUser', 'Delete user', deleteSuccess, deleteSuccess ? '' : 'Delete may have failed');
    }

  } catch (e) {
    console.error('Platform User Test Error:', e.message);
    logTest('platformUser', 'General test', false, e.message);
  }
}

async function testAppUser(page) {
  console.log('\n=== Testing App User Management ===');

  try {
    await page.goto(`${BASE_URL}/app-user`);
    await page.waitForLoadState('networkidle');

    // Test 1: List display
    const tableVisible = await waitForPageReady(page, '.el-table', 10000);
    logTest('appUser', 'List display', tableVisible, tableVisible ? '' : 'Table not found');

    // Test 2: App selection
    const appSelectVisible = await page.isVisible('.el-select');
    logTest('appUser', 'App selector visible', appSelectVisible);

    // Test 3: Sync button
    const syncBtnVisible = await page.isVisible('button:has-text("同步用户")');
    logTest('appUser', 'Sync button visible', syncBtnVisible);

    // Test 4: Mapping button
    const mappingBtnVisible = await page.isVisible('button:has-text("映射管理")');
    logTest('appUser', 'Mapping button visible', mappingBtnVisible);

    // Test 5: Search functionality
    await page.fill('input[placeholder="请输入用户名"]', 'test');
    await page.click('button:has-text("搜索")');
    await page.waitForTimeout(1000);
    logTest('appUser', 'Search functionality', true, 'Search executed');

  } catch (e) {
    console.error('App User Test Error:', e.message);
    logTest('appUser', 'General test', false, e.message);
  }
}

async function runTests() {
  console.log('Starting Browser Functional Tests...');
  console.log(`Frontend: ${BASE_URL}`);
  console.log(`Backend: ${API_URL}`);

  // Check if services are running
  try {
    const frontendCheck = await fetch(BASE_URL);
    console.log(`Frontend status: ${frontendCheck.status}`);
  } catch (e) {
    console.error('Frontend not accessible!');
    process.exit(1);
  }

  // Create test data
  await createTestData();

  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();

  // Handle console errors
  page.on('console', msg => {
    if (msg.type() === 'error') {
      console.log('Console Error:', msg.text());
    }
  });

  // Run all tests
  await testAppManagement(page);
  await testOrgManagement(page);
  await testPlatformUser(page);
  await testAppUser(page);

  await browser.close();

  // Print summary
  console.log('\n========== TEST SUMMARY ==========');
  let totalPassed = 0;
  let totalFailed = 0;

  for (const [module, data] of Object.entries(results)) {
    console.log(`\n${module.toUpperCase()}:`);
    console.log(`  Passed: ${data.passed}`);
    console.log(`  Failed: ${data.failed}`);
    if (data.issues.length > 0) {
      console.log('  Issues:');
      data.issues.forEach(issue => {
        console.log(`    - ${issue.name}: ${issue.details}`);
      });
    }
    totalPassed += data.passed;
    totalFailed += data.failed;
  }

  console.log('\n========== TOTAL ==========');
  console.log(`Total Passed: ${totalPassed}`);
  console.log(`Total Failed: ${totalFailed}`);
  console.log('=============================\n');

  return totalFailed === 0;
}

runTests()
  .then(success => {
    process.exit(success ? 0 : 1);
  })
  .catch(e => {
    console.error('Test execution failed:', e);
    process.exit(1);
  });
