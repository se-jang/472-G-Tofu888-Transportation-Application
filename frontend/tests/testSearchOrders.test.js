import { test, expect } from '@playwright/test';

test.describe('Search functionality', () => {
  let page;

  test.beforeEach(async ({ page }) => {
    console.log("Opening page...");
    await page.goto('http://localhost:5173/orders');
    await page.waitForLoadState('domcontentloaded'); // รอให้ DOM โหลดก่อน
  });

  test('filters orders based on customer name', async ({ page }) => {
    await page.waitForSelector('input[placeholder="Search..."]', { state: 'attached' });
    // ตั้งค่า searchQuery เป็น 'John'
    await page.type('input[placeholder="Search..."]', 'John', { delay: 100 }); // ใส่ค่าในช่องค้นหาที่มี id เป็น 'search'

    // ตรวจสอบว่า 'John Doe' อยู่ในผลลัพธ์
    const order = await page.locator('.order-card').filter({ hasText: 'John Doe' });
    expect(await order.isVisible()).toBe(true);    
  });

  test('shows no results if no matching orders', async ({ page }) => {
    // ตั้งค่า searchQuery เป็นชื่อที่ไม่มี
    await page.type('input[placeholder="Search..."]', 'Johny');

    // ตรวจสอบว่าไม่พบผลลัพธ์
    const noOrdersMessage = await page.locator('.item-error-searchbar p');
    expect(await noOrdersMessage.isVisible()).toBe(true);    
  });

  test('shows all orders when searchQuery is empty', async ({ page }) => {
    // ตั้งค่า searchQuery เป็นค่าว่าง
    await page.fill('input[placeholder="Search..."]', '');

    // ตรวจสอบว่าแสดงรายการทั้งหมด
    const orders = await page.locator('.order-card');
    expect(await orders.count()).toBeGreaterThan(0);    
  });
  
});
