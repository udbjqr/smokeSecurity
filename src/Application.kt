package com.example

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.example.common.units.FileUtil
import com.example.common.units.loadConfig
import common.units.ICCID
import common.units.MSISDN
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(FileUtil::class.java.name)

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
                logger.debug(its.getLong(MSISDN))
            }
            call.respondText("$array", contentType = ContentType.Text.Plain)
        }

        get("/aaa") {
            call.respondText("HELLO WO123123RLD!", contentType = ContentType.Text.Html)
        }


        post("/login") {
        }

    }
}

