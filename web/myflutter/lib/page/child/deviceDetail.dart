import 'package:flutter/material.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';
import 'package:myflutter/common/pluguses/ToastUtils.dart';

class DeviceDetailPage extends StatefulWidget {
  Map<String, dynamic> forms;
  DeviceDetailPage({
    Key key,
    this.forms
  }) : super(key: key);
  @override
  DeviceDetailPageState createState() => DeviceDetailPageState();
}


class DeviceDetailPageState extends State<DeviceDetailPage> {
  Map pageDeviceList;
  List<Map<String, dynamic>> arrMap = [{
                        'value':'name',
                        'label':'设备名称'
                      },{
                        'value':'msisdn',
                        'label':'msisdn'
                      },{
                        'value':'flag',
                        'label':'状态'
                      },{
                        'value':'user_id',
                        'label':'创建人'
                      },{
                        'value':'create_time',
                        'label':'创建时间'
                      },{
                        'value':'place_id',
                        'label':'所属场所'
                      },{
                        'value':'note',
                        'label':'备注'
                      },{
                        'value':'last_connect_time',
                        'label':'最近连接时间'
                      }]; 
  _setDeviceData(){
    NetUtil.post('queryByDeviceId', (data) async {
      setState(() {
        pageDeviceList = data["device_list"][0];
      });
    }, params: widget.forms, errorBack: (data,code,message){
      ToastUtils.showShort("加载错误");
    });
  }
  _setDetails(formes){
    List<Widget> bodys = [];
    if(formes != null){
      bodys.addAll(arrMap.map((item) {
        return ListTile(
          leading: Container(
            width: 100,
            child: Text(item['label'], style: TextStyle(fontWeight: FontWeight.w600, fontSize: 16)),
          ),
          title:Text(formes[item['value']].toString(), style: TextStyle(fontSize: 17),),
        );
      }).toList());
    }
    return bodys;
  }
  _mainBody(){
    return Container(
      child: ListView(
        children: _setDetails(pageDeviceList),
      ),
    );
  }
  @override
  Widget build(BuildContext context) {
    // if(widget.forms=={}){
    //   Navigator.pop(context);
    // }
    return Scaffold(
      appBar: SmartAppBar(text: "设备详情"),
      body: _mainBody(),
    );
  }
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _setDeviceData();
  }
  
  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
  }
  
  @override
  void didUpdateWidget(DeviceDetailPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }
}