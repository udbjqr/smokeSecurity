import 'dart:async';
import 'package:flutter/material.dart';


void main() => runApp(MyApp());


class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: '劳资是真的牛逼1',
      theme: ThemeData(
        primarySwatch: Colors.green,
        primaryColor: Colors.pink[100],
        accentColor:Colors.yellow,
      ),
      home: new DemoStateWidget(text:"kkkkk"),
    );
  }
}


class DemoStateWidget extends StatefulWidget{
  final String text;
  DemoStateWidget({Key key, this.text}) : super(key: key);

  @override
  _DemoStateWidgetState createState() => _DemoStateWidgetState();

}

class _DemoStateWidgetState extends State<DemoStateWidget>{
  String text;
  bool state = true;


  @override
  void initState(){
    super.initState();
    new Future.delayed(const Duration(seconds:1),() {
      setState(() => text = "hahah");
    });
  }


  _incrementCounter(){
    setState(() { 
      text = "撒大声地";
      state = !state;
    });
  }
  @override
    void dispose() {
      // TODO: implement dispose
      super.dispose();
    }

  @override void didChangeDependencies() {
      // TODO: implement didChangeDependencies
      super.didChangeDependencies();
    }

  @override
  Widget build(BuildContext context){
    return Scaffold(
      // body:Container( ///四周10大小的maring
      //   margin: EdgeInsets.all(10.0),
      //   height: 120.0,
      //   width: 500.0,
      //   ///透明黑色遮罩
      //   decoration: new BoxDecoration(
      //       ///弧度为4.0
      //       // borderRadius: BorderRadius.all(Radius.circular(4.0)),
      //       borderRadius: BorderRadius.circular(14.0),
      //       ///设置了decoration的color，就不能设置Container的color。
      //       color: Colors.pink[200],
      //       ///边框
      //       border: new Border.all(color: Colors.green, width: 3)),
      //   child:new Text("666666")
      // ),
      body:Center(
        child: Column(
          // mainAxisAlignment: MainAxisAlignment.center,
          //主轴方向，Colum的竖向、Row我的横向
          mainAxisAlignment: MainAxisAlignment.center, 
          //默认是最大充满、还是根据child显示最小大小
          mainAxisSize: MainAxisSize.min,
          //副轴方向，Colum的横向、Row我的竖向
          crossAxisAlignment :CrossAxisAlignment.center,
          children: <Widget>[
            new Expanded(child: Text(text ?? "ssaaa")),
            new Expanded(child: RaisedButton(
              onPressed:_incrementCounter,
              child: Text("确定"),
            ),flex: 1),
            new Expanded(child: Center(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                mainAxisSize: MainAxisSize.max,
                crossAxisAlignment: CrossAxisAlignment.center,
                
                children: <Widget>[
                  Icon(
                    state ? Icons.star : Icons.star_border,
                    size: 26.0,
                    color: state ? Colors.pink[200] : Colors.green[100],
                  ),
                  Padding(
                    padding: new EdgeInsets.only(left:5.0),
                  ),
                  Text(
                    "1000",
                    style: new TextStyle(
                      color: Colors.blue,fontSize: 24.0
                    ),
                    overflow: TextOverflow.ellipsis,
                    maxLines: 1,
                  )
                ],
              ),
            ))
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ),
    );
  }
}