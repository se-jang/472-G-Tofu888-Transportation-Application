<template>
  <div class="page-container">
    <Header></Header>
    <div class="main-container">
      <div class="order-container">
        <div class="order-header">
          <h2 class="order-title">Add Order</h2>
          <button class="back" @click="goBack">Back</button>
        </div>
        <button v-if="orders.length > 0" @click="assignWorker"></button>

        <div class="order-list">
          <AddOrderWorkerCard
            v-for="order in orders"
            :key="order.id"
            :status="order.status"
            :orderId="order.id"
            :date="order.date"
            :customerName="order.customerName"
            :isChecked="selectedOrderId === order.id"
            @checkOrder="selectOrder(order.id)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Header from "../components/Header.vue";
import AddOrderWorkerCard from "@/components/AddOrderWorkerCard.vue";
import { mapGetters } from "vuex";

export default {
  components: {
    Header,
    AddOrderWorkerCard,
  },
  data() {
    return {
      role: "admin",
      selectedWorkerId: this.$route.params.workerId,
      selectedOrderId: null,
      orders: [],
    };
  },
  computed: {
    ...mapGetters(["userRole"]),
  },
  methods: {
    async assignWorker() {
      console.log("Worker ID: ", this.selectedWorkerId);
      console.log("Order ID: ", this.selectedOrderId);
      if (!this.selectedOrderId || !this.selectedWorkerId) {
        alert("Please select an order and a worker before assigning.");
        return;
      }
      try {
        const status = "ONGOING";
        const response = await axios.post(
          `http://localhost:8080/transportation-workers/worker/worker-detail/${this.selectedWorkerId}/add-order`,
          null,
          {
            params: {
              workerId: this.selectedWorkerId,
              orderId: this.selectedOrderId,
            },
          }
        );
        console.log(response.data.message);
        const statusUpdateResponse = await fetch(
          `http://localhost:8080/orders/order-detail/${this.selectedOrderId}/change-status`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
              orderId: this.selectedOrderId,
              status: status,
            }),
          }
        );

        const statusUpdateData = await statusUpdateResponse.json();
        console.log(statusUpdateData.message);
        alert(
          statusUpdateData.message ||
            "Order status updated to ONGOING successfully."
        );

        this.$router.push({
          name: "worker-detail",
          params: { workerId: this.selectedWorkerId },
        });
      } catch (error) {
        console.error("Failed to assign worker:", error);
      }
    },
    goBack() {
      this.$router.go(-1);
    },
    selectOrder(orderId) {
      console.log("Selected Order ID:", orderId);
      this.selectedOrderId = orderId;
    },
    async fetchOrders() {
      try {
        const response = await axios.get(
          "http://localhost:8080/orders/check-orders"
        );
        this.orders = response.data;
        console.log("orders:", this.orders);
      } catch (error) {
        console.error("Failed to fetch orders:", error);
      }
    },
  },
  created() {
    this.fetchOrders();
  },
};
</script>

<style scoped>
:root {
  --main-bg-color: #717171;
  --sub-bg-color: #eeeeee;
  --card-bg-color: #ffffff;
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
}

.order-title {
  font-family: "Inter", sans-serif;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: left;
}

.order-container {
  background-color: #e6e6e6;
  padding: 20px;
  width: 80%;
  max-width: 1200px;
  overflow-y: auto;
  height: 100%;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.back {
  background-color: black;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 14px;
  margin-bottom: 10px;
  width: 10%;
}

.add-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
}

.order-list {
  margin-top: 20px;
}
</style>
