const { chromium } = require('playwright');

const BASE_URL = 'http://localhost:5173';

let browser;
let context;
let page;

async function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function setup() {
  browser = await chromium.launch({ headless: true });
  context = await browser.newContext();
  page = await context.newPage();

  page.on('console', msg => {
    if (msg.type() === 'error') {
      console.log('[Browser Error]:', msg.text());
    }
  });

  await page.goto(BASE_URL);
  await page.waitForLoadState('networkidle');
  console.log('Page loaded successfully');
}

async function teardown() {
  if (browser) {
    await browser.close();
  }
}

// 关闭任何打开的对话框
async function closeAnyDialog() {
  const dialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
  if (dialogVisible) {
    // 点击取消按钮
    await page.locator('.el-dialog button:has-text("取消")').click().catch(() => {});
    await sleep(500);
    // 如果对话框还在，按 ESC 关闭
    await page.keyboard.press('Escape');
    await sleep(500);
  }
}

// 测试应用管理页面
async function testAppManagement() {
  console.log('\n========== Testing Application Management ==========');

  try {
    console.log('[TC1] Navigating to Application Management page...');
    await page.click('text=应用管理');
    await sleep(2000);

    // 检查新增按钮
    const addButton = page.locator('button:has-text("新增"), .el-button:has-text("新增")').first();
    const hasAddButton = await addButton.isVisible().catch(() => false);
    console.log('Add button visible:', hasAddButton);

    if (!hasAddButton) {
      return { passed: false, error: 'Add button not found' };
    }

    console.log('[TC2] Clicking "New Application" button...');
    await addButton.click();
    await sleep(1000);

    const dialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
    console.log('Dialog opened:', dialogVisible);

    if (!dialogVisible) {
      return { passed: false, error: 'Dialog did not open' };
    }

    console.log('[TC3] Filling form...');
    const form = page.locator('.el-dialog');
    const inputs = form.locator('input');
    await inputs.nth(0).fill('测试应用QA');
    await sleep(200);
    await inputs.nth(1).fill('testqa');
    await sleep(200);

    console.log('[TC4] Submitting form...');
    const submitButton = form.locator('button:has-text("确 定"), .el-button--primary:has-text("确定")');
    await submitButton.click();
    await sleep(2000);

    console.log('[TC5] Verifying result...');
    const newDialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
    console.log('Dialog closed:', !newDialogVisible);

    const pageContent2 = await page.content();
    const appCreated = pageContent2.includes('测试应用QA');
    console.log('App created in list:', appCreated);

    // 如果对话框没关闭，手动关闭
    if (newDialogVisible) {
      console.log('Manually closing dialog...');
      await closeAnyDialog();
    }

    return {
      passed: appCreated,
      details: {
        addButtonVisible: hasAddButton,
        dialogOpened: dialogVisible,
        dialogClosed: !newDialogVisible,
        appCreated: appCreated
      }
    };

  } catch (error) {
    console.error('Test failed with error:', error.message);
    await closeAnyDialog();
    return { passed: false, error: error.message };
  }
}

// 测试组织管理页面
async function testOrgManagement() {
  console.log('\n========== Testing Organization Management ==========');

  try {
    // 先关闭之前的对话框
    await closeAnyDialog();
    await sleep(500);

    console.log('[TC1] Navigating to Organization Management page...');
    await page.click('text=组织管理');
    await sleep(2000);

    const addButton = page.locator('button:has-text("新增"), .el-button:has-text("新增")').first();
    const hasAddButton = await addButton.isVisible().catch(() => false);
    console.log('Add button visible:', hasAddButton);

    if (!hasAddButton) {
      return { passed: false, error: 'Add button not found' };
    }

    console.log('[TC2] Clicking "New Organization" button...');
    await addButton.click();
    await sleep(1000);

    const dialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
    console.log('Dialog opened:', dialogVisible);

    if (!dialogVisible) {
      return { passed: false, error: 'Dialog did not open' };
    }

    console.log('[TC3] Filling form...');
    const form = page.locator('.el-dialog');
    const inputs = form.locator('input');
    await inputs.nth(0).fill('测试组织QA');
    await sleep(200);
    await inputs.nth(1).fill('TESTQA');
    await sleep(200);

    console.log('[TC4] Submitting form...');
    const submitButton = form.locator('button:has-text("确 定"), .el-button--primary:has-text("确定")');
    await submitButton.click();
    await sleep(2000);

    console.log('[TC5] Verifying result...');
    const newDialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
    console.log('Dialog closed:', !newDialogVisible);

    const pageContent2 = await page.content();
    const orgCreated = pageContent2.includes('测试组织QA');
    console.log('Org created in list:', orgCreated);

    // 如果对话框没关闭，手动关闭
    if (newDialogVisible) {
      console.log('Manually closing dialog...');
      await closeAnyDialog();
    }

    return {
      passed: orgCreated,
      details: {
        addButtonVisible: hasAddButton,
        dialogOpened: dialogVisible,
        dialogClosed: !newDialogVisible,
        orgCreated: orgCreated
      }
    };

  } catch (error) {
    console.error('Test failed with error:', error.message);
    await closeAnyDialog();
    return { passed: false, error: error.message };
  }
}

