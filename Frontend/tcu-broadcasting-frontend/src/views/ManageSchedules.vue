<template>
  <div class="container">
    <h1>Manage Game Schedules</h1>

    <div v-if="loading" class="loading">Loading game schedule...</div>

    <div v-else-if="games.length === 0">
      <p class="info">No upcoming games available at this time.</p>
    </div>

    <div v-else>
      <div class="create-btn-wrapper">
        <button class="btn btn-primary" @click="showCreateModal = true">Create Game Schedule</button>
      </div>

      <div v-for="(games, scheduleId) in groupedGames" :key="scheduleId" class="schedule-section">
        <h3>Schedule # {{ scheduleId }}</h3>
        <div class="schedule-actions">
          <button class="btn btn-primary" @click="openAddGameModal(scheduleId)">+ Add New Game</button>
        </div>
        <table class="schedule-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Opponent</th>
              <th>Venue</th>
              <!--<th>Finalized?</th>-->
            </tr>
          </thead>
          <tbody>
            <tr v-for="game in games" :key="game.gameId">
              <td>{{ formatDate(game.gameDate) }}</td>
              <td>{{ game.opponent }}</td>
              <td>{{ game.venue }}</td>
              <!--<td>{{ game.finalized ? 'Yes' : 'No' }}</td>-->
            </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>

  <div v-if="showCreateModal" class="modal">
    <div class="modal-content">
      <h2>Create Game Schedule</h2>

      <div class="form-group">
        <label>Sport Type</label>
        <input v-model="newSchedule.sport" type="text" placeholder="e.g., Baseball" />
      </div>

      <div class="form-group">
        <label>Season</label>
        <input v-model="newSchedule.season" type="text" placeholder="e.g., 2024-2025" />
      </div>

      <div v-for="(game, index) in newSchedule.games" :key="index" class="game-entry">
        <h3>Game {{ index + 1 }}</h3>
        <label>Game Date</label>
        <input v-model="game.gameDate" type="date"/>
        <label>Start Time</label>
        <input v-model="game.startTime" type="time" />
        <label>Report Time</label>
        <input v-model="game.reportTime" type="time"/>
        <input v-model="game.venue" type="text" placeholder="Venue" />
        <input v-model="game.opponent" type="text" placeholder="Opponent" />
      </div>

      <div class="modal-actions">
        <button class="btn" @click="addGame">+ Add Another Game</button>
        <button class="btn btn-success" @click="submitSchedule">Create Schedule</button>
        <button class="btn btn-secondary" @click="resetCreateForm">Cancel</button>
      </div>
    </div>
  </div>

  <div v-if="showAddGameModal" class="modal">
    <div class="modal-content">
      <h2>Add Game to Schedule #{{ selectedScheduleId }}</h2>
      <label>Game Date</label>
      <input class="add-game" v-model="newGame.gameDate" type="date" placeholder="Game Date" />
      <label>Start Time</label>
      <input class="add-game" v-model="newGame.startTime" type="time" placeholder="Start Time" />
      <label>Report Time</label>
      <input class="add-game" v-model="newGame.reportTime" type="time" placeholder="Report Time" />
      <input class="add-game" v-model="newGame.venue" type="text" placeholder="Venue" />
      <input class="add-game" v-model="newGame.opponent" type="text" placeholder="Opponent" />

      <div class="modal-actions">
        <button class="btn btn-success" @click="submitNewGame">Add Game</button>
        <button class="btn btn-secondary" @click="closeAddGameModal">Cancel</button>
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
      showCreateModal: false,
      newSchedule: {
        sport: '',
        season: '',
        games: [
          {
            gameDate: '',
            startTime: '',
            reportTime: '',
            venue: '',
            opponent: ''
          }
        ]
      },
      showAddGameModal: false,
      selectedScheduleId: null,
      newGame: {
        gameDate: '',
        startTime: '',
        reportTime: '',
        venue: '',
        opponent: ''
      }
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
    },
    groupedGames() {
      const grouped = {};

      this.filteredGames.forEach(game => {
        if (!grouped[game.scheduleId]) {
          grouped[game.scheduleId] = [];
        }
        grouped[game.scheduleId].push(game);
      });

      return grouped;
    }
  },
  created() {
    this.fetchGameSchedule();
  },
  methods: {
    async fetchGameSchedule() {
      try {
        //const response = await axios.get('http://localhost:8080/game/gameSchedule/games');
        const response = await axios.get('https://tcu-sports-broadcasting-stefan.azurewebsites.net/game/gameSchedule/games');
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
      return date;
    },
    viewDetails(game) {
      this.selectedGame = game;
    },
    addGame() {
      this.newSchedule.games.push({
        gameDate: '',
        startTime: '',
        reportTime: '',
        venue: '',
        opponent: ''
      });
    },
    resetCreateForm() {
      this.newSchedule = {
        sport: '',
        games: [
          {
            gameDate: '',
            startTime: '',
            reportTime: '',
            venue: '',
            opponent: ''
          }
        ]
      };
      this.showCreateModal = false;
    },
    async submitSchedule() {
      const today = new Date().setHours(0, 0, 0, 0);

      if (!this.newSchedule.sport.trim() || !this.newSchedule.season.trim()) {
        alert('Please enter both sport and season.');
        return;
      }

      for (let i = 0; i < this.newSchedule.games.length; i++) {
        const game = this.newSchedule.games[i];

        if (
          !game.gameDate ||
          !game.startTime ||
          !game.reportTime ||
          !game.venue.trim() ||
          !game.opponent.trim() 
        ) {
          alert(`Please complete all fields for Game ${i + 1}.`);
          return;
        }

        const gameDateValue = new Date(game.gameDate).setHours(0, 0, 0, 0);
        if (gameDateValue < today) {
          alert(`Game ${i + 1} must be scheduled for today or a future date.`);
          return;
        }
      }

      try {
        const token = sessionStorage.getItem('token');
        const authHeader = { headers: { Authorization: `Bearer ${token}` } };
        // Step 1: Create the game schedule
        //const scheduleRes = await axios.post('http://localhost:8080/schedule/gameSchedule', {
        const scheduleRes = await axios.post('https://tcu-sports-broadcasting-stefan.azurewebsites.net/schedule/gameSchedule', {
          sport: this.newSchedule.sport.trim(),
          season: this.newSchedule.season.trim()
        },
        authHeader
      );

        if (!scheduleRes.data.flag || scheduleRes.data.code !== 200) {
          alert('Failed to create game schedule.');
          return;
        }

        const scheduleId = scheduleRes.data.data.id;

        // Step 2: Create the games
        for (const game of this.newSchedule.games) {
          const gamePayload = {
            gameDate: game.gameDate,
            venue: game.venue,
            opponent: game.opponent,
            isFinalized: true,
            reportTime: this.formatTime(game.reportTime),
            gameStartTime: this.formatTime(game.startTime)
          };

          //await axios.post(`http://localhost:8080/game/schedule/gameSchedule/${scheduleId}/games`, gamePayload);
          await axios.post(`https://tcu-sports-broadcasting-stefan.azurewebsites.net/game/schedule/gameSchedule/${scheduleId}/games`, 
            gamePayload,
            authHeader
          );
        }

        alert('Game schedule and games successfully created!');
        this.resetCreateForm();
        this.fetchGameSchedule(); // Refresh the list

      } catch (error) {
        console.error('Error submitting schedule:', error);
        alert('An error occurred while submitting the schedule.');
      }
    },
    openAddGameModal(scheduleId) {
      this.selectedScheduleId = scheduleId;
      this.newGame = {
        gameDate: '',
        startTime: '',
        reportTime: '',
        venue: '',
        opponent: ''
      };
      this.showAddGameModal = true;
    },

    closeAddGameModal() {
      this.showAddGameModal = false;
      this.newGame = {
        gameDate: '',
        startTime: '',
        reportTime: '',
        venue: '',
        opponent: ''
      };
    },

    async submitNewGame() {
      if (
        !this.newGame.gameDate ||
        !this.newGame.startTime ||
        !this.newGame.reportTime ||
        !this.newGame.venue.trim() ||
        !this.newGame.opponent.trim()
      ) {
        alert('Please fill in all fields.');
        return;
      }

      const today = new Date().setHours(0, 0, 0, 0);
      const newGameDate = new Date(this.newGame.gameDate).setHours(0, 0, 0, 0);
      if (newGameDate < today) {
        alert('Game date must be today or later.');
        return;
      }

      // Check for time conflict
      const conflictingGame = this.games.find(
        game =>
          game.scheduleId === this.selectedScheduleId &&
          game.gameDate === this.newGame.gameDate &&
          game.gameStartTime === this.newGame.startTime
      );

      if (conflictingGame) {
        alert(`A game already starts at ${this.newGame.startTime} on ${this.newGame.gameDate}. Please pick a different time.`);
        return;
      }

      try {
        const token = sessionStorage.getItem('token');
        //await axios.post(`http://localhost:8080/game/schedule/gameSchedule/${this.selectedScheduleId}/games`, {
        await axios.post(`https://tcu-sports-broadcasting-stefan.azurewebsites.net/game/schedule/gameSchedule/${this.selectedScheduleId}/games`, {
          gameDate: this.newGame.gameDate,
          gameStartTime: this.formatTime(this.newGame.startTime),
          reportTime: this.formatTime(this.newGame.reportTime),
          venue: this.newGame.venue,
          opponent: this.newGame.opponent,
          isFinalized: true
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );

        alert('Game added successfully!');
        this.closeAddGameModal();
        this.fetchGameSchedule(); // Refresh game list

      } catch (error) {
        console.error('Error adding new game:', error);
        alert('Failed to add game.');
      }
    },

    formatTime(time24) {
      if (!time24) return '';

      const [hourStr, minute] = time24.split(':');
      let hour = parseInt(hourStr, 10);
      const ampm = hour >= 12 ? 'PM' : 'AM';

      hour = hour % 12;
      hour = hour ? hour : 12; // 0 should become 12

      return `${hour}:${minute} ${ampm}`;
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
  padding: 8px 12px;
  border-radius: 6px;
  border: none;
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
  max-width: 600px; /* Wider modal */
  width: 100%;
  max-height: 90vh; /* Constrain height */
  overflow-y: auto; /* Enable vertical scroll */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}
.create-btn-wrapper {
  margin-bottom: 1rem;
}

.form-group input,
.game-entry input {
  display: block;
  margin-bottom: 0.5rem;
  padding: 0.4rem;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-actions .btn {
  margin-right: 0.5rem;
  margin-top: 1rem;
}

.schedule-actions {
  margin: 0.5rem 0 1rem;
}
.add-game{
  display: block;
  margin-bottom: 0.5rem;
  padding: 0.4rem;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
}
label{
  font-family: Arial, sans-serif;
  font-size: 14px;
}
h2, h3 {
  color: #4D1979;
  font-family: Arial, sans-serif;
}


</style>
