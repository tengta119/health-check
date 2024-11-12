<template>
  <div style="width: 80%; margin: 10px auto">
    <div class="card" style="padding: 15px; margin-bottom: 10px">
      <div style="font-size: 20px;font-weight: bold; margin-bottom: 10px">请留下您的反馈与建议</div>
      <div style="margin-bottom: 5px">
        <el-input type="textarea" :rows="5" v-model="data.form.content" placeholder="请输入内容..."></el-input>
      </div>
      <div style="text-align: right">
        <el-button type="primary" @click="addFeedback">发布</el-button>
      </div>
    </div>

    <div class="card" style="padding: 20px">
      <div>
        <div v-for="item in data.tableData" :key="item.id" style="display: flex; grid-gap: 20px; margin-bottom: 15px">
          <div style="width: 60px; text-align: center">
            <img :src="item.userAvatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
            <div style="font-size: 12px">{{ item.userName }}</div>
          </div>
          <div style="flex: 1">
            <div style="margin-bottom: 10px">
              <span style="font-size: 18px; margin-right: 20px">{{ item.content }}</span>
              <span style="font-size: 13px; margin-right: 20px; color: #6a99fa">{{ item.time }}</span>
              <span style="font-size: 13px; color: red; cursor: pointer" @click="del(item.id)" v-if="item.userId === data.user.id">删除</span>
            </div>
            <div style="color: #159102; padding-left: 50px" v-if="item.status === '已回复'">
              <span style="margin-right: 20px">管理员回复：{{ item.replyContent  }}</span>
              <span style="font-size: 13px">回复时间：{{ item.replyTime  }}</span>
            </div>
          </div>
        </div>
      </div>

      <div style="margin-top: 20px" v-if="data.total">
        <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})

const load = () => {
  request.get('/feedback/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      content: data.content
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}
load()

const addFeedback = () => {
  request.post('/feedback/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('发布成功')
      load()
      data.form = {}
    } else {
      ElMessage.error(res.message)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/feedback/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.message)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
</script>