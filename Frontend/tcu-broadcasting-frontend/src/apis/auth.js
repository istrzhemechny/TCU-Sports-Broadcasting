import { ref } from 'vue'
import axios from 'axios'

const isAuthenticated = ref(false)
const userRole = ref(null)
const userId = ref(null)
const token = ref(null)
const loginError = ref(null)

const login = async (username, password) => {
  loginError.value = null

  try {

    const basicAuth = btoa(`${username}:${password}`); // Encode username:password in base64
    console.log(`basic auth is: ${basicAuth}`)


    const response = await fetch('http://localhost:8080/user/auth/login', {
      method: 'POST',
      headers: {
        'Authorization': `Basic ${basicAuth}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })

    const data = await response.json()

    console.log('Login Response:', data);


    if (data.flag && data.code === 200) {
      const { userId: id, role, token: jwt } = data.data

      // Store credentials
      isAuthenticated.value = true
      userRole.value = role
      userId.value = id
      token.value = jwt

      // Optionally persist values
      sessionStorage.setItem('token', jwt)
      sessionStorage.setItem('userId', id)
      sessionStorage.setItem('role', role)

    } else {
      isAuthenticated.value = false
      loginError.value = data.message || 'Login failed'
    }

  } catch (error) {
    isAuthenticated.value = false
    loginError.value = error.response?.data?.message || 'Login failed'
    console.error('Login error:', error)
  }

  
}

const logout = () => {
  isAuthenticated.value = false
  userRole.value = null
  userId.value = null
  token.value = null

  sessionStorage.clear()
}

export {
  isAuthenticated,
  userRole,
  userId,
  token,
  login,
  logout,
  loginError
}
