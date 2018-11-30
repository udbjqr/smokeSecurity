package com.example.cs.src

import com.alibaba.fastjson.JSONObject
import com.cs.common.dbHelper.JDBCHelperFactory
import com.example.common.units.Config
import com.example.common.units.FileUtil
import common.units.*
import io.ktor.application.ApplicationCall
import io.ktor.request.receiveText
import org.apache.logging.log4j.LogManager
import sun.tools.jstat.Operator

/**
 *
 * create by 2018/11/30.
 * @author lipo
 */

private val log = LogManager.getLogger(FileUtil::class.java.name)

suspend fun UserInfoLogin(call: ApplicationCall) {

    val data = JSONObject.parseObject(call.receiveText())


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

    val user: Operator = OperatorFactory.get("login_name", loginName.replace(" ", "")) ?: return NotFoundObject().also { it.message = "未找到该用户" }

    if (user.login(password)) {

        val count = JDBCHelperFactory.helper.queryWithOneValue<Int>("select count(*) from user_info where department_id in (select id from department where id = 3184 or superior_id = 3184) and id = ${user.id}")!!.toInt()

        val bool = count > 0
        return Success().also {
            it.addInfoToData("user", user, arrayOf(ID, "real_name", "login_name")).put("is_saler",bool)
        }
    log.debug(json)

}