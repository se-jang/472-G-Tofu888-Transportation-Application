Feature: Receipt View
#  acceptance criteria 1: แอดมินสามารถดูใบเสร็จของลูกค้าทุกคนได้
  Scenario: Admin views a customer's receipt successfully
    Given an order for customer "Jane Doe"
    And I am logged in as admin
    When I view the receipt
    Then the receipt should be returned successfully and display the customer name "Jane Doe"

#  acceptance criteria 2: ลูกค้าต้องไม่สามารถดูใบเสร็จของคนอื่นได้
  Scenario: Customer views their own receipt successfully
    Given an order for customer "John Doe"
    And I am logged in as customer "John Doe"
    When I view the receipt
    Then the receipt should be returned successfully and display the customer name "John Doe"

#  acceptance criteria 3: ใบเสร็จต้องแสดงข้อมูลชื่อของลูกค้าคนนั้นๆ
  Scenario: Customer attempts to view another customer's receipt
    Given an order for customer "Jane Doe"
    And I am logged in as customer "John Doe"
    When I view the receipt
    Then the receipt should be returned unsuccessfully

Feature: Receipt info
# acceptance criteria 1: เจ้าของออเดอร์ต้องสามารถดูใบเสร็จของออเดอร์ที่สั่งได้หลังจากคำสั่งซื้อเสร็จสิ้นแล้ว
  Scenario: The order owner should be able to view the receipt for the order they placed
    Given an order for customer "Doodle Doe"
    And I am logged in as customer "Doodle Doe"
    And I have placed an order successfully
    When I view the receipt
    Then the receipt should return orderID same as the one I ordered

# acceptance criteria 2: ระบบต้องสร้างหมายเลขใบเสร็จที่ไม่ซ้ำกันสำหรับแต่ละคำสั่งซื้อ
  Scenario: The system should generate a unique receipt number for each order
    Given a new order for customer "Steve Doe"
    And I am logged in as customer "Steve Doe"
    And I have placed an order successfully
    When I view the receipt
    Then the system should generate a unique receipt number for the order

# acceptance criteria 3: ระบบต้องสร้างข้อมูลลูกค้าในใบเสร็จได้ถูกต้อง
  Scenario: The receipt should display the correct information
    Given an order for customer "Koon X"
    And I am logged in as customer "Koon X"
    And I have placed an order successfully
    When I view the receipt
    Then the receipt should display the correct order details for this user
