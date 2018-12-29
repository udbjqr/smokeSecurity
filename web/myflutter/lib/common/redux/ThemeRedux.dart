import 'package:flutter/material.dart';
import 'package:redux/redux.dart';

final ThemeDataRedux = combineReducers<ThemeData>([
  TypedReducer<ThemeData,ThemeDataAction>(_refresh)
]);

ThemeData _refresh(ThemeData themeData, action){
  themeData = action.themeData;
  return themeData;
}

class ThemeDataAction{
  final ThemeData themeData;
  ThemeDataAction(this.themeData);
}