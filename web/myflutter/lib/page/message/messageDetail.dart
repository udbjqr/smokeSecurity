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

class MyItem {
  MyItem({ this.isExpanded: false, this.name, this.create_time, this.device_name, this.flag, this.flagname:'正常'});

  bool isExpanded;
  final String name;
  final String create_time;
  final String device_name;
  final String flagname;
  final int flag;

}
class MessageDetailList extends StatefulWidget {
  Map<String, dynamic> forms;
  MessageDetailList({
    Key key,
    this.forms
  }) : super(key: key);
  @override
  MessageDetailListState createState() => new MessageDetailListState();
}

class MessageDetailListState extends State<MessageDetailList> {
 // 设备每次获取的数据
  int pageMessageCount = 10;
  // 设备页码
  int pageMessageMain = 1;
  bool pageChange = true;
  Store<mainRedux> store;
  List<dynamic> pageMessageList =[];
  Map<String, dynamic> parms;
  GlobalKey<EasyRefreshState> _easyRefreshKey = GlobalKey<EasyRefreshState>();
  GlobalKey<RefreshHeaderState> _headerKey = GlobalKey<RefreshHeaderState>();
  GlobalKey<RefreshFooterState> _footerKey = GlobalKey<RefreshFooterState>();

  _getMessageData({booleans = true}){
    if(pageChange){
      parms['page_number'] = pageMessageMain;
      parms['page_count'] = pageMessageCount;
      NetUtil.post('queryAlarmCaue', (data) async {
        List kk = data['alarm_list'].map((i){
            i['isExpanded'] = false;
            return i;
          }).toList();
        setState(() {
          pageMessageMain++;
          
          if(data['alarm_list'] == null || data['alarm_list']?.length < pageMessageCount){
            pageChange = false;
          }
        });
        // if(data['alarm_list']?.length !=0){
        //   List<dynamic> kk;
        //   kk = data['alarm_list'].map((i){
        //     return MyItem(name:i['name'], device_name: i['device_name'], create_time: i['create_time'], flag: i['flag']);
        //   }).toList();
        //   print(kk[0]);
          if(booleans){
            setState(() {
              pageMessageList.addAll(kk);
            });
          }else{
            setState(() {
              pageMessageList = kk;
            });
          }
         
      }, params: parms, errorBack: (data,code,message){
        ToastUtils.showShort("加载错误");
      });
    }else{
      ToastUtils.showShort("已经全部加载完成");
    }
  }

  _setPanel(item){

      return ExpansionPanel(
        headerBuilder: (context, isExpanded){
          return Text("ss");
          // return ListTile(
          //   title: Text(item['device_name']),
          //   subtitle: Text(item['create_time']),
          //   trailing: Text(item['flag']),
          // );
        },
        body: new Container(
          child: new Text("body"),
        ),
      );
  
  }
  // List<MyItem> _items = <MyItem>[
  //   new MyItem(name:"i.name", device_name: "i.device_name", create_time: "s", flag: false)
  // ];
  _mainList(){
    return  ListView(
      children:[
        new ExpansionPanelList(
          expansionCallback: (int index, bool isExpanded) {
            setState(() {
              pageMessageList[index]["isExpanded"] = !pageMessageList[index]["isExpanded"];
            });
          },
          children: pageMessageList.map((item) {
            return ExpansionPanel(
              headerBuilder: (BuildContext context, bool isExpanded) {
                return ListTile(
                  title: Text(item['device_name']),
                  subtitle: Text(item['create_time']),
                  trailing: Text(item['flag'].toString()),
                );
              },
              isExpanded: item['isExpanded'],
              body: Container(
                padding: EdgeInsets.only(bottom:10),
                child: Text(item['name'] ?? "暂无数据"),
              ),
            );
          }).toList(),
        )
      ],
    );
    
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
  void didUpdateWidget(MessageDetailList oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
    setState(() {
      parms = widget.forms;
      store = StoreProvider.of(context);
    });
    _getMessageData();
  }
}