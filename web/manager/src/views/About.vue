<script lang="tsx">
import Vue,{ CreateElement } from 'vue';
import { Component } from 'vue-property-decorator';

@Component
export default class about extends Vue {
  render(h:CreateElement){
    return (<div>123</div>)
  }
}
</script>
