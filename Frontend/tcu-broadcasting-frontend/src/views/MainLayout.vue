<template>
    <div class="layout-wrapper">
      <TCUBanner></TCUBanner>
      <div class="content">
        <component :is="navComponent" />
        <router-view></router-view> 
      </div>
    </div>
  </template>
  
  <script setup>
  import { userRole } from '@/apis/auth'
  import { computed } from 'vue'

  import NavigationBarCrew from '@/components/NavigationBarCrew.vue'
  import NavigationBarAdmin from '@/components/NavigationBarAdmin.vue'
  import TCUBanner from '@/components/TCUBanner.vue';

  const navComponent = computed(() => {
    console.log(userRole.value)
    return userRole.value === 'ADMIN' ? NavigationBarAdmin : NavigationBarCrew
  })
  </script>
  
  <style scoped>
  .layout-wrapper {
    display: flex;
    flex-direction: column;
    min-height: 100vh; /* Full page height */
  }
  .content{
    display: flex;
    flex: 1; 
    overflow: hidden; 

  }
  
  </style>
  