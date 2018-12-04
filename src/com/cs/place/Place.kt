package com.cs.place

import com.alibaba.fastjson.JSONObject
import com.cs.ParaType
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import com.cs.common.units.*
import com.cs.correctPara
import com.cs.entitys.PlaceFactory

/**
 *
 * create by 2018/12/3.
 * @author lipo
 */

/**
 * 查询所有场所
 */
fun queryPlace(jsonData: JSONObject): HttpResult {
	var result: HttpResult = correctPara("user_id", jsonData, ParaType.LONG, isNeed = true)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result

	result = Success()

	helper.queryBySet("select t.* from (select pl.id,pl.flag,p.name as pro_name,c2.name as city_name,r.name as reg_name,u.name as user_id ,pl.name,pl.address,pl.create_time,pl.administrator,pl.admin_phone,pl.note from  place pl inner join province p inner join city c2 on p.id = c2.province_id inner join region r on c2.id = r.city_id on pl.region_id = r.id inner join users u on pl.user_id = u.id where user_id =  ${jsonData["user_id"]!!} and pl.flag <> 99) t where 1 = 1 ;") { its ->
		result.addSetToData(
			"place_list",
			its,
			arrayOf(
				ID,
				"pro_name",
				"city_name",
				"user_id",
				"reg_name",
				"name",
				"address",
				"create_time",
				"administrator",
				"admin_phone",
				"note",
				"flag"
			)
		)
	}


	return result
}


/**
 * 添加场所
 */
fun addPlace(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara("user_id", jsonData, ParaType.LONG, isNeed = true)
	result = correctPara("region_id", jsonData, ParaType.LONG, result, isNeed = true)
	result = correctPara("name", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("address", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("administrator", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("admin_phone", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("note", jsonData, ParaType.STRING, result, isNeed = false)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result


	if (!helper.queryWithOneValue<Boolean>("select count(*) = 1 from users where id = ${jsonData["user_id"]}")!!) return NotFoundObject().put(
		"message",
		"找不到此用户，请核对"
	)


	if (!helper.queryWithOneValue<Boolean>("select count(*) = 1 from region where id = ${jsonData["region_id"]}")!!) return NotFoundObject().put(
		"message",
		"找不到此地址信息，请核对"
	)


	val place = PlaceFactory.getNewObject()
	jsonData[FLAG] = 1

	place.setValues(jsonData)
	place.save()

	return Success.it
}


/**
 * 修改场所信息
 */
fun updatePlace(jsonData: JSONObject): HttpResult {

	var result: HttpResult = correctPara("user_id", jsonData, ParaType.LONG, isNeed = true)
	result = correctPara("region_id", jsonData, ParaType.LONG, result, isNeed = true)
	result = correctPara("name", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("address", jsonData, ParaType.STRING, result, isNeed = true)
	result = correctPara("administrator", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("admin_phone", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("note", jsonData, ParaType.STRING, result, isNeed = false)
	result = correctPara("id", jsonData, ParaType.LONG, result, isNeed = true)

	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result


	if (!helper.queryWithOneValue<Boolean>("select count(*) = 1 from users where id = ${jsonData["user_id"]}")!!) return NotFoundObject().put(
		"message",
		"找不到此用户，请核对"
	)


	if (helper.queryWithOneValue<Boolean>("select count(*) = 1 from region where id = ${jsonData["region_id"]}")!!) return NotFoundObject().put(
		"message",
		"找不到此地址信息，请核对"
	)


	val place = PlaceFactory.get(ID, jsonData[ID]!!) ?: return NotFoundObject().put("message", "找不到此对象，请核对")

	place.setValues(jsonData)
	place.update()

	return Success.it
}


/**
 * 删除场所
 */
fun deletePlace(jsonData: JSONObject): HttpResult {

	val result: HttpResult = correctPara(ID, jsonData, ParaType.LONG)
	if (JSONObject.parseObject(result.toString())[CODE] != 0) return result


	helper.execute("update place set flag = 99 where id = ${jsonData[ID]}")

	return Success.it
}