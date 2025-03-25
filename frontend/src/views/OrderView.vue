<template>
  <div class="page-container">
    <Header></Header>

    <div class="main-container">
      <div class="order-container">
        <h2 class="order-title">Order</h2>

        <div v-if="userRole === 'ADMIN'">
          <label for="status">Filter by Status:</label>
          <select v-model="selectedStatus" @change="fetchOrdersFilter">
            <option value="all">All</option>
            <option value="unpaid">Unpaid</option>
            <option value="uncheck">Unchecked</option>
            <option value="check">Checked</option>
            <option value="on-going">On-going</option>
            <option value="delivered">Delivered</option>
            <option value="uploaded">Uploaded</option>
            <option value="complete">Complete</option>
          </select>
        </div>
        
        <input type="text" v-model="searchQuery" placeholder="Search..." />
        
        <div class="item-order-searchbar" v-for="order in filteredOrders" :key="order.id">
        </div>

        <div class="item-error-searchbar" v-if="searchQuery && !filteredOrders.length">
          <p>No results found!</p>
        </div>

        <component
          v-for="order in filteredOrders"
          :key="order.id"
          :total="order.total"
          :paymentLink="order.paymentLink"
          :is="orderComponent"
          :orderId="order.id"
          :date="order.date || 'N/A'"
          :status="order.status"
          :customerName="order.customerName"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Header from "../components/Header.vue";
import OrderAdminCard from "../components/OrderAdminCard.vue";
import OrderWorkerCard from "../components/OrderWorkerCard.vue";
import OrderCompanyCard from "../components/OrderCompanyCard.vue";
import axios from "axios";

export default {
  components: {
    Header,
    OrderAdminCard,
    OrderWorkerCard,
    OrderCompanyCard,
  },
  data() {
    return {
      orders: [],
      selectedStatus: "all",
      searchQuery: "",
    };
  },
  created() {
    if (this.id) {
      this.fetchOrdersBasedOnRole();
    } else {
      this.fetchOrders();
    }
  },
  computed: {
    ...mapGetters(["userRole", "id"]),
    
    orderComponent() {
      switch (this.userRole) {
        case "ADMIN":
          return OrderAdminCard;
        case "WORKER":
          return OrderWorkerCard;
        case "USER":
          return OrderCompanyCard;
        default:
          return null;
      }
    },
    
    filteredOrders() {
      return this.orders.filter(order =>
        order.customerName.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },
  methods: {
    async fetchOrdersBasedOnRole() {
      switch (this.userRole) {
        case "USER":
          await this.fetchOwnOrders(this.id);
          break;
        case "ADMIN":
          await this.fetchOrders();
          break;
        case "WORKER":
          await this.fetchWorkerOrders(this.id);
          break;
        default:
          console.error("Unknown user role");
      }
    },
    
    async fetchOrders() {
      try {
        const response = await axios.get("http://localhost:8080/orders/all-orders");
        this.orders = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
      } catch (error) {
        console.error("Error fetching orders:", error);
      }
    },

    async fetchOrdersFilter() {
      let endpoint = `http://localhost:8080/orders/${this.selectedStatus}-orders`;
      if (this.selectedStatus === "all") {
        endpoint = "http://localhost:8080/orders/all-orders";
      }

      try {
        const response = await axios.get(endpoint);
        this.orders = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
      } catch (error) {
        console.error("Error fetching filtered orders:", error);
      }
    },

    async fetchOwnOrders(userId) {
      try {
        const response = await axios.get(`http://localhost:8080/orders/user/${userId}`);
        this.orders = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
      } catch (error) {
        console.error("Error fetching own orders:", error);
      }
    },

    async fetchWorkerOrders(workerId) {
      try {
        const response = await axios.get(`http://localhost:8080/orders/worker/${workerId}`);
        this.orders = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
      } catch (error) {
        console.error("Error fetching worker orders:", error);
      }
    },
  },
};
</script>


<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Montserrat&display=swap");

:root {
  --main-bg-color: #4a4a4a;
  --sub-bg-color: #eeeeee;
  --card-bg-color: #ffffff;
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
</style>
