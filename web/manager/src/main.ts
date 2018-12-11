import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI)



Vue.config.productionTip = false

const _op:any = Vue.prototype
let serviceRoot:String = "";

Vue.prototype.$request = (url:String, data:Object, ok:any, failing:any, handleError:any):void => {

	axios({
		method: 'post',
		url: `${url}`,
		data: data,
		timeout: 0
	}).then(res => {
		res.data = JSON.parse(JSON.stringify(res.data))
		if (res.data.code === 0) {
			ok(res.data.data);
		} else {
			switch (res.data.code) {
				case 9:
					store.commit('setUserInfo', '');
					_op.$Message.error(res.data.message);
					_op.$router.push('/')
					break;
				case 7:
					_op.$Message.info("无操作此功能的权限")
					break;
				case -1:
					_op.$Message.info("未知错误，请联系管理员")
					break;
				default:
					break;
			}

			if (failing)
				failing(res.data, res.data.code, res.data.message);
		}
	}).catch(error => {
		console.log("error throw", error);
		if (handleError) 
			handleError(error)
		
	})
};

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
