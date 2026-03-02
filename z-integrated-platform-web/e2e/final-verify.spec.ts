import { test, expect } from '@playwright/test';

test.describe('最终功能验证测试', () => {

  // 测试1: 应用管理
  test('应用管理 - 新增应用', async ({ page }) => {
    page.on('console', msg => console.log('Browser console:', msg.text()));
    page.on('pageerror', err => console.log('Browser error:', err.message));

    console.log('开始测试: 应用管理');

    await page.goto('/app');
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(3000);

    const pageTitle = page.locator('text=应用管理').first();
    await expect(pageTitle).toBeVisible({ timeout: 10000 });
    console.log('应用管理页面已加载');

    const table = page.locator('.el-table');
    await expect(table).toBeVisible({ timeout: 5000 });
    console.log('应用列表表格显示');

    const addBtn = page.locator('button:has-text("新增应用")');
    await addBtn.click();
    await page.waitForTimeout(1000);

    const dialog = page.locator('.el-dialog');
    await expect(dialog).toBeVisible({ timeout: 5000 });
    console.log('新增对话框已打开');

    await page.fill('input[placeholder*="应用名称"]', '最终测试');
    await page.fill('input[placeholder*="应用编码"]', 'finaltest');
    await page.fill('textarea[placeholder*="描述"]', 'test');

    await page.click('.el-dialog button:has-text("确定")');
    await page.waitForTimeout(3000);

    const errorMessage = page.locator('.el-message--error');
    const hasError = await errorMessage.count() > 0;
    if (hasError) {
      console.log('提交失败，错误消息:', await errorMessage.textContent());
    }

    const successMessage = page.locator('.el-message--success');
    const hasSuccess = await successMessage.count() > 0;

    if (hasSuccess) {
      console.log('新增应用成功');
    } else {
      console.log('新增应用: 未检测到成功消息');
    }

    expect(hasSuccess || !hasError).toBe(true);
  });

  // 测试2: 组织管理
  test('组织管理 - 新增组织', async ({ page }) => {
    page.on('console', msg => console.log('Browser console:', msg.text()));
    page.on('pageerror', err => console.log('Browser error:', err.message));

    console.log('开始测试: 组织管理');

    await page.goto('/org');
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(3000);

    const pageTitle = page.locator('text=组织管理').first();
    await expect(pageTitle).toBeVisible({ timeout: 10000 });
    console.log('组织管理页面已加载');

    const tree = page.locator('.el-tree');
    await expect(tree).toBeVisible({ timeout: 5000 });
    console.log('组织树显示');

    const addBtn = page.locator('button:has-text("新增组织")');
    await addBtn.click();
    await page.waitForTimeout(1000);

    const dialog = page.locator('.el-dialog');
    await expect(dialog).toBeVisible({ timeout: 5000 });
    console.log('新增对话框已打开');

    await page.fill('input[placeholder*="组织名称"]', '测试部门');
    await page.fill('input[placeholder*="组织编码"]', 'DEPT001');

    await page.click('.el-dialog button:has-text("确定")');
    await page.waitForTimeout(3000);

    const errorMessage = page.locator('.el-message--error');
    const hasError = await errorMessage.count() > 0;
    if (hasError) {
      console.log('提交失败，错误消息:', await errorMessage.textContent());
    }

    const successMessage = page.locator('.el-message--success');
    const hasSuccess = await successMessage.count() > 0;

    if (hasSuccess) {
      console.log('新增组织成功');
    } else {
      console.log('新增组织: 未检测到成功消息');
    }

    expect(hasSuccess || !hasError).toBe(true);
  });

  // 测试3: 平台用户管理
  test('平台用户管理 - 新增用户', async ({ page }) => {
    page.on('console', msg => console.log('Browser console:', msg.text()));
    page.on('pageerror', err => console.log('Browser error:', err.message));

    console.log('开始测试: 平台用户管理');

    await page.goto('/platform-user');
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(3000);

    const pageTitle = page.locator('text=平台用户').first();
    await expect(pageTitle).toBeVisible({ timeout: 10000 });
    console.log('平台用户管理页面已加载');

    const addBtn = page.locator('button:has-text("新增用户")');
    await addBtn.click();
    await page.waitForTimeout(1000);

    const dialog = page.locator('.el-dialog');
    await expect(dialog).toBeVisible({ timeout: 5000 });
    console.log('新增对话框已打开');

    await page.fill('input[placeholder*="用户名"]', 'user01');
    await page.fill('input[placeholder*="昵称"]', '测试用户');
    await page.fill('input[type="password"]', '123456');

    await page.click('.el-dialog .el-select');
    await page.waitForTimeout(1000);

    // 使用更精确的选择器，只选择对话框内的下拉框
    const dropdown = page.locator('.el-dialog .el-select-dropdown');
    await expect(dropdown).toBeVisible({ timeout: 3000 });

    const firstOption = page.locator('.el-select-dropdown .el-option').first();
    await firstOption.click();
    await page.waitForTimeout(500);

    console.log('已选择组织');

    await page.click('.el-dialog button:has-text("确定")');
    await page.waitForTimeout(3000);

    const errorMessage = page.locator('.el-message--error');
    const hasError = await errorMessage.count() > 0;
    if (hasError) {
      console.log('提交失败，错误消息:', await errorMessage.textContent());
    }

    const successMessage = page.locator('.el-message--success');
    const hasSuccess = await successMessage.count() > 0;

    if (hasSuccess) {
      console.log('新增用户成功');
    } else {
      console.log('新增用户: 未检测到成功消息');
    }

    expect(hasSuccess || !hasError).toBe(true);
  });
});
