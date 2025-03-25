<template>
  <div class="user-card" v-if="userId && name">
    <h2 class="user-id">User ID: {{ userId }}</h2>
    <p class="user-name">Name: {{ name }}</p>
    <button class="details-button" @click="goDetail">Details</button>
  </div>
  <div v-else>
    <p>Invalid User Data</p>
  </div>
</template>
  
  <script>
  export default {
    name: "UserCard",
    props: {
      userId: {
        type: String,
        required: true,
        default: 'Unknown'
      },
      name: {
        type: String,
        required: true,
        default: 'Unknown'
      },
    },
    methods: {
      goDetail() {
        console.log("Navigating to user detail for ID:", this.userId);
        this.$router.push({ name: 'user-detail', params: { userId: this.userId } });
      }
    }
  };
  </script>
  
  <style scoped>
  .user-card {
    position: relative;
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin-top: 15px;
  }
  
  .user-id,
  .user-name {
    font-family: "Inter", sans-serif;
    margin: 0;
    margin-bottom: 10px;
  }
  
  .details-button {
    padding: 5px 10px;
    font-family: "Inter", sans-serif;
    background-color: gray;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    width: auto;
  }
  </style>
  