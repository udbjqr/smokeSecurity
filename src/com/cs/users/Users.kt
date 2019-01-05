package com.cs.users

import com.alibaba.fastjson.JSONObject
import com.common.units.Config
import com.cs.*
import com.cs.common.dbHelper.JDBCHelperFactory
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.*
import com.cs.common.util.*
import com.cs.entitys.Users
import com.cs.entitys.UsersFactory
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import org.apache.logging.log4j.LogManager
import java.awt.image.RenderedImage
import java.io.FileOutputStream
import javax.imageio.ImageIO

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
	result = correctPara("captcha", jsonData, ParaType.STRING, result)
	result = correctPara("hashCode", jsonData, ParaType.STRING, result)

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

	if (null == jsonData["openid"]) {
		if (null == jsonData["isFitst"] || !(jsonData["isFitst"] as Boolean)) {
			val code = GlobalSave.GLOBAL_SAVE.get<String>(jsonData["hashCode"] as String?)
			if (!jsonData["captcha"].toString().equals(code, true)) return NotFoundCode().put("msg", "验证码输入错误")
		}
	}


	val user: Users = UsersFactory.get("login_name", loginName.replace(" ", "")) ?: return NotFoundObject().also {
		it.message = "未找到该用户"
	}

	if (user.login(password)) {
		if (null == jsonData["openid"]) {
			GlobalSave.GLOBAL_SAVE.remove<String>("hashCode")
		}

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


	val code = GlobalSave.GLOBAL_SAVE.get<String>(jsonData["hashCode"] as String?)
	if (!jsonData["captcha"].toString().equals(code, true)) return NotFoundCode().put("msg", "验证码输入错误")


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
	if (!isLogin(call, JSONObject())) {
		val token = call.request.cookies[InternalConstant.TOKEN]

		//这个是清除tokens的值  判断tokens的值为0 的时候  判断是为登陆的时候
		if (null == token || "null" == token || token == "0") return NotLogin()

		val userId = token!!.substring(0, token.indexOf('#', 0)).split(InternalConstant.TOKEN_SPLIT)[0].toLong()
		val user: Users = UsersFactory.get("id", userId) ?: return NotFoundObject()
		return Success().also {
			it.addInfoToData("user", user, arrayOf(ID, "name", "login_name", "telephone"))
		}
	} else {
		return NotLogin()
	}
}

/**
 * 获取验证码
 */
fun getImage(): HttpResult {
	val time = System.currentTimeMillis()
	val url = "$UPLOAD_DIRECTORY$time.jpg"
	val out = FileOutputStream(url)
	val map = codeUtile.generateCodeAndPic()
	log.debug(map)
	val code = map[CODE].toString()
	GlobalSave.GLOBAL_SAVE.set(time.toString(), code)
	ImageIO.write(map["codePic"] as RenderedImage, "jpeg", out)
	return Success().put("img_url", "$RESOURCES_URL$time.jpg").put("hashCode", time)
}

/**
 * 发送短信
 */
fun sendTextMessage(content: String, phone: String, boolean: Boolean, result: JSONObject? = null): JSONObject {

	log.info("正在发送短信呢！  内容：$content  电话：$phone")

	val gent = Config.getIns(GENERIC)!!.content

	val returnstatus = "<returnstatus>(.*?)</returnstatus>".toRegex()
	val message = "<message>(.*?)</message>".toRegex()
	val remainpoint = "<remainpoint>(.*?)</remainpoint>".toRegex()

	val strMessage = if (boolean) {
		"action=send&userid=${gent["ssm_id"]}&account=${gent["ssm_login"]}&password=${gent["ssm_password"]}&mobile=$phone&content=【物联网】$content&sendTime=&extno="
	} else {
		"action=send&userid=${gent["custom_ssmid"]}&account=${gent["custom_login"]}&password=${gent["custom_password"]}&mobile=$phone&content=【物联网】$content&sendTime=&extno="
	}

	val str = UrlPost.sendPost("http://sms.any163.cn:8888/sms.aspx", strMessage)

	val msg = message.find(str)?.groupValues?.get(1) ?: ""
	val status = returnstatus.find(str)?.groupValues?.get(1) ?: ""
	val count = remainpoint.find(str)?.groupValues?.get(1) ?: ""


	log.debug("发送状态：$status 发送内容：$msg ")

	return (result ?: JSONObject()).apply {
		put("status", status)
		put("message", msg)
		put("count", count)
		put("phone", phone)
		put("content", content)
	}
}