import Vue from 'vue'
import Router from 'vue-router'
const Home = () => import('@/views/Home.vue')
const Main = () => import('@/components/index/main.vue')
const Place = () => import('@/components/place/place.vue')
const Device = () => import('@/components/device/device.vue')
const Alarm = () => import('@/components/device/alarm.vue')
const DeviceMessage = () => import('@/components/device/deviceMessage.vue')
Vue.use(Router)

export default new Router({
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      component: Main
    },
    {
      path: '/office/Place',
      component: Place
    },
    {
      path: '/office/device',
      component: Device
    },
    {
      path: '/office/alarm',
      component: Alarm
    },
    {
      path: '/office/DeviceMessage',
      component: DeviceMessage
    },
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/office/channellist',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    }
  ]
})
