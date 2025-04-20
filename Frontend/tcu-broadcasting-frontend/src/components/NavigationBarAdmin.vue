<template>
  <div class="side-banner">
    <div class="nav-link active">View Game Schedule</div>
    <div class="nav-link">View Crew Members</div>
    <div class="nav-link">View Game Crew Lists</div>
    <!-- Invite, Delete Crew Members-->
    <div class="nav-link"> Manage Crew Members</div>
    <div class="nav-link"> Manage Game Schedules</div>
    <div class="nav-link"> Schedule Crew </div>

  </div>
</template>

<script>
export default {
  name: 'SideBanner'
};
</script>

<style scoped>
.side-banner {
  width: 220px;
  height: 74vh;
  background-color: #4D1979;
  font-family: Arial, sans-serif;
}
.nav-link {
  padding: 18px;
  background-color: transparent;
  color: #ffffff;
  font-weight: normal;
  cursor: pointer;
  
}
.nav-link:hover {
  background-color: #ddd6fe;
  color: #4D1979;
}
.nav-link.active {
  background-color: #cdcbd7;
  font-weight: bold;
  color: #4D1979;
}
</style>
