package com.cs.device

import com.alibaba.fastjson.JSONObject
import com.cs.ParaType
import com.cs.common.dbHelper.JDBCHelperFactory
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.*
import com.cs.correctPara
import com.cs.entitys.DeviceFactory
import com.cs.generateCondition

/**
 *
 * create by 2018/12/3.
 * @author lipo
 */


/**
 * 查询设备
 */

fun queryDevice(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara("user_id", jsonData, ParaType.LONG, isNeed = true)
	correctPara("place_id", jsonData, ParaType.LONG, result, isNeed = true)
	correctPara("id", jsonData, ParaType.LONG, result, isNeed = false)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	result = Success()

	val where = generateCondition(jsonData)

	helper.queryBySet("select * from (select d.id,d.last_connect_time,d.msisdn,u.name as user_id,p.name as place_id,list2.name as device_list_id,d.name,d.create_time,a.name as admin_id,d.note,d.flag from device d inner join administrator a on d.admin_id = a.id inner join users u on d.user_id = u.id inner join place p on d.place_id = p.id inner join device_list list2 on d.device_list_id = list2.id where d.user_id =  ${jsonData["user_id"]} and place_id = ${jsonData["place_id"]} ) t where 1 = 1 $where") { its ->
		result.addSetToData(
			"device_list",
			its,
			arrayOf(
				ID,
				MSISDN,
				"user_id",
				"place_id",
				"device_list_id",
				"name",
				"create_time",
				"admin_id",
				"last_connect_time",
				"note",
				"flag"
			)
		)
	}


	return result
}


/**
 * 添加设备
 */
fun addDevice(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara(MSISDN, jsonData, ParaType.LONG)
	result = correctPara("user_id", jsonData, ParaType.LONG, result)
	result = correctPara("place_id", jsonData, ParaType.LONG, result, isNeed = false)
	result = correctPara("device_list_id", jsonData, ParaType.LONG, result, isNeed = false)
	result = correctPara("name", jsonData, ParaType.STRING, result)
	result = correctPara("admin_id", jsonData, ParaType.LONG, result)
	result = correctPara("note", jsonData, ParaType.STRING, result, isNeed = false)


	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result


	val device = DeviceFactory.getNewObject()
	device.setValues(jsonData)
	device.save()

	return Success.it
}

/**
 * 修改设备信息
 */
fun updateDevice(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara(MSISDN, jsonData, ParaType.LONG)
	result = correctPara("user_id", jsonData, ParaType.LONG, result)
	result = correctPara("place_id", jsonData, ParaType.LONG, result, isNeed = false)
	result = correctPara("device_list_id", jsonData, ParaType.LONG, result, isNeed = false)
	result = correctPara("name", jsonData, ParaType.STRING, result)
	result = correctPara("admin_id", jsonData, ParaType.LONG, result)
	result = correctPara("id", jsonData, ParaType.LONG, result)
	result = correctPara("note", jsonData, ParaType.STRING, result, isNeed = false)


	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	val device = DeviceFactory.get(ID, jsonData[ID]!!) ?: return NotFoundObject().put("message", "找不到此对象，请核对信息！")

	device.setValues(jsonData)

	device.update()

	return Success.it
}


/**
 * 删除设备
 */
fun deleteDevice(jsonData: JSONObject): HttpResult {

	val result: HttpResult = correctPara(ID, jsonData, ParaType.LONG)
	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	JDBCHelperFactory.helper.execute("update device set flag = 99 where id = ${jsonData[ID]}")

	return Success.it

}