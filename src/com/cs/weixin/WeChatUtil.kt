package com.cs.weixin

import com.alibaba.fastjson.JSONObject
import com.cs.common.util.UrlPost
import com.common.units.Config
import com.cs.src.weixin.getWXToken

/**
 *
 * create by 2018/9/19.
 * @author lipo
 */

/**
 * 需要传值进来
 */
fun sendWxMessage() {

	val wxCode = Config.getIns("wxCode")!!.get<JSONObject>("wxCode")

	val jsonData = getWXToken()!!
	val url  = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${jsonData["access_token"]!!}"

	wxCode!!["touser"] = "ouBNT0ULdQM_yXssdE10-AuPsySA"

	val data = wxCode["data"] as JSONObject

	val first = data["first"] as JSONObject
	first["value"] = 1

	val keyword1 = data["keyword1"] as JSONObject
	keyword1["value"] = 2

	val keyword2 = data["keyword2"] as JSONObject
	keyword2["value"] = 3

	val remark = data["remark"] as JSONObject
	remark["value"] = 4

	println(wxCode)

	val json = UrlPost.sendPostJson(url, wxCode.toString())

	println(json)

}

