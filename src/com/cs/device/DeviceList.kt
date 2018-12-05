package com.cs.device

import com.alibaba.fastjson.JSONObject
import com.cs.ParaType
import com.cs.common.dbHelper.JDBCHelperFactory
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.*
import com.cs.correctPara
import com.cs.entitys.DeviceListFactory

/**
 *
 * create by 2018/12/3.
 * @author lipo
 */


/**
 * 查询所有的设备列表
 */
fun queryDeviceList(): HttpResult {

	val result: HttpResult = Success()

	helper.queryBySet("select * from device_list where 1 = 1") { its ->
		result.addSetToData(
			"device_list",
			its,
			arrayOf(
				ID,
				"name",
				"image",
				"flag",
				"note",
				"other"
			)
		)
	}


	return result

}


/**
 * 添加设备列表信息
 */
fun addDeviceList(jsonData: JSONObject): HttpResult {


	var result: HttpResult = correctPara("image", jsonData, ParaType.STRING, isNeed = false)
	result = correctPara("other", jsonData, ParaType.JSON, result, isNeed = false)
	result = correctPara("note", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("name", jsonData, ParaType.STRING, result)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	if (helper.queryWithOneValue<Boolean>(
			"select count(*) > 0 from device_list where name = ${JDBCHelperFactory.default.format(jsonData[ID]!!)}"
		)!!
	) return EXISTENCE().put(
		"message",
		"当前型号存在，请核查！"
	)

	val devicelist = DeviceListFactory.getNewObject()
	devicelist.setValues(jsonData)
	devicelist.save()

	return Success.it
}


/**
 * 修改设备列表信息
 */
fun updateDeviceList(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara("image", jsonData, ParaType.STRING, isNeed = false)
	result = correctPara("other", jsonData, ParaType.JSON, result, isNeed = false)
	result = correctPara("note", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("name", jsonData, ParaType.STRING, result)
	result = correctPara("id", jsonData, ParaType.LONG, result)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	if (helper.queryWithOneValue<Boolean>(
			"select count(*) > 0 from device_list where name = ${JDBCHelperFactory.default.format(jsonData[ID]!!)}"
		)!!
	) return EXISTENCE().put(
		"message",
		"当前型号存在，请核查！"
	)

	val devicelist = DeviceListFactory.get(ID, jsonData[ID]!!) ?: return EXISTENCE().put("message", "找不到此对象，请重试")
	devicelist.setValues(jsonData)
	devicelist.update()


	return Success.it
}


/**
 * 删除设备列表信息
 */
fun deleteDeviceList(jsonData: JSONObject): HttpResult {

	val result: HttpResult = correctPara(ID, jsonData, ParaType.LONG)
	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	helper.execute("delete from device_list where id = ${jsonData[ID]!!}")

	return Success.it
}