package com.cs.alarm

import com.alibaba.fastjson.JSONObject
import com.cs.ParaType
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.CODE
import com.cs.common.units.HttpResult
import com.cs.common.units.ID
import com.cs.common.units.Success
import com.cs.correctPara

/**
 *
 * create by 2018/12/4.
 * @author lipo
 */


/**
 * 查询预警记录
 */
fun queryAlarm(jsonData: JSONObject): HttpResult {


	var result: HttpResult = correctPara("id", jsonData, ParaType.LONG)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	result = Success()

	helper.query("select * from alarm where user_id = ${jsonData[ID]}") {
		result.addRowToData(
			"alarm_list",
			it,
			arrayOf(
				"id",
				"user_id",
				"device_id",
				"cause_id",
				"cause_time",
				"confirm_time",
				"confirm_id",
				"create_time",
				"note",
				"flag"
			)
		)
	}

	return result
}


