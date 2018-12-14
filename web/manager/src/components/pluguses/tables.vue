<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';
import hh from '@/loshed.tsx';


@Component
export default class tables extends Vue {
    inheritAttrs: false
    @Prop() private tableColumn!: Array<any>;
    @Prop() private select!: boolean;
    handleTableRow(val){ 
        this.$refs.CardTable.toggleRowSelection(val);
    }
    renderTableColumn(h, item) {
        item.prop = item.value || item.key || '';
        item.label =  item.label;
        const props = {
          props: item,
        };
        const { render } = item;
        const slotScope = {
          scopedSlots: {
            default:scope => {
              return typeof render === 'function' ? render(h, scope)
                : scope.row[item.prop];
            },
          },
        };
        return (
          <el-table-column
            show-overflow-tooltip
            render-header={this.$header}
            {...props}
            {...slotScope}>
          </el-table-column>
        );
      }
      renderTable(h) {
        const props = {props: this.$attrs};        
        if(this.select) this.$listeners['row-click'] = this.handleTableRow;
        const on = {on: this.$listeners};
        const { body } = this.$slots;
        if (body) return body.map(item => item);
        return (
          <el-table
            ref="CardTable"
            {...props}
            {...on}>
            {this.select && <el-table-column type="selection" width="55"></el-table-column>}
            {this.tableColumn.map(item => this.renderTableColumn(h, item))}
          </el-table>
        );
    }
    
    render(h:CreateElement){
      return (
        <div class="table-border">
          {this.renderTable(h)}
        </div>
      )
    }

}
</script>