// 测试平台用户管理页面
async function testUserManagement() {
  console.log('\n========== Testing Platform User Management ==========');

  try {
    // 先关闭之前的对话框
    await closeAnyDialog();
    await sleep(500);

    console.log('[TC1] Navigating to Platform User Management page...');
    await page.click('text=平台用户');
    await sleep(2000);

    const addButton = page.locator('button:has-text("新增"), .el-button:has-text("新增")').first();
    const hasAddButton = await addButton.isVisible().catch(() => false);
    console.log('Add button visible:', hasAddButton);

    if (!hasAddButton) {
      return { passed: false, error: 'Add button not found' };
    }

    console.log('[TC2] Clicking "New User" button...');
    await addButton.click();
    await sleep(1000);

    const dialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
    console.log('Dialog opened:', dialogVisible);

    if (!dialogVisible) {
      return { passed: false, error: 'Dialog did not open' };
    }

    console.log('[TC3] Filling form...');
    const form = page.locator('.el-dialog');
    const inputs = form.locator('input');
    await inputs.nth(0).fill('testuserqa');
    await sleep(200);
    await inputs.nth(1).fill('测试用户QA');
    await sleep(200);
    await inputs.nth(2).fill('123456');
    await sleep(200);

    console.log('[TC4] Submitting form...');
    const submitButton = form.locator('button:has-text("确 定"), .el-button--primary:has-text("确定")');
    await submitButton.click();
    await sleep(2000);

    console.log('[TC5] Verifying result...');
    const newDialogVisible = await page.locator('.el-dialog').isVisible().catch(() => false);
    console.log('Dialog closed:', !newDialogVisible);

    const pageContent2 = await page.content();
    const userCreated = pageContent2.includes('testuserqa');
    console.log('User created in list:', userCreated);

    // 如果对话框没关闭，手动关闭
    if (newDialogVisible) {
      console.log('Manually closing dialog...');
      await closeAnyDialog();
    }

    return {
      passed: userCreated,
      details: {
        addButtonVisible: hasAddButton,
        dialogOpened: dialogVisible,
        dialogClosed: !newDialogVisible,
        userCreated: userCreated
      }
    };

  } catch (error) {
    console.error('Test failed with error:', error.message);
    await closeAnyDialog();
    return { passed: false, error: error.message };
  }
}

async function main() {
  console.log('========================================');
  console.log('QA Test: Frontend Functionality Verification');
  console.log('========================================');

  const results = {
    appManagement: null,
    orgManagement: null,
    userManagement: null
  };

  try {
    await setup();
    await sleep(1000);

    results.appManagement = await testAppManagement();
    await closeAnyDialog();
    await sleep(500);

    results.orgManagement = await testOrgManagement();
    await closeAnyDialog();
    await sleep(500);

    results.userManagement = await testUserManagement();

  } catch (error) {
    console.error('Fatal error:', error);
  } finally {
    await teardown();
  }

  console.log('\n========================================');
  console.log('TEST SUMMARY');
  console.log('========================================');

  const tests = [
    { name: 'App Management', result: results.appManagement },
    { name: 'Org Management', result: results.orgManagement },
    { name: 'User Management', result: results.userManagement }
  ];

  let passed = 0;
  let failed = 0;

  tests.forEach(test => {
    const status = test.result?.passed ? 'PASS' : 'FAIL';
    console.log(`${test.name}: ${status}`);
    if (test.result?.passed) passed++;
    else failed++;
  });

  console.log('----------------------------------------');
  console.log(`Total: ${tests.length} | Passed: ${passed} | Failed: ${failed}`);

  process.exit(failed > 0 ? 1 : 0);
}

main();
