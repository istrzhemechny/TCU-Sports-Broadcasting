<template>
  <header>
    <Banner></Banner>
    <div class="content">
      <NavigationBarAdmin></NavigationBarAdmin>
      <ManageCrew></ManageCrew>
    </div>
  </header>
</template>

<script setup>
import AvailabilityTable from './components/AvailabilityTable.vue';
import GameSchedule from './components/GameSchedule.vue';
import NavigationBarCrew from './components/NavigationBarCrew.vue'
import NavigationBarAdmin from './components/NavigationBarAdmin.vue'
import CrewMemberList from './components/CrewMemberList.vue';
import CrewList from './components/CrewList.vue';
import Banner from './components/Banner.vue';
import RegistrationForm from './components/RegistrationForm.vue';
import Login from './components/Login.vue';
import ManageCrew from './components/ManageCrew.vue';
</script>

<style scoped>

.content{
  display: flex;
}

</style>
