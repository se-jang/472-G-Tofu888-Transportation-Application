<template>
  <div class="worker-details-page">
    <Header></Header>

    <div class="main-container">
      <div class="content-container">
        <h2 class="worker-h2">Worker ID: {{ workerId }}</h2>
        <div class="order-list-header">
          <h2>Order list</h2>
          <button class="add-button" @click="addOrder">Add</button>
        </div>

        <div class="order-list-container">
          <table class="order-list-table">
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Date</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.id }}</td>
                <td>{{ formattedDate(order.date) }}</td>
                <td>{{ order.status }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <button class="back-button" @click="goBack">Back</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapGetters } from "vuex";
import Header from "../components/Header.vue";
import dayjs from "dayjs";

export default {
  components: {
    Header,
  },
  computed: {
    ...mapGetters(["userRole"]),
  },
  data() {
    return {
      workerId: this.$route.params.workerId,
      orders: [],
      role: "admin",
    };
  },
  created() {
    console.log("workerId from route params:", this.workerId);
    if (this.workerId) {
      this.fetchOrders();
    } else {
      console.error("workerId is not defined.");
    }
  },

  methods: {
    async fetchOrders() {
      try {
        const response = await axios.get(`http://localhost:8080/transportation-workers/${this.workerId}/orders`);
        this.orders = response.data;
      } catch (error) {
        console.error("Error fetching orders:", error);
      }
    },
    formattedDate(date) {
      return dayjs(date).format('DD/MM/YYYY HH:mm:ss');
    },
    addOrder() {
      this.$router.push({ name: "add-order", params: { workerId: this.workerId } });
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap");

.worker-details-page {
  background-color: #f5f5f5;
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

.content-container {
  background-color: #ffffff;
  padding: 20px;
  width: 80%;
  max-width: 1000px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

h1 {
  display: inline-block;
  font-weight: 600;
}

.order-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.add-button {
  width: auto;
  font-weight: 500;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 550px;
}

.order-list-container {
  margin-top: 20px;
}

.order-list-table {
  width: 100%;
  border-collapse: collapse;
}

.order-list-table th,
.order-list-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.back-button {
  margin-top: 20px;
  background-color: #000;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
}
</style>
