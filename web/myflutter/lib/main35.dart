import 'package:flutter/material.dart';

void main() => runApp(MyApp());
class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    Widget titleSection = new Container(
      padding: const EdgeInsets.all(32.0),
      child: new Row(
        children:[
          new Expanded(
            child: new Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                new Container(
                  padding: const EdgeInsets.only(bottom: 8.0),
                  child: Text("偶来组成第一行",style: new TextStyle(
                    fontWeight: FontWeight.bold
                  )),
                ),
                Text("我来组成第二行",style: new TextStyle(
                  color: Colors.grey[500]
                ))
              ],
            ),
          ),
          FavoriteWidget()
          // Icon(Icons.star,color:Colors.red),
          // Text("41")
        ],
      ),
    );
    Column buildButtonColumn(IconData icons, String label){
      Color color = Theme.of(context).primaryColor;
      return new Column(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Icon(icons,color:color),
          new Container(
            child: Text(
              label,
              style:new TextStyle(
                fontSize: 12.0,
                fontWeight: FontWeight.w400,
                color: color
              )
            ),
          )
        ],
      );
    }
    List<Container> _buildGridList(int count){
      return new List<Container>.generate(count, (int i)=>
        new Container(child: Text(i.toString()))
      );
    }
    Widget buildGrid(){
      return new GridView.extent(
        maxCrossAxisExtent: 150.0,
        padding: new EdgeInsets.all(4.0),
        mainAxisSpacing: 4.0,
        crossAxisSpacing: 4.0,
        children:_buildGridList(10),
      );
    }
    Widget buutonSelection = new Container(
      child: new Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        mainAxisSize: MainAxisSize.max,
        children:[
          buildButtonColumn(Icons.call,"CALL"),
          buildButtonColumn(Icons.near_me,"ROUTE"),
          buildButtonColumn(Icons.share,"SHARE")
        ],
      ),
    );
    Widget textSection = new Container(
      padding: const EdgeInsets.all(32.0),
      child: new Text(
        '''
Lake Oeschinen lies at the foot of the Blüemlisalp in the Bernese Alps. Situated 1,578 meters above sea level, it is one of the larger Alpine Lakes. A gondola ride from Kandersteg, followed by a half-hour walk through pastures and pine forest, leads you to the lake, which warms to 20 degrees Celsius in the summer. Activities enjoyed here include rowing, and riding the summer toboggan run.
        ''',
        softWrap: true,
        style: new TextStyle(
          fontFamily: "Heiti SC",
          letterSpacing: 0.5,
          fontSize: 16.0,
          height: 1.5
        ),
      ),
    );
    
    return new MaterialApp(
      title: "第一个布局",
      theme: new ThemeData(
        primaryColor: Colors.pink[200],
      ),
      home: new Scaffold(
        // body: new Center(child: buildGrid())
        body:new ListView(
          children: <Widget>[
            // new Image.asset(
            //   'images/1.jpg',
            //   width: 600.0,
            //   height: 400.0,
            //   fit: BoxFit.cover,
            // ),
            _parenImageState(),
            titleSection,
            buutonSelection,
            textSection,
            borderFull()
          ],
        ),
      ),
    );
  }
}
class FavoriteWidget extends StatefulWidget{
  @override
  _FullWidgetState createState() => _FullWidgetState();
}

class _FullWidgetState extends State<FavoriteWidget>{
  bool _bool = true;
  String _text = "41";
  _toggle(){
    setState(() {
      _bool = !_bool;
      _text = _bool ? "41" : "40";
    });
  }
  @override
  Widget build(BuildContext context){
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        new Container(
          padding: EdgeInsets.all(0.0),
          child: IconButton(
            icon : _bool ? Icon(Icons.star) : Icon(Icons.star_border),
            color: Colors.pink[200],
            onPressed: _toggle,
          ),
        ),
        Text('$_text')
      ],
    );
  }
}

class borderFull extends StatefulWidget{
  @override
  _borderFullState createState() => _borderFullState();
}

class _borderFullState extends State<borderFull>{
  bool _active = true;
  _activeToole(){
    setState(() {
      _active = !_active;
    });
  }
  @override
  Widget build(BuildContext context){
    return Container(
      child: GestureDetector(
        onTap: _activeToole,
        child: Container(
          child: Center(
            child: Text(_active ? "TRUE":"FALSE"),
          ),
          width: 200,
          height: 200,
          decoration: BoxDecoration(
            color: _active ? Colors.lightGreen[200] : Colors.grey[200]
          ),
        ),
      ),
    );
  }
}

class _parenImageState extends StatefulWidget{
  _parenImage createState() => _parenImage();
}
class _parenImage extends State<_parenImageState>{
  bool _state = true;
  ChangeUrl(val){
    setState(() {
      _state = val;
    });
  }
  @override
  Widget build(BuildContext context){ 
    return Container(
        child:ImageToggle(active:_state,onChanged:ChangeUrl)
    );
  }
}

class ImageToggle extends StatefulWidget{
  ImageToggle({Key key, this.active, @required this.onChanged}) : super(key:key);
  final bool active;
  final ValueChanged<bool> onChanged;
  @override
  ImageToggleState createState() => ImageToggleState();
}

class ImageToggleState extends State<ImageToggle>{
  bool _height = true;

  _hasChanged(){
    widget.onChanged(!widget.active);
  }
  _hasOnTapUp(TapUpDetails detail){
    setState(() {
          _height = true;
        });
  }
  _hasOnDown(TapDownDetails details){
    setState(() {
          _height = false;
        });
  }
  _hasOnCancel(){
    setState(() {
          _height = false;
        });
  }
  @override
  Widget build(BuildContext context){
    return GestureDetector(
      onTap: _hasChanged,
      onTapUp: _hasOnTapUp,
      onTapDown: _hasOnDown,
      onTapCancel: _hasOnCancel,
      child: Container(
        child: ClipRRect(
          borderRadius: BorderRadius.circular(15.0),
          child:  Image.asset(
            widget.active ? 'images/2.jpg' : 'images/1.jpg',
            width: 600,
            height: 350,
            fit: BoxFit.cover,
          ),
        ),
        decoration: BoxDecoration(
          color:widget.active ? Colors.blue[200] : Colors.grey,
          shape: BoxShape.rectangle,
          borderRadius: BorderRadius.circular(6.0),
          border: _height ? Border.all(
            color: Colors.pink[100],
            width: 10,
          ) : null ,
        ),
      ) 
    );
  }

}