<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="login-title">Login</h1>
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="password" required />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";

export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    ...mapActions(["updateUserRole", "updateUsername", "updateId"]),
    async login() {
      try {
        const response = await fetch("http://localhost:8080/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        });

        if (response.ok) {
          const data = await response.json();

          if (data.token && data.role && data.username && data.id) {
            localStorage.setItem("jwt", data.token);

            const userRole = data.role;
            const username = data.username;

            console.log("User role:", userRole);
            console.log("Username:", username);
            console.log("worker: ", data.id);

            this.updateUserRole(userRole);
            this.updateUsername(username);
            this.updateId(data.id);

            this.$router.push("/main");
          } else {
            alert(
              "Login failed: Role, username, or token is missing in the response."
            );
          }
        } else {
          const error = await response.text();
          alert("Login failed: " + error);
        }
      } catch (error) {
        console.error("Error during login:", error);
      }
    },
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Hanuman&display=swap");

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #87ceeb;
}

.login-box {
  background-color: #f0f0f0;
  border-radius: 10px;
  padding: 30px;
  width: 300px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  font-family: "Hanuman", sans-serif;
}

.form-group {
  margin-bottom: 15px;
  margin-right: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #0096fa;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
