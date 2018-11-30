package com.example.cs.src

import com.alibaba.fastjson.JSONObject
import com.example.common.units.FileUtil
import io.ktor.application.ApplicationCall
import io.ktor.request.receiveText
import org.apache.logging.log4j.LogManager

/**
 *
 * create by 2018/11/30.
 * @author lipo
 */

private val log = LogManager.getLogger(FileUtil::class.java.name)

suspend fun UserInfoLogin(call: ApplicationCall) {

    val data = JSONObject.parseObject(call.receiveText())

    log.debug(data)

}