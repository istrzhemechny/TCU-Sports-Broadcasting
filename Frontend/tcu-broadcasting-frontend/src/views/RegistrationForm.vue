<template>
    <div class="container">
      <h1>Crew Member Profile Setup</h1>
  
      <div v-if="alreadyRegistered" class="alert alert-warning">
        <!--Modify after Vue Router Code Alongs-->
        <p>Your account is already set up. Please <router-link to="/login">log in</router-link>.</p>
      </div>
  
      <form v-if="!submitted" @submit.prevent="handleSubmit">
        <label>First Name</label>
        <input type="text" v-model="form.firstName" class="input" required />
  
        <label>Last Name</label>
        <input type="text" v-model="form.lastName" class="input" required />
  
        <label>Email</label>
        <input type="email" v-model="form.email" class="input" required />
  
        <label>Phone Number (999-999-9999)</label>
        <div class="phone-input">
          <input type="tel" v-model="form.phone" class="input" required @input="formatPhoneNumber" placeholder="XXX-XXX-XXXX"/>
        </div>
        <div v-if="!validPhone" class="error">Invalid phone number format.</div>
  
        <label>Password</label>
        <input type="password" v-model="form.password" class="input" required />
  
        <label>Role</label>
        <input type="text" v-model="form.role" class="input" required />

        <label>Qualified Positions</label>
        <div v-for="(position, index) in form.qualifiedPosition" :key="index" class="position-row">
        <input
            type="text"
            v-model="form.qualifiedPosition[index]"
            class="input"
            required
            :placeholder="`Position ${index + 1}`"
        />
        <button type="button" class="btn btn-secondary" @click="removePosition(index)" v-if="form.qualifiedPosition.length > 1">Remove</button>
        </div>
        <div class="btn-div">
            <button type="button" class="btn btn-add" @click="addPosition">Add Position</button>
        </div>


        <button type="submit" class="btn btn-primary">Submit</button>
        <button type="button" class="btn btn-secondary" @click="resetForm">Modify</button>
      </form>
  
      <div v-else class="confirmation">
        <h2>Confirm Your Details</h2>
        <ul>
          <li><strong>Name:</strong> {{ form.firstName }} {{ form.lastName }}</li>
          <li><strong>Email:</strong> {{ form.email }}</li>
          <li><strong>Phone:</strong> {{ form.phone}}</li>
          <li><strong>Role:</strong> {{ form.role }}</li>
          <li><strong>Qualified Positions:</strong>
          <ul>
            <li v-for="(pos, i) in form.qualifiedPosition" :key="i">{{ pos }}</li>
          </ul>
          </li>
        </ul>
        <button class="btn btn-success" @click="confirmRegistration">Confirm</button>
        <button class="btn btn-warning" @click="submitted = false">Modify</button>
      </div>
  
      <div v-if="registered" class="alert alert-success">
        <p>Your account has been created. Redirecting to login page...</p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        alreadyRegistered: false,
        submitted: false,
        registered: false,
        validPhone: true,
        form: {
          firstName: '',
          lastName: '',
          email: '',
          phone: '',
          password: '',
          role: '',
          qualifiedPosition: ['']
        }
      };
    },
    methods: {
      formatPhoneNumber() {
        let number = this.form.phone.replace(/\D/g, '');
        if (number.length > 10) {
            number = number.slice(0, 10);
        }
        const areaCode = number.slice(0, 3);
        const prefix = number.slice(3, 6);
        const line = number.slice(6, 10);

        if (number.length > 6) {
            this.form.phone = `${areaCode}-${prefix}-${line}`;
        } else if (number.length > 3) {
            this.form.phone = `${areaCode}-${prefix}`;
        } else if (number.length > 0) {
            this.form.phone = `${areaCode}`;
        } else {
            this.form.phone = '';
        }
        this.validatePhoneNumber();
        },
        validatePhoneNumber() {
        this.validPhone = /^\d{3}-\d{3}-\d{4}$/.test(this.form.phone);
      },
      addPosition() {
        this.form.qualifiedPosition.push('');
      },
      removePosition(index) {
        this.form.qualifiedPosition.splice(index, 1);
      },
      handleSubmit() {
        if (!this.validPhone) return;
        const fieldsToCheck = {
          ...this.form
        };
        const allFieldsFilled = Object.values({ ...this.form, phone: this.form.phone }).every(field => {
          if (Array.isArray(field)) {
              return field.every(p => p.trim() !== '');
          }
          return field.trim() !== '';
        });
        if (!allFieldsFilled) {
          alert('Please fill in all required fields.');
          return;
        }
        this.submitted = true;
      },
      async confirmRegistration() {
        try {
            // Format phone number: remove dashes
            const plainPhone = this.form.phone.replace(/\D/g, '');

            const payload = {
            firstName: this.form.firstName,
            lastName: this.form.lastName,
            email: this.form.email,
            phoneNumber: plainPhone,
            password: this.form.password,
            role: this.form.role,
            position: this.form.qualifiedPosition
            };

            await axios.post('http://localhost:8080/User/crewMember', payload);

            this.registered = true;
            //Modify after Vue Router Code Alongs
            setTimeout(() => {
            this.$router.push('/login');
            }, 2000);
        } catch (error) {
            if (error.response && error.response.status === 400) {
                const emailError = error.response.data?.data?.email;

                if (emailError) {
                    alert('This email is already registered. Please use a different email or log in.');
                } else {
                    alert('Registration failed. Please check your input.');
                }
            } else {
                console.error('Unexpected error during registration:', error);
                alert('An unexpected error occurred. Please try again later.');
            }
        }
      },
      resetForm() {
        this.submitted = false;
      }
    },
    mounted() {
      const params = new URLSearchParams(window.location.search);
      if (params.get('alreadyRegistered') === 'true') {
        this.alreadyRegistered = true;
      }
    }
  };
  </script>
  
  <style scoped>

  h1 {
    color: #4D1979;
  }
  .container {
    font-family: Arial, sans-serif;
    padding: 20px;
    max-width: 600px;
    margin: auto;
  }
  .input {
    padding: 8px;
    margin: 4px 0 16px;
    border: 1px solid #ccc;
    border-radius: 6px;
    width: 100%;
  }
  .phone-inputs {
    display: flex;
    gap: 6px;
    margin-bottom: 16px;
  }
  .phone-inputs .input {
    flex: 1;
    width: auto;
  }
  .btn {
    padding: 8px 16px;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
    margin-right: 8px;
  }
  .btn-primary { background-color: #4f46e5; color: white; border: none; }
  .btn-secondary { background-color: #e5e7eb; border: none; margin-bottom: 10px;}
  .btn-add { background-color: #4D1979; color: white; border: none; margin-bottom: 15px; }
  .btn-success { background-color: #10b981; color: white; border: none; }
  .btn-warning { background-color: #f59e0b; color: white; border: none; }
  .btn-div{
    padding-top: 5px;
  }
  .alert {
    padding: 12px;
    border-radius: 6px;
    margin-bottom: 20px;
  }
  .alert-warning { background-color: #fef3c7; color: #92400e; }
  .alert-success { background-color: #d1fae5; color: #065f46; }
  .error {
    color: red;
    font-size: 14px;
    margin-top: -12px;
    margin-bottom: 12px;
  }
  </style>
  