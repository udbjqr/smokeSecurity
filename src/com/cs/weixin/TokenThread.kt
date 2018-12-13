package com.cs.src.weixin

import com.alibaba.fastjson.JSONObject
import com.cs.common.util.Decript
import com.cs.common.util.GlobalSave
import com.cs.common.util.UrlPost
import org.apache.logging.log4j.LogManager
import java.util.*
import java.util.UUID


private val log = LogManager.getLogger("com.cs.schedule")


var appId = "wxe795f972eb691695"
var appSecret = "8649babb63e204f25c9fa7baf6bc31aa"

/**
 * 获取access_token
 * @return
 */

fun main(args: Array<String>) {
	getWXsign("https://aiot.wl1688.net")
}


fun getWXToken(): JSONObject? {
	//如果上次获取时间小于25分钟，直接返回token
	val getTime = GlobalSave.getInstance().get("getWXAccessTokenTime", 0L)

	log.debug("${System.currentTimeMillis()} -------  $getTime")

	if (System.currentTimeMillis() - getTime <= 7000000L) {
		log.debug("access_token，ticket的值在当前时间存在 ：{}", Date(System.currentTimeMillis()))
		return GlobalSave.getInstance().get<JSONObject>("WXAccessToken")
	}

	val url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$appId&secret=$appSecret"
	val jsonData = JSONObject()
	val json = UrlPost.sendPostJson(url, "")
	if (null != json!!["access_token"]) {
		val urls =
			"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + json["access_token"].toString() + "&type=jsapi"
		val jsonObject = UrlPost.sendPostJson(urls, "")
		jsonData["access_token"] = json["access_token"]
		jsonData["ticket"] = jsonObject!!["ticket"]
	}
	//保存微信访问token
	GlobalSave.getInstance().set("getWXAccessTokenTime", System.currentTimeMillis())
	GlobalSave.getInstance().set("WXAccessToken", jsonData)
	return jsonData
}

fun getWXsign(urls: String): JSONObject {

	val jsonObject = getWXToken()

	val noncestr = UUID.randomUUID().toString()
	val timestamp = java.lang.Long.toString(System.currentTimeMillis() / 1000)
	val jsapiTicket = jsonObject!!["ticket"].toString()

	val straw = "jsapi_ticket=$jsapiTicket&noncestr=$noncestr&timestamp=$timestamp&url=$urls"
	log.debug("{}", straw)
	//sha1加密
	val signature = Decript.SHA1(straw)

	val json = JSONObject()
	json["noncestr"] = noncestr
	json["timestamp"] = timestamp
	json["signature"] = signature
	json["appid"] = appId

	return json
}

