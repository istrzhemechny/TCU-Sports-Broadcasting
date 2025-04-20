<template>
    <div class="crew-list-wrapper">
        <div class="dropdown-wrapper">
            <h2>Select a Game to View Crew List</h2>

            <div v-if="loadingGames">Loading games...</div>
            <div v-else>
                <select v-model="selectedGameId" @change="fetchCrewList">
                <option disabled value="">Please select a game</option>
                <option v-for="game in games" :key="game.gameId" :value="game.gameId">
                    {{ game.gameDate }} vs {{ game.opponent }} at {{ game.venue }}
                </option>
                </select>
            </div>
        </div>

  
      <div v-if="crewDetails">
        <h2>Crew List Details</h2>
        <div class="crew-list-details-container">
            <p><strong>Sport:</strong> Football</p> <!-- Static -->
            <p><strong>Opponent:</strong> {{ crewDetails.opponent }}</p>
            <p><strong>Game Date:</strong> {{ crewDetails.gameDate }}</p>
            <p><strong>Game Time:</strong> {{ crewDetails.gameStart }}</p>
            <p><strong>Venue:</strong> {{ crewDetails.venue }}</p>
    
            <table class="crew-table">
            <thead>
                <tr>
                <th>Position</th>
                <th>Name</th>
                <th>Report Time</th>
                <th>Report Location</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="member in crewDetails.crewedMembers" :key="member.crewedUserId">
                <td>{{ member.Position }}</td>
                <td>{{ member.fullName }}</td>
                <td>{{ member.ReportTime }}</td>
                <td>{{ member.ReportLocation }}</td>
                </tr>
            </tbody>
            </table>
        </div>
       
      </div>
  
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  
  export default {
    name: 'ViewCrewList',
    data() {
      return {
        games: [],
        selectedGameId: '',
        crewDetails: null,
        loadingGames: true,
        errorMessage: ''
      }
    },

    methods: {
      async fetchGames() {
        try {
          const response = await axios.get('http://localhost:8080/gameSchedule/games')
          if (response.data.flag && response.data.code === 200) {
            this.games = response.data.data
          }
        } catch (error) {
          this.errorMessage = 'Failed to load games.'
        } finally {
          this.loadingGames = false
        }
      },
      async fetchCrewList() {
        this.crewDetails = null
        this.errorMessage = ''
  
        try {
          const response = await axios.get(`http://localhost:8080/crewList/${this.selectedGameId}`)
          if (response.data.flag && response.data.code === 200) {
            this.crewDetails = response.data.data
          } else {
            this.errorMessage = response.data.message || 'Failed to retrieve crew list.'
          }
        } catch (error) {
          this.errorMessage = 'An error occurred while fetching the crew list.'
        }
      }
    },









    mounted() {
      this.fetchGames()
    }
  }
  </script>
  
  <style scoped>
  .crew-list-wrapper {
    width: 130vh;
    font-family: 'Inter', sans-serif;
    padding: 1.5rem;
    text-align: center;
  }

  .dropdown-wrapper{
    text-align: center;
  }

  .crew-list-details-container{
    border: solid rgb(183, 182, 182) 1.5px;
    border-radius: 10px;
    text-align: left;
    padding-left: 1rem;
    padding-right: 1rem;
    padding-bottom: 1rem;
  }
  
  select {
    padding: 0.5rem;
    font-size: 1rem;
    margin: 1rem 0;
  }
  
  .crew-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }
  
  .crew-table th, .crew-table td {
    border: 1px solid #ccc;
    padding: 0.5rem;
    text-align: left;
  }
  
  .crew-table th {
    background-color: #f3f3f3;
  }
  
  .error-message {
    color: red;
    margin-top: 1rem;
  }
  </style>
  