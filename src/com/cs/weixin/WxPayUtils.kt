package com.cs.weixin

import com.alibaba.fastjson.JSONObject
import com.cs.src.weixin.MyConfigs
import com.github.wxpay.sdk.WXPay
import org.apache.logging.log4j.LogManager
import java.util.*
private val log = LogManager.getLogger("com.cs.weixin")

fun wxGetPrepayId(subject: String, order_number: String, money: Double, openid: String): JSONObject {
	val config = MyConfigs()
	val wxpay = WXPay(config)

	val data = HashMap<String, String>()
	data["body"] = subject
	data["out_trade_no"] = order_number
	data["device_info"] = "WEB"
	data["fee_type"] = "CNY"
	data["total_fee"] = ((money * 100).toInt()).toString()
	data["spbill_create_ip"] = "127.0.0.1"
	data["notify_url"] = "https://aiot.wl1688.net/cardService/wxpayNottify.do"
	data["trade_type"] = "JSAPI"  // 此处指定为公众号支付
	data["product_id"] = System.currentTimeMillis().toString()
	data["openid"] = openid
	val json = JSONObject()
	try {
		val resp = wxpay.unifiedOrder(data)
		resp.forEach { t, u ->
			json[t] = u
		}
		log.debug("得到返回的结果： $resp  \t  $json")
	} catch (e: Exception) {
		log.error(e.message)
	}
	return json
}




fun wxqrcode(subject: String, order_number: String, money: Double): JSONObject {
	val config = MyConfigs()
	val wxpay = WXPay(config)
	val data = HashMap<String, String>()
	data["body"] = subject
	data["out_trade_no"] = order_number
	data["device_info"] = ""
	data["fee_type"] = "CNY"
	data["total_fee"] = ((money * 100).toInt()).toString()
	data["spbill_create_ip"] = "127.0.0.1"
	data["notify_url"] = "https://aiot.wl1688.net/cardService/wxpayNottify.do"
	data["trade_type"] = "NATIVE"

	val json = JSONObject()
	try {
		val resp = wxpay.unifiedOrder(data)
		resp.forEach { t, u ->
			json[t] = u
		}
	} catch (e: Exception) {
		log.error(e.message)
	}
	return json
}