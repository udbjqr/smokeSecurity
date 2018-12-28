import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/style/MainStyle.dart';
import 'dart:io';
import 'package:dio/dio.dart';
import 'package:myflutter/common/router/NavigatorRouter.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';



class WelcomePage extends StatefulWidget{
  WelcomePage({Key key}):super(key:key);
  @override
  createState() => WelcomePageState();
}

class WelcomePageState extends State<WelcomePage>{

  bool hadInit = false;
  String _text = "欢饮光临！";

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _getUserInfo();
  }
  _getUserInfo() async{
    NetUtil.post('/getResToken',(data){
      print(data);
    },errorBack: (data,code,message){
      print(message);
    });
    new Future.delayed(const Duration(seconds: 3), () {
      if(!hadInit){
         NavigatorRouter.goLogin(context);
      }else{
        setState(() {
          _text = "谢谢光临";
        });
      }
    });

  }
  @override
  // Widget build(BuildContext context) {
  //   return Center(
  //     child: Text(_text),
  //   );
  // }
  Widget build(BuildContext context) {
    return StoreBuilder<mainRedux>(
      builder: (context, store) {
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