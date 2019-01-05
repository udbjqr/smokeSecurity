import 'dart:convert';
import 'dart:io';
import 'package:redux/redux.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/redux/UserRedux.dart';
import 'package:myflutter/common/model/User.dart';
import 'dart:convert';
import 'package:myflutter/common/dao/localStore.dart';
import 'package:myflutter/common/router/NavigatorRouter.dart';

class userDao{
  ///获取本地登录用户信息
  static setuserInfo(context) async {
    var userText = await LocalStorage.get("userInfo");
    if (userText != null) {
      User user = User.fromJson(json.decode(userText));
      Store<mainRedux> store = StoreProvider.of(context);
      store.dispatch(UpdateUserAction(user));
      NavigatorRouter.goMain(context);
    } else {
      NavigatorRouter.goLogin(context);
    }
  }
}