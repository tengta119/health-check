
<template>
  <div class="manager-container">

    <div class="manager-header">

      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">管理系统</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>
            {{ router.currentRoute.value.meta.name }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor: pointer">
          <div style="padding-right: 20px; display: flex; align-items: center">
            <img style="width: 40px; height: 40px; border-radius: 50%; "  :src="data.user.avatar">
            <span style="margin-left: 5px; color: white">{{ data.user.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人资料</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

    </div>
    
<!--   下面部分 -->
    <div style="display: flex">

      <div class="manager-main-left">
        <el-menu router :default-openeds="['1','2']" :default-active="router.currentRoute.value.path">
          <el-menu-item index="/manager/home">
            <el-icon><home-filled/></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Menu/></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice">系统公告</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><Menu/></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="manager-main-right">
        <RouterView @updateUser="updateUser"/>
      </div>

    </div>
<!--    下面部分结束-->

  </div>
</template>

<script setup>

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

import {HomeFilled, Location, Menu, Setting} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import { reactive } from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

if (!data.user.id) {
  logout()
  ElMessage.error("请登录")
}


</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>

