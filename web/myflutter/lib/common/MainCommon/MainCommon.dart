import 'package:flutter/material.dart';
import 'package:dio/dio.dart';
import 'dart:async';
import 'dart:convert';

//封装http 
class NetUtil {
  static const String GET = "get";
  static const String POST = "post"; 
  static const String _baseurl = "http://192.168.2.124:8081/";
  //get请求
  static void get(String url, Function callBack,
      {Map<String, String> params, Function errorCallBack, Function errorBack}) async {
    _request(_baseurl + url, callBack,
        method: GET, params: params, errorCallBack: errorCallBack,  errorBack: errorBack);
  }

  //post请求
  static void post(String url, Function callBack,
      {Map<String, String> params, Function errorCallBack, Function errorBack}) async {
    _request(_baseurl + url, callBack,
        method: POST, params: params, errorCallBack: errorCallBack, errorBack: errorBack);
  }
  //具体的还是要看返回数据的基本结构
  static void _request(String url, Function callBack,
      {String method,
      Map<String, String> params,
      Function errorCallBack,
      Function errorBack}) async {

    if (params != null && params.isNotEmpty) {
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
          response = await dio.post(url, data: params);
        } else {
          response = await dio.post(url);
        }
      }

      statusCode = response.statusCode;

      //处理错误部分
      if (statusCode < 0) {
        errorMsg = "网络请求错误,状态码:" + statusCode.toString();
        _handError(errorCallBack, errorMsg);
        return;
      }

      if (callBack != null) {
        final res = json.decode(response.data);
        if(res["code"] == 0){
          callBack(res["data"]);
        }else{
          errorBack(res["data"],res["code"],res["message"]);
        }
      }
    } catch (exception) {
      _handError(errorCallBack, exception.toString());
    }
  }

  //处理异常
  static void _handError(Function errorCallback, String errorMsg) {
    if (errorCallback != null) {
      errorCallback(errorMsg);
    }
  }
}
