import 'package:flutter/material.dart';

class LoginPage extends StatefulWidget{
  LoginPage({Key key}):super(key:key);
  @override
  createState() => loginPageState();
}

class loginPageState extends State<LoginPage>{
  @override
  Widget build(BuildContext context){
    return Text("这里是登录页");
  }
}