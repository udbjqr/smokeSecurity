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
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SmartAppBar(text: "增加场所"),
      body: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Expanded(
              child: new AndroidView(
                viewType: 'MapView', 
                onPlatformViewCreated: _calba), 
              flex: 2),
            Expanded(child: Text("data"), flex: 3)
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
  _calba(int i) async {
    // if (widget.onMapViewCreated == null) {
    //   return;
    // }
    MethodChannel _channel = MethodChannel('MapView_$i');
    final dynamic trace = await _channel.invokeMethod('HandleChange');
    _callBacl = json.decode(trace);
    if(!_callBacl["is_succ"]){
      print("定位未开启");
    }
    return;
    // widget.onMapViewCreated(new MapViewController._(i));
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