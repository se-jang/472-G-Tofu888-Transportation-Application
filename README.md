# Transport Application

## รายชื่อสมาชิก ของกลุ่ม JANG

- 6510450607 ปริยานุช มั่งคั่ง
- 6510450763 ภัควัฒน์ ปานกลาง
- 6510450593 ปริยวิศว์ เตชะกฤตเมธีธำรง

## Installation

1. **Clone repository**: `git clone https://github.com/se-jang/transport-application.git`
2. **Backend Setup**:

- เข้าไปที่โฟลเดอร์ frontend: `cd backend`
- ติดตั้ง dependencies: `mvn install`
- รัน Spring Boot application: `mvn spring-boot:run`

3. **Frontend Setup**:

- เข้าไปที่โฟลเดอร์ frontend: `cd frontend`
- ติดตั้ง dependencies: `npm install`
- รัน application: `npm run dev`

## Configuration

### 1. การตั้งค่า Environment Variables

สร้างไฟล์ `.env` ในโฟลเดอร์ src/main/resources ด้วยเนื้อหาดังนี้:

```plaintext
JWT_SECRET=c2b000d555fe251e11a30a7ae438b65a6a51c9989f42997b361fd9c9bd543757d167ff92a3961ff4ca3c25c2122e94a3e53db62a45ffd198846a7441f57a68e82f1ca43e218a458ade6d2e979dd609198ebc0b75d96f87c60e3213a22321479a9cbb304bbf04ed0b49af8c04d5cef5aef143530b6867cb7627d965bc53979d253fa8686b0f249c29cdd447f443d896a2a939740ea9a376feada51a481f825cc65ebf25cde6e4ce308363c139dd1a6367249cf8a242882396792770ed98760970c12768eefa787c6f99c5fb446bb6c187a274310d8580a6db8e5120c420800e5717417047f6fd5a1fdf6529b62dfd6a262a5dca4b617d987c00f853b089084637
STRIPE_TEST_KEY=sk_test_51Qo5Kd4GNo7fC8bY88uFMlUtHNOMPSPpFGubObIeqydMgCTafI98hX1sWOCnidWZJAC4DwIJeYG2ZI1dqu9VTSEE00G5MxXsFE
SPRING_MAIL_PASSWORD='iuzq kgqr uilf wcmn'
SPRING_MAIL_USERNAME=transportapplicationjab@gmail.com
SPRING_EMAIL_SENDER_HOST=smtp.gmail.com
```

### 2. การตั้งค่าการส่งอีเมล

สร้างไฟล์ application-secret.properties ในโฟลเดอร์ src/main/resources ด้วยเนื้อหาดังนี้:

```plaintext
spring.email.sender.host=${SPRING_EMAIL_SENDER_HOST}
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}
spring.mail.debug=true

stripe.api.key.test=${STRIPE_TEST_KEY}
```

## Features

- **สร้างผู้ใช้ (Create User)**
  - Admin สามารถสร้าง Account ของ User และ Worker ได้
- **สร้างคำสั่งซื้อ (Create Order)**

  - ใช้สำหรับสร้างคำสั่งซื้อใหม่ในระบบโดยจะให้กรอกชื่อ ที่อยู่ของลูกค้า และ ข้อมูล product

- **เข้าสู่ระบบ (Login)**

  - ผู้ใช้สามารถเข้าสู่ระบบโดยใช้ชื่อผู้ใช้และรหัสผ่านที่ Admin ได้สร้างไว้

- **การจัดการคำสั่งซื้อ (Order Management)**

  - แสดงและกรองคำสั่งซื้อที่สถานะต่าง ๆ เช่น:
    - **Unchecked**
    - **Checked**
    - **On-going**

- **การอัพโหลดเอกสาร (File Upload)**

  - Worker สามารถอัพโหลดไฟล์ PDF ที่เกี่ยวข้องกับใบออเดอร์ได้หลังจากที่ลูกค้าเซ็นเอกสาร

- **การจัดการพนักงานขนส่ง (Transportation Worker Management)**

  - สามารถดูรายละเอียดและมอบหมายพนักงานขนส่งให้กับคำสั่งซื้อได้

- **การแจ้งเตือนผ่านอีเมล (Email Notification)**
  - เมื่อมีการอัปเดตสถานะของคำสั่งซื้อ ระบบจะส่งอีเมลแจ้งเตือนไปยัง User เพื่อให้ทราบถึงสถานะล่าสุดของคำสั่งซื้อ

## Usage

