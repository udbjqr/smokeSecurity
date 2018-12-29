import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      home: LoginMains()
    );
  }
}

class LoginMains extends StatefulWidget{
  LoginMains({Key key}): super(key:key);
  @override
  createState() => LoginMainState();
}

class LoginMainState extends State<LoginMains>{
  final TextEditingController  _text = TextEditingController();
  _Dialog(){
    showDialog(
      context: context,
      child: AlertDialog(
        title: Text("123456"),
        content: Text(_text.text),
      )
    );
  }
  @override
 void didChangeDependencies() {
    super.didChangeDependencies();
    ///通过给 controller 的 value 新创建一个 TextEditingValue
    _text.value = new TextEditingValue(text: "给输入框填入参数");
 }

  @override
  Widget build(BuildContext context){
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text("123"),
      ),
      body: ListView(
        children: <Widget>[
          TextField(
            controller: _text,
            decoration: InputDecoration(
              hintText: "haha"
            ),
          ),
          RaisedButton(
            onPressed: _Dialog,
            child: Text("Done"),
          )
        ],
      ),
    );
  }
}