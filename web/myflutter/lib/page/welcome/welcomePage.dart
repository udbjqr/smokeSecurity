import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/style/MainStyle.dart';
import 'package:myflutter/common/router/NavigatorRouter.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/dao/localStore.dart';
import 'package:myflutter/common/dao/userDao.dart';


class WelcomePage extends StatefulWidget{
  WelcomePage({Key key}):super(key:key);
  @override
  createState() => WelcomePageState();
}

class WelcomePageState extends State<WelcomePage>{
  String _text = "用户您好！！！";

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _getUserInfo();
  }

  _getUserInfo() async{
    new Future.delayed(const Duration(seconds: 3), () {
      dynamic hadInit = LocalStorage.get('userInfo');
      if(hadInit == null){
        NavigatorRouter.goLogin(context);
      }else{
        userDao.setuserInfo(context);
      }
    });

  }
  @override
  Widget build(BuildContext context) {
    return StoreBuilder<mainRedux>(
      builder: (context,  store) {
        return new Container(
          color: Color(DlyColors.white),
          child: new Center(
            child: Text(this._text),
          ),
        );
      },
    );
  }
}