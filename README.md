# Transport Application

## รายชื่อสมาชิก ของกลุ่ม JANG

- 6510450313 ณฐนนท์ ภูธนกิจ (Product Owner)
- 6510450607 ปริยานุช มั่งคั่ง (Developer)
- 6510450763 ภัควัฒน์ ปานกลาง (Developer)
- 6510450593 ปริยวิศว์ เตชะกฤตเมธีธำรง (Developer)
- 6510450879 ยศวัจน์ บวรศรีธนนนท์ (Developer)

---

## Features

### ปริยวิศว์ เตชะกฤตเมธีธำรง 6510450593 หมู่ 200 ปี 3

@xssxx Feat: Payment

- As a customer, I want to be able to view my payment history so that I can track my previous transactions.
- As a customer, I want to be able to select a payment service so that I can use my preferred provider.
- As a customer, I want to be able to pay so that I can use my preferred payment method.

### ยศวัจน์ บวรศรีธนนนท์ 6510450879 หมู่ 200 ปี 3

@FrostBitzX Feat: Receipt

- As a customer, I want to see and show my receipt correctly.
- As an admin, I want to be able to view any customer's receipt.
- As a customer, I want to be able to view my order receipt so that I can check receipt details.

### ปริยานุช มั่งคั่ง 6510450607 หมู่ 200 ปี 3

@Pariyanuch Feat: Order

- As a customer, I want to be able to delete my order so that I can remove an order I no longer need.
- As a customer, I want to be able to edit my order so that I can change my order details.
- As an user, I want to be able to search for orders by customer's name so that I can view specific user orders.

### ภัควัฒน์ ปานกลาง 6510450763 หมู่ 200 ปี 3

@JIMpk2w Feat: Account

- As an admin, I want to be able to delete a customer's account so that I can remove inactive users.
- As a customer, I want to add a profile picture so that I can see an image of myself on my profile.
- As a customer, I want to be able to edit my profile so that I can update my personal information.
---

## Installation (Manual)

1. **Clone repository**: `git clone https://github.com/se-jang/transport-application.git`
2. **Backend Setup**:

- เข้าไปที่โฟลเดอร์ frontend: `cd backend`
- ติดตั้ง dependencies: `mvn install`
- รัน Spring Boot application: `mvn spring-boot:run`

3. **Frontend Setup**:

- เข้าไปที่โฟลเดอร์ frontend: `cd frontend`
- ติดตั้ง dependencies: `npm install`
- รัน application: `npm run dev`

---

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
