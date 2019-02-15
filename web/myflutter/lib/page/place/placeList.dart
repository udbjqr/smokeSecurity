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

class PlaceListPage extends StatefulWidget {
  @override
  PlaceListPageState createState() => PlaceListPageState();
}

class PlaceListPageState extends State<PlaceListPage> {
  // 设备每次获取的数据
  int pageMessageCount = 10;
  // 设备页码
  int pageMessageMain = 1;
  bool pageChange = true;
  Store<mainRedux> store;
  List<Object> pageMessageList = [];
  static final Color _themeColor = DlyColors.primaryTheme;
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
      NetUtil.post('queryPlace', (data) async {
        setState(() {
          pageMessageMain++;
          if(data['place_list'] == null || data['place_list']?.length < pageMessageCount){
            pageChange = false;
          }
        });
        if(booleans){
          setState(() {
            pageMessageList.addAll(data['place_list']);
          });
        }else{
          setState(() {
            pageMessageList = data['place_list'];
          });
        }
      }, params: parms, errorBack: (data,code,message){
        ToastUtils.showShort("加载错误");
      });
    }else{
      ToastUtils.showShort("已经全部加载完成");
    }
  }

  _buildRow(item, index){
    return Slidable(
      delegate: SlidableDrawerDelegate(),
      actionExtentRatio: 0.25,
      child: Container(
        padding: EdgeInsets.fromLTRB(5, 10, 2, 10),
        color: Colors.white,
        child: ListTile(
          leading: CircleAvatar(
            backgroundColor: Colors.indigoAccent,
            child: Text((++index).toString()),
            foregroundColor: Colors.white,
          ),
          title: Text(item['name']),
          subtitle: Text(item['address']),
          trailing: Container(
            child: Stack(
              alignment: Alignment(1.1, 0),
              children: <Widget>[
                Icon(Icons.keyboard_arrow_right, color: Color(0xff1784fd)),
                Container(
                  margin: EdgeInsets.only(right:15),
                  child: Text(item['create_time'].toString().substring(0,10), style: TextStyle(color: Color(0xff1784fd))),
                ),
              ],
            ),
          ),
          onTap: (){
            NavigatorRouter.goPlaceListDetailPage(context, item['id']);
          },
        ),
      ),
      secondaryActions: <Widget>[
        IconSlideAction(
          caption: '删除',
          color: Colors.red,
          icon: Icons.delete,
          onTap: () { return showDialog<bool>(
            context: context,
            builder: (context) {
              return AlertDialog(
                title: Text('删除'),
                content: ListTile(
                  leading: Icon(Icons.error, color: Color(0xffe6a23c)),
                  title: Text("是否删除此场所"),
                ),
                actions: <Widget>[
                  FlatButton(
                    child: Text('取消'),
                    onPressed: () => Navigator.of(context).pop(false),
                  ),
                  FlatButton(
                    child: Text('确定', style: TextStyle(color: _themeColor)),
                    onPressed: () {
                      Navigator.of(context).pop(true);
                      NetUtil.post('deletePlace', (data) async {
                        ToastUtils.showSuccessShort("成功删除！");
                        setState(() {
                          pageMessageMain = 1;
                          pageChange = true;        
                        });
                        _getMessageData(booleans: false);
                      }, params: {'id': item['id']}, errorBack: (data,code,message){
                        ToastUtils.showShort(data['data']['message']);
                      });
                    },
                  ),
                ],
              );
            },
          );
          }
        ),
      ],
    );
  }
  _mainList(){
    return ListView.builder(
      padding: const EdgeInsets.all(0),
      itemCount: pageMessageList?.length * 2 ,
      itemBuilder: (context, i){
        if (i.isOdd) return new Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.6));
        final index = i ~/ 2 ;
        return _buildRow(pageMessageList[index], index);
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SmartAppBar(text: "场所列表"),
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
  void didUpdateWidget(PlaceListPage oldWidget) {
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