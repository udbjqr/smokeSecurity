package com.cs.weixin

import com.alibaba.fastjson.JSONObject
import com.common.units.Config
import com.cs.common.util.UrlPost
import com.cs.src.weixin.getWXToken
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date

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
	val url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${jsonData["access_token"]!!}"
//	ouBNT0a5kKbvJCcbo4p_QttkgkMc
	//ouBNT0ULdQM_yXssdE10-AuPsySA
	wxCode!!["touser"] = "ouBNT0ULdQM_yXssdE10-AuPsySA"
	wxCode["url"] = "https://aiot.wl1688.net/card/#/MainActive?msisdn=861064647764001&code=1"

	val data = wxCode["data"] as JSONObject

	val first = data["first"] as JSONObject
	first["value"] = "订单已经完成！！"

	val keyword1 = data["keyword1"] as JSONObject
	keyword1["value"] = "充值加油包"

	val keyword2 = data["keyword2"] as JSONObject
	keyword2["value"] = "${date()}"

	val remark = data["remark"] as JSONObject
	remark["value"] = "欢迎再次购买！"

	println(wxCode)

	val json = UrlPost.sendPostJson(url, wxCode.toString())

	println(json)

}

