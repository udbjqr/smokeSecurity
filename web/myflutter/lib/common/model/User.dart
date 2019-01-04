import 'package:json_annotation/json_annotation.dart';

part 'User.g.dart';
@JsonSerializable()
class User {
  User(
    this.id,
    this.login_name,
    this.name,
    this.telephone,
  );
  int id;
  String login_name;
  String name;
  dynamic telephone;

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  Map<String, dynamic> toJson() => _$UserToJson(this);

  // 命名构造函数
  User.empty();
}