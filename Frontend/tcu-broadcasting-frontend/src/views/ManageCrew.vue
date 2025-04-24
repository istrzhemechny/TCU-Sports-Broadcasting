<template>
  <div class="manage-crew-wrapper">
    <h1>Manage Crew Members</h1>

    <div v-if="loading">Loading crew members...</div>

    <div v-else-if="emptyList">No Registered Members.</div>

    <div v-else >
      <div class="search-bar">
        <label for="search">Search by Name:</label>
        <input
          id="search"
          type="text"
          v-model="searchQuery"
          placeholder="Type a name..."
        />
        <button id="export-btn" @click="exportCSV">Export List</button>
      </div>

      <table class="crew-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in filteredCrewMembers" :key="member.userId">
            <td>{{ member.fullName }}</td>
            <td>{{ member.email }}</td>
            <td>
              <button class="action-button" @click="viewDetails(member.userId)">View Details</button>
              <button class="action-button" @click="handleDeleteClick(member)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>


    
    <!-- Confirmation Modal -->
    <div v-if="showConfirm" class="modal">
      <div class="modal-content">
        <h3>Confirm Deletion</h3>
        <p>
          Are you sure you want to delete <strong>{{ selectedMember.fullName }}</strong>?
          <br />
          <br />
          <span v-if="hasFutureAssignments">
              This crew member has pending/confirmed availability for upcoming shifts. 
              Deleting their account will also remove these assignments.
          </span>
        </p>
        <div class="modal-actions">
          <button @click="performDeletion" :disabled="checkingAvailability">Confirm</button>
          <button @click="cancelDeletion">Cancel</button>
        </div>
      </div>
    </div>


  <!-- Invite Crew Member Button -->
  <div style="margin-top: 1.4rem;">
    <button class="invite-btn" @click="showInviteModal = true">Invite a Crew Member</button>
  </div>

  <!-- Success Popup -->
  <div v-if="showSuccessPopup" class="success-popup">
    <span class="close-btn" @click="showSuccessPopup = false">&times;</span>
    <p>You have successfully deleted the Crew Member.</p>
  </div>


  <!-- Invite Crew Member Modal -->
  <div v-if="showInviteModal" class="modal">
    <div class="modal-content">
      <h3>Invite Crew Member</h3>
      <label for="inviteEmail">Email Address:</label>
      <input
        type="email"
        id="inviteEmail"
        v-model="inviteEmail"
        placeholder="Enter email"
        required
      />
      <div class="modal-actions">
        <button @click="sendInvite" :disabled="!inviteEmail">Send Invite</button>
        <button @click="cancelInvite">Cancel</button>
      </div>
    </div>
  </div>

  <!-- Crew Member Details Modal -->
  <div v-if="showDetailsModal" class="modal">
    <div class="modal-content">
      <h3>Crew Member Details</h3>
      <div v-if="selectedDetails">
        <p><strong>Name:</strong> {{ selectedDetails.firstName }} {{ selectedDetails.lastName }}</p>
        <p><strong>Email:</strong> {{ selectedDetails.email }}</p>
        <p><strong>Phone:</strong> {{ selectedDetails.phoneNumber }}</p>
        <p><strong>Role:</strong> {{ selectedDetails.role }}</p>
        <p><strong>Positions:</strong> {{ selectedDetails.position.join(', ') }}</p>
      </div>
      <div class="modal-actions">
        <button @click="closeDetailsModal">Close</button>
      </div>
    </div>
  </div>


  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ManageCrew',
  data() {
    return {
      crewMembers: [],
      loading: true,
      selectedMember: null,
      showConfirm: false,
      hasFutureAssignments: false,
      checkingAvailability: false,
      showSuccessPopup: false,
      emptyList: false,
      inviteEmail: '',
      showInviteModal: false,
      selectedDetails: null,
      showDetailsModal: false,
      searchQuery: ''
    }
  },
  methods: {
    async fetchCrewMembers() {
      try {
        const res = await axios.get('http://localhost:8080/user/crewMember')
        if (res.data.flag && res.data.code === 200) {
          if (res.data.data.length === 0) {
              this.emptyList = true;
              return;
          }
          this.crewMembers = res.data.data
        } else {
          alert('Failed to load crew members.')
        }
      } catch (error) {
        console.error('Error fetching crew members:', error)
        alert('An error occurred while fetching crew members.')
      } finally {
        this.loading = false
      }
    },
    handleDeleteClick(member){
      this.showSuccessPopup = false
      this.confirmDelete(member)
    },
    async confirmDelete(member) {
      this.selectedMember = member
      this.showConfirm = true
      this.checkingAvailability = true
      this.hasFutureAssignments = false

      try {
        const res = await axios.get(`http://localhost:8080/availability/availability/${member.userId}`)
        if (res.data.flag && res.data.code === 200 && Array.isArray(res.data.data) && res.data.data.length > 0) {
          this.hasFutureAssignments = true
        }
      } catch (error) {
        console.error('Failed to check availability:', error)
        this.hasFutureAssignments = false
      } finally {
        this.checkingAvailability = false
      }
    },
    cancelDeletion() {
      this.selectedMember = null
      this.showConfirm = false
      this.hasFutureAssignments = false
    },
    async performDeletion() {
      try {
        console.log(`crewMember to delete is ${this.selectedMember.userId}`)
        await axios.delete(`http://localhost:8080/user/crewMember/${this.selectedMember.userId}`)
        this.crewMembers = this.crewMembers.filter(
          m => m.userId !== this.selectedMember.userId
        )
        this.showSuccessPopup = true

      } catch (error) {
        alert('Failed to delete the crew member.')
        console.error(error)
      }

      this.selectedMember = null
      this.showConfirm = false
      this.hasFutureAssignments = false
    },
    cancelInvite() {
      this.inviteEmail = '';
      this.showInviteModal = false;
    },
    async sendInvite() {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailPattern.test(this.inviteEmail)) {
        alert('Please enter a valid email address.');
        return;
      }

      try {
        const res = await axios.post('http://localhost:8080/user/invite', {
          emails: [this.inviteEmail.trim()]
        });

        if (res.data.flag && res.data.code === 200) {
          alert('Invitation sent successfully!');
        } else {
          alert('Failed to send invitation.');
        }
      } catch (error) {
        console.error('Error sending invite:', error);
        alert('An error occurred while sending the invitation.');
      } finally {
        this.inviteEmail = '';
        this.showInviteModal = false;
      }
    },
    async viewDetails(userId) {
      this.showSuccessPopup = false
      try {
        const res = await axios.get(`http://localhost:8080/user/crewMember/${userId}`);
        if (res.data.flag && res.data.code === 200) {
          this.selectedDetails = res.data.data;
          this.showDetailsModal = true;
        } else {
          alert('Failed to load crew member details.');
        }
      } catch (error) {
        console.error('Error loading crew member details:', error);
        alert('An error occurred while fetching details.');
      }
    },

    closeDetailsModal() {
      this.showDetailsModal = false;
      this.selectedDetails = null;
    },
    exportCSV() {
      const headers = ['Name', 'Email'];
      const rows = this.filteredCrewMembers.map(member => [
        member.fullName,
        member.email
      ]);

      const csvContent = [
        headers.join(','),
        ...rows.map(row => row.join(','))
      ].join('\n');

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'crew_members.csv';
      link.click();
    },

    exportExcel() {
      const headers = ['Name', 'Email'];
      const rows = this.filteredCrewMembers.map(member => [
        member.fullName,
        member.email
      ]);

      let table = `<table><tr>${headers.map(h => `<th>${h}</th>`).join('')}</tr>`;
      table += rows.map(row => `<tr>${row.map(cell => `<td>${cell}</td>`).join('')}</tr>`).join('');
      table += '</table>';

      const blob = new Blob(
        [`<html><head><meta charset="UTF-8"></head><body>${table}</body></html>`],
        { type: 'application/vnd.ms-excel' }
      );

      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'crew_members.xls';
      link.click();
    }




  },
  computed: {
    filteredCrewMembers() {
      if (!this.searchQuery.trim()) return this.crewMembers;
      const query = this.searchQuery.toLowerCase();
      return this.crewMembers.filter(member =>
        member.fullName.toLowerCase().includes(query)
      );
    }
  },
  mounted() {
    this.fetchCrewMembers()
  },
}

