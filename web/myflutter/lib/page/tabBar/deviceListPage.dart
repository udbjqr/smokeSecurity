import 'package:flutter/material.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/pluguses/ToastUtils.dart';

class DeviceListPage extends StatefulWidget {
  @override
  DeviceListPageState createState() => new DeviceListPageState();
}

class DeviceListPageState extends State<DeviceListPage> {
  // 每次获取的数据
  int pagePlaceCount = 1000;
  // 页码
  int pagePlaceMain = 1;
  double _drawerHeaderHeight = 80;
  int deviceNumber = 0;
  Store<mainRedux> store;
  List<Object> pagePlaceList = [];
  void _handleDrawerButtons() {
    Scaffold.of(context).openDrawer();
  }

  getPlaceDatas({booleans = true}){
    Map<String, dynamic> parms = {
      'user_id': store.state.userinfo.id,
      'page_number': pagePlaceMain,
      'page_count': pagePlaceCount
    };
    NetUtil.post('queryPlace', (data) async {
      setState(() {
        this.pagePlaceList.addAll(data['place_list']);
      });
    }, params: parms, errorBack: (data,code,message){
      ToastUtils.showShort("加载错误");
    });
  }
  _drawerBody(){
    return Container(
      margin: EdgeInsets.only(top:_drawerHeaderHeight),
      child: ListView.builder(
          padding: const EdgeInsets.all(0),
          itemCount: pagePlaceList?.length * 2 ,
          itemBuilder: (context, i){
            // if(i==0 || i==1) return Container(height: this._pageHeight/2,);
            if (i.isOdd) return new Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.6));
            final index = i ~/ 2 ;
            return _buildRow(pagePlaceList[index]);
          },
      ),
    );
    
  }
  _buildRow(Map arrs){
    return ListTile(
      title: Text(arrs['name'], style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600)),
      // subtitle: Text(arrs['address'], style: TextStyle(fontSize: 14)),
      // trailing: Text(arrs['note'], style: TextStyle(color: Color(0xff1784fd)),),
      contentPadding: EdgeInsets.fromLTRB(15, 8, 15, 8),
    );
  }
  get _drawerHeader => Container(
    height: _drawerHeaderHeight,
    padding: EdgeInsets.all(20),
    decoration: BoxDecoration(
      color: Colors.lightBlueAccent,
    ),
    child: Center(
      child: Text("我的场所", style: TextStyle(fontSize: 20),),
    ),
  );

  get _appbar => AppBar(
    backgroundColor: Color(0xFF1784fd),
    title: Text('场所设备总数:$deviceNumber' ,style: TextStyle(color: Colors.white)),
    centerTitle: true,
  );

  get _drawer => SmartDrawer (
    widthPercent: 0.6,
    child: Stack(
      children: <Widget>[
        _drawerBody(),
        Positioned(
          child: _drawerHeader
        )
      ],
    )
  );
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _appbar,
      drawer: _drawer,
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
  void didUpdateWidget(DeviceListPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
    setState(() {
      store = StoreProvider.of(context);
    });
    getPlaceDatas();
  }
}