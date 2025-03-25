<template>
  <div class="page-container">
    <Header></Header>
    <div class="main-container">
      <div class="order-detail-container" v-if="order">
        <button class="delete-button" v-if="userRole === 'USER' && (order.status === 'UNPAID' || order.status === 'COMPLETED')" @click=deleteOrder>Delete</button>
        <button class="edit-button" v-if="userRole === 'USER' && order.status === 'UNPAID'" @click=editOrder>Edit</button>
        <button class="back-button" @click="$router.back()">Back</button>
        <p class="order-id-text">Order ID: {{ this.$route.params.orderId }}</p>
        <div class="order-info-wrapper">
          <div class="order-info-box">
            <div class="order-info-left">
              <p><strong>Date:</strong> {{ formattedDate(order.date) }}</p>
              <p>
                <strong>Transportation Worker:</strong>
                {{ order.workerUsername || "N/A" }}
              </p>
            </div>
            <div class="order-info-right">
              <p><strong>Status:</strong> {{ order.status }}</p>
              <p><strong>Customer Name:</strong> {{ order.customerName }}</p>
              <p>
                <strong>Customer Address:</strong>
                {{ order.customerAddress || "N/A" }}
              </p>
            </div>
          </div>
        </div>

        <div class="product-list-box">
          <p class="product-list-title">Product List</p>
          <table class="product-list-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Type</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in order.productDetails" :key="product.id">
                <td>{{ product.id }}</td>
                <td>{{ product.productName }}</td>
                <td>{{ product.productType }}</td>
                <td v-if="product.quantity">{{ product.quantity }}</td>
                <td v-else>N/A</td>
              </tr>
            </tbody>
          </table>
        </div>

        <button
          v-if="userRole === 'ADMIN' && order.status === 'UNCHECK'"
          @click="checked"
          class="checked-button"
        >
          Checked
        </button>
        <button
          v-if="userRole === 'ADMIN' && order.status === 'UPLOADED'"
          @click="clickComplete"
          class="checked-button"
        >
          Complete
        </button>
        <button
          v-if="userRole === 'ADMIN' && order.status === 'UPLOADED'"
          @click="showFileModal"
          class="file-pop"
        >
          File
        </button>
        <FileViewerModal
          :isVisible="isFileModalVisible"
          :fileUrl="fileUrl"
          @close="isFileModalVisible = false"
        />
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
import Header from "../components/Header.vue";
import FileViewerModal from "../components/FileViewerModal.vue";
import dayjs from "dayjs";

