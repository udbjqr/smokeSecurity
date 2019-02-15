import 'package:flutter/material.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';

class RegisterPage extends StatefulWidget {
  @override
  RegisterPageState createState() => RegisterPageState();
}

class RegisterPageState extends State<RegisterPage> {
  final double leftRightPadding = 30.0;
  final double topBottomPadding = 4.0;
  var name = TextEditingController();
  var login_name = TextEditingController();
  var telephone = TextEditingController();
  var passwd = TextEditingController();
  bool eyePasswd = true;
  double fontSizes = 15;

  Widget stackWidget(IconData iconName, IconData trailingIconName,TextEditingController textvalue, String hintText, Function btnPressed, {obscureText = false}){
    return Container(
      padding: EdgeInsets.fromLTRB(10, 15, 20, 10),
      child:Stack(
        alignment: Alignment(1, 1),
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Padding(
                padding: EdgeInsets.fromLTRB(5, 10, 10, 0),
                child: Icon(iconName, color: Colors.grey, size: 30)
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
            icon: Icon(trailingIconName, color: Colors.grey,size: 24,),
            onPressed: btnPressed,
          )
        ],
      )
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SmartAppBar(text: "注册"),
      body: ListView(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.fromLTRB(0, 30, 0, 10),
          ),
          stackWidget(Icons.perm_identity, Icons.highlight_off, name, "请输入用户名", () {name.clear();}),
          stackWidget(Icons.perm_identity, Icons.highlight_off, login_name, "请输入登录名", () {login_name.clear();}),
          stackWidget(Icons.stay_current_portrait, Icons.highlight_off, telephone, "请输入手机号", () {telephone.clear();}),
          stackWidget(Icons.lock, Icons.highlight_off, passwd, "请输入密码", () {passwd.clear();}, obscureText : eyePasswd),
          Container(
            width: 360.0,
            margin: new EdgeInsets.fromLTRB(10.0, 40.0, 10.0, 0.0),
            padding: new EdgeInsets.fromLTRB(leftRightPadding,
                topBottomPadding, leftRightPadding, topBottomPadding),
            child: Card(
              // color: Color(0xFF63CA6C),
              color: Color(0xFF508eff),
              elevation: 6.0,
              child: FlatButton(
                  onPressed: () {
                    // _postLogin(username.text, password.text);
                  },
                  child: Padding(
                    padding: EdgeInsets.all(15.0),
                    child: Text(
                      '立即注册',
                      style: TextStyle(color: Colors.white, fontSize: 19.0),
                    ),
                  )),
            ),
          ),
          Container(
            margin: EdgeInsets.fromLTRB(40, 15, 0, 0),
            child: Row(
              children: <Widget>[
                Text("用户注册即代表同意", style: TextStyle(fontSize: fontSizes),),
                InkWell(
                    child: Container(
                        child: Text(
                          '《用户注册协议》', style: TextStyle(color: Color(0xff1361b0), fontSize: fontSizes),
                        ),
                        padding: new EdgeInsets.fromLTRB(
                            2, 0.0, leftRightPadding, 0.0)),
                    onTap: (() {
                    
                    }),
                  ),
              ],
            ),
          ),
        ],
      )
    );
  }
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }
  
  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
  }
  
  @override
  void didUpdateWidget(RegisterPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }
}