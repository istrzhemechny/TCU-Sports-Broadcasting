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
    const res = await axios.post('http://localhost:8080/user/auth/login', {
      email: username,
      password: password
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    })

    if (res.data.flag && res.data.code === 200) {
      const { userId: id, role, token: jwt } = res.data.data

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
