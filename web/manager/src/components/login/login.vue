<template>
    <div class="login">
        <div class="br"> <br></div>
        <div class="login_main">
            <div class="login_main_title">
                <h1>{{title}}</h1> 
            </div>
            <div class="login_main_body" v-show="activeIndex == 1">
                <el-form :model="form" :rules="rules" ref="ruleForm" label-width="10%" >
                    <el-form-item  prop="login_name">
                        <el-input v-model="form.login_name" placeholder="请输入用户名">
                            <i slot="prefix" class="el-input__icon el-icon-tickets"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="form.password" placeholder="请输入密码" type="password">
                            <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="captcha">
                        <el-col :span="12">
                        <el-input clearable placeholder="验证码" maxlength="4" v-model="form.captcha"></el-input>
                        </el-col>
                        <el-col :span="2">&nbsp;</el-col>
                        <el-col :span="10">
                        <img :src="captchahttp" @click="loadcaptcha" alt="" class="VerificationCode"> 
                        </el-col>
                    </el-form-item>
                    <el-form-item class="main_left">
                        <el-button type="primary" @click="submitForm('ruleForm')">Sign in</el-button>
                    </el-form-item>
                    <div class="login_footer">
                        <div @click="activeIndex=3;title = 'Retrieve From'" class="login_footer_left">忘记了密码？</div>
                        <div @click="activeIndex=2;title = 'Registe From'" class="login_footer_right">注册</div>
                    </div>
                </el-form>
            </div>
            <div class="login_main_body" v-show="activeIndex == 2">
                <el-form :model="registe_form" :rules="registe_rules" ref="registeForm" label-width="10%" >
                    <el-form-item  prop="name">
                        <el-input v-model="registe_form.name" placeholder="请输入用户名">
                            <i slot="prefix" class="el-input__icon el-icon-tickets"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item  prop="login_name">
                        <el-input v-model="registe_form.login_name" placeholder="请输入登录名">
                            <i slot="prefix" class="el-input__icon el-icon-tickets"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="passwd">
                        <el-input v-model="registe_form.passwd" placeholder="请输入密码" type="password">
                            <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="telephone">
                        <el-input v-model="registe_form.telephone" placeholder="请输入电话" type="password">
                            <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item class="main_left">
                        <el-button type="primary" @click="submitRegiste('registeForm')">Registe</el-button>
                    </el-form-item>
                    <div class="login_footer">
                        <div @click="activeIndex=1;title = 'Login From'" class="login_footer_left">返回 ↩</div>
                    </div>
                </el-form>
            </div>
            <div class="login_main_body" v-show="activeIndex == 3">
                <el-form :model="forget_form" :rules="forget_rules" ref="forgetForm" label-width="10%" >
                    <el-form-item  prop="login_name">
                        <el-input v-model="forget_form.telephone" placeholder="请输入电话">
                            <i slot="prefix" class="el-input__icon el-icon-tickets"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="passwd">
                        <el-input v-model="forget_form.passwd" placeholder="请输入新密码" type="password">
                            <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="telephone">
                        <el-input class="left_input" v-model="forget_form.verificationCode" placeholder="请输入验证码" type="password">
                            <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                        </el-input>
                        <el-button class="right_input" :disabled="captcha_bool" @click="captcha">{{Buttons}}</el-button>
                    </el-form-item>
                    <el-form-item class="main_left">
                        <el-button type="primary" @click="submitForget('forgetForm')">确认</el-button>
                    </el-form-item>
                    <div class="login_footer">
                        <div @click="activeIndex=1;title = 'Login From'" class="login_footer_left">返回 ↩</div>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue,{ CreateElement } from 'vue';
import { Component } from 'vue-property-decorator';

