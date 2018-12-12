package com.cs.alarm

import com.alibaba.fastjson.JSONObject
import com.cs.ParaType
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.*
import com.cs.correctPara
import com.cs.generateCondition
import com.cs.sortDeskCondition

/**
 *
 * create by 2018/12/4.
 * @author lipo
 */


/**
 * 查询预警记录查询
 */
fun queryAlarmCaue(jsonData: JSONObject): HttpResult {


	var result: HttpResult = correctPara("cause_id", jsonData, ParaType.LONG)
	result = correctPara("device_id", jsonData, ParaType.LONG, result)
	result = correctPara(PAGE_NUMBER, jsonData, ParaType.INT, result)
	result = correctPara(PAGE_COUNT, jsonData, ParaType.INT, result)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	val offset = (jsonData[PAGE_NUMBER] as Int - 1) * jsonData[PAGE_COUNT] as Int
	result = Success()
	val where = generateCondition(jsonData)
	val orderBy = sortDeskCondition(jsonData)

	helper.queryBySet("select * from (select  ac.id,ac.name,ac.type,ac.flag,a.create_time,a.cause_id,a.device_id,d.name as device_name from alarm_cause ac inner join alarm a on ac.id = a.cause_id inner join device d on a.device_id = d.id inner join users u on d.user_id = u.id inner join device_list list2 on d.device_list_id = list2.id where a.user_id = ${jsonData["user_id"]}) t where 1 = 1 $where $orderBy limit ${jsonData[PAGE_COUNT]} offset $offset; ") {
		result.addSetToData(
			"alarm_list",
			it,
			arrayOf(
				"id", "name", "type", "flag", "create_time", "cause_id", "device_id", "device_name"
			)
		)
	}

	return result.put(
		"counts",
		helper.queryWithOneValue("select count(*) from (select  ac.id,ac.name,ac.type,ac.flag,a.create_time,a.cause_id,a.device_id,d.name as device_name from alarm_cause ac inner join alarm a on ac.id = a.cause_id inner join device d on a.device_id = d.id inner join users u on d.user_id = u.id inner join device_list list2 on d.device_list_id = list2.id where a.user_id = ${jsonData["user_id"]}) t where 1 = 1 $where")
	)
}


