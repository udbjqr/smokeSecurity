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

class DeviceListPage extends StatefulWidget {
  int userinfoId;
  DeviceListPage({Key key, this.userinfoId}):super(key: key);
  @override
  DeviceListPageState createState() => new DeviceListPageState();
}

class DeviceListPageState extends State<DeviceListPage> {
  // 每次获取的数据
  int pagePlaceCount = 1000;
  // 页码
  int pagePlaceMain = 1;

  // 设备每次获取的数据
  int pageDeviceCount = 10;
  // 设备页码
  int pageDeviceMain = 1;
  var placeId = 0;
  double _drawerHeaderHeight = 80;
  int deviceNumber = 0;
  bool pageChange = true;
  Store<mainRedux> store;
  List<Object> pagePlaceList = [];
  List<Object> pageDeviceList = [];

  GlobalKey<EasyRefreshState> _easyRefreshKey = GlobalKey<EasyRefreshState>();
  GlobalKey<RefreshHeaderState> _headerKey = GlobalKey<RefreshHeaderState>();
  GlobalKey<RefreshFooterState> _footerKey = GlobalKey<RefreshFooterState>();

  void _handleDrawerButtons() {
    Scaffold.of(context).openDrawer();
  }

  getPlaceDatas(){
    Map<String, dynamic> parms = {
      'user_id': widget.userinfoId,
      'page_number': pagePlaceMain,
      'page_count': pagePlaceCount
    };
    NetUtil.post('queryPlace', (data) async {
      updatePlaceId(data['place_list'][0]['id']);
      _getDeviceData();
      setState(() {
        this.pagePlaceList.addAll(data['place_list']);
      });
    }, params: parms, errorBack: (data,code,message){
      ToastUtils.showShort("加载错误");
    });
  }
  _getDeviceData({booleans = true}){
    if(pageChange){
      Map<String, dynamic> parms = {
        'user_id': widget.userinfoId,
        'place_id': placeId,
        'page_number': pageDeviceMain,
        'page_count': pageDeviceCount
      };
      NetUtil.post('queryDevice', (data) async {
        setState(() {
          deviceNumber = data['count'];
          pageDeviceMain++;
          if(data['device_list'] == null || data['device_list']?.length < pageDeviceCount){
            pageChange = false;
          }
        });
        if(booleans){
          setState(() {
            pageDeviceList.addAll(data['device_list']);
          });
        }else{
          setState(() {
            pageDeviceList = data['device_list'];
          });
        }
      }, params: parms, errorBack: (data,code,message){
        ToastUtils.showShort("加载错误");
      });
    }else{
      ToastUtils.showShort("已经全部加载完成");
    }
  }
  updatePlaceId(id){
    setState(() {
      this.placeId = id;
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
      title: Container(
        padding: EdgeInsets.only(left: 20),
        child: Text(arrs['name'], style: TextStyle(fontSize: 18, fontWeight: FontWeight.w400, color: Color(0xff333333))),
      ),
      contentPadding: EdgeInsets.fromLTRB(15, 8, 15, 8),
      onTap: (){
        updatePlaceId(arrs['id']);
        setState(() {
            pageDeviceMain = 1;
            pageChange = true;        
          });
        _getDeviceData(booleans: false);
        //关闭抽屉
        // Navigator.pop(context);
      },
    );
  }
  _buildDeviceRow(Map arrs){
    return ListTile(
      title: Text(arrs['name'], style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600)),
      subtitle: Text(arrs['msisdn'].toString(), style: TextStyle(fontSize: 14)),
      trailing: Text(arrs['flag'].toString(), style: TextStyle(color: Color(0xff1784fd)),),
      contentPadding: EdgeInsets.fromLTRB(15, 8, 15, 8),
      onTap: (){
        NavigatorRouter.goDeviceDetailPage(context, maps:{
          "user_id" : widget.userinfoId,
          "place_id" : this.placeId,
          "id" : arrs["id"]
        });
      },
    );
  }
  _mainList(){
    return ListView.builder(
      padding: const EdgeInsets.all(0),
      itemCount: pageDeviceList?.length * 2 ,
      itemBuilder: (context, i){
        if (i.isOdd) return new Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.6));
        final index = i ~/ 2 ;
        return _buildDeviceRow(pageDeviceList[index]);
      },
    );
  }
  get _drawerHeader => Container(
    height: _drawerHeaderHeight,
    padding: EdgeInsets.all(15),
    decoration: BoxDecoration(
      color: Color(0xFF1784fd),
    ),
    child: Center(
      child: Text("我的场所", style: TextStyle(fontSize: 18, color: Colors.white)),
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
  get _mainBody => Center(
    child: EasyRefresh(
      key: _easyRefreshKey,
      refreshHeader: TaurusHeader(
        key: _headerKey,
      ),
      refreshFooter: TaurusFooter(
        key: _footerKey,
      ),
      child: _mainList(),
      onRefresh: () async{
        await Future.delayed(const Duration(seconds: 2), () {
          setState(() {
            pageDeviceMain = 1;
            pageChange = true;        
          });
          _getDeviceData(booleans: false);
        });
      },
      loadMore: () async {
        await Future.delayed(const Duration(seconds: 1), () {
          _getDeviceData();
        });
      },
    ),
  );
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _appbar,
      drawer: _drawer,
      body: _mainBody,
    );
  }
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    getPlaceDatas();
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
  }
}