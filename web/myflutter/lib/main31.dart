import 'package:flutter/material.dart';

void main() => runApp(Stacks());

class Stacks extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return new MaterialApp(
      home: new BuildStack()
    );
  }
}

class BuildStack extends StatefulWidget{
  _BuildStackList createState() => _BuildStackList();
}

class _BuildStackList extends State<BuildStack>{
  @override
  Widget build(BuildContext context){
    return Scaffold(
      body: new Center(
        // child: Stack(
        //   alignment: Alignment(-0.6, 0.6),
        //   children: <Widget>[
        //     CircleAvatar(
        //       backgroundImage: AssetImage('images/1.jpg'),
        //       radius: 100.0,
        //     ),
        //     Container(
        //       decoration: BoxDecoration(
        //         color: Colors.black45
        //       ),
        //       child: Text(
        //         "hahaha",
        //         style: TextStyle(
        //           fontSize: 20,
        //           fontWeight: FontWeight.w600,
        //           color: Colors.pink[200]
        //         ),
        //       ),
        //     )
        //   ],
        // )
        child: Card(
          child: Column(
            children: <Widget>[
              ListTile(
                title: Text('kk'),
                subtitle: Text('haha'),
                leading: Icon(
                  Icons.star
                ),
              ),
              Divider(),
              ListTile()
            ],
          ),
        ),
      )
    );
  }
}