- เปิดเว็บเบราว์เซอร์ไปที่ `http://localhost:5173/login` เพื่อเข้าถึงระบบ
- เมื่อเปิดแอพพลิเคชั่น ระบบจะพาคุณไปที่หน้า **Login** เท่านั้น เนื่องจากเป็นแอพพลิเคชั่นแบบ **Private**
- **การสร้างผู้ใช้ (Create User)**:
  - ต้องให้ Admin เป็นผู้สร้างผู้ใช้ โดยสามารถทำได้ผ่าน Postman
  - ใช้คำสั่ง **POST** ที่ URL: `http://localhost:8080/create-user`
  - สำหรับการสร้างผู้ใช้ใหม่ ต้องส่งคำขอ (request) ตามรูปแบบดังนี้:

### Create User Request Example

```json
{
  "username": "user123",
  "name": "John Doe",
  "password": "your_password",
  "phoneNumber": "0123456789",
  "email": "john.doe@example.com",
  "role": 1
}
```

### Note for Creating Admin

- Role ของ User
  - 0 คือ Admin
  - 1 คือ User
  - 2 คือ Worker
- หากต้องการสร้างผู้ใช้ที่มีบทบาทเป็น Admin จะต้องสร้างผู้ใช้เป็น User ก่อน จากนั้นทำการเปลี่ยน role ของผู้ใช้ในฐานข้อมูลเป็น 0 เพื่อกำหนดให้เป็น Admin

### Field Descriptions

- **username**: ชื่อผู้ใช้ ต้องมีความยาวอย่างน้อย 4 ตัวอักษร
- **name**: ชื่อผู้ใช้ ต้องเป็นตัวอักษรและเว้นวรรค
- **password**: รหัสผ่าน ต้องมีความยาวตั้งแต่ 8 ตัวอักษรขึ้นไป
- **phoneNumber**: หมายเลขโทรศัพท์ ต้องเป็นตัวเลข 10 หลัก
- **email**: อีเมล ต้องมีรูปแบบที่ถูกต้อง
- **role**: ระบุบทบาทของผู้ใช้ เช่น USER หรือ WORKER

## Design Principles

### Single Responsibility Principle (SRP)

ในโค้ดนี้ ได้ทำการแบ่งแยกความรับผิดชอบออกเป็นคลาสแยกกัน เพื่อให้แต่ละคลาสมีหน้าที่เฉพาะและไม่ทับซ้อนกัน ซึ่งช่วยให้โค้ดเข้าใจง่ายและจัดการได้ดีขึ้น:

- **การใช้งานในโค้ด:**
  - `CreateOrderController` รับผิดชอบเฉพาะการสร้างคำสั่งซื้อใหม่ในระบบ โดยจะดำเนินการที่เกี่ยวข้องกับการสร้างออเดอร์เพียงอย่างเดียว
  - `CreateUserController` รับผิดชอบการสร้างผู้ใช้หรือพนักงานขนส่ง (worker) ตามบทบาทที่ระบุ
  - `LoginController` รับผิดชอบเฉพาะการจัดการการเข้าสู่ระบบของผู้ใช้ เพื่อแยกฟังก์ชันการทำงานออกจากฟังก์ชันอื่น
  - `OrderController` ทำหน้าที่เฉพาะในการแสดงและจัดการคำสั่งซื้อที่มีอยู่ ซึ่งเป็นการแยกการทำงานเฉพาะของการจัดการคำสั่ง
  - `UserController` ทำหน้าที่ในการดึงข้อมูลของผู้ใช้
  - `TransportationWorkerController` ใช้สำหรับดึงข้อมูลพนักงานขนส่ง และจัดการการมอบหมายพนักงานขนส่งให้กับคำสั่งซื้อที่เฉพาะเจาะจง
  - `UploadFileController` จัดการการอัพโหลดไฟล์ที่เกี่ยวข้องกับคำสั่งซื้อ โดยแยกการทำงานเฉพาะส่วนการอัพโหลดออกมา
  - `GlobalExceptionHandler` ทำหน้าที่จัดการข้อผิดพลาดที่เกิดขึ้นทั่วระบบ โดยแยกการจัดการข้อผิดพลาดที่เกี่ยวข้องกับการตรวจสอบข้อมูล, IllegalArgumentException, และ Exception ทั่วไป

### Dependency Inversion Principle (DIP)

ในโค้ดนี้ ใช้หลักการ DIP เพื่อแยกส่วนที่เป็น Dependency ของแต่ละ Service ออกจากกัน ทำให้โค้ดยืดหยุ่นและง่ายต่อการปรับเปลี่ยนในอนาคต:

- **การใช้งานในโค้ด:**
  - การใช้ `@Autowired` ของ Spring ในการจัดการ Dependency Injection ของ Service ต่าง ๆ เช่น `UserService` และ `OrderService` ช่วยลดการพึ่งพาโดยตรงระหว่างคลาส โดยให้ Spring Framework จัดการการเชื่อมโยงและสร้างอินสแตนซ์ของ Service ให้โดยอัตโนมัติ
