import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';


class MainPage extends StatefulWidget {
  @override
  MainPageState createState() =>  MainPageState();
}

class MainPageState extends State<MainPage> {
  int _tabIndex = 1;
  getBottomNavigationBarItem(){

  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('主页面'),
      ),
    );
  }
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // Store<mainRedux> store = StoreProvider.of(context);
    // print(store.state);
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