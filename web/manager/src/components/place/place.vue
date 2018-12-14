<template>
<div>
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

@Component({
    components: {
        tables,
        Foot
    },
})
export default class placeIndex extends Vue {
    setzero:any[] = []
    tableData:Array<any> = []
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
    tableColumn:Array<any>=[{
        label:'id',
        value:'id'
    },{
        label:'name',
        value:'name'
    }]
    ChangeMessage(){
        this.$set(this.list,'page_count',this.$store.state.sizePage);
        this.$set(this.list,'page_number',this.$store.state.currentPage);
        this.getQuery();
    }
    getQuery(){
        this.$expactData(this.search,'queryPlace',this.list,'place_list')
    }
    sort(val,event){
        console.log(val)
        let kk = this.tableColumn.find(it => it.label == val.label).value
        if(kk!=='usage'&& kk!=='ts_package_name'){
            const indexs = this.tableColumn.findIndex(it=>it.label=== val.label)
            const firstI = document.getElementById("kk"+indexs).firstChild;
            const lastI = document.getElementById("kk"+indexs).lastChild;
            this.$sort(indexs,firstI,lastI,this,kk)
            this.getQuery();
        }
    }
  
}
</script>