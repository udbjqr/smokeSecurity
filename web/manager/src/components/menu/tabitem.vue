<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';

@Component
export default class tabItem extends Vue {
    get activeName (){
        return this.$store.state.tabMenuUrl;
    }
    
    get TabMenu (){
        return this.$store.state.tabMenu;
    }
    handleClick(tab,event){
        this.$store.commit('setTabMenuUrl',tab.name);
        this.$router.push(tab.name)
    }
    renderNavItem(h, item){
        return(
            <el-tab-pane label={item.name} name={item.url}></el-tab-pane>
        )
    }
    render(h:CreateElement){
        let arr:object[] = []
        arr['tab-click'] = this.handleClick
        return (
            <el-tabs value={this.activeName} {...{on:arr}}>
              {this.TabMenu.map(item => this.renderNavItem(h, item))}
            </el-tabs>
        )
    }
}
</script>
