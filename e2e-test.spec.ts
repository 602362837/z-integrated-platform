import { test, expect } from '@playwright/test';

const BASE_URL = 'http://localhost:5173';

test.describe('最终功能验证测试', () => {

  // 测试1: 应用管理
  test('应用管理 - 新增应用', async ({ page }) => {
    console.log('开始测试: 应用管理');

    // 打开应用管理页面
    await page.goto(`${BASE_URL}/system/app`);
    await page.waitForLoadState('networkidle');

    // 等待页面加载
    await page.waitForTimeout(2000);

    // 验证列表正常显示（检查是否有表格或列表元素）
    const hasContent = await page.locator('.ant-table, .ant-list, table, [class*="table"]').count() > 0;
    console.log(`应用列表显示: ${hasContent ? '是' : '否'}`);

    // 点击新增按钮
    await page.click('button:has-text("新增应用"), button:has-text("新增"), .ant-btn-primary:has-text("新增")');
    await page.waitForTimeout(1000);

    // 填写表单
    await page.fill('input[id*="appName"], input[placeholder*="应用名称"], input[name*="appName"]', '最终测试');
    await page.fill('input[id*="appCode"], input[placeholder*="应用编码"], input[name*="appCode"]', 'final');
    await page.fill('textarea[id*="description"], textarea[placeholder*="描述"], input[name*="description"]', 'test');

    // 提交
    await page.click('button:has-text("确定"), button:has-text("提交"), button[type="submit"]');
    await page.waitForTimeout(2000);

    // 验证成功
    const successMessage = await page.locator('.ant-message-success, .ant-notification-success').count();
    console.log(`新增应用成功: ${successMessage > 0 ? '是' : '否'}`);

    expect(successMessage).toBeGreaterThan(0);
  });

  // 测试2: 组织管理
  test('组织管理 - 新增组织', async ({ page }) => {
    console.log('开始测试: 组织管理');

    // 打开组织管理页面
    await page.goto(`${BASE_URL}/system/org`);
    await page.waitForLoadState('networkidle');

    // 等待页面加载
    await page.waitForTimeout(2000);

    // 验证树形结构显示
    const hasTree = await page.locator('.ant-tree, [class*="tree"]').count() > 0;
    console.log(`组织树显示: ${hasTree ? '是' : '否'}`);

    // 点击新增按钮
    await page.click('button:has-text("新增组织"), button:has-text("新增"), .ant-btn-primary:has-text("新增")');
    await page.waitForTimeout(1000);

    // 填写表单
    await page.fill('input[id*="orgName"], input[placeholder*="组织名称"], input[name*="orgName"]', '测试部门');
    await page.fill('input[id*="orgCode"], input[placeholder*="组织编码"], input[name*="orgCode"]', 'DEPT');

    // 选择父级组织（选择根组织）
    await page.click('.ant-select, [class*="select"]:has-text("父级"), .ant-modal .ant-select');
    await page.waitForTimeout(500);
    await page.click('.ant-select-dropdown .ant-select-item:first-child, .ant-tree-select-dropdown .ant-select-item:first-child');
    await page.waitForTimeout(500);

    // 提交
    await page.click('button:has-text("确定"), button:has-text("提交"), button[type="submit"]');
    await page.waitForTimeout(2000);

    // 验证成功
    const successMessage = await page.locator('.ant-message-success, .ant-notification-success').count();
    console.log(`新增组织成功: ${successMessage > 0 ? '是' : '否'}`);

    expect(successMessage).toBeGreaterThan(0);
  });

  // 测试3: 平台用户管理
  test('平台用户管理 - 新增用户', async ({ page }) => {
    console.log('开始测试: 平台用户管理');

    // 打开平台用户管理页面
    await page.goto(`${BASE_URL}/system/user`);
    await page.waitForLoadState('networkidle');

    // 等待页面加载
    await page.waitForTimeout(2000);

    // 点击新增按钮
    await page.click('button:has-text("新增用户"), button:has-text("新增"), .ant-btn-primary:has-text("新增")');
    await page.waitForTimeout(1000);

    // 填写表单
    await page.fill('input[id*="username"], input[placeholder*="用户名"], input[name*="username"]', 'user01');
    await page.fill('input[id*="nickname"], input[placeholder*="昵称"], input[name*="nickname"]', '测试用户');
    await page.fill('input[id*="password"], input[placeholder*="密码"], input[type="password"]', '123456');

    // 选择组织
    await page.click('.ant-select:has-text("选择组织"), [class*="org"] .ant-select');
    await page.waitForTimeout(500);
    await page.click('.ant-select-dropdown .ant-select-item:first-child, .ant-tree-select-dropdown .ant-select-item:first-child');
    await page.waitForTimeout(500);

    // 提交
    await page.click('button:has-text("确定"), button:has-text("提交"), button[type="submit"]');
    await page.waitForTimeout(2000);

    // 验证成功
    const successMessage = await page.locator('.ant-message-success, .ant-notification-success').count();
    console.log(`新增用户成功: ${successMessage > 0 ? '是' : '否'}`);

    expect(successMessage).toBeGreaterThan(0);
  });
});
