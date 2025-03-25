<template>
  <div class="order-card">
    <div class="status-indicator" :class="statusClass">{{ status }}</div>
    <h2 class="order-id">Order ID: {{ orderId }}</h2>
    <p class="customer-name">Customer Name: {{ customerName }}</p>
    <p class="due-date">Date: {{ formattedDate(date) }}</p>

    <button class="details-button" @click="viewDetails">Details</button>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: "OrderAdminCard",
  props: {
    status: {
      type: String,
      required: true,
    },
    orderId: {
      type: String,
      required: true,
    },
    date: {
      type: String,
      required: true,
    },
    customerName: {
      type: String,
      required: true,
    },
  },
  computed: {
    statusClass() {
      switch (this.status) {
        case "UNPAID":
          return "status-unpaid";
        case "PAID":
          return "status-paid";
        case "UNCHECK":
          return "status-uncheck";
        case "CHECKED":
          return "status-checked";
        case "ONGOING":
          return "status-ongoing";
        case "DELIVERED":
          return "status-delivered";
        case "UPLOADED":
          return "status-uploaded";
        case "COMPLETED":
          return "status-completed";
        default:
          return "";
      }
    },
  },
  methods: {
    viewDetails() {
      console.log(`Viewing details for order ID: ${this.orderId}`);
      this.$router.push({
        name: "order-detail",
        params: { orderId: this.orderId },
      });
    },
    formattedDate() {
      return dayjs(this.date).format("DD/MM/YYYY HH:mm:ss");
    },
  },
};
</script>

<style scoped>
* {
  font-family: "Inter", sans-serif;
}
.order-card {
  position: relative;
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 15px;
}

.order-id,
.due-date,
.customer-name,
.customer-address {
  margin: 0;
  margin-bottom: 10px;
}

.details-button {
  padding: 5px 10px;
  background-color: gray;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: auto;
}

.status-indicator {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 10px;
  font-size: 14px;
  font-weight: bold;
  color: white;
  border-radius: 5px;
}

.status-unpaid {
  background-color: #e21a1a;
}

.status-paid {
  background-color: #dce532;
}

.status-uncheck {
  background-color: #d3d3d3;
}

.status-checked {
  background-color: #1e90ff;
}

.status-ongoing {
  background-color: #ffa500;
}

.status-delivered {
  background-color: #32cd32;
}

.status-uploaded {
  background-color: #8a2be2;
}

.status-completed {
  background-color: #00289e;
}
</style>
