import 'package:flutter/material.dart';
import 'package:dio/dio.dart';
import 'dart:async';
import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:myflutter/common/style/MainStyle.dart';

//封装http 
class NetUtil {
  static const String GET = "get";
  static const String POST = "post"; 
  // static const String _baseurl = "http://192.168.2.124:8911/iotdevice/";
  static const String _baseurl = "http://114.55.104.31:30337/iotdevice/";
  //get请求
  static void get(String url, Function callBack,
      {Map<String, dynamic> params, Function errorCallBack, Function errorBack}) async {
    _request(_baseurl + url, callBack,
        method: GET, params: params, errorCallBack: errorCallBack,  errorBack: errorBack);
  }

  //post请求
  static void post(String url, Function callBack,
      {Map<String, dynamic> params, Function errorCallBack, Function errorBack}) async {
    _request(_baseurl + url, callBack,
        method: POST, params: params, errorCallBack: errorCallBack, errorBack: errorBack);
  }
  //具体的还是要看返回数据的基本结构
  static void _request(String url, Function callBack,
      {String method,
      Map<String, dynamic> params,
      Function errorCallBack,
      Function errorBack}) async {

    if (params != null && params.isNotEmpty) {
      params['isFitst'] = true;
      params.toString();
    }

    String errorMsg = "";
    int statusCode;
    Dio dio = new Dio();

    try {
      Response response;
      if (method == GET) {
        //组合GET请求的参数
        if (params != null && params.isNotEmpty) {
          StringBuffer sb = new StringBuffer("?");
          params.forEach((key, value) {
            sb.write("$key" + "=" + "$value" + "&");
          });
          String paramStr = sb.toString();
          paramStr = paramStr.substring(0, paramStr.length - 1);
          url += paramStr;
        }
        response = await dio.get(url);
      } else {
        if (params != null && params.isNotEmpty) {
          print("asdasdasd--------");
          response = await dio.post(url, data:params);
        } else {
          response = await dio.post(url);
        }
      }

      statusCode = response.statusCode;
      print(statusCode.toString());
      //处理错误部分
      if (statusCode < 0) {
        errorMsg = "网络请求错误,状态码:" + statusCode.toString();
        _handError(errorCallBack, errorMsg);
        return;
      }

      if (callBack != null) {
        final res = json.decode(response.data);
        print(res);
        if(res["code"] == 0){
          callBack(res["data"]);
        }else{
          errorBack(res, res["code"], res["message"]);
        }
      }
    } catch (exception) {
      _handError(errorCallBack, exception.toString());
      print(exception);
    }
  }
  //处理异常
  static void _handError(Function errorCallback, String errorMsg) {
    print(errorMsg);
    if (errorCallback != null) {
      errorCallback(errorMsg);
    }
  }
}

//封装 Drawer
class SmartDrawer extends StatefulWidget {
  final double elevation;
  final Widget child;
  final String semanticLabel;
  final double widthPercent;
  final DrawerCallback callback;

  const SmartDrawer({
    Key key,
    this.elevation = 16.0,
    this.child,
    this.semanticLabel,
    this.widthPercent,
    this.callback,

  })  : assert(widthPercent < 1.0 && widthPercent > 0.0),
        super(key: key);
  @override
  _SmartDrawerState createState() => _SmartDrawerState();
}

class _SmartDrawerState extends State<SmartDrawer> {

  @override
  void initState() {
    if(widget.callback!=null){
      widget.callback(true);
    }
    super.initState();
  }
  @override
  void dispose() {
    if(widget.callback!=null){
      widget.callback(false);
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    assert(debugCheckHasMaterialLocalizations(context));
    String label = widget.semanticLabel;
    switch (defaultTargetPlatform) {
      case TargetPlatform.iOS:
        label = widget.semanticLabel;
        break;
      case TargetPlatform.android:
      case TargetPlatform.fuchsia:
        label = widget.semanticLabel ?? MaterialLocalizations.of(context)?.drawerLabel;
    }
    final double _width = MediaQuery.of(context).size.width * widget.widthPercent;
    return Semantics(
      scopesRoute: true,
      namesRoute: true,
      explicitChildNodes: true,
      label: label,
      child: ConstrainedBox(
        constraints: BoxConstraints.expand(width: _width),
        child: Material(
          elevation: widget.elevation,
          child: widget.child,
        ),
      ),
    );
  }
}


//封装 AppBar
class SmartAppBar extends StatefulWidget implements PreferredSizeWidget{
  @override
  Size get preferredSize => new Size.fromHeight(kToolbarHeight);

  final IconData icons;
  final String text;
  final Color colors;
  final bool centerTitle;
  final List<Widget> actionList;
  

  const SmartAppBar({
    Key key,
    this.icons = Icons.arrow_back_ios,
    this.text,
    this.colors = DlyColors.primaryTheme,
    this.centerTitle = true,
    this.actionList
  })  : super(key: key);
  @override
  _SmartAppBarState createState() => _SmartAppBarState();
}

class _SmartAppBarState extends State<SmartAppBar> {
  @override
  Widget build(BuildContext context) {
    return AppBar(
      leading: IconButton(
        color: widget.colors,
        icon: Icon(widget.icons),
        onPressed: (){
          Navigator.pop(context);
        },
      ),
      title: Text(widget.text ?? "主题", style: TextStyle(color: widget.colors)),
      centerTitle: widget.centerTitle,
      backgroundColor: Color(0xFFffffff),
      actions: widget.actionList,
    ); 
  }
}