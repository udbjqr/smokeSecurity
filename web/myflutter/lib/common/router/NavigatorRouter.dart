import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:myflutter/page/login/loginPage.dart';
import 'package:myflutter/page/welcome/welcomePage.dart';
import 'package:myflutter/page/login/loginPage.dart';
import 'package:myflutter/page/main/mainPage.dart';
import 'package:myflutter/page/place/placeList.dart';

//路由
class NavigatorRouter{

  static const String _login = "/login";
  static const String _main = "/main";
  static const String _placeList = "/placeList";

  //Navigator.of(context).pop(); 刷新状态
  static Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
    Navigator.defaultRouteName: (context) => WelcomePage(),
    _login : (context) => LoginPage(),
    _main : (context) => MainPage(),
    _placeList : (context) => PlaceListPage(),
  };
  ///替换
  static pushReplacementNamed(BuildContext context, String routeName) {
    Navigator.pushReplacementNamed(context, routeName);
  }

  ///切换无参数页面
  static pushNamed(BuildContext context, String routeName) {
    Navigator.pushNamed(context, routeName);
  }

  //登录页
  static goLogin(BuildContext context) {
    Navigator.pushReplacementNamed(context, _login);
  }

  //tab页
  static goMain(BuildContext context) {
    Navigator.pushReplacementNamed(context, _main);
  }

  //tab页
  static goPlaceListPage(BuildContext context) {
    Navigator.pushReplacementNamed(context, _placeList);
  }

  //  ///个人中心
  // static goPerson(BuildContext context, String userName) {
  //   Navigator.push(context, new CupertinoPageRoute(builder: (context) => new PersonPage(userName)));
  // }

  // ///仓库详情
  // static Future<Null> goReposDetail(BuildContext context, String userName, String reposName) {
  //   return Navigator.push(context, new CupertinoPageRoute(builder: (context) => new RepositoryDetailPage(userName, reposName)));

} 