export default {
  name: "OrderDetail",
  components: {
    Header,
    FileViewerModal,
  },
  data() {
    return {
      order: null,
      isFileModalVisible: false,
      fileUrl: "",
    };
  },
  computed: {
    ...mapGetters(["userRole", "id"]),
  },
  methods: {
    async fetchOrderDetails() {
      try {
        const response = await axios.get(
          `http://localhost:8080/orders/order-detail/${this.$route.params.orderId}`
        );
        this.order = response.data;
        console.log("Order data:", this.order);
      } catch (error) {
        console.error("Error fetching order details:", error);
      }
    },
    formattedDate(date) {
      return dayjs(date).format("DD/MM/YYYY HH:mm:ss");
    },
    editOrder() {
      const orderId = this.$route.params.orderId;
      this.$router.push({
        name: 'edit-order',
        query: {orderDetails: JSON.stringify(this.order)}});
    },
    deleteOrder() {
      const orderId = this.$route.params.orderId;
      if (confirm("Do you want to delete this order?")) { // ยืนยันการลบ
        axios
          .delete(`http://localhost:8080/orders/${orderId}/delete`)
          .then((response) => {
            alert(response.data || "Order delete successfully");
            this.$router.push({ name: "orders" }); // ย้ายไปหน้า orders หลังลบสำเร็จ
          })
          .catch((error) => {
            console.error("Error deleting order:", error);
            if (error.response && error.response.status === 403) {
              alert("Order cannot be deleted due to invalid orders status");
            } else {
              alert("An error occurred while deleting the order");
            }
          });
      }
    },
    checked() {
      const orderId = this.$route.params.orderId;
      console.log("order: ", orderId);
      const status = "CHECKED";

      fetch(
        `http://localhost:8080/orders/order-detail/${orderId}/change-status?staus=CHECKED`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: new URLSearchParams({
            orderId: orderId,
            status: status,
          }),
        }
      )
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
        .then((data) => {
          console.log(data.message);
          alert(data.message || "Order status updated successfully");
          this.$router.push({ name: "orders" });
        })
        .catch((error) => {
          console.error("Error updating order status:", error);
          alert("Error updating order status");
        });
    },
    clickComplete() {
      const orderId = this.$route.params.orderId;
      console.log("order: ", orderId);
      const status = "COMPLETED";

      fetch(
        `http://localhost:8080/orders/order-detail/${orderId}/change-status`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: new URLSearchParams({
            orderId: orderId,
            status: status,
          }),
        }
      )
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
        .then((data) => {
          console.log(data.message);
          alert(data.message || "Order status updated successfully");
          this.$router.push({ name: "orders" });
        })
        .catch((error) => {
          console.error("Error updating order status:", error);
          alert("Error updating order status");
        });
    },
    showFileModal() {
      this.fileUrl = `http://localhost:8080/orders/order-detail/${this.$route.params.orderId}/shipment-doc`;
      this.isFileModalVisible = true;
    },
  },
  mounted() {
    this.fetchOrderDetails();
  },
};
</script>

<style>
:root {
  --main-bg-color: #f5f5f5;
  --sub-bg-color: #ffffff;
  --button-bg-color: #000;
  --button-text-color: #ffffff;
  --border-color: #ddd;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.page-container {
  background-color: var(--main-bg-color);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  display: flex;
  justify-content: center;
  flex: 1;
  padding: 20px;
}

.order-detail-container {
  background-color: #ffffff;
  padding: 20px;
  width: 80%;
  max-width: 1000px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

.order-id-text {
  font-size: 20px;
  font-weight: bold;
  position: absolute;
  top: 20px;
  left: 20px;
}

.order-info-wrapper {
  background-color: var(--sub-bg-color);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  margin-top: 60px;
}

.back-button{
  position: absolute;
  background-color: var(--button-bg-color);
  color: var(--button-text-color);
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  width: auto;
}
.edit-button {
  position: absolute;
  background-color: var(--button-bg-color);
  color: var(--button-text-color);
  border: none;
  top: 40px;
  right: 100px;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  width: auto;
}
.delete-button {
  position: absolute;
  background-color: var(--button-bg-color);
  color: var(--button-text-color);
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  width: auto;
  bottom: 20px;
  right: 20px;
}

.checked-button {
  position: absolute;
  bottom: 20px;
  right: 20px;
  background-color: var(--button-bg-color);
  color: var(--button-text-color);
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  width: auto;
}

.file-pop {
  position: absolute;
  bottom: 20px;
  right: 150px;
  background-color: var(--button-bg-color);
  color: var(--button-text-color);
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  width: auto;
}

.order-info-box {
  display: flex;
  justify-content: space-between;
  background-color: #ffffff;
  border-radius: 1px;
}

.order-info-right {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
  margin-right: 100px;
}

.order-info-left {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
  margin-left: 5px;
}

.order-info-left p,
.order-info-right p {
  margin-bottom: 10px;
  font-size: 16px;
}

.product-list-box {
  margin-bottom: 30px;
}

.product-list-title {
  font-size: 18px;
  margin-bottom: 15px;
}

.product-list-table {
  width: 100%;
  border-collapse: collapse;
}

.product-list-table th,
.product-list-table td {
  border: 1px solid var(--border-color);
  padding: 10px;
  text-align: left;
}
</style>
