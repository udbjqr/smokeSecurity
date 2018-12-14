package com.example

import com.common.units.Config
import com.cs.weixin.sendWxMessage

/**
 *
 * create by 2018/12/12.
 * @author lipo
 */


fun main(args: Array<String>) {
	Config.createConfig("wxCode", "wx_code.json")
	sendWxMessage()
}
