import { createRouter, createWebHistory } from 'vue-router'
import { isAuthenticated } from '@/apis/auth'

// Create a router instance
const router = createRouter({
  // Provide the history implementation to use. We are using the HTML5 history API
  history: createWebHistory(),
  // Define some routes, each route record should map to a component
  routes: [
    { 
        path: '/', 
        name: 'mainLayout', 
        component: () => import('@/views/MainLayout.vue'),
        redirect: { name: 'gameSchedule' },
        children: [
            { 
                path: '/gameSchedule', 
                name: 'gameSchedule', 
                component: () => import('@/views/GameSchedule.vue'),
                meta: { requiresAuth: true }
            },
            { 
                path: '/crewMember', 
                name: 'crewMembers', 
                component: () => import('@/views/CrewMemberList.vue'),
                meta: { requiresAuth: true },
            },
            { 
                path: '/crewList', 
                name: 'crewList', 
                component: () => import('@/views/CrewList.vue'),
                meta: { requiresAuth: true },
            },
            { 
                path: '/availability', 
                name: 'availability', 
                component: () => import('@/views/AvailabilityTable.vue'),
                meta: { requiresAuth: true }, 
            },
            { 
                path: '/manageCrew', 
                name: 'manageCrewMembers', 
                component: () => import('@/views/ManageCrew.vue'),
                meta: { requiresAuth: true }, 
            },
            { 
                path: '/manageSchedules', 
                name: 'manageSchedules', 
                component: () => import('@/views/ManageSchedules.vue'),
                meta: { requiresAuth: true }, 
            },
            { 
                path: '/scheduleCrew', 
                name: 'scheduleCrew', 
                component: () => import('@/views/ScheduleCrew.vue'),
                meta: { requiresAuth: true }, 
            },

            
            
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login.vue'),
        meta: { requiresAuth: false },
      },

    /*
    {
      path: '/:pathMatch(.*)*', // Match any path that hasn't been matched by a previous route
      name: 'notFound',
      component: NotFound,
      meta: { requiresAuth: true },
    },
    */
  ],
})

router.beforeEach((to, from) => {
    if (to.meta.requiresAuth && !isAuthenticated.value) {
      // Redirect to the login page with the originally requested page as the redirect query parameter
      return { name: 'login', query: { redirect: to.fullPath } }
    }
  })
  
  // Global after each navigation guard (for cleanup or login)
  router.afterEach((to, from) => {
    console.log(`Successfully navigated to: ${to.fullPath}`)
  })
  

// Export the router instance
export default router