@Component
export default class login extends Vue {
    title:string = "Login Form"
    captcha_bool:boolean = false
    Interval:number = 60
    get Buttons(){
        if(this.captcha_bool){
            return `(${this.Interval})重新发送`
        }else{
            return "点击发送"
        }
    }
    captchahttp:string = ''
    form:Object = {
        login_name:'',
        password:'',
        captcha:''
    }
    rules:Object =  {
        login_name: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        captcha: [
            { required: true, message: '请输入验证码', trigger: 'blur' }
        ],
    }
    registe_form:Object = {
        login_name:'',
        passwd:'',
        name:'',
        telephone:''
    }
    registe_rules:Object =  {
        login_name: [
            { required: true, message: '请输入登录名', trigger: 'blur' }
        ],
        name: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        passwd: [
            { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        telephone: [
            { required: true, message: '请输入电话', trigger: 'blur' }
        ],
    }
    forget_form:Object = {
        passwd:'',
        verificationCode:'',
        telephone:''
    }
    forget_rules:Object =  {
        verificationCode: [
            { required: true, message: '请输入验证码', trigger: 'blur' }
        ],
        passwd: [
            { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        telephone: [
            { required: true, message: '请输入电话', trigger: 'blur' }
        ],
    }
    activeIndex:number = 1
    submitForm(formName){
        this.$refs[formName].validate(valid => {
            if (valid) {
                let rese = {...this.form,isFitst : true}
                this.$request('login',rese,data => {
                    this.$store.commit('setUserInfo', data.user);
                    this.$router.push('/')
                },(data,code,message)=>{
                    this.$message.error(data.message)
                })
            }else{  
                return false;
            }
        })
    }
    loadcaptcha(){
        this.$request(
            'getImage',
            '',
            data => {
                this.captchahttp = data.img_url;
                this.form.hashCode = data.hashCode.toString()
            }
        )
    }
    submitRegiste(formName){
        this.$refs[formName].validate(valid => {
            if (valid) {
                this.$request('regist',this.registe_form,data => {
                    // this.setCookie('token',data.token);
                    this.$store.commit('setUserInfo', data.user);
                    this.$router.push('/')
                },(data,code,message)=>{
                    this.$message.error(data.message)
                })
            }else{  
                return false;
            }
        })
    } 
    submitForget(formName){
        this.$refs[formName].validate(valid => {
            if (valid) {
                this.$request('retrieve',this.forget_form,data => {
                    this.$store.commit('setUserInfo', data.user);
                    this.$router.push('/')
                },(data,code,message)=>{
                    this.$message.error(data.message)
                })
            }else{  
                return false;
            }
        })
    } 
    captcha(){
        let times =  new Date().getTime() + 60 * 1000
        this.setCookie('captcha', times, new Date().setTime(new Date().getTime() + 60 * 1000))
        this.captcha_bool = true
        this.setTime(times)
    }
    getCookie(name){
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }
    setCookie(c_name, value, expire) {
        document.cookie = `${c_name} = ${value};expires= ${expire}`
    }
    created() {
        this.loadcaptcha()
        const cookie = this.getCookie('captcha')
        if(cookie !== null && parseInt(cookie) > new Date().getTime()){
            this.setTime(cookie)
            this.captcha_bool = true
        }
    }
    setTime(val){
        let times = val - new Date().getTime()
        this.Interval =  parseInt(times / 1000)
        let myTime = setInterval(()=>this.Interval--,1000)
        setTimeout(() => {
            this.captcha_bool = false
            clearInterval(myTime);
        }, times);
    }
}
</script>
<style scoped lang="scss">
.left_input{
    width: 50%;float: left;
}
.right_input{
    float:right;
}
.login_footer{
    border-top:1px solid #eee;overflow: hidden;font-size:14px;color:#0070d2;width: 90%;margin:10% 0 0 10%;
    text-align:left;padding: 24px 0;
    &_left{
        float: left;width: 40%;cursor: pointer;
    }
    &_right{
        float: right;width: 40%;text-align:right;cursor: pointer;
    }
}
.main{
    width:100%;height: 100%;
}
h1{
    padding: 0;margin: 0;
}
.main_left{
    text-align: left;
}
.login{
    width:100%;height: 100%; background: radial-gradient( rgb(223, 243, 240) 15% , rgb(19, 230, 201) 85% );
    &_main{
      width: 30%;
      background: rgba(255,255,255,0.6) ;
      margin: 200px auto 0;
      &_title{
          background: rgb(19, 230, 201);padding: 2em 0;position: relative;
          h1{
            color: #fff;
            font-size: 1.5em;
            text-align: left;
            margin: 0 23px;
          }
      } 
      &_body{
          width: 90%;padding: 10% 0 5% ;
      }
    } 
}
</style>