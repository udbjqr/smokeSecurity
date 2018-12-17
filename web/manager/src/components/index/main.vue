<template>
    <div>
        <div id="container"></div>
        <p>该地图禁址了鼠标拖动、鼠标轮滚缩放、双击放大功能。</p>
    </div>
</template>
<script lang="ts">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';


@Component
export default class indexMain extends Vue {
    mounted(){
        this.init();
    } 
    //初始化函数
    init () {
        var map = new qq.maps.Map(document.getElementById("container"),{
            //加载地图经纬度信息
            center: new qq.maps.LatLng(28.65619,115.87683),
            zoom: 13,                       //设置缩放级别
            draggable: true,               //设置是否可以拖拽
            scrollwheel: true,             //设置是否可以滚动
            disableDoubleClickZoom: true    //设置是否可以双击放大
        });
        var center = new qq.maps.LatLng(28.65619,115.87683);
        var marker = new qq.maps.Marker({
            position: center,
            map: map
        });
    }

}
</script>
<style lang="scss" scoped>
#container{
    min-width:600px;
    min-height:1000px;
}
</style>
