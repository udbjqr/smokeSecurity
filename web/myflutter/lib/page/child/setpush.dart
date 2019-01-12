import 'package:flutter/material.dart';
import 'package:myflutter/common/style/MainStyle.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';
import 'package:myflutter/common/pluguses/ToastUtils.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/likebutton/like_button.dart';
import 'package:myflutter/common/likebutton/model.dart';

class SetPushPageList extends StatefulWidget {
  @override
  SetPushPageListState createState() => new SetPushPageListState();
}

class SetPushPageListState extends State<SetPushPageList> {
  bool kk = false;
  static final Color _themeColor = DlyColors.primaryTheme;
  // 每次获取的数据
  int pageCount = 1000;
  // 页码
  int pageMain = 1;
  // 列表
  var pageList = List<Object>();
  bool pageChange = true;
  Store<mainRedux> store;

  getDatas({booleans = true}){
    if(pageChange){
      Map<String, dynamic> parms = {
        'user_id': store.state.userinfo.id,
        'page_number': pageMain,
        'page_count': pageCount
      };
      NetUtil.post('queryPlaceOther', (data) async {
        setState(() {
          pageMain++;
          if(data['place_list'] == null || data['place_list']?.length < pageCount){
            pageChange = false;
          }
        });
        if(booleans){
          setState(() {
            pageList.addAll(data['place_list']);
          });
        }else{
          setState(() {
            pageList = data['place_list'];
          });
        }
      }, params: parms, errorBack: (data,code,message){
        ToastUtils.showShort("加载错误");
      });
    }else{
      ToastUtils.showShort("已经全部加载完成");
    }
  }

  _buildRow(Map arrs){
    bool phoneState = arrs["other"]['phone'] == null ? false : arrs["other"]['phone'];
    bool messageState = arrs["other"]['message'] == null ? false : arrs["other"]['message'];
    return SizedBox(
      width: 800,
      height: 300,
      child: Column(
        children: <Widget>[
          ListTile(
            title: Text(arrs['name']),
            trailing: RaisedButton(
              child: Text("提交"),
              onPressed: (){
                print(arrs);
              },
            ),
          ),
          LikeButton(
            is_trues:phoneState,
            onIconClicked: (phoneState) {
              arrs["other"]['phone'] = phoneState;
            },
            width: 80.0,
          ),
          LikeButton(
            is_trues:messageState,
            onIconClicked: (messageState) {
              arrs["other"]['message'] = messageState;
            },
              width: 80.0,
              circleStartColor: Color(0xff669900),
              circleEndColor: Color(0xff669900),
              dotColor: DotColor(
                dotPrimaryColor: Color(0xff669900),
                dotSecondaryColor: Color(0xff99cc00),
              ),
              icon: LikeIcon(
                Icons.adb,
                iconColor: Colors.green,
              ),
            ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SmartAppBar(text: "推送设置"),
      body: Container(
        child: ListView.builder(
        padding: const EdgeInsets.all(0),
        itemCount: pageList?.length * 2 ,
        itemBuilder: (context, i){
          if (i.isOdd) return Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.4));
          final index = i ~/ 2 ;
          return _buildRow(pageList[index]);
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
  void didUpdateWidget(SetPushPageList oldWidget) {
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
    getDatas();
  }
}