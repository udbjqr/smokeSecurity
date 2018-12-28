import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:myflutter/page/login/loginPage.dart';
import 'package:myflutter/page/welcome/welcomePage.dart';
import 'package:myflutter/page/login/loginPage.dart';

//路由
class NavigatorRouter{

  static const String _login = "/login";

  static Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
    Navigator.defaultRouteName: (context) => WelcomePage(),
    _login : (context) => LoginPage(),
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

  //  ///个人中心
  // static goPerson(BuildContext context, String userName) {
  //   Navigator.push(context, new CupertinoPageRoute(builder: (context) => new PersonPage(userName)));
  // }

  // ///仓库详情
  // static Future<Null> goReposDetail(BuildContext context, String userName, String reposName) {
  //   return Navigator.push(context, new CupertinoPageRoute(builder: (context) => new RepositoryDetailPage(userName, reposName)));

} 
