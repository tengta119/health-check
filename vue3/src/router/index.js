import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/manager/home'},
    {
      path: '/manager', component: () => import('@/views/Manager.vue'),
      children: [
        {path: 'home', meta: {name: '系统首页'}, component: () => import('@/views/manager/Home.vue')},
        {path: 'admin', meta: {name: '管理员信息'}, component: () => import('@/views/manager/Admin.vue')},
        {path: 'person', meta: {name: '个人信息'}, component: () => import('@/views/manager/Person.vue')},
        {path: 'password', meta: {name: '修改密码'}, component: () => import('@/views/manager/Password.vue')},
        {path: 'notice', meta: {name: '系统公告'}, component: () => import('@/views/manager/Notice.vue')},
        { path: 'examinationType', meta: { name: '普通体检类型' }, component: () => import('@/views/manager/ExaminationType.vue')},
        { path: 'doctor', meta: { name: '普通体检类型' }, component: () => import('@/views/manager/Doctor.vue')},
        { path: 'office', meta: { name: '科室信息' }, component: () => import('@/views/manager/Office.vue')},
        { path: 'title', meta: { name: '科室信息' }, component: () => import('@/views/manager/Title.vue')},
        { path: 'user', meta: { name: '普通用户信息' }, component: () => import('@/views/manager/User.vue')},
        { path: 'information', meta: { name: '健康科普信息' }, component: () => import('@/views/manager/Information.vue')},
        { path: 'userInformation', meta: { name: '健康科普信息' }, component: () => import('@/views/manager/UserInformation.vue')},
        { path: 'informationDetail', meta: { name: '健康科普信息详情' }, component: () => import('@/views/manager/InformationDetail.vue')},
        { path: 'physicalExamination', meta: { name: '健康科普信息详情' }, component: () => import('@/views/manager/PhysicalExamination.vue')},
        { path: 'userPhysicalExamination', meta: { name: '用户预约普通体检' }, component: () => import('@/views/manager/UserPhysicalExamination.vue')},
        { path: 'examinationOrder', meta: { name: '体检预约订单' }, component: () => import('@/views/manager/ExaminationOrder.vue')},
        { path: 'examinationPackage', meta: { name: '套餐体检项目信息' }, component: () => import('@/views/manager/ExaminationPackage.vue')},
        { path: 'userExaminationPackage', meta: { name: '套餐体检项目信息' }, component: () => import('@/views/manager/UserExaminationPackage.vue')},
        { path: 'feedback', meta: { name: '用户反馈' }, component: () => import('@/views/manager/Feedback.vue')},
        { path: 'userFeedback', meta: { name: '用户反馈' }, component: () => import('@/views/manager/UserFeedback.vue')},
        { path: 'calendar', meta: { name: '日程安排' }, component: () => import('@/views/manager/Calendar.vue')},
        { path: 'dataAnalysis', meta: { name: '数据统计' }, component: () => import('@/views/manager/DataAnalysis.vue')},
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        {path: 'home', component: () => import('@/views/front/Home.vue')},
        {path: 'person', component: () => import('@/views/front/Person.vue')},
      ]
    },
    {path: '/login', component: () => import('@/views/Login.vue')},
    {path: '/register', component: () => import('@/views/Register.vue')},
    {path:'/404', component: () => import('@/views/404.vue')},
    {path: '/:pathMatch(.*)', redirect: '/404'}
  ]
})

export default router
