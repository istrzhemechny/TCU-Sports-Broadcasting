<template>
    <div class="container">
      <h1>General Game Schedule</h1>
  
      <div v-if="loading" class="loading">Loading game schedule...</div>
  
      <div v-else-if="games.length === 0">
        <p class="info">No upcoming games available at this time.</p>
        <router-link to="/dashboard" class="btn btn-secondary">Return to Dashboard</router-link>
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
              <th>Time</th>
              <th>Opponent</th>
              <th>Venue</th>
              <th>Required Crew</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="game in filteredGames" :key="game.gameId">
              <td>{{ formatDate(game.gameDate) }}</td>
              <td>{{ game.gameTime }}</td>
              <td>{{ game.opponent }}</td>
              <td>{{ game.venue }}</td>
              <td>{{ game.requiredCrew.join(', ') }}</td>
              <td><button @click="viewDetails(game)" class="btn btn-info">View</button></td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <div v-if="selectedGame" class="modal">
        <div class="modal-content">
          <h2>Game Details</h2>
          <p><strong>Date:</strong> {{ formatDate(selectedGame.gameDate) }}</p>
          <p><strong>Time:</strong> {{ selectedGame.gameTime }}</p>
          <p><strong>Opponent:</strong> {{ selectedGame.opponent }}</p>
          <p><strong>Venue:</strong> {{ selectedGame.venue }}</p>
          <p><strong>Required Crew:</strong> {{ selectedGame.requiredCrew.join(', ') }}</p>
          <p><strong>Reporting Time:</strong> {{ selectedGame.reportingTime }}</p>
          <p><strong>Contact:</strong> {{ selectedGame.contact }}</p>
          <button @click="selectedGame = null" class="btn btn-secondary">Close</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        games: [],
        loading: true,
        sortOption: 'date',
        searchQuery: '',
        selectedGame: null
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
      fetchGameSchedule() {
        // Mocked game data
        setTimeout(() => {
          this.games = [
            {
              gameId: 1,
              gameDate: '2024-09-07',
              gameTime: '6:00 PM',
              venue: 'Carter',
              opponent: 'LIU',
              requiredCrew: ['Announcer', 'Technician'],
              reportingTime: '5:00 PM',
              contact: 'game.manager@example.com'
            },
            {
              gameId: 2,
              gameDate: '2024-09-14',
              gameTime: '7:30 PM',
              venue: 'Carter',
              opponent: 'UCF',
              requiredCrew: ['Technician', 'Camera Crew'],
              reportingTime: '6:15 PM',
              contact: 'staff@example.com'
            }
          ];
          this.loading = false;
        }, 800);
      },
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      },
      viewDetails(game) {
        this.selectedGame = game;
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
    background-color: white;
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
  