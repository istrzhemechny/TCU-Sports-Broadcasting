import { createRouter, createWebHistory } from 'vue-router'
import { isAuthenticated, userRole, userId, token } from '@/apis/auth'

// Restore sessionStorage on page reload
const savedToken = sessionStorage.getItem('token')
const savedRole = sessionStorage.getItem('role')
const savedUserId = sessionStorage.getItem('userId')

if (savedToken && savedRole && savedUserId) {
  isAuthenticated.value = true
  token.value = savedToken
  userRole.value = savedRole
  userId.value = savedUserId
}

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
                meta: { requiresAuth: true, roles: ['ADMIN', 'USER']  }
            },
            { 
                path: '/crewMember', 
                name: 'crewMembers', 
                component: () => import('@/views/CrewMemberList.vue'),
                meta: { requiresAuth: true, roles: ['USER']  },
            },
            { 
                path: '/crewList', 
                name: 'crewList', 
                component: () => import('@/views/CrewList.vue'),
                meta: { requiresAuth: true, roles: ['ADMIN', 'USER'] },
            },
            { 
                path: '/availability', 
                name: 'availability', 
                component: () => import('@/views/AvailabilityTable.vue'),
                meta: { requiresAuth: true, roles: ['USER'] }, 
            },
            { 
                path: '/manageCrew', 
                name: 'manageCrewMembers', 
                component: () => import('@/views/ManageCrew.vue'),
                meta: { requiresAuth: true, roles: ['ADMIN'] }, 
            },
            { 
                path: '/manageSchedules', 
                name: 'manageSchedules', 
                component: () => import('@/views/ManageSchedules.vue'),
                meta: { requiresAuth: true, roles: ['ADMIN'] }, 
            },
            { 
                path: '/scheduleCrew', 
                name: 'scheduleCrew', 
                component: () => import('@/views/ScheduleCrew.vue'),
                meta: { requiresAuth: true,  roles: ['ADMIN']  }, 
            }
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login.vue'),
        meta: { requiresAuth: false },
      },
      { 
        path: '/register', 
        name: 'register', 
        component: () => import('@/views/RegistrationForm.vue'),
        meta: { requiresAuth: false }, 
    }

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
  const requiresAuth = to.meta.requiresAuth
  const allowedRoles = to.meta.roles

  if (to.meta.requiresAuth && !isAuthenticated.value) {
      // Redirect to the login page with the originally requested page as the redirect query parameter
      return { name: 'login', query: { redirect: to.fullPath } }
  }

  if (requiresAuth && allowedRoles && !allowedRoles.includes(userRole.value)) {
    // Authenticated but wrong role -> show alert and redirect
    alert('You do not have permission to view this page.');
    return { name: 'gameSchedule' } // Redirect to a safe place (like dashboard)
  }

})
  
  // Global after each navigation guard (for cleanup or login)
  router.afterEach((to, from) => {
    //console.log(`Successfully navigated to: ${to.fullPath}`)
  })
  

// Export the router instance
export default router