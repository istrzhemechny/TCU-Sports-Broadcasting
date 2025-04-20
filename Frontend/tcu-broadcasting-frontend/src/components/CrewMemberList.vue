<template>
    <div class="container">
      <h1>Crew Member List</h1>
  
      <div v-if="loading" class="loading">Loading crew members...</div>
      <div v-else>
        <table class="crew-table">
          <thead>
            <tr>
              <th>Crew Members</th>
              <th>Details</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="member in crewMembers" :key="member.userId">
              <td>{{ member.fullName}}</td>
              <td><button @click="selectMember(member)">View Details</button></td>
            </tr>
          </tbody>
        </table>

        <div v-if="selectedMember" class="modal-overlay" @click.self="selectedMember = null">
          <div class="modal-content">
            <h2>Crew Member Details</h2>
            <p><strong>Full Name:</strong> {{ selectedMember.firstName }} {{ selectedMember.lastName }}</p>
            <p><strong>Email:</strong> {{ selectedMember.email }}</p>
            <p><strong>Phone Number:</strong> {{ selectedMember.phoneNumber }}</p>
            
            <div v-if="selectedMember.position && selectedMember.position.length">
              <p><strong>Positions:</strong></p>
              <ul>
                <li v-for="(pos, index) in selectedMember.position" :key="index">{{ pos }}</li>
              </ul>
            </div>

            <button @click="selectedMember = null">Close</button>
          </div>
        </div>


      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';

  export default {
    data() {
      return {
        crewMembers: [],
        loading: true,
        selectedMember: null
      };
    },
    created() {
      this.fetchCrewMembers();
    },
    methods: {
      async fetchCrewMembers() {
        try {
          const response = await axios.get('http://localhost:8080/user/crewMember');
          if (response.data.flag && response.data.code === 200) {
            this.crewMembers = response.data.data;
          } else {
            console.error("Failed to fetch crew members:", response.data.message);
          }
        } catch (error) {
          console.error("API error:", error);
        } finally {
          this.loading = false;
        }
      },

      async selectMember(member) {
        try {
          const response = await axios.get(`http://localhost:8080/user/crewMember/${member.userId}`);
          if (response.data.flag && response.data.code === 200) {
            this.selectedMember = response.data.data;
          } else {
            console.error("Failed to fetch member details:", response.data.message);
          }
        } catch (error) {
          console.error("Error fetching member by ID:", error);
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .container {
    font-family: Arial, sans-serif;
    padding: 20px;
    max-width: 800px;
    margin-left: 30px;
    background-color: white;
  }
  h1{
    color: #4D1979;
  }
  .crew-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
    margin-right: 60px;
  }
  .crew-table th, .crew-table td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  .crew-table th {
    background-color: #f3e8ff;
  }
  .loading {
    font-size: 16px;
    font-weight: bold;
    color: #6b21a8;
  }
  .modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 25px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  text-align: left;
}
  </style>
  