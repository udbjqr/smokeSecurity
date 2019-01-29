import 'package:flutter/material.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/pluguses/ToastUtils.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_easyrefresh/taurus_header.dart';
import 'package:flutter_easyrefresh/taurus_footer.dart';
import 'package:myflutter/common/router/NavigatorRouter.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:myflutter/common/style/MainStyle.dart';
import 'dart:async';
import 'dart:ui';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'dart:convert';



typedef void MapViewCreatedCallback(MapViewController controller);

class addPlacePage extends StatefulWidget {
  const addPlacePage({
    Key key,
    this.onMapViewCreated,
  }) : super(key: key);

  final MapViewCreatedCallback onMapViewCreated;

  @override
  addPlacePageState createState() => new addPlacePageState();
}

class addPlacePageState extends State<addPlacePage> {
  dynamic _callBacl;
  Store<mainRedux> store;
  Map<String, dynamic> forms = {
    "note" :'',
    "addresss" :'',
    "name":''
  };  

  var names = TextEditingController();
  var address = TextEditingController();
  var notes = TextEditingController();

  _calba(int i) async {
    // if (widget.onMapViewCreated == null) {
    //   return;
    // }
    MethodChannel _channel = MethodChannel('MapView_$i');
    final dynamic trace = await _channel.invokeMethod('HandleChange');
    _callBacl = json.decode(trace);
    setState(() {
            this.forms["region_id"] = int.parse(_callBacl["abcode"]) ;
            this.forms["address"] = _callBacl["address"];
            this.forms["xaxis"] = _callBacl["lat"];
            this.forms["yaxis"] = _callBacl["lon"];
            address.text = _callBacl["address"];
        });
    
    if(!_callBacl["is_succ"]){
      print("定位未开启");
    }
    return;
    // widget.onMapViewCreated(new MapViewController._(i));
  }
  _quren(String names, String address, String notes){
    if(names.isNotEmpty && address.isNotEmpty && notes.isNotEmpty){
      forms['name'] = names;
      forms['address'] = address;
      forms['note'] = notes;
      NetUtil.post('addPlace',(data) async{
        Navigator.pop(context, 'addPlaces');
      },params: forms,errorBack: (data,code,message){
        ToastUtils.showShort(data["data"]["message"].toString());
      });
    }else{
      ToastUtils.showShort('请完善表单');
    }
  }
  _textWidget(String label,{TextEditingController textvalue, bool is_text = false, String hintText}){
    return ListTile(
      leading: Container(
        width: 80,
        child: Text(label, style: TextStyle(fontSize: 16),),
      ),
      title: is_text ? 
          TextField(
            controller: textvalue,
            decoration: InputDecoration(
              hintText: hintText,
            ),
          ) : Text(hintText),
      trailing: is_text ? 
          IconButton(
            icon: Icon(Icons.highlight_off, color: Colors.grey,size: 24,),
            onPressed: () {
              textvalue.clear();
            },
          ):null
    );
  }
  _mainBody(){
    return ListView(
      children: <Widget>[
        _textWidget("部署位置", textvalue: names, is_text: true, hintText: "请输入部署位置"),
        _textWidget("详细地址", textvalue: address, is_text: true, hintText: "请输入详细地址"),
        _textWidget("备注", textvalue: notes, is_text: true, hintText: "请输入备注"),
        _textWidget("管理员名称", hintText: store.state.userinfo.name.toString()),
        _textWidget("手机号", hintText: store.state.userinfo.telephone.toString()),
        Container(
          padding: EdgeInsets.fromLTRB(30, 0, 30, 0),
          child: RaisedButton(
            onPressed: (){
              _quren(names.text, address.text, notes.text);
            },
            textColor: Colors.blue,
            color: Colors.lightBlueAccent,
            child: Text("增加场所", style: TextStyle(color: Colors.white, fontSize: 20, letterSpacing: 3, fontWeight: FontWeight.w600)),
          ),
        )
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    setState(() {
      store = StoreProvider.of(context);
      this.forms["user_id"] = store.state.userinfo.id;
      this.forms["administrator"] = store.state.userinfo.name;
      this.forms["admin_phone"] = store.state.userinfo.telephone;
    });
    // print( store.state.userinfo.);
    return Scaffold(
      appBar: SmartAppBar(text: "增加场所"),
      body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Expanded(
              child: AndroidView(
                viewType: 'MapView', 
                onPlatformViewCreated: _calba), 
              flex: 2),
            Expanded(child: _mainBody(), flex: 3)
          ],
        ),
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
  void didUpdateWidget(addPlacePage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }
}

class MapViewController{
  MapViewController._(int id)
      : _channel = MethodChannel('MapView_$id');

  final MethodChannel _channel;
  Future<dynamic> setText({String text = "111"}) async {
    var trace =  _channel.invokeMethod('setText', text);
    return trace;
  }
}