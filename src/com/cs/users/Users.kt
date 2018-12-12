package com.cs.users

import com.alibaba.fastjson.JSONObject
import com.common.units.Config
import com.cs.*
import com.cs.common.dbHelper.JDBCHelperFactory
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.*
import com.cs.common.util.GlobalSave
import com.cs.common.util.InternalConstant
import com.cs.common.util.StringUtil
import com.cs.entitys.Users
import com.cs.entitys.UsersFactory
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import org.apache.logging.log4j.LogManager

/**
 *
 * create by 2018/11/30.
 * @author lipo
 */
private val log = LogManager.getLogger(Application::class.java.name)

/**
 * 登陆
 */
fun usersLogin(jsonData: JSONObject, call: ApplicationCall): HttpResult {

	var result: HttpResult = correctPara("login_name", jsonData, ParaType.STRING, isNeed = true)
	result = correctPara("password", jsonData, ParaType.STRING, result, isNeed = true)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	val loginName: String
	val password: String
	try {
		loginName = jsonData["login_name"].toString()
		password = jsonData["password"].toString()
	} catch (e: ClassCastException) {
		log.error("参数login_name,password类型转换异常")
		return TypeCasting().also { it.message = "参数login_name,password类型转换异常" }
	} catch (e: NullPointerException) {
		log.error("参数login_name,password或enterprise为空")
		return LackVariable().also { it.message = "参数login_name,password类型转换异常" }
	}

	val user: Users = UsersFactory.get("login_name", loginName.replace(" ", "")) ?: return NotFoundObject().also {
		it.message = "未找到该用户"
	}

	if (user.login(password)) {

		GlobalSave.GLOBAL_SAVE.remove<String>("hashCode")


		refreshToken(generateToken(call, user.id, Config.getIns(GENERIC)!!.get<Long>(LOGIN_KEEP_TIME).toString()), call)

		return Success().also {
			it.addInfoToData("user", user, arrayOf(ID, "name", "login_name", "telephone"))

		}
	}

	return ErrorPassword().also {
		it.put("password", password)
		it.put("login_name", loginName)
	}


}


/**
 * 注册
 */

fun usersRegist(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara("login_name", jsonData, ParaType.STRING, isNeed = true)
	result = correctPara("passwd", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("name", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("openid", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("telephone", jsonData, ParaType.STRING, result, isNeed = true)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	if (helper.queryWithOneValue<Boolean>(
			"select count(*) > 0 from users where login_name = ${JDBCHelperFactory.default.format(
				jsonData["login_name"]
			)}"
		)!!
	) return UnsupportedOperation().put(
		"message",
		"${jsonData["login_name"]} 当前登陆账号存在，请重新输入！"
	)


	if (helper.queryWithOneValue<Boolean>(
			"select count(*) > 0 from users where telephone = ${JDBCHelperFactory.default.format(
				jsonData["telephone"]
			)}"
		)!!
	) return UnsupportedOperation().put(
		"message",
		"${jsonData["telephone"]} 当前手机已经注册账号，请找回密码！"
	)


	if (helper.queryWithOneValue<Boolean>(
			"select count(*) > 0 from users where openid = ${JDBCHelperFactory.default.format(
				jsonData["openid"]
			)}"
		)!!
	) return UnsupportedOperation().put(
		"message",
		"${jsonData["openid"]} 当前openid已经存在"
	)


	val user = UsersFactory.getNewObject()
	jsonData[FLAG] = 1
	if (null == jsonData["openid"]) {
		jsonData["openid"] = "ouBNT0ULdQM_yXssdE10-AuPsySA"
	} else {
		jsonData["openid"] = ""
	}
	jsonData["passwd"] = StringUtil.encrypt(jsonData["passwd"] as String)
	user.setValues(jsonData)
	user.save()

	return Success.it

}


/**
 * 找回密码
 */
fun userRetrieve(jsonData: JSONObject): HttpResult {
	var result: HttpResult = correctPara("verificationCode", jsonData, ParaType.STRING, isNeed = true)
	result = correctPara("telephone", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("passwd", jsonData, ParaType.STRING, result, isNeed = true)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result


	val user = UsersFactory.get("telephone", jsonData["telephone"] as String) ?: return NotFoundObject().put(
		"message",
		"未找到指定对象"
	)

	//todo 这里需要判断短信验证码的code是否相同 暂时没有做

	jsonData["passwd"] = StringUtil.encrypt(jsonData["passwd"] as String)
	user.setValues(jsonData)
	user.update()

	return Success.it
}

/**
 * 获取菜单
 */
fun getMenu(jsonData: JSONObject): HttpResult {

	val result: HttpResult = correctPara("user_id", jsonData, ParaType.LONG, isNeed = true)
	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result
	val users = UsersFactory.get(ID, jsonData["user_id"]!!) ?: return NotFoundObject().put("message", "找不到此用户")
	val menu = users.getMenu()
	return Success().put("menu", menu)
}

/**
 * 判断是否登陆
 */
fun getResToken(call: ApplicationCall): HttpResult {
	if (isLogin(call,JSONObject())) {
		val token = call.request.cookies[InternalConstant.TOKEN]
		val userId = token!!.substring(0, token.indexOf('#', 0)).split(InternalConstant.TOKEN_SPLIT)[0].toLong()
		val user: Users = UsersFactory.get("id", userId) ?: return NotFoundObject()
		return Success().also {
			it.addInfoToData("user", user, arrayOf(ID, "name", "login_name", "telephone"))
		}
	}else{
		return NotLogin()
	}
}