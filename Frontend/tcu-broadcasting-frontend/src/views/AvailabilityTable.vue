<template>
    <div class="container">
      <h1>Submit Availability</h1>
  
      <div v-if="loading" class="loading">Loading games...</div>
      <div v-else-if="gamesToSubmit.length === 0">
        <p class="info">You've submitted availability for all games in this season.</p>
      </div>
      <div v-else>
        <form @submit.prevent="submitAvailability">
          <table class="availability-table">
            <thead>
              <tr>
                <th>Available?</th>
                <th>Game</th>
                <th>Optional Comment</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="game in gamesToSubmit" :key="game.gameId">
                <td>
                  <input type="checkbox" v-model="selectedGames" :value="game.gameId" />
                </td>
                <td>
                  {{ formatDate(game.gameDate) }} — {{ game.opponent }} at {{ game.venue }}
                </td>
                <td>
                  <input
                    v-if="selectedGames.includes(game.gameId)"
                    type="text"
                    v-model="comments[game.gameId]"
                    placeholder="Optional comment"
                    class="input"
                  />
                </td>
              </tr>
            </tbody>
          </table>
          <button type="submit" class="btn btn-primary">Submit Availability</button>
          <div v-if="showWarning" class="alert alert-warning">
            Please select at least one game before submitting your availability.
          </div>
        </form>
      </div>
  
      <div v-if="submitted" class="alert alert-success">Availability submitted successfully!</div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        userId: 1,
        allGames: [],
        existingAvailability: [],
        gamesToSubmit: [],
        selectedGames: [],
        comments: {},
        loading: true,
        showWarning: false,
        submitted: false
      };
    },
    created() {
      this.loadData();
    },
    methods: {
      async loadData() {
        try {
          const [gamesRes, availRes] = await Promise.all([
            this.getAllGames(),
            this.getUserAvailability(this.userId)
          ]);
  
          this.allGames = gamesRes.data;
          this.existingAvailability = availRes.data;
  
          const submittedGameIds = new Set(this.existingAvailability.map(a => a.gameId));
          this.gamesToSubmit = this.allGames.filter(game => !submittedGameIds.has(game.gameId));
        } catch (err) {
          console.error("Error loading data", err);
        } finally {
          this.loading = false;
        }
      },
      async submitAvailability() {
        if (this.selectedGames.length === 0) {
          this.showWarning = true;
          return;
        }

        this.showWarning = false;

        try {
          const payloads = this.selectedGames.map(gameId => ({
            userId: this.userId,
            gameId,
            availability: 1,
            comment: this.comments[gameId] || null
          }));

          for (const payload of payloads) {
            await axios.post('http://localhost:8080/availability/availability', payload);
          }

          this.submitted = true;
          this.loadData(); // Refresh list after submission
        } catch (error) {
          console.error("Error submitting availability:", error);
        }
      },
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      },
      async getUserAvailability(userId) {
        const response = await axios.get(`http://localhost:8080/availability/availability/${userId}`);
        if (response.data.flag && response.data.code === 200) {
          return response.data;
        } else {
          throw new Error("Failed to fetch availability");
        }
      },
      async getAllGames() {
        const response = await axios.get('http://localhost:8080/game/gameSchedule/games');
        if (response.data.flag && response.data.code === 200) {
          return response.data;
        } else {
          throw new Error("Failed to fetch games");
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .container {
    font-family: Arial, sans-serif;
    max-width: 700px;
    margin-left: 30px;
    padding: 20px;
  }
  h1{
    color: #4D1979;
  }
  .availability-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  .availability-table th,
  .availability-table td {
    padding: 10px;
    border: 1px solid #ccc;
    vertical-align: top;
  }
  .availability-table th {
    background-color: #f3e8ff;
  }
  .availability-table td:first-child {
    text-align: center;
  }


  .input {
    width: 95%;
    padding: 6px;
    margin-top: 4px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  .btn {
    padding: 10px 20px;
    background-color: #4f46e5;
    color: white;
    border: none;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
    margin-top: 16px;
  }
  .loading {
    font-weight: bold;
    color: #6b21a8;
  }
  .alert-success {
    background-color: #d1fae5;
    color: #065f46;
    padding: 12px;
    border-radius: 6px;
    margin-top: 20px;
  }
  .info {
    color: #333;
    background: #fef9c3;
    padding: 10px;
    border-radius: 6px;
    border: 1px solid #facc15;
  }
  .alert-warning {
    background-color: #fef3c7;
    color: #d61919;
    font-weight: bold;
    padding: 12px;
    border-radius: 6px;
    margin-top: 20px;
    border: 1px solid #d61919;
  }
  </style>
  