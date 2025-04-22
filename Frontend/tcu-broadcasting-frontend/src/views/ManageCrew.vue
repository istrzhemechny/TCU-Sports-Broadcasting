<template>
    <div class="manage-crew-wrapper">
      <h2>Manage Crew Members</h2>
  
      <div v-if="loading">Loading crew members...</div>

      <div v-else-if="emptyList">No Registered Members.</div>
  
      <table v-else class="crew-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in crewMembers" :key="member.userId">
            <td>{{ member.fullName }}</td>
            <td>{{ member.email }}</td>
            <td>
              <button @click="handleDeleteClick(member)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
  
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

      <!-- Success Popup -->
    <div v-if="showSuccessPopup" class="success-popup">
      <span class="close-btn" @click="showSuccessPopup = false">&times;</span>
      <p>You have successfully deleted the Crew Member.</p>
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
        emptyList: false
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
      }
    },
    mounted() {
      this.fetchCrewMembers()
    }
  }
  </script>
  
  <style scoped>
  .manage-crew-wrapper {
    padding: 2rem;
    max-width: 800px;
    margin-left: 2rem;
    font-family: 'Inter', sans-serif;
  }
  
  .crew-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }
  
  .crew-table th,
  .crew-table td {
    border: 1px solid #ddd;
    padding: 0.75rem;
  }
  
  .crew-table th {
    background-color: #f9f9f9;
    text-align: left;
  }
  
  button {
    background-color: #b02a7d;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    cursor: pointer;
    border-radius: 4px;
  }
  
  button:hover {
    background-color: #861f63;
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

  /*
  .success-popup {
    position: fixed;
    margin-top: 30px;
    border: solid green;
    background-color: #77d37a;
    color: green;
    padding: 1rem 1.5rem;
    border-radius: 5px;

    }*/

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
  </style>
  