<template>
  <header>
    <Banner></Banner>
    <div class="content">
      <NavigationBar></NavigationBar>
      <GameSchedule></GameSchedule>
    </div>
  </header>

  <main>
    <TheWelcome />
  </main>
</template>

<script setup>
import AvailabilityTable from './components/AvailabilityTable.vue';
import GameSchedule from './components/GameSchedule.vue';
import NavigationBar from './components/NavigationBar.vue'
import CrewList from './components/CrewList.vue';
import Banner from './components/Banner.vue';
</script>

<style scoped>

.content{
  display: flex;
}

</style>
