<template>
  <div class="side-banner">
    <router-link class="nav-link" :to="{ name: 'gameSchedule' }">View Game Schedule</router-link>
    <router-link class="nav-link" :to="{ name: 'crewMembers' }">View Crew Members</router-link>
    <router-link class="nav-link" :to="{ name: 'crewList' }">View Game Crew Lists</router-link>
    <router-link class="nav-link" :to="{ name: 'availability' }">Submit Availability</router-link>
  </div>
</template>

<script>
export default {
  name: 'SideBanner'
};
</script>

<style scoped>
.side-banner {
  width: 235px;
  height: calc(100vh - 135px);
  background-color: #4D1979;
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);

}

.nav-link {
  padding: 1.3rem;
  color: #ffffff;
  text-decoration: none;
  display: block;
  transition: background-color 0.2s, color 0.2s;
  
}

.nav-link:hover {
  background-color: #ddd6fe;
  color: #4D1979;
}

.router-link-active {
  background-color: #cdcbd7;
  font-weight: bold;
  color: #4D1979;
}
</style>
