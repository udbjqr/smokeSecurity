import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux/redux.dart';
import 'package:myflutter/common/redux/mainredux.dart';
import 'package:myflutter/common/style/MainStyle.dart';
import 'package:myflutter/common/model/User.dart';
import 'package:myflutter/common/router/NavigatorRouter.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  final store = Store<mainRedux>(
    addReducer,
    initialState: mainRedux(
      userinfo: User.empty(),
      themeData: ThemeData(
        primarySwatch:DlyColors.primarySwatch 
      )
    )
  );
  
  MyApp({Key key}) : super(key:key);
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context){
    return StoreProvider(
      store: store,
      child: StoreBuilder<mainRedux>(builder: (context, store){
        return MaterialApp(
          // debugShowMaterialGrid:true,
          theme: store.state.themeData,
          routes: NavigatorRouter.routes
        );
      }),
    );
  }
}


// class Mains extends StatelessWidget{
//   final store = Store<mainRedux>(
//     addReducer,
//     initialState: mainRedux(
//       userinfo: User.empty(),
//       themeData: ThemeData(
//         primarySwatch:DlyColors.primarySwatch 
//       )
//     )
//   );
//   @override
//   Mains({Key key}) : super(key:key);

//   Widget build(BuildContext context){
//     return StoreProvider(
//       store: store,
//       child: StoreBuilder<mainRedux>(builder: (context,store){
//         return MaterialApp(
//           title: '劳资是真的牛逼1',
//           theme: store.state.themeData,
//         );
//       }),
//     );
//   }
// }
