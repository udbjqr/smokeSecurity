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

class addPlacePage extends StatefulWidget {
  @override
  addPlacePageState createState() => new addPlacePageState();
}

class addPlacePageState extends State<addPlacePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SmartAppBar(text: "增加场所"),
      body: ListView(
        children: <Widget>[
          Expanded(
            child: AndroidView(
              viewType: "MapView",
            ),
          )
        ],
      )
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