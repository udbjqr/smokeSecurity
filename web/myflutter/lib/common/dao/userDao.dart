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

class userDao{
  ///获取本地登录用户信息
  static setuserInfo() async {
    var userText = await LocalStorage.get("userInfo");
    if (userText != null) {
      var userMap = json.decode(userText);
      User user = User.fromJson(userMap);
      print(user.id);
      // return new DataResult(user, true);
    } else {
      // return new DataResult(null, false);
    }
  }
}