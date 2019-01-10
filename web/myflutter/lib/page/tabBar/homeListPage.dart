import 'package:flutter/material.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';
import 'package:myflutter/common/pluguses/ToastUtils.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_easyrefresh/bezier_circle_header.dart';
import 'package:flutter_easyrefresh/bezier_bounce_footer.dart';
import 'package:flutter/gestures.dart';


class HomeListPage extends StatefulWidget {
  @override
  HomeListPageState createState() => new HomeListPageState();
}

class HomeListPageState extends State<HomeListPage> {
 // 每次获取的数据
  int pageCount = 10;
  // 页码
  int pageMain = 1;
  // 列表
  var pageList = List<Object>();
  bool pageChange = true;
  Store<mainRedux> store;

  double _pageHeight = 265;
  Size _windowHeight;

  static Color refreshColor = Colors.pink[200];
  static int number = 1;
  static Radius radiusBorder = Radius.circular(5.0);
  static String _mainText = "正常";

  GlobalKey<EasyRefreshState> _easyRefreshKey = new GlobalKey<EasyRefreshState>();
  GlobalKey<RefreshHeaderState> _headerKey = new GlobalKey<RefreshHeaderState>();
  GlobalKey<RefreshFooterState> _footerKey = new GlobalKey<RefreshFooterState>();
  
  IconData serecdIcon = Icons.add_circle_outline;


  getDatas({booleans = true}){
    if(pageChange){
      Map<String, dynamic> parms = {
        'user_id': store.state.userinfo.id,
        'page_number': pageMain,
        'page_count': pageCount
      };
      NetUtil.post('queryPlace', (data) async {
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

  //Row 图标
  iconButtons(icon, eventChange){
      return IconButton(
        iconSize: 30,
        icon: Icon(icon),
        color: Colors.white,
        onPressed: eventChange,
      );
  }

  // 渲染 TextTile
  _listTile(_text, radiuer){
    final TapGestureRecognizer recognizer = new TapGestureRecognizer();
    final TapGestureRecognizer _recognizer = new TapGestureRecognizer();
    recognizer.onTap=(){
      print("object");
    };
    _recognizer.onTap=(){
      print("123");
    };
    return Container(
      padding: EdgeInsets.fromLTRB(5, 5, 10, 5),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: radiuer ? BorderRadius.only(topLeft: radiusBorder,topRight: radiusBorder) 
                              : BorderRadius.only(bottomLeft: radiusBorder,bottomRight: radiusBorder)
      ),
      child: Row(
        children: <Widget>[
          Icon(Icons.add),
          RichText(
            text: TextSpan(
              style: TextStyle(
                color: Colors.black,
                fontSize: 18
              ),
              text: _text,
              recognizer: radiuer ? recognizer : _recognizer
            ),
          )
        ],
      ),
    );
  }

  // Positioned 图标 
  _setIconsAdd(){
    if(number==2){
      return Positioned(
        right: 10,
        top:40,
        child: Icon(Icons.arrow_drop_up,color: Colors.white, size: 35,),
      );
    }else{
      return Center();
    }
  }

  // Positioned 选项
  _setAddWidget(){
    if(number==2){
      return Positioned(
        right: 10,
        top:60,
        child: Column(
            children: <Widget>[
              _listTile("添加场所", true),
              _listTile("添加设备", false),
            ],
          )
      );
    }else{
      return Center();
    }
  }
  
  //main 圆圈 显示状态
  _setCircle(){
    return Center(
      child: Container(
        width: 120,
        height: 120,
        child: Container(
          width: 100,
          height: 100,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(100),
            color: Colors.white
          ),
          child: Text(_mainText,style: TextStyle(fontSize: 24, height:1.9), textAlign: TextAlign.center),
        ),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(100),
          border: Border.all(
            color: Color.fromRGBO(255, 255, 255, 0.4),
            width: 15
          )
        ),
      ),
    );
  }
  // main 按钮
  _setMainButton(){
    double _padding = 24;
    return Container(
      margin: EdgeInsets.fromLTRB(0, _padding , 0, 0),
      padding: EdgeInsets.fromLTRB(_padding, 0, _padding, 0),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(24),
        border: Border.all(
            color: Color.fromRGBO(255, 255, 255, 0.8),
            width: 2
          )
      ),
      child: OutlineButton(
        padding: EdgeInsets.fromLTRB(_padding, 0, _padding, 0),
        borderSide: BorderSide(color: Color(0x00fff)),
        child: Text("我的场所",
          style: TextStyle(
            color: Colors.white,
            fontSize: 16,
            letterSpacing: 0.5,
          ),
        ),
        onPressed: (){
          print("object");
        },
      ),
    );
  }

