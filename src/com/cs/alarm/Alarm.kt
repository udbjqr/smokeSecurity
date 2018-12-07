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
 * 查询报警记录
 */
fun queryAlarm(jsonData: JSONObject): HttpResult {


	var result: HttpResult = correctPara("user_id", jsonData, ParaType.LONG)
	result = correctPara(PAGE_NUMBER, jsonData, ParaType.INT, result)
	result = correctPara(PAGE_COUNT, jsonData, ParaType.INT, result)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	val offset = (jsonData[PAGE_NUMBER] as Int - 1) * jsonData[PAGE_COUNT] as Int
	result = Success()
	val where = generateCondition(jsonData)
	val orderBy = sortDeskCondition(jsonData)

	helper.queryBySet("select * from (select a.id,a.user_id,a.device_id,a.cause_id,a.cause_time,a.confirm_time,a.confirm_id,a.create_time,a.note,a.flag,d.name as device_name from alarm a inner join device d on a.device_id = d.id  where a.user_id =  ${jsonData["user_id"]})  t where 1 = 1 $where $orderBy limit ${jsonData[PAGE_COUNT]} offset $offset; ") {
		result.addSetToData(
			"alarm_list",
			it,
			arrayOf(
				"id",
				"user_id",
				"device_id",
				"cause_id",
				"cause_time",
				"confirm_time",
				"device_name",
				"confirm_id",
				"create_time",
				"note",
				"flag"
			)
		)
	}

	return result
}


