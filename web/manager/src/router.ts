import Vue from 'vue'
import Router from 'vue-router'
const Home = () => import('@/views/Home.vue')
const Main = () => import('@/components/index/main.vue')
const Place = () => import('@/components/place/place.vue')

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
