part of 'User.dart';


// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

User _$UserFromJson(Map<String, dynamic> json) {
  return User(
    json['loginName'] as String,
  );
}


Map<String, dynamic> _$UserToJson(User instance) => <String, dynamic>{
    'loginName': instance.loginName,
};