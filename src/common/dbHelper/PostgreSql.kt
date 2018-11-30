package com.cs.common.dbHelper

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.text.DecimalFormat
import java.util.*


/**
 *
 * create by 2017/6/1.
 * @author yimin
 */
class PostgreSql(trim: String, connection: Connection) : AbstractJDBCHelper(connection) {
	private val trim = "$$trim$"

	override fun format(value: Any?): String {
		if (value == null) return "null"

		return when (value) {
			is Double -> if (value <= 9999999.0) value.toString() else DecimalFormat("###0.0000").format(value)
			is String -> "$trim$value$trim"
			is Int -> value.toString()
			is JSONObject -> "$trim$value$trim::jsonb"
			is Date -> "to_timestamp(${value.time / 1000})"
			is Array<*> -> {
				if (value.size == 0) {
					return "null"
				}
				buildString {
					append("ARRAY [ ")
					value.forEach {
						if (it is String) append(trim)
						append("$it")
						if (it is String) append(trim)
						append(",")
					}
					delete(length - 1, length)
					append("]")
				}
			}
			is JSONArray -> {
				if (value.size == 0) {
					return "null"
				}
				buildString {
					append("ARRAY [ ")
					value.forEach {
						if (it is String) append(trim)
						append("$it")
						if (it is String) append(trim)
						append(",")
					}
					delete(length - 1, length)
					append("]")
				}
			}
			else -> "$trim$value$trim"
		}
	}

	override fun getTableInfo(tableName: String): MutableList<Field> {
		val sql = "WITH class AS (SELECT to_regclass('$tableName') :: OID AS id), pk AS (select a.attname from pg_constraint inner join pg_attribute a on a.attnum = pg_constraint.conkey[1] where pg_constraint.contype = 'p' and pg_constraint.conrelid = (select id from class) and a.attrelid = (select id from class)) SELECT a.attname, a.atttypid, t.typname, a.attndims, a.atttypmod, a.attnotnull, pg_get_expr(ad.adbin, a.attrelid), des.description, exists(select 1 from pk where pk.attname = a.attname) as ispk, pg_get_expr(ad.adbin, a.attrelid)~\$a\$^nextval\\('.{1,}'::regclass\\)\$\$a\$ AS isSequ FROM pg_attribute a INNER JOIN pg_type t ON a.atttypid = t.oid LEFT JOIN pg_attrdef ad ON a.attnum = ad.adnum AND a.attrelid = ad.adrelid LEFT JOIN pg_description des ON a.attrelid = des.objoid AND a.attnum = des.objsubid WHERE a.attrelid = (select id from class) AND a.attnum > 0 AND attisdropped = FALSE ORDER BY ispk desc,a.attnum"


		val structure = ArrayList<Field>()
		query(sql) {
			val type = when (it.getInt("atttypid")) {
				1043 -> ColumnType.VARCHAR
				23 -> ColumnType.INT4
				1082,1114 -> ColumnType.TIMESTAMP
				1007 -> ColumnType.INT4_ARRAY
				3802, 114 -> ColumnType.JSON
				790 -> ColumnType.MONEY
				1015 -> ColumnType.VARCHAR_ARRAY
				1700 -> ColumnType.NUMERIC
				2950 -> ColumnType.UUID
				16 -> ColumnType.BOOLEAN
				1005 -> ColumnType.INT2_ARRAY
				20 -> ColumnType.INT8
				else -> ColumnType.UNKNOWN
			}

			structure.add(Field(it.getString("attname"), type, it.getBoolean("attnotnull"), it.getString("pg_get_expr")
				?: "", it.getString("description") ?: "", it.getBoolean("ispk"), it.getBoolean("isSequ")))
		}

		return structure
	}
}
