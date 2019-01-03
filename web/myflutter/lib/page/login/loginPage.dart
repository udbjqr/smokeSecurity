import 'package:flutter/material.dart';

class LoginPage extends StatefulWidget{
  LoginPage({Key key}):super(key:key);
  @override
  createState() => loginPageState();
}

class loginPageState extends State<LoginPage>{
  final double leftRightPadding = 30.0;
  final double topBottomPadding = 4.0;
  final dynamic backColors = 0xFFfbfbfb;
  var username = TextEditingController();
  var password = TextEditingController();
  TextStyle hintTips = TextStyle(
    color: Colors.black,
    fontSize: 16
  );


  _postLogin(String username, String password){

  }

  Widget stackWidget(String imageName, TextEditingController textvalue, String hintText, {obscureText = false}){
    return Container(
      padding: EdgeInsets.fromLTRB(10, 20, 20, 10),
      child:Stack(
        alignment: Alignment(1, 1),
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Padding(
                padding: EdgeInsets.fromLTRB(5, 10, 10, 0),
                child: Image.asset(
                  imageName,
                  width: 40,
                  height: 40,
                  fit: BoxFit.fill,
                ),
              ),
              Expanded(
                child: TextField(
                  controller: textvalue,
                  decoration: InputDecoration(
                    hintText: hintText,
                  ),
                  obscureText: obscureText,
                ),
              )
            ],
          ),
          IconButton(
            icon: Icon(Icons.highlight_off, color: Colors.grey,size: 24,),
            onPressed: () {
              textvalue.clear();
            },
          )
        ],
      )
    );
  }

  @override
  Widget build(BuildContext context){
    return Scaffold(
      appBar: AppBar(
        title: Text("登录"),
      ),
      body: new ListView(
        children: <Widget>[
          //TODO:这里写子控件。
          Container(
            height: 600,
            padding: EdgeInsets.zero,
            margin: EdgeInsets.zero,
            decoration: BoxDecoration(
              // backgroundBlendMode: ,
              image:DecorationImage(
                image: AssetImage("images/login-bg.jpg"),
                fit: BoxFit.cover
              ),
            ),
            child:Container(
              margin: EdgeInsets.fromLTRB(leftRightPadding, 130.0, leftRightPadding, 30),
              decoration: BoxDecoration(
                color: Color(backColors),
                borderRadius: BorderRadius.circular(8.0),
                border: Border.all(
                  color: Colors.grey
                )
              ),
              child: Column(
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Padding(
                    padding: EdgeInsets.fromLTRB(0, 50, 0, 10),
                  ),
                  stackWidget('images/user.png',username,"请输入用户名"),
                  stackWidget('images/lock.png',password,"请输入密码",obscureText : true),
                  //TODO:这里写子控件。
                  // Container(
                  //   margin: EdgeInsets.fromLTRB(leftRightPadding, 50, leftRightPadding, 10),
                  //   child: TextField(
                  //     style: hintTips,
                  //     controller: username,
                  //     decoration: InputDecoration(
                  //       hintText: "请输入用户名"
                  //     ),
                  //     obscureText: false,
                  //   ),
                  // ),
                  // Padding(
                  //   padding: EdgeInsets.fromLTRB(leftRightPadding, 10, leftRightPadding, 10),
                  //   child: TextField(
                  //     style: hintTips,
                  //     controller: password,
                  //     decoration: InputDecoration(
                  //       hintText: "请输入密码"
                  //     ),
                  //     obscureText: true,
                  //   ),
                  // ),
                  new InkWell(
                    child: Container(
                        alignment: Alignment.centerRight,
                        child: Text(
                          '没有账号？马上注册',
                          style: hintTips,
                        ),
                        padding: new EdgeInsets.fromLTRB(
                            leftRightPadding, 10.0, leftRightPadding, 0.0)),
                    onTap: (() {
                    
                    }),
                  ),
                  new Container(
                    width: 360.0,
                    margin: new EdgeInsets.fromLTRB(10.0, 80.0, 10.0, 0.0),
                    padding: new EdgeInsets.fromLTRB(leftRightPadding,
                        topBottomPadding, leftRightPadding, topBottomPadding),
                    child: Card(
                      // color: Color(0xFF63CA6C),
                      color: Color(0xFF508eff),
                      elevation: 6.0,
                      child: FlatButton(
                          onPressed: () {
                            _postLogin(username.text, password.text);
                          },
                          child: Padding(
                            padding: EdgeInsets.all(10.0),
                            child: Text(
                              '马上登录',
                              style:
                                   TextStyle(color: Colors.white, fontSize: 16.0),
                            ),
                          )),
                    ),
                  )
                ],
              ),
            )
          )
        ],
      )
      
    );
  }
}
