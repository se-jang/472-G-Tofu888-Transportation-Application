<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const receipt = ref(null)

onMounted(async () => {
    try {
        const rest = await axios.get(
            `http://localhost:8080/receipt/${route.params.id}`
        )
        receipt.value = rest.data
    } catch (error) {
        console.error('Error fetching receipt:', error)
    }
})

const printReceipt = () => {
    const printContent = document.getElementById('print-section').innerHTML
    const originalContent = document.body.innerHTML

    document.body.innerHTML = printContent
    window.print()
    document.body.innerHTML = originalContent
    window.location.reload()
}
</script>

<template>
    <Header />
    <main class="container" v-if="receipt">
        <div id="print-section" class="receipt-container">
            <h2 class="title">ใบส่งของ</h2>
            <p class="subtitle">Delivery Order</p>

            <div class="info-section">
                <div class="company-info">
                    <strong>{{ receipt.user }}</strong>
                    <p>โทร: {{ receipt.phone }}</p>
                    <p>อีเมล: {{ receipt.email }}</p>
                </div>
                <div class="invoice-details">
                    <p>เลขที่ {{ receipt.id }}</p>
                    <p>วันที่ {{ receipt.date }}</p>
                </div>
            </div>

            <div class="info-section">
                <div class="customer-info">
                    <strong>ลูกค้า: {{ receipt.cus_name }}</strong>
                    <p>{{ receipt.cus_address }}</p>
                    <p>สถานะ: {{ receipt.status }}</p>
                    <p>วันที่: {{ receipt.date }}</p>
                    <p>ผู้ส่ง: {{ receipt.worker }}</p>
                </div>
            </div>

            <table class="invoice-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>ชื่อสินค้า</th>
                        <th>ประเภท</th>
                        <th>จำนวน</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in receipt.products" :key="item.id">
                        <td>{{ item.id }}</td>
                        <td>{{ item.name }}</td>
                        <td>{{ item.type }}</td>
                        <td>{{ item.quantity }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <button class="print-btn" @click="printReceipt">พิมพ์ใบเสร็จ</button>
    </main>

    <p v-else class="loading-message">กำลังโหลดข้อมูล...</p>
</template>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
}

.receipt-container {
    width: 80%;
    max-width: 700px;
    padding: 1.5rem;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
    border: 1px solid #e0e0e0;
    background-color: #fdfdfd;
    text-align: center;
    margin-top: 1rem;
}

.title {
    font-size: 1.5rem;
    font-weight: bold;
}

.subtitle {
    font-size: 1rem;
    color: #555;
    margin-bottom: 10px;
}

.info-section {
    display: flex;
    justify-content: space-between;
    text-align: left;
    margin-bottom: 10px;
}

.company-info,
.customer-info {
    flex: 1;
}

.invoice-details {
    text-align: right;
}

.invoice-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
}

.invoice-table th,
.invoice-table td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center;
}

.print-btn {
    margin-top: 10px;
    padding: 10px 16px;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    max-width: 30%;
}

.print-btn:hover {
    background-color: #45a049;
}

.loading-message {
    text-align: center;
    font-size: 1.2rem;
    color: #666;
}
</style>
