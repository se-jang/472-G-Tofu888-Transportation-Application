<template>
  <div class="edit-profile-page">
    <Header />

    <div class="container">
      <div class="card">
        <h2>Edit Profile</h2>

        <form @submit.prevent="updateProfile">
          <div class="profile-picture-section">
            <div class="picture-preview">
              <img :src="profilePictureUrl" alt="Profile Picture" />
            </div>
            <input
                type="file"
                id="profilePicture"
                accept=".jpg, .jpeg, .png"
                @change="onFileChange"
                hidden
            />
            <label for="profilePicture" class="btn-upload">Change Photo</label>
          </div>

          <div class="form-group">
            <label for="name">Name</label>
            <input
              type="text"
              id="name"
              v-model="profile.name"
              placeholder="Enter your name"
              required
            />
          </div>

          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              v-model="profile.email"
              placeholder="Enter your email"
              required
            />
          </div>

          <div class="form-group">
            <label for="phoneNumber">Phone Number</label>
            <input
              type="text"
              id="phoneNumber"
              v-model="profile.phoneNumber"
              placeholder="Enter your phone number"
              required
            />
          </div>

          <div class="button-group">
            <button type="submit" class="btn btn-primary">Save</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Header from "@/components/Header.vue";
import { mapGetters } from "vuex";

export default {
  name: "EditProfileView",
  components: {
    Header,
  },
  data() {
    return {
      profile: {
        name: "",
        email: "",
        phoneNumber: "",
        profilePicture: "",
      },
      profilePictureUrl: "/default-profile.png",
      selectedProfilePicture: null,
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
        this.fetchOwnProfile();
      }
    }
  },
  methods: {
    validateForm() {
      const namePattern = /^[a-zA-Z\s]+$/;
      if (!namePattern.test(this.profile.name)) {
        alert("Name can only contain letters and spaces");
        return false;
      }

      const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      if (!emailPattern.test(this.profile.email)) {
        alert("Please enter a valid email");
        return false;
      }

      const phonePattern = /^[0-9]{10}$/;
      if (
        this.profile.phoneNumber &&
        !phonePattern.test(this.profile.phoneNumber)
      ) {
        alert("Phone number must be exactly 10 digits");
        return false;
      }

      return true;
    },
    async fetchOwnProfile() {
      try {
        const response = await axios.get(
          `http://localhost:8080/users/${this.id}`
        );
        this.profile = { ...response.data };
        console.log(this.profile.profilePicture);
        this.profilePictureUrl = this.profile.profilePicture
          ? `http://localhost:8080${
              this.profile.profilePicture
            }?t=${new Date().getTime()}`
          : "/default-profile.png";
      } catch (error) {
        console.error("Error fetching own profile:", error);
      }
    },
    async fetchWorkerProfile() {
      try {
        const response = await axios.get(
          `http://localhost:8080/transportation-workers/${this.id}`
        );
        this.profile = { ...response.data };
        console.log(this.profile.profilePicture);
        this.profilePictureUrl = this.profile.profilePicture
          ? `http://localhost:8080${
              this.profile.profilePicture
            }?t=${new Date().getTime()}`
          : "/default-profile.png";
      } catch (error) {
        console.error("Error fetching worker profile:", error);
      }
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const allowedTypes = ["image/jpeg", "image/png"];
        const maxSize = 5 * 1024 * 1024;

        if (!allowedTypes.includes(file.type)) {
          alert("Only JPG and PNG files are allowed");
          return;
        }

        if (file.size > maxSize) {
          alert("File size must not exceed 5MB");
          return;
        }

        this.selectedProfilePicture = file;
        this.profilePictureUrl = URL.createObjectURL(file);
      }
    },
    async updateProfile() {
      if (!this.validateForm()) return;
      try {
        const formData = new FormData();
        formData.append("editProfileRequest", JSON.stringify({
          name: this.profile.name,
          email: this.profile.email,
          phoneNumber: this.profile.phoneNumber
        }));

        if (this.selectedProfilePicture) {
          formData.append("profilePicture", this.selectedProfilePicture);
        }

        const url =
          this.userRole === "WORKER"
            ? `http://localhost:8080/transportation-workers/update-profile/${this.id}`
            : `http://localhost:8080/users/update-profile/${this.id}`;

        await axios.put(url, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        alert("Profile updated successfully!");
        this.$router.push({ name: "main" });
      } catch (error) {
        console.error("Error updating profile:", error);
        alert("Error updating profile!");
      }
    },
  },
};
</script>

<style scoped>
.edit-profile-page {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 40px;
}

.container {
  max-width: 600px;
  margin: 40px auto;
  padding: 0 15px;
}

.card {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
}

h2 {
  font-weight: 600;
  margin-bottom: 20px;
  text-align: center;
}

.profile-picture-section {
  text-align: center;
  margin-bottom: 20px;
}

.picture-preview {
  width: 120px;
  height: 120px;
  margin: 0 auto;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.picture-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.btn-upload {
  display: inline-block;
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #007bff;
  color: #fff;
  font-size: 14px;
  border-radius: 4px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.button-group {
  text-align: center;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary {
  background-color: #007bff;
  color: #fff;
}
</style>
