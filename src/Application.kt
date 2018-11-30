@file:Suppress("DUPLICATE_LABEL_IN_WHEN")

package com.example

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.ICCID
import com.cs.common.units.MSISDN
import com.example.com.cs.users.userRetrieve
import com.example.com.cs.users.usersLogin
import com.example.com.cs.users.usersRegist
import com.example.common.units.loadConfig
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.ContentType
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.post
import io.ktor.routing.routing
import org.apache.logging.log4j.LogManager
import kotlin.collections.set

private val log = LogManager.getLogger(Application::class.java.name)

fun main(args: Array<String>): Unit {
    loadConfig()
    io.ktor.server.cio.EngineMain.main(args)

}


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    HttpClient(CIO)

    routing {
        post("/") {
            val array = JSONArray()

            helper.query("select * from card limit 10 ") { its ->
                val json = JSONObject()
                json[ICCID] = its.getString(ICCID)
                json[MSISDN] = its.getLong(MSISDN)
                array.add(json)
                log.debug(its.getLong(MSISDN))
            }
            call.respondText("$array", contentType = ContentType.Text.Plain)
        }

        /**
         * 客户登陆
         */
        post("/login") {
            val data = JSONObject.parseObject(call.receiveText())
            log.debug("当前正在进行登录操作,传过来的值:$data")
            val str = usersLogin(data).toString()
            log.debug("当前正在结束登录操作,返回前台的值:$str")
            call.respondText(str)
        }


        /**
         * 客户注册
         */
        post("/regist") {
            val data = JSONObject.parseObject(call.receiveText())
            log.debug("当前正在进行客户注册操作,传过来的值:$data")
            val str = usersRegist(data).toString()
            log.debug("当前正在结束客户注册操作,返回前台的值:$str")
            call.respondText(str)
        }

        /**
         * 找回密码
         */

        post("/retrieve") {
            val data = JSONObject.parseObject(call.receiveText())
            log.debug("当前正在进行找回密码操作,传过来的值:$data")
            val str = userRetrieve(data).toString()
            log.debug("当前正在结束找回密码操作,返回前台的值:$str")
            call.respondText(str)
        }




    }
}

