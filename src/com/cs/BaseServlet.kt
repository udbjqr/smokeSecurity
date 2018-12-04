package com.cs

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.cs.common.dbHelper.JDBCHelperFactory
import com.cs.common.units.*
import com.cs.common.util.InternalConstant.DATE_FORMAT
import io.ktor.application.ApplicationCall
import org.apache.logging.log4j.LogManager
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * create by 2018/11/30.
 * @author lipo
 */

val log = LogManager.getLogger(ApplicationCall::class.java.name)!!


/**
 * 判断_type的值是否存在
 */

fun correctPara(
	name: String,
	data: JSONObject,
	type: ParaType,
	result: HttpResult? = null,
	minValue: Any? = null,
	maxValue: Any? = null,
	isNeed: Boolean = true
): HttpResult {

	val thisResult = result ?: Success.it

	thisResult.found = false


	if (isNeed) {
		if (!data.containsKey(name)) {
			return writeCorrectParaResult(thisResult, "\"$name\":\"not_found\"")
		}
	} else if (!data.containsKey(name)) {
		return thisResult
	}

	val value = data[name] ?: return writeCorrectParaResult(thisResult, "\"$name\":{\"value\":null}")

	return when (type) {
		ParaType.LONG -> {
			val v = (value as? Int)?.toLong() ?: value
			data[name] = v
			if ((v is Long) && (v <= (maxValue as Long?) ?: Long.MAX_VALUE) && (v >= (minValue as Long?) ?: Long.MIN_VALUE)) {
				thisResult
			} else {
				writeCorrectParaResult(
					thisResult,
					"\"$name\":{\"value\":\"$v\",\"type\":\"${v::class.java}\",\"need_type\":\"${type.name}\",\"min_value\":$minValue,\"max_value\":$maxValue}"
				)
			}
		}

		ParaType.INT -> if ((value is Int) && (value <= (maxValue as Int?) ?: Int.MAX_VALUE) && (value >= (minValue as Int?) ?: Int.MIN_VALUE)) {
			thisResult
		} else {
			writeCorrectParaResult(
				thisResult,
				"\"$name\":{\"value\":\"$value\",\"type\":\"${value::class.java}\",\"need_type\":\"${type.name}\",\"min_value\":$minValue,\"max_value\":$maxValue}"
			)
		}
		ParaType.STRING -> if ((value is String) && (value.length <= (maxValue as Int?) ?: Int.MAX_VALUE) && (value.length >= (minValue as Int?) ?: Int.MIN_VALUE)) {
			data[name] = value.replace("\\", "").trim()
			thisResult
		} else {
			writeCorrectParaResult(
				thisResult,
				"\"$name\":{\"value\":\"$value\",\"type\":\"${value::class.java.simpleName}\",\"need_type\":\"${type.name}\",\"min_value\":$minValue,\"max_value\":$maxValue}"
			)
		}


		ParaType.DATETIME -> if ((value is String) && value.length == 19) {
			val format = SimpleDateFormat(DATE_FORMAT)
			val date =
				try {
					format.parse(value)
				} catch (e: Exception) {
					return writeCorrectParaResult(
						thisResult,
						"\"$name\":{\"value\":\"$value\",\"message\":\"无法转换成日期格式.\"}"
					)
				}
			val minDate = if (minValue == null) 0L else (minValue as Date).time
			val maxDate = if (maxValue == null) Long.MAX_VALUE else (maxValue as Date).time
			if (date.time > maxDate || date.time < minDate) {
				writeCorrectParaResult(
					thisResult,
					"\"$name\":{\"value\":\"$value\",\"type\":\"${value::class.java.simpleName}\",\"need_type\":\"${type.name}\",\"min_value\":${format.format(
						minValue
					)},\"max_value\":${format.format(maxValue)}}"
				)
			} else
				thisResult
		} else {
			writeCorrectParaResult(
				thisResult,
				"\"$name\":{\"value\":\"$value\",\"format\":\"$DATE_FORMAT\",\"need_type\":\"String\"}"
			)
		}


		ParaType.DOUBLE -> {
			val v: Double = when (value) {
				is Int -> value.toDouble()
				is Long -> value.toDouble()
				is BigDecimal -> value.toDouble()
				is String -> try {
					value.toDouble()
				} catch (e: Exception) {
					Double.MIN_VALUE
				}
				is Double -> value
				else -> Double.MIN_VALUE
			}
			data[name] = v


			if (v != Double.MIN_VALUE && (v <= (maxValue as Double?) ?: Double.MAX_VALUE) && (v >= (minValue as Double?) ?: 0.0)) {
				thisResult
			} else {
				writeCorrectParaResult(
					thisResult,
					"\"$name\":{\"value\":\"$value\",\"type\":\"${value::class.java}\",\"need_type\":\"${type.name}\",\"min_value\":$minValue,\"max_value\":$maxValue}"
				)
			}
		}
		ParaType.BOOL -> if (value is Boolean) {
			thisResult
		} else {
			writeCorrectParaResult(
				thisResult,
				"\"$name\":{\"value\":$value,\"type\":\"${value::class.java}\",\"need_type\":\"${type.name}\"}"
			)
		}
		ParaType.JSON -> if (value is JSONObject) {
			thisResult
		} else {
			writeCorrectParaResult(
				thisResult,
				"\"$name\":{\"value\":$value,\"type\":\"${value::class.java}\",\"need_type\":\"${type.name}\"}"
			)
		}
		ParaType.ARRAY -> if (value is JSONArray) {
			value.forEach {
				if (it != null && (it.toString().contains("\t") || it.toString().contains("\n"))) {
					writeCorrectParaResult(thisResult, "\"$name\":{\"value\":\"$value\"}")
				}
			}

			thisResult
		} else {
			writeCorrectParaResult(
				thisResult,
				"\"$name\":{\"value\":\"$value\",\"type\":\"${value::class.java}\",\"need_type\":\"${type.name}\"}"
			)
		}
	}
}

