<template>
    <div class="login-wrapper">
      <div class="login-card">
        <h2>LOGIN</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="email">Email</label>
            <input v-model="email" type="email" id="email" required />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input v-model="password" type="password" id="password" required />
          </div>
          
          <button type="submit">LOGIN</button>
          <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  
  export default {
    name: 'LoginPage',
    data() {
      return {
        email: '',
        password: '',
        errorMessage: '',
      }
    },
    methods: {
        async handleLogin() {
            this.errorMessage = ''

            try {
                const response = await axios.post('http://localhost:8080/auth/login', {
                email: this.email,
                password: this.password
                }, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
                })

                const res = response.data

                //work on with backend
                if (res.flag && res.code === 200) {
                //const { token, role, userId } = res.data
                //localStorage.setItem('token', token)
                //localStorage.setItem('userId', userId)
                //localStorage.setItem('role', role)

                this.$router.push('/dashboard')
                } else {
                this.errorMessage = res.message || 'Login failed.'
                }
            } catch (error) {
                this.errorMessage = error.response?.data?.message || 'An unexpected error occurred.'
            }
        }
    }
  }
  </script>
  
  <style scoped>

  
  .login-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 75vh;
    background-color: #4D1979;
    font-family: 'Inter', sans-serif;
  }
  
  .login-card {
    background: #fff;
    padding: 2rem;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    width: 360px;
    text-align: center;
    margin-top: 2.5rem;
  }
  
  .login-card h2 {
    font-size: 1.5rem;
    margin-bottom: 1.5rem;
  }
  
  .form-group {
    text-align: left;
    margin-bottom: 1rem;
  }
  
  .form-group label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 500;
    color: #333;
  }
  
  .form-group input {
    width: 100%;
    padding: 0.5rem;
    border-radius: 6px;
    border: 1px solid #ccc;
    font-size: 1rem;
  }
  
  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 0.875rem;
    margin-bottom: 1rem;
  }
  

  
  button[type="submit"] {
    background-color: #4D1979;
    color: white;
    border: none;
    width: 100%;
    padding: 0.75rem;
    font-size: 1rem;
    border-radius: 6px;
    cursor: pointer;
    margin-top: .5rem;
  }
  
  button[type="submit"]:hover {
    background-color: #3c1461;
  }
  
  .error {
    color: red;
    font-size: 0.875rem;
    margin-bottom: 1rem;
  }


  </style>
  