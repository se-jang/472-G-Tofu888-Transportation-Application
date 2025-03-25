<template>
  <div class="header">
    <div class="header-container">
      <router-link to="/main" class="logo">Transport application</router-link>
      <nav class="nav">
        <router-link v-if="userRole === 'ADMIN'" to="/orders"
          >Order</router-link
        >
        <router-link v-if="userRole === 'ADMIN'" to="/worker-list"
          >Transport worker</router-link
        >
        <router-link v-if="userRole === 'ADMIN'" to="/user-list"
          >User</router-link
        >
        <router-link v-if="userRole === 'ADMIN'" to="/create-user"
          >Create User</router-link
        >

        <router-link v-if="userRole === 'USER'" to="/create-order"
          >Create Order</router-link
        >
        <router-link
          v-if="userRole === 'USER' || userRole === 'WORKER'"
          to="/orders"
          >My Order</router-link
        >
        <router-link v-if="userRole === 'USER'" to="/payment/history"
          >Transaction History</router-link
        >
      </nav>
    </div>
    <div class="profile" ref="profileContainer">
      <img
        :src="`http://localhost:8080${profile.profilePicture}`"
        alt="Profile"
        class="profile-image"
        @click="toggleProfileMenu"
      />
      <div v-if="showProfileMenu" class="profile-menu">
        <a href="#" @click.prevent="editProfile">Edit Profile</a>
        <a href="#" @click.prevent="contact">Contact</a>
        <div class="menu-divider"></div>
        <a href="#" @click.prevent="logout" class="logout-link">Logout</a>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import axios from "axios";

export default {
  name: "Header",
  data() {
    return {
      showProfileMenu: false,
      profile: {
        profilePicture: "",
      },
    };
  },
  computed: {
    ...mapGetters(["userRole", "id"]),
  },
  created() {
    if (this.id) {
      if (this.userRole === "WORKER") {
        this.fetchWorkerProfile();
      } else if (this.userRole === "USER" || this.userRole === "ADMIN") {
        this.fetchUserProfile();
      }
    }
  },
  methods: {
    toggleProfileMenu() {
      this.showProfileMenu = !this.showProfileMenu;
      if (this.showProfileMenu) {
        document.addEventListener("click", this.handleClickOutside);
      } else {
        document.removeEventListener("click", this.handleClickOutside);
      }
    },
    handleClickOutside(event) {
      if (!this.$refs.profileContainer?.contains(event.target)) {
        this.showProfileMenu = false;
        document.removeEventListener("click", this.handleClickOutside);
      }
    },
    async fetchUserProfile() {
      try {
        const response = await axios.get(
          `http://localhost:8080/users/${this.id}`
        );
        this.profile = { ...response.data };
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    },
    async fetchWorkerProfile() {
      try {
        const response = await axios.get(
          `http://localhost:8080/transportation-workers/${this.id}`
        );
        this.profile = { ...response.data };
      } catch (error) {
        console.error("Error fetching worker profile:", error);
      }
    },
    editProfile() {
      this.$router.push("/edit-profile");
      this.showProfileMenu = false;
    },
    contact() {
      alert("Tel: 089-***-****");
      this.showProfileMenu = false;
    },
    logout() {
      this.$store.dispatch("clearUserData");
      this.$router.push("/login");
      this.showProfileMenu = false;
    },
  },
  beforeDestroy() {
    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>

<style scoped>
.profile {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.profile-menu {
  position: absolute;
  right: 0;
  top: 100%;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 10px 0;
  z-index: 1000;
  margin-top: 5px;
  min-width: 150px;
}

.profile-menu a {
  display: block;
  padding: 8px 16px;
  text-decoration: none;
  color: #333;
  font-size: 14px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.profile-menu a:hover {
  background-color: #f5f5f5;
}

.menu-divider {
  height: 1px;
  background-color: #e0e0e0;
  margin: 8px 0;
}

.logout-link {
  margin-top: 8px;
  color: #ff4444;
}

.logout-link:hover {
  background-color: #ffe5e5;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  padding: 10px 20px;
  border-bottom: 2px solid #e0e0e0;
}

.logo {
  text-decoration: none;
  font-size: 16px;
  color: #000;
  font-weight: bold;
  margin-right: 20px;
}

.header-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.nav {
  margin-top: 10px;
  margin-left: 100px;
  display: flex;
  gap: 10px;
}

.nav a {
  text-decoration: none;
  font-size: 12px;
  color: #000;
  padding: 3px 6px;
  border-bottom: 1px solid transparent;
}

.nav a:hover,
.nav a:focus {
  border-bottom: 1px solid black;
}

.profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}
</style>
