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

  double _sizeBoxPaddingTop = 20;

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

  _state(bool _state, Function _fun, String _text){
    return Expanded(
        child: ListTile(
          leading: LikeButton(
              is_trues:_state,
              onIconClicked: _fun,
              width: 60.0,
              circleStartColor: _themeColor,
              circleEndColor: _themeColor,
              dotColor: DotColor(
                dotPrimaryColor: _themeColor,
                dotSecondaryColor: Color(0xff649cff),
              ),
              icon: LikeIcon(
                Icons.check_circle,
                iconColor: _themeColor,
              ),
            ),
          title: Text(_text),
        ),
      );
  }

  _buildRow(Map arrs){
    bool phoneState = arrs["other"]['phone'] == null ? false : arrs["other"]['phone'];
    bool messageState = arrs["other"]['message'] == null ? false : arrs["other"]['message'];
    return SizedBox(
      width: 800,
      height: 120 + _sizeBoxPaddingTop,
      child: Container(
        padding: EdgeInsets.only(top: _sizeBoxPaddingTop),
        child: Column(
          children: <Widget>[
            ListTile(
              title: Text(arrs['name']),
              trailing: RaisedButton(
                color: _themeColor,
                child: Text("提交", style: TextStyle(color: Colors.white, fontSize: 16, letterSpacing: 10)),
                onPressed: (){
                  print(arrs);
                  NetUtil.post('updatePlaceOther', (data) async {
                    ToastUtils.showSuccessShort("提交成功");
                    setState(() {
                      pageMain = 1;
                      pageChange = true;        
                    });
                    getDatas(booleans: false);
                  }, params: arrs, errorBack: (data,code,message){
                    ToastUtils.showShort("提交错误");
                  });
                },
              ),
            ),
            Row(
              children: <Widget>[
                _state(phoneState, (phoneState) {arrs["other"]['phone'] = phoneState;}, "接受火警电话"),
                _state(messageState, (messageState) {arrs["other"]['message'] = messageState;}, "接受短信通知"),
              ],
            ),
          ],
        ),
      ) 
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