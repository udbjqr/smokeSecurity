<template>
    <div>
        <div id="container" :style="{height:bodyHeight}"></div>
    </div>
</template>
<script lang="ts">
import Vue,{ CreateElement } from 'vue';
import { Component ,Prop} from 'vue-property-decorator';


@Component
export default class indexMain extends Vue {
    arr:object[] = []
    list:object={
        page_number:1,
        page_count:100
    }
    bodyHeight:string = ''
    mounted(){
        this.bodyHeight = (document.documentElement.clientHeight-115) + 'px'
        this.queryPlace()
    } 
     queryPlace(){
         this.$request('queryPlace',this.list,data => {
            this.arr = data.place_list
            this.init()
        })
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
        var info = new qq.maps.InfoWindow({ map: map }); 
        this.arr.map(data=>{
            var marker = new qq.maps.Marker({ position: new qq.maps.LatLng(data.xaxis, data.yaxis), map: map });    //创建标记
            //***将必要的数据存入每一个对应的marker对象
            marker.id = data.id;
            marker.name = data.admin_phone;
            marker.locate = data.address;
            qq.maps.event.addListener(marker, 'click', function() {    //获取标记的点击事件
                info.open();  //点击标记打开提示窗
                info.setContent('<div class="mapInfo"><p class="center">'+this.name+'</p><p>'+this.locate+'</p></div>');  //***设置提示窗内容（这里用到了marker对象中保存的数据）
                info.setPosition(new qq.maps.LatLng(this.position.lat, this.position.lng));  //提示窗位置
            });
        })
    }

}
</script>
<style lang="scss" scoped>
#container{
    min-width:600px;
}
</style>
