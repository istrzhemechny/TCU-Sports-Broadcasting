<template>
    <div class="container">
      <h1>Crew List</h1>
  
      <div v-if="loading" class="loading">Loading crew members...</div>
      <div v-else>
        <table class="crew-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Full Name</th>
              <th>Email</th>
              <th>Phone Number</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="member in crewMembers" :key="member.userId">
              <td>{{ member.userId }}</td>
              <td>{{ member.fullName }}</td>
              <td>{{ member.email }}</td>
              <td>{{ member.phoneNumber }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';

  export default {
    data() {
      return {
        crewMembers: [],
        loading: true
      };
    },
    created() {
      this.fetchCrewMembers();
    },
    methods: {
      async fetchCrewMembers() {
        try {
          const response = await axios.get('http://localhost:8080/crewMember');
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
  </style>
  