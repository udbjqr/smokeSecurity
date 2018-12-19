import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo:'',
    Menu:'',
    tabMenu:[],
    tabMenuFlag:false,
    tabMenuUrl:'',
    sizePage:3,
    currentPage:1,
    total:0
  },
  mutations: {
    setUserInfo: (state, data) => {
			state.userInfo = data;
    },
    setMenu: (state, data) => {
			state.Menu = data;
    },
    settabMenu: (state, data) => {
			state.tabMenu = data;
    },
    settabMenuFlag: (state, data) => {
			state.tabMenuFlag = data;
    },
    setTabMenuUrl: (state, data) => {
			state.tabMenuUrl = data;
    },
    setSizePage:(state,data)=>{
			state.sizePage = data;
		},
		setCurrentPage:(state,data)=>{
			state.currentPage = data;
    },
    setTotal:(state,data)=>{
			state.total = data;
		},
  },
  actions: {

  }
})
