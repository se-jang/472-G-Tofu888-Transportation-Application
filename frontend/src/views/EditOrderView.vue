<template>
    <Header></Header>
    <div class="sub-container">
      <h2>{{ orderId ? "Edit Order" : "Create Order" }}</h2>
      <div class="form-container">
        <form @submit.prevent="submitOrder" class="form">
          <div class="customer-info">
            <div class="text-box">
              <label for="customerName">Customer Name:</label>
              <input
                type="text"
                id="customerName"
                class="input"
                placeholder="Enter Customer Name"
                v-model="orderRequest.customerName"
                required
              />
            </div>
  
            <div class="text-box">
              <label for="customerAddress">Customer Address:</label>
              <input
                type="text"
                id="customerAddress"
                class="input"
                placeholder="Enter Customer Address"
                v-model="orderRequest.customerAddress"
                required
              />
            </div>
  
            <div>
              <span>Select Payment Type: </span>
              <select v-model="orderRequest.paymentMethod" required>
                <option value="promptPayService">Promptpay</option>
                <option value="creditCardService">Credit Card</option>
              </select>
            </div>
          </div>
  
          <h3>Products</h3>
          <div class="scroll-container">
            <div
              class="product-fields"
              v-for="product in orderRequest.productDetails" :key="product.id"
            >
              <input
                type="text"
                class="input"
                placeholder="Product Name"
                v-model= product.productName
                required
              />
              <select v-model= product.productType required>
                <option disabled value="">Select Type</option>
                <option value="LIGHTWEIGHT">Lightweight</option>
                <option value="HEAVYWEIGHT">Heavyweight</option>
                <option value="FRAGILE">Fragile</option>
                <option value="HAZARDOUS">Hazardous</option>
                <option value="SPECIALTY">Specialty</option>
              </select>
              <input
                type="number"
                class="input"
                placeholder="Quantity"
                v-model.number=product.quantity
                min="1"
                required
              />
              <button type="button" class="x" @click="removeProduct(index)">
                ✖
              </button>
            </div>
          </div>
          <button type="button" class="add" @click="addProduct">
            + Add Product
          </button>
          <button type="submit" class="accept-button">Accept</button>
          <button type="button" class="accept-button" @click="cancelEdit">
            Cancel
          </button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import { mapGetters } from "vuex";
  import Header from "../components/Header.vue";
  
  export default {
    components: {
      Header,
    },
    computed: {
      ...mapGetters(["userRole", "username"]),
    },
    data() {
      return {
        orderId: null,
        orderRequest: {
          customerName: "",
          customerAddress: "",
          paymentMethod: "",
          productDetails: [{ productName: "", productType: "", quantity: 1 }],
        },
        originalOrderRequest: null,
      };
    },
    created() {
      this.orderId = this.$route.params.orderId;
      const orderDetails = JSON.parse(this.$route.query.orderDetails);
      if (this.orderId) {
        this.fetchOrderData(this.orderId, orderDetails);
        console.log("Edit Order: ", this.orderId)
      }
    },
    methods: {
      async fetchOrderData(orderId, orderDetails) {
        try {
          const response = await fetch(`http://localhost:8080/orders/edit-order/${this.$route.params.orderId}`);
          if (response.ok) {
            const orderData = await response.json();
            console.log("Edit Fetched Order Data:", orderData);
            console.log("Edit Order Det:", orderDetails);
            console.log("Request Body:", JSON.stringify(orderData, null, 2));
            this.originalOrderRequest = { ...orderData };
            this.orderRequest = JSON.parse(JSON.stringify(orderData));
            this.orderRequest.productDetails = orderDetails?.productDetails ?? [];
          } else {
            alert("Error: Unable to fetch order data");
          }
        } catch (error) {
          console.error("Error fetching order:", error);
        }
      },
      async submitOrder() {
        const method = this.orderId ? "PUT" : "POST";
        const url = this.orderId
          ? `http://localhost:8080/orders/edit-order/${this.$route.params.orderId}`
          : "http://localhost:8080/orders/create-order";
  
        try {
          const response = await fetch(url, {
            method,
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ ...this.orderRequest, username: this.username }),
          });
  
          if (response.ok) {
            alert(this.orderId ? "Order updated successfully" : "Order created successfully");
            this.$router.push("/orders");
          } else {
            const errorData = await response.json();
            alert(`Error: ${errorData.message || "Failed to process order"}`);
          }
        } catch (error) {
          console.error("Error:", error);
          alert("There was an error processing the order");
        }
      },
      addProduct() {
        this.orderRequest.productDetails.push({
          productName: "",
          productType: "",
          quantity: 1,
        });
      },
      removeProduct(index) {
        this.orderRequest.productDetails.splice(index, 1);
      },
      cancelEdit() {
        if (this.originalOrderRequest) {
          this.orderRequest = JSON.parse(JSON.stringify(this.originalOrderRequest));
        }
        this.$router.back();
      },
    },
  };
  </script>  
  
  <style scoped>
  .sub-container {
    max-width: 70%;
    height: calc(100vh - 62px);
    margin: 0 auto;
    background-color: #f9f9f9;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  .form-container {
    max-width: 80%;
    height: 89.7%;
    margin: auto;
    background-color: #f9f9f9;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: center;
    align-items: center;
    flex: 1;
  }
  
  .form {
    padding: 2em;
    max-width: 100%;
    max-height: 100%;
  }
  
  .customer-info {
    display: flex;
    gap: 20px;
    margin-top: 2%;
  }
  
  .scroll-container {
    box-sizing: border-box;
    max-width: 100%;
    max-height: 100%;
    max-height: 100%;
    min-height: calc(100vh / 2);
    overflow-y: overlay;
    background-color: rgb(255, 255, 255);
    box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.5);
    display: flex;
    flex-direction: column;
    gap: 10px;
    border-radius: 8px;
    padding: 10px;
    width: 1000px;
  }
  
  .product-fields {
    justify-content: center;
    align-items: center;
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
    width: 99%;
    background-color: rgb(255, 255, 255);
  }
  
  h2 {
    display: inline-block;
    text-align: left;
    margin-top: 5%;
    margin-bottom: 0px;
    margin-left: 10%;
  }
  
  h3 {
    margin-top: 2%;
  }
  
  .input {
    width: 100%;
    max-width: 500px;
    padding: 5px;
    box-sizing: border-box;
  }
  
  .accept-button,
  .add {
    margin-top: 20px;
    background-color: #189e1f;
  }
  
  .accept-button:hover,
  .add:hover {
    background-color: #24be2c;
  }
  
  .text-box {
    width: 45%;
  }
  
  input {
    width: 100%;
    padding: 8px;
    margin-top: 5px;
  }
  select {
    margin-top: 5px;
    height: 29px;
    border-radius: 5px;
    border-color: rgb(199, 199, 199);
  }
  
  .x {
    margin-top: 5px;
    width: 10%;
    height: 28px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #bebebe;
    border: none;
    cursor: pointer;
    font-size: 16px;
  }
  
  .x:hover {
    background-color: #4b4b4b;
  }
  </style>
  