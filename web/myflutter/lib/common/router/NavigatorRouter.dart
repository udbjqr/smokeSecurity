import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:myflutter/page/login/loginPage.dart';
import 'package:myflutter/page/welcome/welcomePage.dart';
import 'package:myflutter/page/login/loginPage.dart';
import 'package:myflutter/page/main/mainPage.dart';
import 'package:myflutter/page/place/placeList.dart';
import 'package:myflutter/page/child/telpage.dart';
import 'package:myflutter/page/child/setpush.dart';
import 'package:myflutter/page/message/messageDetail.dart';
import 'package:myflutter/page/place/placeDetail.dart';
import 'package:myflutter/page/place/addPlace.dart';

//路由
class NavigatorRouter{

  static const String _login = "/login";
  static const String _main = "/main";
  static const String _placeList = "/placeList";
  static const String _placeListDetail = "/placeListDetail";
  static const String _telPage = "/telPage";
  static const String _setPushPage = "/pushPage";
  static const String _messageDetail = "/messageDetail";
  static const String _addPlace = "/addPlace";

  //Navigator.of(context).pop(); 刷新状态
  static Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
    Navigator.defaultRouteName: (context) => WelcomePage(),
    _login : (context) => LoginPage(),
    _main : (context) => MainPage(),
    _placeList : (context) => PlaceListPage(),
    _telPage : (context) => TelPage(),
    _setPushPage : (context) => SetPushPageList(),
    _messageDetail : (context) => MessageDetailList(),
    _placeListDetail : (context) => PlaceDetailPage()
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

  //场所列表页
  static goPlaceListPage(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) =>  PlaceListPage()));
  }

  //场所详情页
  static goPlaceListDetailPage(BuildContext context, int placeId) {
    Navigator.push(context, MaterialPageRoute(builder: (context) =>  PlaceDetailPage(place_detail_id: placeId ?? 1)));
  }

  //增加场所页
  static goAddPlacePage(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) =>  addPlacePage()));
  }

  //客服电话页
  static goTelPage(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) =>  TelPage()));
  }

  //推送管理页
  static goSetPushPage(BuildContext context) {
    // Navigator.pushReplacementNamed(context, _setPushPage);
    Navigator.push(context, MaterialPageRoute(builder: (context) =>  SetPushPageList()));
  }

  //信息详情页
  static goMessageDetailPage(BuildContext context, {Map<String, dynamic> maps}) {
    // Navigator.pushReplacementNamed(context, _messageDetail);
    Navigator.push(context, MaterialPageRoute(builder: (context) =>  MessageDetailList(forms : maps ?? {})));
  }


  //  ///个人中心
  // static goPerson(BuildContext context, String userName) {
  //   Navigator.push(context, new CupertinoPageRoute(builder: (context) => new PersonPage(userName)));
  // }

  // ///仓库详情
  // static Future<Null> goReposDetail(BuildContext context, String userName, String reposName) {
  //   return Navigator.push(context, new CupertinoPageRoute(builder: (context) => new RepositoryDetailPage(userName, reposName)));

} 
