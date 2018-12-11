<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import Component from 'vue-class-component';
import HeadMenu from '@/components/menu/HeadMenu.vue'; // @ is an alias to /src
import login from '@/components/login/login.vue';
@Component({
  components: {
    HeadMenu,
    login
  },
})
export default class app extends Vue {
  get hasUserInfo(){
    return this.$store.state.userInfo;
  }
  render(h:CreateElement){
    return (
      <div id="app">
        {
          this.hasUserInfo ? <div>
            <HeadMenu></HeadMenu>
            <router-view/>
          </div> 
          : <login />
        }
        
      </div>
    )
  }
  created() {
       this.$request('/getResToken','',data=>{
         
       })
    }
}
</script>
>

<style lang="scss">
html{
  width:100%;height: 100%; 
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
