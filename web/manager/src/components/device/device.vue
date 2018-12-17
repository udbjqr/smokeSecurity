<template>
<div>
    <search 
        :compon.sync="component" 
        :search.sync="search" 
        @handleStaffSearch="handleStaffSearch">
    </search>
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
    place:object={
        form:{},
        disabled:true,
    }
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
        value:'name'
    },{
        label:'备注',
        value:'note'
    },{
        label:'状态',
        value:'flag'
    }]
    ChangeMessage(){
        this.$set(this.list,'page_count',this.$store.state.sizePage);
        this.$set(this.list,'page_number',this.$store.state.currentPage);
        this.getQuery();
    }
    getQuery(){
        this.$expactData(this.search,'queryDeviceList',this.list,'device_list')
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
  
}
</script>