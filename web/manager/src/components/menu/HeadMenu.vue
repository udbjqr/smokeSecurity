<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import { Component } from 'vue-property-decorator';
import navItem from "@/components/menu/navitem";
import tabItem from "@/components/menu/tabitem";

@Component({
  components: {
    navItem,
    tabItem
  },
})
export default class headmenu extends Vue {
    render(h:CreateElement){
        return (
            <div>
                <el-menu  class="el-menu-demo" 
                    mode="horizontal" 
                    background-color="#545c64"
                    text-color="#fff"
                    active-text-color="#ffd04b"
                    ref="navbar"
                    default-active="1"
                    select={this.selectMenu}>
                    {this.navList.map((it,index) => this.renderNavItem(h, it, index))}
                </el-menu>
                {this.$store.state.tabMenuFlag && <tab-item />}
            </div>
                )
    }
    created() {
       this.havemenu()
    }
    navList:Array<any> = []
    havemenu(){
        this.$request('getMenu','',data=>{
            this.navList = data.menu.menu
            this.$store.commit('setMenu', this.navList);
        },(data,code,message)=>{

        })
    }
    selectMenu(index, indexPath){
            /**
             * 在选择父级菜单时自动关闭其下所有子菜单
             * 选择时获取点击菜单的父级index，并计算得到该index在已打开菜单中的索引值，
             * 关闭位于当前打开菜单中该索引值之后的全部菜单
             */
            // 获取当前打开的所有菜单
            let openMenu = this.$refs.navbar.openedMenus.concat([])
            // 获取点击菜单的父级index，如果当前点击的是根节点，则直接关闭所有打开菜单
            let nowMenuPath = indexPath.length > 1 ? indexPath[indexPath.length-2] : ""
            if(nowMenuPath){
                // 获取父级index在数组中索引，关闭其后所有的菜单
                let menuIndex = openMenu.indexOf(nowMenuPath)
                openMenu = openMenu.slice(menuIndex+1)
            }
            openMenu = openMenu.reverse()
            openMenu.forEach((ele) => {
                this.$refs.navbar.closeMenu(ele)
            })
        }
    renderNavItem(h,it,index){
        return (
            <nav-item navIndex={String(index)} item={it} key={index}></nav-item>
        )
    }
}
</script>
