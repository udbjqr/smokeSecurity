<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';

@Component({
  components: {
    navItem,
  },
})
export default class navItem extends Vue {
    name:"navItem"
    @Prop() private item!: any;
    @Prop() private navIndex!: number | string;
    render(h:CreateElement){
      if(this.item.children && this.item.children.length){
        return (
            <el-submenu index={this.navIndex}>
              <template slot="title" class="item-fa">
                  <i class={this.item.icon}></i>
                  <span slot="title">{ this.item.name }</span>
              </template>
              {this.item.children.map((it,index) => this.renderNavItem(h, it, index))}
            </el-submenu>
            )
      }else{
        return(
          <el-menu-item class="lili" index={this.navIndex} onClick={() => this.routerUrl(this.item.url)}>
              <i class={this.item.icon}></i>
              <span slot="title">{ this.item.name }</span>
          </el-menu-item>
        )
      } 
    }
    renderNavItem(h,it,index){
        return (
            <navItem navIndex={String(index)} item={it} key={index}></navItem>
        )
    }
    routerUrl(url){
      for(let ele of this.$store.state.Menu){
        if(ele.children && ele.children.length){
          for(let i of ele.children){
             if(i.url == url) {
               this.$store.commit('settabMenu', ele.children);
               this.$store.commit('setTabMenuUrl',url);
               this.$router.push(url)
               this.$store.commit('settabMenuFlag', true);
               return false
             }
          }
        }else if(url=="/"){
          this.$router.push(url)
        }
      }
      
    }
}
</script>