enum class ParaType {
	LONG, INT, STRING, DOUBLE, ARRAY, JSON, BOOL, DATETIME
}


private fun writeCorrectParaResult(result: HttpResult, message: String): HttpResult {
	val thisResult = if (result is Success) NoSetRequestType() else result
	thisResult.addToBuilder(message)
	return thisResult
}


/**
 * 根据给出的东西生成条件
 *
 * @param jsonData 包含上传的参数的json,不是servletData
 */
fun generateCondition(
	jsonData: JSONObject,
	special: String? = null,
	block: ((data: JSONObject) -> String)? = null
): String {
	val dbHelp = JDBCHelperFactory.default
	return if (jsonData.containsKey(CONDITION)) {
		if ((jsonData[CONDITION] as JSONArray).size > 0) {
			buildString {
				for (data in (jsonData[CONDITION] as JSONArray)) {
					append(" and ")
					if (data is JSONObject) {
						if (block != null && data[NAME] == special)
							append(block(data))
						else
							when (data["operator"] as String) {
								"like" -> append("cast (t.").append(data[NAME]).append(" as varchar) like ").append(
									dbHelp.format("%${data["value"].toString().trim()}%")
								)

								"between" -> append("t.").append(data[NAME]).append(" between ").append(
									dbHelp.format(
										data["minvalue"]
									)
								).append(" and ").append(dbHelp.format(data["maxvalue"]))

								"in" -> {
									append("t.").append(data[NAME]).append(" in ").append(
										data["value"].toString().replace("[", "(").replace(
											"]",
											")"
										).replace("\"", "\'")
									)
								}

								"or" -> {
									append(" ( ").append("t.").append(data["minvalue"]).append(" = ").append(data[NAME])
										.append(" or ").append("t.").append(data["maxvalue"]).append(" = ")
										.append(dbHelp.format(data[NAME])).append(" ) ")
								}

								else -> append("t.").append(data[NAME]).append(data["operator"]).append(
									dbHelp.format(
										data["value"]
									)
								)
							}
					}
				}
			}
		} else ""
	} else ""
}

/**
 * 根据条件生成 order by + 后续的语句  默认desc排序
 *
 * 传值需要传 sorting 数组   例如：["id desc","name asc"]
 */
fun sortDeskCondition(jsonData: JSONObject, str: String? = null): String {

	val str = str ?: ""

	return if (jsonData["sorting"] != null) {
		val jsonArray = jsonData["sorting"] as JSONArray
		buildString {
			append(" order by ")
			jsonArray.forEach {
				append(" t.").append(it).append(" , ")
			}
			delete(length - 2, length)
		}
	} else " order by 1 desc $str"
}