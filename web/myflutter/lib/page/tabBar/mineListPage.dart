import 'package:flutter/material.dart';
import 'package:myflutter/common/router/NavigatorRouter.dart';
class MineListPage extends StatefulWidget {
  @override
  MineListPageState createState() => new MineListPageState();
}

class MineListPageState extends State<MineListPage> {
  static final Color _color = Color(0xff1784fd); 
  List<Map<String, dynamic>> arrMap;

  _setSliverList(){
    List<Widget> _bodys = [
      Container(
        padding: EdgeInsets.only(top:40),
      )
    ];
    _bodys.addAll(arrMap.map((product) {
      return ListTile(
        leading: product['icons'],
        title:Container(
          child:Text(product['title'], style: TextStyle(fontSize: 20)) ,
        ) ,
        onTap: product['url'],
        trailing: Icon(Icons.keyboard_arrow_right),
      );
    }).toList());
    _bodys.addAll([Center(),Center()]);
    _bodys.add(Container(
      padding: EdgeInsets.fromLTRB(30, 0, 30, 0),
      child: RaisedButton(
        onPressed: (){},
        textColor: Colors.blue,
        color: Colors.lightBlueAccent,
        child: Text("退出登录", style: TextStyle(color: Colors.white, fontSize: 20, letterSpacing: 3, fontWeight: FontWeight.w600)),
      ),
    ));
    _bodys.addAll([Center()]);
    return _bodys;
  }
  @override
  Widget build(BuildContext context) {
    return CustomScrollView(
        slivers: <Widget>[
          SliverAppBar(
            // title: Text('SliverAppBar'),
            backgroundColor: Theme.of(context).accentColor,
            expandedHeight: 300,
            flexibleSpace: Container(
              child: Column(
                children: <Widget>[
                  AppBar(
                    title: Text("我的", style: new TextStyle(color: Color(0xFF1784fd))),
                    centerTitle: true,
                    backgroundColor: Color(0xFFffffff),
                  ),
                   Expanded(
                    child: new Container(
                      child: Image.asset(
                        "images/3.jpg",
                        repeat: ImageRepeat.repeat,
                      ),
                      width: double.infinity,
                    ),
                  )
                ],
              ),
            ),
            pinned: true,
          ),
          SliverFixedExtentList(
            itemExtent: 60.0,
            delegate: SliverChildListDelegate(
              _setSliverList()
            ),
          ),
        ],
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
  void didUpdateWidget(MineListPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
    setState(() {
      arrMap = [{
        "title": "场所管理",
        "icons": Icon(Icons.account_balance, color: _color),
        "url": (){ NavigatorRouter.goPlaceListPage(context);}
      },{
        "title": "设备管理",
        "icons": Icon(Icons.device_hub, color: _color),
        "url": (){ NavigatorRouter.goPlaceListPage(context);}
      },{
        "title": "历史记录",
        "icons": Icon(Icons.description, color: _color),
        "url": (){ NavigatorRouter.goPlaceListPage(context);}
      },{
        "title": "火警通知",
        "icons": Icon(Icons.speaker_phone, color: _color),
        "url": (){ NavigatorRouter.goPlaceListPage(context);}
      },{
        "title": "推送管理",
        "icons": Icon(Icons.settings, color: _color),
        "url": (){ NavigatorRouter.goPlaceListPage(context);}
      },{
        "title": "客服电话",
        "icons": Icon(Icons.call, color: _color),
        "url": (){ NavigatorRouter.goPlaceListPage(context);}
      }];      
    });
  }
}