  // ? 点击事件
  eventHelp(){}
  // + 点击事件
  eventAdd(){
    setState(() {
      number = number == 1 ? 2 : 1;
      serecdIcon = number == 1 ? Icons.add_circle_outline : Icons.highlight_off;
    });
  }
  MainDiv(){
    return Stack(
      children: <Widget>[
        Container(
          height: this._pageHeight,
          width: 500,
          decoration: BoxDecoration(
            gradient: LinearGradient(
              colors: [
                Color(0xff4050f7),
                Color(0xff1cccf7),
              ],
              begin: Alignment.topLeft,
              end: Alignment.bottomRight
            )
          ),
          child: Column(
            children: <Widget>[
              Container(
                margin: EdgeInsets.fromLTRB(0, 8, 0, 0),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: <Widget>[
                  iconButtons(Icons.help_outline, eventHelp),
                  iconButtons(serecdIcon, eventAdd)
                ],
              ),
              _setCircle(),
              _setMainButton()
            ],
          ),
        ),
        _setIconsAdd(),
        _setAddWidget()
      ],
    );
  }
  _mainRefresh(){
    return Container(
      margin: EdgeInsets.only(top: this._pageHeight),
      height: _windowHeight.height - this._pageHeight - 140,
      // height:  _windowHeight.height ,
      child: ListView.builder(
        padding: const EdgeInsets.all(0),
        itemCount: pageList?.length * 2 ,
        itemBuilder: (context, i){
          // if(i==0 || i==1) return Container(height: this._pageHeight/2,);
          if (i.isOdd) return new Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.6));
          final index = i ~/ 2 ;
          return _buildRow(pageList[index]);
        },
      ),
    );
  }
  _mainList(){
    return ListView(
      children: <Widget>[
        Stack(
          children: <Widget>[
            _mainRefresh(),
            Positioned(
              child: MainDiv(),
            ),
          ],
        )
      ],
    );
  }

  _buildRow(Map arrs){
    return ListTile(
      title: Text(arrs['name'], style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600)),
      subtitle: Text(arrs['address'], style: TextStyle(fontSize: 14)),
      trailing: Text(arrs['note'], style: TextStyle(color: Color(0xff1784fd)),),
      contentPadding: EdgeInsets.fromLTRB(15, 8, 15, 8),
    );
  }

  @override
  Widget build(BuildContext context) {
    return EasyRefresh(
      key: _easyRefreshKey,
      refreshHeader: BezierCircleHeader(
        backgroundColor: refreshColor,
        key: _headerKey,
      ),
      refreshFooter: BezierBounceFooter(
        backgroundColor: refreshColor,
        key: _footerKey,
      ),
      child: _mainList(),
      onRefresh: () async{
        await Future.delayed(const Duration(seconds: 2), () {
          setState(() {
            pageMain = 1;
            pageChange = true;        
          });
          getDatas(booleans: false);
        });
      },
      loadMore: () async {
        await Future.delayed(const Duration(seconds: 1), () {
          getDatas();
        });
      },
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
  void didUpdateWidget(HomeListPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
    print("ssss");
    setState(() {
      _windowHeight = MediaQuery.of(context).size;
      store = StoreProvider.of(context);
    });
    getDatas();
  }
}