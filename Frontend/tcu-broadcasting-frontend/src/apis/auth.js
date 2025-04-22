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
    /*
    const res = await axios.post('http://localhost:8080/auth/login', {
      email: username,
      password: password
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    })
    */

    // Mock Data
    let res
    if (username === 'admin@example.com' && password === 'adminpass') {
      res = {
        data: {
          flag: true,
          code: 200,
          message: 'Login Success',
          data: {
            userId: 1,
            role: 'ADMIN',
            token: 'mock-jwt-token'
          }
        }
      }
    } else if (username === 'crew@example.com' && password === 'crewpass') {
      res = {
        data: {
          flag: true,
          code: 200,
          message: 'Login Success',
          data: {
            userId: 2,
            role: 'CREW',
            token: 'mock-jwt-token-2'
          }
        }
      }
    } else {
      res = {
        data: {
          flag: false,
          code: 401,
          message: 'username or password is incorrect',
          data: null
        }
      }
    }

    //// End of Mock Data

    if (res.data.flag && res.data.code === 200) {
      const { userId: id, role, token: jwt } = res.data.data

      // Store credentials in memory or localStorage if needed
      isAuthenticated.value = true
      userRole.value = role
      userId.value = id
      token.value = jwt

      // Optionally persist values
      localStorage.setItem('token', jwt)
      localStorage.setItem('userId', id)
      localStorage.setItem('role', role)

    } else {
      isAuthenticated.value = false
      loginError.value = res.data.message || 'Login failed'
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

  localStorage.clear()
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
