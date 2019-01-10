import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart'; 
import 'package:myflutter/page/tabBar/homeListPage.dart';
import 'package:myflutter/page/tabBar/deviceListPage.dart';
import 'package:myflutter/page/tabBar/mineListPage.dart';



class MainPage extends StatefulWidget {
  @override
  MainPageState createState() =>  MainPageState();
}

class MainPageState extends State<MainPage> {


  // 默认索引第一个tab
  int _tabIndex = 0;

  // 正常情况的字体样式
  final tabTextStyleNormal = new TextStyle(color: const Color(0xff333333));

  // 选中情况的字体样式
  final tabTextStyleSelect = new TextStyle(color: const Color(0xff1784fd));

  // 底部菜单栏图标数组
  var tabImages;

  // 页面内容
  var _body;

  // 菜单文案
  var tabTitles = ['首页', '设备列表', '信息', '我的'];

  // 生成image组件
  Image getTabImage(path) {
    return Image.asset(path, width: 20.0, height: 20.0);
  }

  void initDatas() {
    // 先那一次数据，把accesstoken放到内存
    if (tabImages == null) {
      tabImages = [
        [
          getTabImage('images/home1.png'),
          getTabImage('images/home2.png')
        ],
        [
          getTabImage('images/list1.png'),
          getTabImage('images/list2.png')
        ],
        [
          getTabImage('images/message1.png'),
          getTabImage('images/message2.png')
        ],
        [
          getTabImage('images/users1.png'),
          getTabImage('images/users2.png')
        ]
      ];
    }
    _body = IndexedStack(
      children: <Widget>[ 
        HomeListPage(),
        DeviceListPage(),
        Text("data2"),
        MineListPage()
        // new NewsListPage(),
        // new SystemPage(),
        // new DiscoveryPage(),
        // new MyInfoPage()
      ],
      index: _tabIndex,
    );
  }

  //获取菜单栏字体样式
  TextStyle getTabTextStyle(int curIndex) {
    if (curIndex == _tabIndex) {
      return tabTextStyleSelect;
    } else {
      return tabTextStyleNormal;
    }
  }
  // 获取图标
  Image getTabIcon(int curIndex) {
    if (curIndex == _tabIndex) {
      return tabImages[curIndex][1];
    }
    return tabImages[curIndex][0];
  }

  // 获取标题文本
  Text getTabTitle(int curIndex) {
    return Text(
      tabTitles[curIndex],
      style: getTabTextStyle(curIndex),
    );
  }

  // 获取BottomNavigationBarItem
  List<BottomNavigationBarItem> getBottomNavigationBarItem() {
    List<BottomNavigationBarItem> list = List();
    for (int i = 0; i < 4; i++) {
      list.add(BottomNavigationBarItem(
          icon: getTabIcon(i), title: getTabTitle(i)));
    }
    return list;
  }
  
  @override
  Widget build(BuildContext context) {
    initDatas();
    return Scaffold(
      appBar:tabTitles[_tabIndex]!="我的" ? AppBar(
        title: Text(tabTitles[_tabIndex],
          style: new TextStyle(color: Color(0xFF1784fd))),
        centerTitle: true,
        backgroundColor: Color(0xFFffffff),
      ):null,
      body: _body,
      bottomNavigationBar: CupertinoTabBar(
        items: getBottomNavigationBarItem(),
        //默认选中首页
        currentIndex: _tabIndex,
        backgroundColor: Color(0xFFeeeeee),
        border: Border(top: BorderSide(width: 1.0, color: Color(0xFF000000))),
        iconSize: 24.0,
        onTap: (index) {
          setState(() {
            _tabIndex = index;
          });
        },
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
  void didUpdateWidget(MainPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }
}
  