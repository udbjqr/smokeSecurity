import 'package:flutter/material.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';
import 'package:myflutter/common/pluguses/ToastUtils.dart';


class PlaceDetailPage extends StatefulWidget {
  int place_detail_id;
  PlaceDetailPage({Key key, this.place_detail_id}):super(key: key);
  @override
  PlaceDetailPageState createState() => PlaceDetailPageState();
}

class PlaceDetailPageState extends State<PlaceDetailPage> {
  Map forms;
  List<Map<String, dynamic>> arrMap = [{
                        'value':'name',
                        'label':'部署位置'
                      },{
                        'value':'address',
                        'label':'详细地址'
                      },{
                        'value':'note',
                        'label':'备注'
                      },{
                        'value':'administrator',
                        'label':'管理员名称'
                      },{
                        'value':'admin_phone',
                        'label':'手机号'
                      }]; 
  _getPlaceDetailData(){
    NetUtil.post('queryByIdPlace', (data) async {
      setState(() {
          forms = data["place_list"][0];
      });
    }, params: {'id' :widget.place_detail_id}, errorBack: (data,code,message){
      ToastUtils.showShort(data['data']['message']);
    });
  }
  _setDetails(formes){
    List<Widget> bodys = [];
    if(formes != null){
      bodys.addAll(arrMap.map((item) {
        return ListTile(
          title: Text(item['label'], style: TextStyle(fontWeight: FontWeight.w600)),
          trailing:Text(formes[item['value']].toString(), style: TextStyle(fontSize: 17),),
        );
      }).toList());
    }
    return bodys;
  }
  _mainBody(){
    return Container(
      child: ListView(
        children: _setDetails(this.forms),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SmartAppBar(text: "场所详情"),
      body: _mainBody(),
    );
  }
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }
  
  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
  }
  
  @override
  void didUpdateWidget(PlaceDetailPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
    // print(widget.place_detail_id.toString());
    _getPlaceDetailData();
  }
}