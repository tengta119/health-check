
<template>
  <div>
    <div class="front-notice">
      <el-icon><Bell /></el-icon>公告：{{ data.top }}
    </div>
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">前台系统</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/person">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div style="padding-right: 20px" v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer">
            <div style="padding-right: 20px; display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%; "  :src="data.user.avatar" alt="">
              <span style="margin-left: 5px">{{ data.user.name }}</span> <el-icon><arrow-down/></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import router from '@/router/index.js'
import {ArrowDown, Bell} from "@element-plus/icons-vue";
import {reactive} from "vue";
import request from "@/utils/request.js";
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  top: null,
  noticeData: [],
})
const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    data.noticeData = res.data
    let i = 0
    if (data.noticeData && data.noticeData.length) {
      data.top = data.noticeData[0].content
      setInterval(() => {
        data.top = data.noticeData[i].content
        i++
        if (i === data.noticeData.length) {
          i = 0
        }
      }, 2500)
    }
  })
}
loadNotice()

loadNotice()
</script>

<style scoped>
  @import "@/assets/css/front.css";

</style>

