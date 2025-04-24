<template>
    <div class="assign-crew-wrapper">
      <h1 class="page-title">Assign Crew to Game</h1>
      <p class="instructions">Select a game and schedule available crew members.</p>
  
      <div class="form-group">
        <label for="gameSelect">Select Game:</label>
        <select id="gameSelect" v-model="selectedGameId">
          <option disabled value="">-- Choose a game --</option>
          <option v-for="game in games" :key="game.gameId" :value="game.gameId">
            {{ formatGameDisplay(game) }}
          </option>
        </select>
      </div>
  
      <div v-if="selectedGameId" class="crew-availability">
        <h2>Crew Available to Schedule</h2>
  
        <div v-if="filteredCrew.length === 0" class="empty-message">
          No available crew members (all are already scheduled or unavailable).
        </div>
  
        <div v-else>
          <div
            v-for="member in filteredCrew"
            :key="member.id"
            class="crew-card"
          >
            <div class="crew-header">
              <input
                type="checkbox"
                v-model="assignmentSelections[member.id].selected"
                id="select-crew"
              />
              <label for="select-crew">
                <strong>{{ member.firstName }} {{ member.lastName }}</strong> —
                <span class="email">{{ member.email }}</span>
              </label>
            </div>
  
            <div class="crew-form-row">
              <label>Position:</label>
              <select v-model="assignmentSelections[member.id].position">
                <option disabled value="">-- Select Position --</option>
                <option v-for="pos in member.position" :key="pos" :value="pos">{{ pos }}</option>
              </select>
            </div>
  
            <div class="crew-form-row">
              <label>Report Time:</label>
              <input type="time" v-model="assignmentSelections[member.id].reportTime" />
            </div>
  
            <div class="crew-form-row">
              <label>Report Location:</label>
              <input type="text" v-model="assignmentSelections[member.id].reportLocation" placeholder="e.g., Control Room" />
            </div>
          </div>
  
          <button class="submit-btn" @click="submitAssignments">Submit Assignments</button>
        </div>
      </div>
    </div>
  </template>
  

<script>
import axios from 'axios';

export default {
  name: 'AssignCrewToGame',
  data() {
    return {
      games: [],
      selectedGameId: '',
      crewMembers: [],
      availabilityData: {},
      detailedCrew: {},
      assignmentSelections: {},
      alreadyScheduledUserIds: new Set()
    };
  },
  computed: {
    filteredCrew() {
        const matchingUserIds = Object.entries(this.availabilityData)
        .filter(([_, entries]) =>
            entries.some(entry => entry.gameId === this.selectedGameId)
        )
        .map(([userId]) => parseInt(userId))
        .filter(userId => !this.alreadyScheduledUserIds.has(userId)); // 🚫 filter out scheduled

        matchingUserIds.forEach(id => {
        if (!this.assignmentSelections[id]) {
            this.assignmentSelections[id] = {
            selected: false,
            position: '',
            reportTime: '',
            reportLocation: ''
            };
        }
        });

        return matchingUserIds
        .map(id => this.detailedCrew[id])
        .filter(member => member);
    }
  },
  watch: {
    selectedGameId(newGameId) {
      if (newGameId) {
        this.fetchScheduledCrew(newGameId);
      }
    }
  },
  methods: {
    async fetchGames() {
      try {
        const res = await axios.get('http://localhost:8080/game/gameSchedule/games');
        this.games = res.data.data;
      } catch (err) {
        console.error('Error fetching games', err);
      }
    },
    formatGameDisplay(game) {
      return `${game.gameDate} vs ${game.opponent} @ ${game.venue}`;
    },
    async fetchCrewAndAvailability() {
      try {
        const crewRes = await axios.get('http://localhost:8080/user/crewMember');
        this.crewMembers = crewRes.data.data;

        const promises = this.crewMembers.map(async (member) => {
          try {
            const availRes = await axios.get(`http://localhost:8080/availability/availability/${member.userId}`);
            this.availabilityData[member.userId] = availRes.data.data;

            const detailRes = await axios.get(`http://localhost:8080/user/crewMember/${member.userId}`);
            this.detailedCrew[member.userId] = detailRes.data.data;
          } catch (err) {
            console.error(`Error fetching data for user ${member.userId}`, err);
          }
        });

        await Promise.all(promises);
      } catch (error) {
        console.error('Error fetching crew data', error);
      }
    },
    async fetchScheduledCrew(gameId) {
      try {
        const res = await axios.get(`http://localhost:8080/crewSchedule/crewList/crewList/${gameId}`);
        const scheduledMembers = res.data.data.crewedMembers;
        this.alreadyScheduledUserIds = new Set(scheduledMembers.map(member => member.userId));
      } catch (error) {
        console.error('Error fetching scheduled crew:', error);
      }
    },
    async submitAssignments() {
        if (!this.selectedGameId) {
            alert("Please select a game before submitting.");
            return;
        }

        const errors = [];

        const payload = Object.entries(this.assignmentSelections)
            .filter(([userId, val]) => val.selected && !this.alreadyScheduledUserIds.has(parseInt(userId)))
            .map(([userId, val]) => {
            const id = parseInt(userId);

            // Validation
            if (!val.position || !val.reportTime || !val.reportLocation) {
                errors.push(`${this.detailedCrew[id]?.firstName || 'User'}: Missing position, time, or location`);
            }

            return {
                crewedUserId: 0,
                userId: id,
                gameId: this.selectedGameId,
                position: val.position,
                reportTime: val.reportTime,
                reportLocation: val.reportLocation
            };
            });

        if (errors.length > 0) {
            alert("Fix the following before submitting:\n\n" + errors.join('\n'));
            return;
        }

        if (payload.length === 0) {
            alert("No new crew members selected.");
            return;
        }

        try {
            await axios.post(`http://localhost:8080/crewSchedule/crewSchedule/${this.selectedGameId}`, payload);
            alert("Crew assigned successfully!");
            await this.fetchScheduledCrew(this.selectedGameId); // refresh after submission
        } catch (error) {
            console.error("Failed to submit crew assignments:", error);
            alert("Submission failed. Check console for details.");
        }
        }

  },
  mounted() {
    this.fetchGames();
    this.fetchCrewAndAvailability();
  }
};
</script>

<style scoped>

.assign-crew-wrapper {
  padding: 30px;
  width: 500px;
  margin: auto;
  font-family: 'Inter', sans-serif;
}
h1, h2 {
    color: #4D1979;
}

.page-title {
  text-align: center;
  font-size: 28px;
  margin-bottom: 10px;
}

.instructions {
  text-align: center;
  margin-bottom: 30px;
  color: #555;
}

.form-group {
  margin-bottom: 25px;
}

select,
input[type="text"],
input[type="time"] {
  width: 100%;
  padding: 8px;
  font-size: 15px;
  border: 1px solid #ccc;
  border-radius: 6px;
  margin-top: 5px;
}

.crew-availability h2 {
  margin-bottom: 20px;
}

.crew-card {
  background-color: #f9f9f9;
  padding: 15px 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  margin-bottom: 15px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.crew-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.crew-header input[type="checkbox"] {
  margin-right: 10px;
  transform: scale(1.2);
}

.email {
  color: #666;
  font-style: italic;
}

.crew-form-row {
  margin-bottom: 10px;
}

.submit-btn {
  display: block;
  width: 100%;
  padding: 12px;
  background-color: #4b6cb7;
  color: white;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 20px;
}

.submit-btn:hover {
  background-color: #3952a3;
}

.empty-message {
  text-align: center;
  color: #888;
  font-style: italic;
  margin-top: 20px;
}

</style>