</script>

<style scoped>
.manage-crew-wrapper {
  padding: 2rem;
  max-width: 800px;
  margin-left: 2rem;
  font-family: 'Inter', sans-serif;
}

h1{
    color: #4D1979;
  }

.crew-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.crew-table th,
.crew-table td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: left;
}

.crew-table th {
  background-color: #f3e8ff;
  text-align: left;
}

button {
  background-color: #3b82f6;
  color: white;
  border: none;
  padding: 0.5rem .5rem;
  cursor: pointer;
  border-radius: 4px;
}

.action-button {
  margin: .2rem;
}

button:hover {
  background-color: #2663c6;
}

.invite-btn{
  font-weight: bold;
}

.modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  max-width: 400px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

.success-popup {
  position: fixed;
  margin-top: 30px;
  background-color: #d1fae5;
  color: #065f46;
  padding: 1rem 1.5rem;

  border-radius: 6px;
  margin-top: 20px;
}

.success-popup p {
margin: 0;
font-weight: 500;
}

.close-btn {
position: absolute;
top: 2px;
right: 5px;
font-size: 1.2rem;
cursor: pointer;
color: green;
}
input[type="email"] {
  width: 100%;
  padding: 0.5rem;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-bar {
  margin-bottom: 1rem;
}

.search-bar input {
  padding: 0.5rem;
  margin-left: .5rem;
  border: 1px solid #ccc;
}

#export-btn {
  background-color: #6b7280;
  margin: .5rem;
  
}

#export-btn:hover {
  background-color: #4b5563;
}

</style>
