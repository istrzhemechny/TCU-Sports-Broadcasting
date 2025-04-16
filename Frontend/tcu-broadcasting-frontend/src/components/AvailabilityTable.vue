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
        </form>
      </div>
  
      <div v-if="submitted" class="alert alert-success">Availability submitted successfully!</div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        userId: 1,
        seasonId: 1,
        allGames: [],
        existingAvailability: [],
        gamesToSubmit: [],
        selectedGames: [],
        comments: {},
        loading: true,
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
            this.mockGetAllGames(),
            this.mockGetUserAvailability(this.userId, this.seasonId)
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
      submitAvailability() {
        const payload = this.selectedGames.map(gameId => ({
          userId: this.userId,
          gameId,
          availability: true,
          comment: this.comments[gameId] || null
        }));
  
        console.log("Submitting availability:", payload);
        this.submitted = true;
      },
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      },
      // Mock API methods
      mockGetUserAvailability(userId, seasonId) {
        return new Promise(resolve => {
          setTimeout(() => {
            resolve({
              flag: true,
              code: 200,
              message: "Find Success",
              data: [
                {
                  userId: 1,
                  scheduleId: 1,
                  gameId: 1,
                  availability: true,
                  comment: "Will be coming from another game"
                },
                {
                  userId: 1,
                  scheduleId: 2,
                  gameId: 2,
                  availability: true,
                  comment: null
                }
              ]
            });
          }, 500);
        });
      },
      mockGetAllGames() {
        return new Promise(resolve => {
          setTimeout(() => {
            resolve({
              flag: true,
              code: 200,
              message: "Find Success",
              data: [
                {
                  gameId: 1,
                  scheduleId: 1,
                  gameDate: "2024-09-07",
                  venue: "Carter",
                  opponent: "LIU",
                  isFinalized: false
                },
                {
                  gameId: 2,
                  scheduleId: 2,
                  gameDate: "2024-09-14",
                  venue: "Carter",
                  opponent: "UCF",
                  isFinalized: false
                },
                {
                  gameId: 3,
                  scheduleId: 3,
                  gameDate: "2024-09-21",
                  venue: "Carter",
                  opponent: "Texas",
                  isFinalized: false
                }
              ]
            });
          }, 500);
        });
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
    background: white;
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
  </style>
  