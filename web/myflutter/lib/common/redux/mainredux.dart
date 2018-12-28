import 'package:flutter/material.dart';
import 'package:myflutter/common/model/User.dart';
import 'package:myflutter/common/redux/UserRedux.dart';
import 'package:myflutter/common/redux/ThemeRedux.dart';

class mainRedux {
  User userinfo;
  ThemeData themeData;

  mainRedux({this.userinfo, this.themeData});
}

///创建 Reducer
///源码中 Reducer 是一个方法 typedef State Reducer<State>(State state, dynamic action);
///我们自定义了 appReducer 用于创建 store
mainRedux addReducer(mainRedux state, action){
  return mainRedux(
    userinfo: UserReducer(state.userinfo, action),
    themeData:ThemeDataRedux(state.themeData, action)
  );
}