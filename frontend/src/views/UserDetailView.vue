<template>
  <div class="user-detail-page">
    <Header></Header>

    <div class="main-container">
      <div class="content-container">
        <h2 class="user-h2">User ID: {{ userId }}</h2>
        <div class="user-info-wrapper">
          <div class="user-info-box" v-if="userInfo">
            <div class="user-info-left">
              <p><strong>Username:</strong> {{ userInfo.username }}</p>
              <p><strong>Name:</strong> {{ userInfo.name }}</p>
            </div>
            <div class="user-info-right">
              <p><strong>Email:</strong> {{ userInfo.email }}</p>
              <p><strong>Phone Number:</strong> {{ userInfo.phoneNumber }}</p>
            </div>
          </div>
          <div v-else>
            <p>Loading user information...</p>
          </div>
        </div>

        <div class="order-history-box">
          <p class="order-history-title">Order History</p>
          <table class="order-history-table">
            <thead>
            <tr>
              <th>ID</th>
              <th>Due Date</th>
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

        <button class="back-button" @click="$router.back()">Back</button>

        <button
            v-if="userRole === 'ADMIN' && canDeleteUser"
            class="delete-button"
            @click="deleteUser">
          Delete User
        </button>
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
      userId: this.$route.params.userId,
      userInfo: null,
      orders: [],
      canDeleteUser: false,
    };
  },
  created() {
    if (this.userId) {
      console.log("User Role:", this.userRole);
      this.fetchUserInfo();
      this.fetchUserOrders();
    } else {
      console.error("userId is not defined.");
    }
  },
  watch: {
    orders: {
      deep: true,
      handler(newOrders) {
        console.log("Orders updated:", newOrders);
        this.canDeleteUser = newOrders.length > 0
            && newOrders.every(order => ["completed", "unpaid"].includes(order.status.toLowerCase()))
        console.log("Can delete user:", this.canDeleteUser);
      }
    }
  },
  methods: {
    async fetchUserInfo() {
      try {
        const response = await axios.get(`http://localhost:8080/users/${this.userId}`);
        this.userInfo = response.data;
      } catch (error) {
        console.error("Error fetching user info:", error);
      }
    },
    async fetchUserOrders() {
      try {
        const response = await axios.get(`http://localhost:8080/users/${this.userId}/order`);
        this.orders = response.data;
      } catch (error) {
        console.error("Error fetching user orders:", error);
      }
    },
    async deleteUser() {
      if (!confirm("Are you sure you want to delete this user?")) {
        return;
      }
      try {
        await axios.delete(`http://localhost:8080/users/${this.userId}/delete`);
        alert("User deleted successfully!");
        this.$router.push("/user-list");
      } catch (error) {
        console.error("Error deleting user:", error);
        alert("Failed to delete user.");
      }
    },
    formattedDate(date) {
      return dayjs(date).format('DD/MM/YYYY HH:mm:ss');
    },
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

  .main-container {
    display: flex;
    justify-content: center;
    flex: 1;
    padding: 20px;
  }

  .user-detail-container {
    background-color: #e0e0e0;
    width: 80%;
    max-width: 1000px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    position: relative;
  }

  .user-id-text {
    font-size: 20px;
    font-weight: bold;
    position: absolute;
    top: 20px;
    left: 20px;
  }

  .user-info-wrapper {
    background-color: var(--sub-bg-color);
    border: 1px solid var(--border-color);
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 30px;
    margin-top: 60px;
  }

  .back-button {
    position: absolute;
    top: 20px;
    right: 20px;
    background-color: var(--button-bg-color);
    color: var(--button-text-color);
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    width: auto;
    margin-bottom: 20px;
  }

  .user-info-box {
    display: flex;
    justify-content: space-between;
    background-color: #ffffff;
    border-radius: 1px;
  }

  .user-info-right {
    display: flex;
    flex-direction: column;
    text-align: left;
    margin-right: 100px;
  }

  .user-info-left {
    display: flex;
    flex-direction: column;
    text-align: left;
    margin-left: 5px;
  }

  .user-info-left p,
  .user-info-right p {
    margin-bottom: 10px;
    font-size: 16px;
  }

  .order-history-box {
    margin-bottom: 30px;
  }

  .order-history-title {
    font-size: 18px;
    margin-bottom: 15px;
  }

  .order-history-table {
    width: 100%;
    border-collapse: collapse;
  }

  .order-history-table th,
  .order-history-table td {
    border: 1px solid var(--border-color);
    padding: 10px;
    text-align: left;
  }

   .delete-button {
     background-color: red;
     color: white;
     border: none;
     padding: 10px 20px;
     border-radius: 5px;
     cursor: pointer;
     margin-top: 20px;
   }
  .delete-button:hover {
    background-color: darkred;
  }

  </style>
