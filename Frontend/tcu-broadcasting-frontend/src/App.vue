<template>
  <header>
    <Banner></Banner>
    <div class="content">
      <NavigationBar></NavigationBar>
      <CrewList></CrewList>
      
    </div>
  </header>
</template>

<script setup>
import AvailabilityTable from './components/AvailabilityTable.vue';
import GameSchedule from './components/GameSchedule.vue';
import NavigationBar from './components/NavigationBar.vue'
import CrewMemberList from './components/CrewMemberList.vue';
import CrewList from './components/CrewList.vue';
import Banner from './components/Banner.vue';
import RegistrationForm from './components/RegistrationForm.vue';
import Login from './components/Login.vue';
</script>

<style scoped>

.content{
  display: flex;
}

</style>
