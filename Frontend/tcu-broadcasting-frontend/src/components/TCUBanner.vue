<template>
    <div class="banner">
        <div class="banner-content">
          <img src="/src/assets/tcu-logo-banner.svg" alt="TCU Logo" class="banner-logo" />
          <h1 class="banner-title">TCU Frog Crew</h1>
        </div>
        <button v-if="isAuthenticated" class="logout-btn" @click="handleLogout">Logout</button>
    </div>
  </template>
  
  <script>
  import { isAuthenticated, logout } from '@/apis/auth';

  export default {
    name: "Banner",
    setup() {
      const handleLogout = () => {
        logout();
        window.location.href = "/login"; // Optional redirect after logout
      };

      return {
        isAuthenticated,
        handleLogout
      };
    }
  };
  </script>
  
  <style scoped>

  .banner {
    width: 100%;
    background-color: #4D4D4A;
    padding-bottom: 25px;
    display: flex;
    align-items: center;
    justify-content: space-between; /* Space between title and logout */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  }
  
  .banner-content {
    align-items: flex-start;
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  
  .banner-logo {
    margin-left: 5rem;
    margin-right: 1rem;
    /*width: 130px;*/
    /*height: 110px;*/
    height: 110px
  }
  
  .banner-title {
    color: white;
    font-family: 'Eurostile', sans-serif;
    font-size: 1.8rem;
    margin: 0;
  }
  .logout-btn {
  background-color: #9D2235;
  margin-top: 20px;
  color: white;
  border: none;
  padding: 10px 20px;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 3rem;
}

.logout-btn:hover {
  background-color: #801c2f;
}
  

  
  </style>
  