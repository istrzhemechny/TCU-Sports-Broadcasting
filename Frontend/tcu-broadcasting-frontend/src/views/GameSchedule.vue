<template>
    <div class="container">
      <h1>General Game Schedule</h1>
  
      <div v-if="loading" class="loading">Loading game schedule...</div>
  
      <div v-else-if="games.length === 0">
        <p class="info">No upcoming games available at this time.</p>
      </div>
  
      <div v-else>
        <div class="filters">
          <label>Sort By:
            <select v-model="sortOption">
              <option value="date">Date (Newest to Oldest)</option>
              <option value="opponent">Opponent</option>
              <option value="venue">Venue</option>
            </select>
          </label>
          <input v-model="searchQuery" placeholder="Search opponent..." class="input" />
        </div>
  
        <table class="schedule-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Opponent</th>
              <th>Venue</th>
              <th>Action</th>
              <!--<th>Finalized?</th>-->
            </tr>
          </thead>
          <tbody>
            <tr v-for="game in filteredGames" :key="game.gameId">
              <td>{{ formatDate(game.gameDate) }}</td>
              <td>{{ game.opponent }}</td>
              <td>{{ game.venue }}</td>
              <td>
                <button class="btn btn-info" @click="viewCrewList(game.gameId)">View Crew List</button>
              </td>
              <!--<td>{{ game.finalized ? 'Yes' : 'No' }}</td>-->
              <!--<td>{{ game.requiredCrew.join(', ') }}</td>-->
            </tr>
          </tbody>
        </table>
      </div>
  
       <!-- Modal to show Crew List -->
    <div v-if="selectedCrewList" class="modal">
      <div class="modal-content">
          <h2>Crew List for Game on {{ formatDate(selectedCrewList.gameDate) }}</h2>
          <p><strong>Opponent:</strong> {{ selectedCrewList.opponent }}</p>
          <p><strong>Venue:</strong> {{ selectedCrewList.venue }}</p>

          <h3>Assigned Crew Members</h3>
          <ul>
            <li v-for="member in selectedCrewList.crewedMembers" :key="member.crewedUserId">
              {{ member.fullName }} - {{ member.position }} (Report: {{ member.reportTime }}, {{ member.reportLocation }})
            </li>
          </ul>

          <button @click="selectedCrewList = null" class="btn btn-secondary">Close</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';

  export default {
    data() {
      return {
        games: [],
        loading: true,
        sortOption: 'date',
        searchQuery: '',
        selectedGame: null,
        selectedCrewList: null 
      };
    },
    computed: {
      filteredGames() {
        let filtered = this.games.filter(game =>
          game.opponent.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
  
        switch (this.sortOption) {
          case 'opponent':
            return filtered.sort((a, b) => a.opponent.localeCompare(b.opponent));
          case 'venue':
            return filtered.sort((a, b) => a.venue.localeCompare(b.venue));
          case 'date':
          default:
            return filtered.sort((a, b) => new Date(b.gameDate) - new Date(a.gameDate));
        }
      }
    },
    created() {
      this.fetchGameSchedule();
    },
    methods: {
      async fetchGameSchedule() {
        try {
          const response = await axios.get('http://localhost:8080/game/gameSchedule/games');
          if (response.data.flag && response.data.code === 200) {
            this.games = response.data.data;
          } else {
            console.error('Failed to fetch games:', response.data.message);
            this.games = [];
          }
        } catch (error) {
          console.error('API error:', error);
          this.games = [];
        } finally {
          this.loading = false;
        }
      },
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      },
      async viewCrewList(gameId) {
        try {
          const response = await axios.get(`http://localhost:8080/crewSchedule/crewList/crewList/${gameId}`);
          if (response.data.flag && response.data.code === 200) {
            this.selectedCrewList = response.data.data;
          } else {
            alert('Failed to load crew list.');
          }
        } catch (error) {
          console.error('Error fetching crew list:', error);
          alert('Error loading crew list.');
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .container {
    font-family: Arial, sans-serif;
    padding: 20px;
    max-width: 900px;
    margin-left:30px;
  }
  h1{
    color: #4D1979;
  }
  .filters {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;
    gap: 10px;
  }
  .input {
    padding: 6px;
    border: 1px solid #ccc;
    border-radius: 4px;
    flex: 1;
  }
  .schedule-table {
    width: 100%;
    border-collapse: collapse;
  }
  .schedule-table th,
  .schedule-table td {
    padding: 12px;
    border: 1px solid #ddd;
    text-align: left;
  }
  .schedule-table th {
    background-color: #f3e8ff;
  }
  .btn {
    padding: 6px 12px;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
    
  }
  .btn-info {
    background-color: #3b82f6;
    color: white;
    border: none;
  }
  .btn-secondary {
    background-color: #e5e7eb;
    color: black;
    border: none;
    margin-top: 10px;
  }
  .loading {
    color: #6b21a8;
    font-weight: bold;
  }
  .info {
    background: #fef9c3;
    padding: 10px;
    border: 1px solid #facc15;
    border-radius: 6px;
    color: #92400e;
  }
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 8px;
    max-width: 500px;
    width: 100%;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  }
  </style>
  