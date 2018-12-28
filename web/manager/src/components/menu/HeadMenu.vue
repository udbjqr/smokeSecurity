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
                <div class="headTop">
                    <div class="headTop_left">展示平台</div>

                    <el-menu class="el-menu-demo" 
                        mode="horizontal"
                        background-color="#2d5db0"//蓝色
                        text-color="#fff"//白色
                        active-text-color="#fff"//黄色(选中颜色)
                        ref="navbar"
                        default-active="1"//当前激活的索引(下边框位置，从1开始)
                        select={this.selectMenu}//菜单激活回调	index 选中菜单项的 index, indexPath: 选中菜单项的 index path
                    >
                    {this.navList.map((it,index) => this.renderNavItem(h, it, index))}
                    </el-menu>

                    <div class="headTop_right" onClick={this.loginOuts}><i class="el-icon-remove-outline"></i> 注销</div>
                </div>
                
                {this.$store.state.tabMenuFlag && <tab-item />}
            </div>
                )
    }
    created() {
       this.havemenu()
    }
    navList:Array<any> = []
    loginOuts(){
        this.$mess('是否确定退出','注销',this.loginOut)
    }
    loginOut(){ 
        this.$request('loginOut','',data=>{
            this.$message({
                message:'成功注销',
                type:'success'
            })
            this.$router.push('/')
            location.reload()
        },(data,code,message)=>{

        })
    }
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
