part of 'User.dart';


// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

User _$UserFromJson(Map<String, dynamic> json) {
  return User(
    json['id'] as int,
    json['login_name'] as String,
    json['name'] as String,
    json['telephone'] as dynamic,
  );
}


Map<String, dynamic> _$UserToJson(User instance) => <String, dynamic>{
  'id': instance.id,
  'name': instance.name,
  'loginName': instance.login_name,
  'telephone': instance.telephone,
};