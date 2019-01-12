import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:myflutter/common/style/MainStyle.dart';
import 'package:myflutter/common/MainCommon/MainCommon.dart';

class TelPage extends StatefulWidget {
  @override
  TelPageState createState() => new TelPageState();
}

class TelPageState extends State<TelPage> {
  double _fontSize = 18;
  Color _themeColor = DlyColors.primaryTheme;
  static final String _telphone =  "tel:+40040086565";

  Future<void> _makePhoneCall(String url) async {
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      throw 'Could not launch $url';
    }
  }

  _telWidget(String titles, String bodys){
    return RichText(
      text: TextSpan(
        text: titles,
        style: TextStyle(color: Color(0xff111111), fontSize: _fontSize),
        children: <TextSpan>[
          TextSpan(
            text: bodys,
            style: TextStyle(color: Color(0xff555555), fontSize: _fontSize),
          )
        ]
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: SmartAppBar(text: "联系电话"),
      body: Container(
        child: ListView(
          children: <Widget>[
            Padding(
              padding: EdgeInsets.only(top:26),
            ),
            ListTile(
              title: _telWidget("客服电话:", " 400-4008-6565"),
              trailing: IconButton(
                icon: Icon(Icons.phone, color: _themeColor, size: 30),
                onPressed: (){
                  _makePhoneCall(_telphone);
                },
              ),
            ),
            Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.3)),
            ListTile(
              title: _telWidget("工作时间:", " 8:30-17:30(周一到周五）"),
            ),
            Divider(height:1.0, color: Color.fromRGBO(0, 0, 0, 0.3)),
          ],
        ),
      ),
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
  void didUpdateWidget(TelPage oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
  }
  
  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }
}