
import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';

main() {
  runApp(Mains());
  PaintingBinding.instance.imageCache.maximumSize = 100;
}

class Mains extends StatelessWidget{
  final store = Store(

  ) ;
  @override
  Widget build(BuildContext context){

  }
}
