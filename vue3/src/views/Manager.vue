
<template>
  <div class="manager-container">

    <div class="manager-header">

      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">体检预约系统</div>
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
        <el-menu router :default-openeds="['1', '2', '3']" :default-active="router.currentRoute.value.path">
          <el-menu-item index="/manager/home">
            <el-icon><home-filled/></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-menu-item index="/manager/dataAnalysis" v-if="data.user.role === 'ADMIN'">
            <el-icon><DataLine/></el-icon>
            <span>数据统计</span>
          </el-menu-item>
          <el-menu-item index="/manager/calendar" v-if="data.user.role === 'DOCTOR'">
            <el-icon><Calendar /></el-icon>
            <span>日程安排</span>
          </el-menu-item>
          <el-menu-item index="/manager/userInformation">
            <el-icon><Document/></el-icon>
            <span>健康科普</span>
          </el-menu-item>
          <el-menu-item index="/manager/userPhysicalExamination">
            <el-icon><Check /></el-icon>
            <span>预约普通体检</span>
          </el-menu-item>
          <el-menu-item index="/manager/UserExaminationPackage">
            <el-icon><CoffeeCup /></el-icon>
            <span>预约套餐体检</span>
          </el-menu-item>
          <el-menu-item index="/manager/examinationOrder">
            <el-icon><Document /></el-icon>
            <span>体检订单</span>
          </el-menu-item>
          <el-menu-item index="/manager/userFeedback">
            <el-icon><ChatDotRound /></el-icon>
            <span>反馈与建议</span>
          </el-menu-item>
          <el-sub-menu index="1" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu/></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/examinationType">普通体检类型</el-menu-item>
            <el-menu-item index="/manager/information">健康科普信息</el-menu-item>
            <el-menu-item index="/manager/physicalExamination">普通体检项目</el-menu-item>
            <el-menu-item index="/manager/examinationPackage">套餐体检项目</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu/></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/manager/notice">系统公告</el-menu-item>
            <el-menu-item index="/manager/office">科室信息</el-menu-item>
            <el-menu-item index="/manager/title">职称信息</el-menu-item>
            <el-menu-item index="/manager/feedback" v-if="data.user.role === 'ADMIN'">用户反馈</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu/></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/doctor">医生信息</el-menu-item>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">普通用户信息</el-menu-item>
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

import {
  Calendar,
  ChatDotRound,
  Check,
  CoffeeCup, DataLine,
  Document,
  HomeFilled,
  Location,
  Menu,
  Setting
} from "@element-plus/icons-vue";
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

