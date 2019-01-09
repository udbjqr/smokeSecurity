import 'package:fluttertoast/fluttertoast.dart';
import 'package:flutter/material.dart';
class ToastUtils{
  static showShort(String msg){
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_LONG,
        gravity: ToastGravity.CENTER,
        timeInSecForIos: 1,
        backgroundColor: Colors.pink[200],
        textColor:  Color(0xFF333333)
    );
  }
}