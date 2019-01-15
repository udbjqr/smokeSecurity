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

class MessageListPage extends StatefulWidget {
  @override
  MessageListPageState createState() => new MessageListPageState();
}

class MessageListPageState extends State<MessageListPage> {
  // 设备每次获取的数据
  int pageMessageCount = 10;
  // 设备页码
  int pageMessageMain = 1;
  bool pageChange = true;
  Store<mainRedux> store;
  List<Object> pageMessageList = [];
  GlobalKey<EasyRefreshState> _easyRefreshKey = new GlobalKey<EasyRefreshState>();
  GlobalKey<RefreshHeaderState> _headerKey = new GlobalKey<RefreshHeaderState>();
  GlobalKey<RefreshFooterState> _footerKey = new GlobalKey<RefreshFooterState>();

  _getMessageData({booleans = true}){
    if(pageChange){
      Map<String, dynamic> parms = {
        'user_id': store.state.userinfo.id,
        'page_number': pageMessageMain,
        'page_count': pageMessageCount
      };
      NetUtil.post('queryAlarm', (data) async {
        setState(() {
          pageMessageMain++;
          if(data['alarm_list'] == null || data['alarm_list']?.length < pageMessageCount){
            pageChange = false;
          }
        });
        if(booleans){
          setState(() {
            pageMessageList.addAll(data['alarm_list']);
          });
        }else{
          setState(() {
            pageMessageList = data['alarm_list'];
          });
        }
      }, params: parms, errorBack: (data,code,message){
        ToastUtils.showShort("加载错误");
      });
    }else{
      ToastUtils.showShort("已经全部加载完成");
    }
  }
  _buildDeviceRow(Map arrs){
    return ListTile(
      title: Container(
        margin: EdgeInsets.only(top: 10),
        child: Text(arrs['device_name'], style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600)),
      ),
      subtitle: Text(arrs['flag'].toString(), style: TextStyle(fontSize: 14)),
      trailing: Container(
        child: Stack(
          alignment: Alignment(1.1, 0),
          children: <Widget>[
            Icon(Icons.keyboard_arrow_right, color: Color(0xff1784fd)),
            Container(
              margin: EdgeInsets.only(right:15),
              child: Text(arrs['create_time'].toString(), style: TextStyle(color: Color(0xff1784fd))),
            ),
          ],
        ),
      ),
      contentPadding: EdgeInsets.fromLTRB(15, 8, 15, 8),
      onTap: (){
        NavigatorRouter.goMessageDetailPage(context,maps: {
          "user_id": arrs["user_id"],
          "cause_id": arrs["cause_id"],
          "device_id": arrs["device_id"],
        });
      },
    );
  }
  _mainList(){
    return ListView.builder(
      padding: const EdgeInsets.all(0),
      itemCount: pageMessageList?.length * 2 ,
      itemBuilder: (context, i){
        if (i.isOdd) return new Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.6));
        final index = i ~/ 2 ;
        return _buildDeviceRow(pageMessageList[index]);
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: Center(
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
                pageMessageMain = 1;
                pageChange = true;        
              });
              _getMessageData(booleans: false);
            });
          },
          loadMore: () async {
            await Future.delayed(const Duration(seconds: 1), () {
              _getMessageData();
            });
          },
        ),
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
  void didUpdateWidget(MessageListPage oldWidget) {
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
    _getMessageData();
  }
}