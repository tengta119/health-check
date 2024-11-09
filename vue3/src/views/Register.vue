
<template>

  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">欢 迎 注 册</div>

      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input  show-password :prefix-icon="Lock" type="password" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input  show-password :prefix-icon="Lock" type="password" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码"></el-input>
        </el-form-item>
<!--        <el-form-item prop="role">-->
<!--          <el-select size="large" v-model="data.form.role">-->
<!--            -->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button type="primary" size="large" style="width: 100%" @click="register">注 册</el-button>
        </el-form-item>
        <div style="text-align: right">
          已有账号？请<router-link to="/login">登录</router-link>
        </div>
      </el-form>

    </div>
  </div>


</template>

<script setup>

import { reactive, ref } from 'vue'
import { User, Lock } from "@element-plus/icons-vue";
import  request  from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else {
    if (value !== data.form.password) {
      callback(new Error('两次输入密码不一致'))
    }
    callback()
  }
}

const data = reactive({
  form: {
    role: 'USER',
    username: '',
    password: '',
    confirmPassword: '',
  },
  rules: {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    confirmPassword: [{ validator: validatePass, trigger: 'blur'}]
  }

})

const formRef = ref(null)

const register = () => {
  formRef.value.validate(valid => {
    if (valid) { //表示表单校验通过
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          router.push('/login')
        } else {
          ElMessage.error(res.message)
        }
      })
    }
  })
}

</script>

<style scoped>

.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to top, #00467f, #a5cc82);
}

.login-box {
  width: 350px;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}

</style>

