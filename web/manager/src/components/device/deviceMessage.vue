<template>
<div>
    <el-tabs v-model="activeName2" type="card" @tab-click="handleClick" class="tab">
        <el-tab-pane label="详细信息" name="first">
            <el-form :model="delist" label-width="80px" class="tab_form">
                <el-form-item label="名称">
                    <el-input v-model="delist.name" disabled></el-input>
                </el-form-item>
                <el-form-item label="msisdn">
                    <el-input v-model="delist.msisdn" disabled></el-input>
                </el-form-item>
                <el-form-item label="使用人">
                    <el-input v-model="delist.user_id" disabled></el-input>
                </el-form-item>
                <el-form-item label="场所">
                    <el-input v-model="delist.place_id" disabled></el-input>
                </el-form-item>
                <el-form-item label="创建时间">
                    <el-input v-model="delist.create_time" disabled></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="delist.note" disabled></el-input>
                </el-form-item>
            </el-form>
        </el-tab-pane>
        <el-tab-pane label="报警记录" name="second">
            <!-- <search 
                :compon.sync="component" 
                :search.sync="search" 
                @handleStaffSearch="handleStaffSearch">
            </search> -->
            <tables
                stripe
                border
                highlight-current-row
                :data="tableData"
                @header-click="sort"
                style="width: 100%;border-radius: 5px;"
                :tableColumn.sync="tableColumn">
            </tables>

            <Foot @ChangeMessage="ChangeMessage"></Foot>
        </el-tab-pane>
        <el-tab-pane label="心跳记录" name="third">暂无数据</el-tab-pane>
    </el-tabs>
    


    </div>
</template>
<script lang="ts">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';
import tables from '@/components/pluguses/tables.vue';
import Foot from '@/components/pluguses/foot.vue';
import search from '@/components/pluguses/search.vue';

@Component({
    components: {
        tables,
        Foot,
        search
    },
})
export default class DeviceIndex extends Vue {
    delist:object = {}
    activeName2:string = 'first'
    setzero:any[] = []
    tableData:Array<object> = []
    total:number = 0
    list:object={
        page_number:1,
        page_count:10
    }
    search:object={
        early_warning_time:[],
        name:'',
        msisdn:''
    }
    component:Array<object>=[{
            type:"input",
            props:{
                clearable:true
            },
            propertys:{
                placeholder:'客户名称',
                key:'name'						
            }
        },{
            type:"button",
            props: {
                type: 'success',
                size: 'small',
                icon:'el-icon-search',
            },
            propertys:{},
            style: {
                marginLeft: '10px'
            },
            on:{
                name:'handleStaffSearch'
            },
            value:'查询'
        }]
    tableColumn:object[]=[{
        label:'id',
        value:'id'
    },{
        label:'名称',
        value:'name',
        render:(h:CreateElement, scope)=>{
            return h('router-link',{props:{to:`DeviceMessage?place=${scope.row['place_id']}&device=${scope.row['id']}`}},scope.row['name'])					
        },
    },{
        label:'备注',
        value:'note'
    },{
        label:'状态',
        value:'flag'
    }]
    handleClick(){

    }
    ChangeMessage(){
        this.$set(this.list,'page_count',this.$store.state.sizePage);
        this.$set(this.list,'page_number',this.$store.state.currentPage);
        this.getQuery();
    }
    getQuery(){
        this.$expactData(this.search,'queryAlarm',this.list,'alarm_list')
    }
    sort(val,event){
        let kk = this.tableColumn.find(it => it.label == val.label).value
        if(kk!=='usage'&& kk!=='ts_package_name'){
            const indexs = this.tableColumn.findIndex(it=>it.label=== val.label)
            const firstI = document.getElementById("kk"+indexs).firstChild;
            const lastI = document.getElementById("kk"+indexs).lastChild;
            this.$sort(indexs,firstI,lastI,this,kk)
            this.getQuery();
        }
    }
    handleStaffSearch(val) {
        this.search = val
        this.$store.commit('setCurrentPage', 1);
        this.$set(this.list,'page_number',1);
        this.getQuery();
    }
    created(){
        if(!(this.$route.query.place && this.$route.query.device)) {
            this.$router.push('/office/Device');
            this.$message.error('请重新选择设备！')
        }
        this.search.device_id = parseInt(this.$route.query.device)
        this.$request('queryByDeviceId',{
            id:parseInt(this.$route.query.device),
            place_id:parseInt(this.$route.query.place)
        },data=>{
            this.delist = data.device_list[0]
        })
    }
}
</script>
<style lang="scss" scoped>
.tab{
    margin-top: 20px;
}
.tab_form{
    width: 60%;margin:50px 0 0 5%;
}
</style>
