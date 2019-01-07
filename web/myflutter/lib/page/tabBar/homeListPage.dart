import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class HomeListPage extends StatefulWidget {
  @override
  HomeListPageState createState() => new HomeListPageState();
}

class HomeListPageState extends State<HomeListPage> {
  static int number = 1;
  static Radius radiusBorder = Radius.circular(5.0);
  static String _mainText = "正常";
  
  IconData serecdIcon = Icons.add_circle_outline;

  //Row 图标
  iconButtons(icon, eventChange){
      return IconButton(
        iconSize: 30,
        icon: Icon(icon),
        color: Colors.white,
        onPressed: eventChange,
      );
  }

  // 渲染 TextTile
  _listTile(_text, radiuer){
    return Container(
      padding: EdgeInsets.fromLTRB(5, 5, 10, 5),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: radiuer ? BorderRadius.only(topLeft: radiusBorder,topRight: radiusBorder) 
                              : BorderRadius.only(bottomLeft: radiusBorder,bottomRight: radiusBorder)
      ),
      child: Row(
        children: <Widget>[
          Icon(Icons.add),
          RichText(
            text: TextSpan(
              style: TextStyle(
                color: Colors.black,
                fontSize: 18
              ),
              text: _text
            ),
          )
        ],
      ),
    );
  }

  // Positioned 图标 
  _setIconsAdd(){
    if(number==2){
      return Positioned(
        right: 10,
        top:40,
        child: Icon(Icons.arrow_drop_up,color: Colors.white, size: 35,),
      );
    }else{
      return Center();
    }
  }

  // Positioned 选项
  _setAddWidget(){
    if(number==2){
      return Positioned(
        right: 10,
        top:60,
        child: Column(
            children: <Widget>[
              _listTile("添加场所", true),
              _listTile("添加设备", false),
            ],
          )
      );
    }else{
      return Center();
    }
  }
  
  //main 圆圈 显示状态
  _setCircle(){
    return Center(
      child: Container(
        width: 140,
        height: 140,
        child: Container(
          width: 120,
          height: 120,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(100),
            color: Colors.white
          ),
          child: Text(_mainText,style: TextStyle(fontSize: 27, height:2.1), textAlign: TextAlign.center),
        ),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(100),
          border: Border.all(
            color: Color.fromRGBO(255, 255, 255, 0.4),
            width: 15
          )
        ),
      ),
    );
  }
  // main 按钮
  _setMainButton(){
    TapGestureRecognizer _recognizer = TapGestureRecognizer();
    _recognizer.onTap = (){
      print("object");
    };
    return RichText(
      text: TextSpan(
        style: TextStyle(
          color: Colors.white,
          fontSize: 18
        ),
        text: "我的场所",
        recognizer: _recognizer
      ),
    );
  }

  // ? 点击事件
  eventHelp(){}
  // + 点击事件
  eventAdd(){
    setState(() {
      number = number == 1 ? 2 : 1;
      serecdIcon = number == 1 ? Icons.add_circle_outline : Icons.highlight_off;
    });
  }
  MainDiv(){
    return Stack(
      children: <Widget>[
        Container(
          height: 295,
          width: 500,
          decoration: BoxDecoration(
            gradient: LinearGradient(
              colors: [
                Color(0xff4050f7),
                Color(0xff1cccf7),
              ],
              begin: Alignment.topLeft,
              end: Alignment.bottomRight
            )
          ),
          child: Column(
            children: <Widget>[
              Padding(
                padding: EdgeInsets.fromLTRB(0, 8, 0, 0),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: <Widget>[
                  iconButtons(Icons.help_outline, eventHelp),
                  iconButtons(serecdIcon, eventAdd)
                ],
              ),
              _setCircle(),
              _setMainButton()
            ],
          ),
        ),
        _setIconsAdd(),
        _setAddWidget()
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: <Widget>[
        MainDiv()
      ],
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
  void didUpdateWidget(HomeListPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }
}