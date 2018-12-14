import Vue,{ CreateElement } from 'vue';
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

Vue.prototype.$expactData = function(){
	const arr = Array.prototype.slice.call(arguments)
	let obj:any[] = []
	for(let key in arr[0])
		if(typeof arr[0][key]=="number"||(arr[0][key]!=""&&arr[0][key]!=null))
			switch (key) {
				case 'name':case 'msisdn':case 'iccid':case 'order_number':case 'content':case 'customer_name':case 'custom_name':case 'consignee_name':case 'address':
					obj.push({name:key,operator:'like',value:arr[0][key]})
					break;
				case 'agent_id':
					obj.push({name:'custom_id',operator:'=',value:arr[0][key]})	
					break;
				case 'create_time':case 'activating_time':case 'op_time':case 'createtime':case 'early_warning_time':case 'start_useing_time':case 'expire_time':case "payment_time":
					if(arr[0][key][0]!=undefined&&arr[0][key][0]!=''&&arr[0][key][1]!=undefined&&arr[0][key][1]!=''){
						obj.push({name:key,operator:'between',minvalue:arr[0][key][0],maxvalue:arr[0][key][1]+" 23:59:59"})		
					}else if(arr[0][key][0]!=undefined&&arr[0][key][0]!=''){
						obj.push({name:key,operator:'>=',value:arr[0][key][0]})
					}else if(arr[0][key][1]!=undefined&&arr[0][key][1]!=''){
						obj.push({name:key,operator:'<=',value:arr[0][key][1]})
					}	
					break;
				case 'more':case "monthly_already_usage":case 'usage':
					if(arr[0][key][0]!=undefined&&arr[0][key][0]!=''&&arr[0][key][1]!=undefined&&arr[0][key][1]!=''){
						obj.push({name:key,operator:'between',minvalue:arr[0][key][0],maxvalue:arr[0][key][1]})	
					}else if(arr[0][key][0]!=undefined&&arr[0][key][0]!=''){
						obj.push({name:key,operator:'>=',value:arr[0][key][0]})
					}else if(arr[0][key][1]!=undefined&&arr[0][key][1]!=''){
						obj.push({name:key,operator:'<=',value:arr[0][key][1]})
					}					
					break;
				default:
					obj.push({name:key,operator:'=',value:arr[0][key]})
					break;
			}
					
	
	/*
		arr0:search 
		arr1:url 
		arr2:data 
		arr3:result 
	*/

	if(arr[3]!=undefined){
		const list = arr[2]
		list.condition = obj
		this.$request(
			arr[1],
			list,
			data => {
				if(arr[4]!=undefined){
					this.AgentCustomPackages.tableData=data[arr[3]];
				}else{
					this.tableData=data[arr[3]];
				}
				
				this.$store.commit('setTotal', data.count);
		})
	}else{
		this.$confirm('此操作将导出xls, 是否继续?', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'success'
		  }).then(() => {
			_op.$request(arr[1],{
				_type:arr[2],
				condition:obj
			},data=>{
				// console.log("http://"+window.location.host+this.$store.state.RootUrl+data.file_name)
				window.location.href = "http://"+window.location.host+this.$store.state.RootUrl+data.file_name;
			})
		  }).catch(() => {
			this.$message({
			  type: 'info',
			  message: '已取消导出'
			});          
		  });		
	}	
}



Vue.prototype.$sort = (indexs:any,firstI:any,lastI:any,_this:any,kk:any) => {
	const zeroIndex = _this.setzero.findIndex(it => it.name === kk) 
	if(zeroIndex !== -1){
		if(_this.setzero[zeroIndex].sort === 'asc'){
			_this.setzero.splice(zeroIndex, 1)
			firstI.style.borderBottomColor="#fbfbfb"
			lastI.style.borderTopColor="#FF5722"
			_this.setzero.push({name:kk,sort:'desc',value:indexs})  
		}else{
			_this.setzero.splice(zeroIndex, 1)
			lastI.style.borderTopColor="#fbfbfb"
			document.getElementById("ff"+indexs).innerHTML=""; 
			document.getElementById("kk"+indexs).style="display:none"
		}
	}else{
		document.getElementById("kk"+indexs).style="display:inline-flex"
		_this.setzero.push({name:kk,sort:'asc',value:indexs})
		firstI.style.borderBottomColor="#FF5722"   
	}

	if(_this.setzero.length!==0){
		_this.$set(_this.list,"sorting",_this.setzero.map((it,index)=>{
			document.getElementById("ff"+it.value).innerHTML=index+1; 
			return it.name+" "+it.sort
		}));
	}else{
		delete _this.list.sorting
	}     
	_this.$store.commit('setCurrentPage', 1);
	_this.$set(_this.list,'page_number',1);
}

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
