<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';


@Component
export default class search extends Vue {
    inheritAttrs: false
    chan:any = this.search
    @Prop() private search!: Object|Array<any>;
    @Prop() private compon!: any[];
    setValue(it){
        for (let i in this.chan){
            if(i === it.propertys.key) it.props.value = this.chan[i];
        }
    }
    renderComponent(h,arr){
        let that = this
        return arr.map(it => {
            if(it.propertys.key)this.setValue(it);
            if(it.chilren!=undefined){
            switch (it.type) {
              case 'select':
                it.props.value = that.chan[it.propertys.key]
                let option = it.chilren.map(item => { 
                    return <el-option key={item.value} label={item.label} value={item.value}></el-option>  
                }) 
                return (
                  <el-select {...{
                      props:it.props,
                      style:it.style,
                      on:{
                        change:(event) => { 
                          that.chan[it.propertys.key] = event;
                        }
                      }
                    }
                  }>{option}</el-select>
                )
                break;            
              default:
                // return h(it.name,{
                //   domProps:it.props,
                //   attrs:it.propertys
                // },this.setEle(h,it.chilren))
                break;
            }
            
          }else{
            switch (it.type) {
              case 'input':
                // if(it.props)console.log(it.props);
                return (
                    <el-input 
                        {...{props:it.props,
                        attrs:it.propertys,
                        on:{input:s => that.chan[it.propertys.key] = s}
                        }}
                        nativeOnKeyup={s=>{
                            if (s.keyCode === 13 && it.native && it.native.keyup) that.$emit(it.native.keyup,that.chan);
                        }}
                    ></el-input>
                )
                break;
              case 'span':
                return(<span style={it.style}>{it.value}</span>)
                break;
              case 'datePicker':
                return h('el-date-picker',{
                  props:it.props,
                  attrs:it.propertys,
                  style:it.style,
                   on:{
                      input(event){                      
                        that.chan[it.propertys.key] = event;
                      }
                    }
                })
                break;
                case 'button':
                    return(<el-button 
                    {...{
                        props:it.props,
                        style:it.style,
                        on:{click:() => this.$emit(it.on.name,that.chan)}
                    }}>
                    {it.value}
                    </el-button>)
                    break;
              default:
                break;
            }           
          }  
        })
    }

    render(h:CreateElement){
      return (
        <div class="search-border">{this.renderComponent(h,this.compon)}</div>
      )
    }

}
</script>