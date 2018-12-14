<template>
    <div class="block" :style="{left:$store.state.footleft}">
        <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChanges"
        :current-page="$store.state.currentPage"
        :page-sizes="PageSizes"
        :page-size="PageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="$store.state.total">
        </el-pagination>
    </div>
</template>
<script>
  export default {
    data() {
      return {
          PageSizes:[10,20,50,100,500],
          PageSize:10,
          CurrentPage:1,
      }
    },
    methods: {
        handleSizeChange(val) {
            this.$store.commit('setSizePage', val);
            this.$store.commit('setCurrentPage', this.CurrentPage);
            this.ChangeMessage();
        },
        handleCurrentChanges(val) {
             this.$store.commit('setCurrentPage', val);
             this.ChangeMessage();
        },
        ChangeMessage(){
            this.$emit('ChangeMessage')
        }
    },
    created(){
        this.$store.commit('setSizePage', this.PageSize);
        this.$store.commit('setCurrentPage', this.CurrentPage);
    },
    mounted: function () {
        this.$nextTick(function () {
            let search = document.getElementsByClassName('search-border')[0]
            if(search){
                let number = document.documentElement.clientHeight-search.offsetHeight-630
                let val = number > 45 ? parseInt((number / 45).toFixed(0)) :0
                if(val!=0){
                    this.PageSize = val+9
                    this.PageSizes=[...new Set([val+9,...this.PageSizes])].sort((a, b) => a - b); 
                    this.$store.commit('setSizePage', this.PageSize); 
                }
            }
            this.ChangeMessage();
        })
    }
  }


</script>
          

        