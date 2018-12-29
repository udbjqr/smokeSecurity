@file:Suppress("DUPLICATE_LABEL_IN_WHEN", "UNUSED_EXPRESSION")

package com

import com.alibaba.fastjson.JSONObject
import com.common.units.Config
import com.common.units.loadConfig
import com.cs.alarm.queryAlarm
import com.cs.alarm.queryAlarmCaue
import com.cs.common.units.CODE
import com.cs.common.util.InternalConstant.TOKEN
import com.cs.common.util.InternalConstant.TOKEN_SPLIT
import com.cs.device.*
import com.cs.isLogin
import com.cs.place.*
import com.cs.users.*
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.ContentType
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.receiveOrNull
import io.ktor.response.respondText
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.io.core.String
import org.apache.logging.log4j.LogManager

private val log = LogManager.getLogger("com.cs.iot")

fun main(args: Array<String>) {
	loadConfig()
	io.ktor.server.cio.EngineMain.main(args)
}

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
	HttpClient(CIO)

	routing {

		static(Config.defaultConfig!!.get<String>("resourcesURL")!!) {
			files(Config.defaultConfig!!.get<String>("resourcesRootPath")!!)
		}

		post("/") {
			call.respondText("Hellow word", contentType = ContentType.Text.Plain)
		}

		post("/iotdevice/getMenu") {
			val data = getText(call)
			log.debug("当前正在获取菜单操作,传过来的值:$data")
			val str = getMenu(data)
			log.debug("当前正在结束获取菜单操作,返回前台的值:$str")
			call.respondText("$str")
		}


//		-------------------------------登陆--------------------------------------


		/**
		 * 客户登陆
		 */
		post("/iotdevice/login") {


			val data = getText(call)
			log.debug("当前正在进行登录操作,传过来的值:$data")
			val str = usersLogin(data, call).toString()
			log.debug("当前正在结束登录操作,返回前台的值:$str")

			call.respondText(str)
		}


		/**
		 * 客户注册
		 */
		post("/iotdevice/regist") {
			val data = getText(call)
			log.debug("当前正在进行客户注册操作,传过来的值:$data")
			val str = usersRegist(data).toString()
			log.debug("当前正在结束客户注册操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 找回密码
		 */

		post("/iotdevice/retrieve") {
			val data = getText(call)
			log.debug("当前正在进行找回密码操作,传过来的值:$data")
			val str = userRetrieve(data).toString()
			log.debug("当前正在结束找回密码操作,返回前台的值:$str")
			call.respondText(str)
		}


		/**
		 * 获取验证码
		 */
		post("/iotdevice/getImage") {

			val str = getImage()
			log.debug("当前正在结束获取验证码操作,返回前台的值:$str")
			call.respondText(str.toString())
		}


//		-------------------------------场所--------------------------------------


		/**
		 * 查询所有场所
		 */
		post("/iotdevice/queryPlace") {
			val data = getText(call)
			log.debug("当前正在查询所有场所操作,传过来的值:$data")
			val str = queryPlace(data).toString()
			log.debug("当前正在结束查询所有场所操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 查询单个场所信息
		 */
		post("/iotdevice/queryByIdPlace") {

			val data = getText(call)
			log.debug("当前正在查询单个场所操作,传过来的值:$data")
			val str = queryByIdPlace(data).toString()
			log.debug("当前正在结束单个场所操作,返回前台的值:$str")
			call.respondText(str)
		}


		/**
		 * 增加场所
		 */
		post("/iotdevice/addPlace") {
			val data = getText(call)
			log.debug("当前正在增加场所操作,传过来的值:$data")
			val str = addPlace(data).toString()
			log.debug("当前正在结束增加场所操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 修改场所
		 */
		post("/iotdevice/updatePlace") {
			val data = getText(call)
			log.debug("当前正在修改场所操作,传过来的值:$data")
			val str = updatePlace(data).toString()
			log.debug("当前正在结束 修改场所操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 删除场所
		 */
		post("/iotdevice/deletePlace") {
			val data = getText(call)
			log.debug("当前正在删除场所操作,传过来的值:$data")
			val str = deletePlace(data).toString()
			log.debug("当前正在结束删除场所操作,返回前台的值:$str")
			call.respondText(str)
		}


		/**
		 * 查看场所其他信息
		 */
		post("/iotdevice/queryPlaceOther") {
			val data = getText(call)
			log.debug("当前正在查看场所其他信息操作,传过来的值:$data")
			val str = queryPlaceOther(data).toString()
			log.debug("当前正在查看场所其他信息操作,返回前台的值:$str")
			call.respondText(str)
		}


		/**
		 * 修改场所其他信息
		 */
		post("/iotdevice/updatePlaceOther") {
			val data = getText(call)
			log.debug("当前正在修改场所其他信息操作,传过来的值:$data")
			val str = updatePlaceOther(data).toString()
			log.debug("当前正在修改场所其他信息操作,返回前台的值:$str")
			call.respondText(str)
		}


//		-------------------------------设备--------------------------------------


		/**
		 * 查询所有设备
		 */
		post("/iotdevice/queryDevice") {
			val data = getText(call)
			log.debug("当前正在查询所有设备操作,传过来的值:$data")
			val str = queryDevice(data).toString()
			log.debug("当前正在结束查询所有设备操作,返回前台的值:$str")
			call.respondText(str)
		}


		/**
		 * 查询单个设备详细信息
		 */
		post("/iotdevice/queryByDeviceId") {
			val data = getText(call)
			log.debug("当前正在查询单个设备操作,传过来的值:$data")
			val str = queryByDeviceId(data).toString()
			log.debug("当前正在结束查询单个设备操作,返回前台的值:$str")
			call.respondText(str)
		}


		/**
		 * 增加设备
		 */
		post("/iotdevice/addDevice") {
			val data = getText(call)
			log.debug("当前正在增加设备操作,传过来的值:$data")
			val str = addDevice(data).toString()
			log.debug("当前正在结束增加设备操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 修改设备
		 */
		post("/iotdevice/updateDevice") {
			val data = getText(call)
			log.debug("当前正在修改设备操作,传过来的值:$data")
			val str = updateDevice(data).toString()
			log.debug("当前正在结束 修改设备操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 删除设备
		 */
		post("/iotdevice/deleteDevice") {
			val data = getText(call)
			log.debug("当前正在删除设备操作,传过来的值:$data")
			val str = deleteDevice(data).toString()
			log.debug("当前正在结束删除设备操作,返回前台的值:$str")
			call.respondText(str)
		}


//		--------------------------设备列表--------------------------------------

		/**
		 * 查询所有设备列表
		 */
		post("/iotdevice/queryDeviceList") {
			val data = getText(call)
			log.debug("当前正在查询所有设备列表操作,传过来的值:$data")
			val str = queryDeviceList(data).toString()
			log.debug("当前正在结束查询所有设备列表操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 增加设备列表
		 */
		post("/iotdevice/addDeviceList") {
			val data = getText(call)
			log.debug("当前正在增加设备列表操作,传过来的值:$data")
			val str = addDeviceList(data).toString()
			log.debug("当前正在结束增加设备列表操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 修改设备列表
		 */
		post("/iotdevice/updateDeviceList") {
			val data = getText(call)
			log.debug("当前正在修改设备列表操作,传过来的值:$data")
			val str = updateDeviceList(data).toString()
			log.debug("当前正在结束 修改设备列表操作,返回前台的值:$str")
			call.respondText(str)
		}

		/**
		 * 删除设备列表
		 */
		post("/iotdevice/deleteDeviceList") {
			val data = getText(call)
			log.debug("当前正在删除设备列表操作,传过来的值:$data")
			val str = deleteDeviceList(data).toString()
			log.debug("当前正在结束删除设备列表操作,返回前台的值:$str")
			call.respondText(str)
		}


//-------------------------------报警信息--------------------------------------
		/**
		 * 查询报警记录
		 */
		post("/iotdevice/queryAlarm") {
			val data = getText(call)
			log.debug("当前正在查询报警记录操作,传过来的值:$data")
			val str = queryAlarm(data).toString()
			log.debug("当前正在结束查询报警记录操作,返回前台的值:$str")
			call.respondText(str)

		}


//--------------------------------报警信息--------------------------------------
		/**
		 * 查询报警信息
		 */
		post("/iotdevice/queryAlarmCaue") {
			val data = getText(call)
			log.debug("当前正在查询报警信息操作,传过来的值:$data")
			val str = queryAlarmCaue(data).toString()
			log.debug("当前正在结束查询报警信息操作,返回前台的值:$str")
			call.respondText(str)
		}


//--------------------------------判断是否登陆--------------------------------------

		post("/iotdevice/getResToken") {
			log.debug("当前正在判断是否登陆操作,传过来的值:")
			val str = getResToken(call).toString()
			log.debug("当前正在结束判断是否登陆操作,返回前台的值:$str")
			call.respondText(str)
		}


//--------------------------------登出--------------------------------------

		post("/iotdevice/loginOut") {
			call.response.cookies.append(TOKEN, "0")
			val json = JSONObject()
			json[CODE] = 0
			call.respondText(json.toString())
		}


//--------------------------------获取烟雾报警器的接口信息--------------------------------------

		post("/iotdevice/getSmokeInfo") {
			val data = call.receiveOrNull<ByteArray>()

			log.debug("这个是传输过来的值，打印看看：$data")
		}

	}

}


/**
 * 获取值
 */
suspend fun getText(call: ApplicationCall): JSONObject {
	val data = call.receiveOrNull<ByteArray>()
	if (data != null) {
		val jsonObject = JSONObject.parseObject(String(data)) ?: JSONObject()
		if (null == jsonObject["isFitst"]) {

			log.debug("看看这个值：${isLogin(call, jsonObject)}")
			if (!isLogin(call, jsonObject)) {
				val token = call.request.cookies[TOKEN]
				val userId = token!!.substring(0, token.indexOf('#', 0)).split(TOKEN_SPLIT)[0].toLong()
				log.debug("获取到的userId的值：$userId")
				jsonObject["user_id"] = userId
				return jsonObject
			}
		} else {
			if (null == jsonObject["hashCode"] || null == jsonObject["captcha"]) {
				jsonObject["hashCode"] = "0"
				jsonObject["captcha"] = "0"
			}
		}
		return jsonObject
	} else {
		return JSONObject()
	}
}