package common.units

import com.cs.common.persistence.Persistence
import com.cs.common.utils.StringUtil
import java.sql.ResultSet

/**
 *
 * create by 2018/3/30.
 * @author yimin
 */
open class HttpResult(private val code: Int, var message: String) {
	private var builder = StringBuilder("{}")
	var found = true
	var data: String
		get() = builder.toString()
		set(value) {
			builder = StringBuilder(value)
		}

	fun returnMessage(value: String): HttpResult {
		message = value
		return this
	}


	/**
	 * 用以将数据库对应的列表字段转成对象发送出去
	 * 如果原有数据,将继续加入
	 *
	 * @param dataName    需要向前台显示的名称
	 * @param set     需要转换的列表
	 * @param columns 需要转换的列表当中某些列
	 * @param columnName  需要向前台显示名称,对应列名称
	 */
	fun addSetToData(dataName: String, set: ResultSet, columns: Array<String>, columnName: Array<String>? = null) {
		val cName = columnName ?: columns
		val tempBuilder = StringBuilder().also {
			while (set.next()) {
				if (it.isNotEmpty()) {
					it.append(",")
				}

				it.append("{")
				for (i in columns.indices) {
					if (i > 0) {
						it.append(",")
					}

					it.append("\"").append(cName[i]).append("\":").append(StringUtil.converToJSONString(set.getObject(columns[i])))
				}
				it.append("}")
			}

			it.insert(0, "\":[").insert(0, dataName).insert(0, "\"").append("]")
		}

		addToBuilder(tempBuilder.toString())
	}


	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 *
	 * @param dataName    需要向前台显示的名称
	 * @param lists   需要转换的列表
	 * @param columns 需要转换的列表当中某些列
	 * @param columnName  需要向前台显示名称,对应列名称
	 *
	 */
	@Suppress("unused")
	fun addListToData(dataName: String, lists: List<Persistence>, columns: Array<String>, columnName: Array<String>? = null) {
		val cName = columnName ?: columns
		val tempBuilder = StringBuilder()

		for (item in lists) {
			addJsonToBuilder(tempBuilder, item, columns, cName)
			tempBuilder.append(",")
		}

		if (tempBuilder.isNotEmpty()) {
			tempBuilder.delete(tempBuilder.length - 1, tempBuilder.length)
		}

		tempBuilder.insert(0, "\":[").insert(0, dataName).insert(0, "\"").append("]")

		addToBuilder(tempBuilder.toString())
	}


	private fun addJsonToBuilder(builder: StringBuilder, item: Persistence, columns: Array<String>, columnName: Array<String>) {
		builder.append("{")
		for (i in columns.indices) {
			if (i > 0) {
				builder.append(",")
			}

			builder.append("\"").append(columnName[i]).append("\":")
				.append(StringUtil.converToJSONString(item[columns[i]]))
		}

		builder.append("}")
	}


	/**
	 * 用以将一条数据加入至返回列表当中
	 * 如果原有数据,将继续加入
	 *
	 * @param dataName    需要向前台显示的名称
	 * @param set     需要转换的列表
	 * @param columns 需要转换的列表当中某些列
	 * @param columnName  需要向前台显示名称,对应列名称
	 */
	fun addRowToData(dataName: String, set: ResultSet, columns: Array<String>, columnName: Array<String>? = null) {
		val cName = columnName ?: columns
		val tempBuilder = StringBuilder().also {
			if (it.isNotEmpty()) {
				it.append(",")
			}

			it.append("{")
			for (i in columns.indices) {
				if (i > 0) {
					it.append(",")
				}

				it.append("\"").append(cName[i]).append("\":").append(StringUtil.converToJSONString(set.getObject(columns[i])))
			}
			it.append("}")

			it.insert(0, "\"$dataName\":")
		}

		addToBuilder(tempBuilder.toString())
	}

	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 * 此方法处理单条数据的插入
	 *
	 *
	 * @param name        需要向前台显示的名称
	 * @param persistence 数据对象。
	 * @param columns     需要转换的列表当中某些列
	 * @param columnName  需要向前台显示名称,对应列名称
	 *
	 */
	fun addInfoToData(name: String, persistence: Persistence, columns: Array<String>, columnName: Array<String>? = null): HttpResult {
		val cName = columnName ?: columns

		val tempBuilder = StringBuilder().also {
			it.append("\"").append(name).append("\"").append(":")
			addJsonToBuilder(it, persistence, columns, cName)
		}

		addToBuilder(tempBuilder.toString())
		return this
	}

	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 * 此值仅放在DATA的最上面一层
	 *
	 * @param name  需要向前台显示的名称
	 * @param value 值
	 */
	fun put(name: String, value: Any?): HttpResult {
		addToBuilder("\"$name\":${StringUtil.converToJSONString(value)}")
		return this
	}

	fun addToBuilder(value: String): HttpResult {
		if (this.builder.length > 2) {
			this.builder.insert(1, ",").insert(1, value)
		} else {
			this.builder.insert(1, value)
		}

		found = true

		return this
	}

	override fun toString(): String {
		return "{\"code\":$code,\"data\":${this.builder},\"message\":\"$message\"}"
	}

	override fun equals(other: Any?): Boolean {
		if (other != null && other is HttpResult) {
			return this.code == other.code
		}

		return false
	}

	override fun hashCode(): Int {
		return code
	}

}


class Unknown : HttpResult(-1, "未知错误")

class Success : HttpResult(0, "成功") {
	companion object {
		val it = Success()
	}
}

class TypeCasting : HttpResult(116, "类型转换异常")
class NoSetRequestType : HttpResult(3, "参数类型不正确，请检查")
class UnsupportedOperation : HttpResult(4, "当前不支持此操作")
class NotLoginNull : HttpResult(9, "用户未登录！")
class LackVariable : HttpResult(102, "缺少必须的参数")
class NotFoundObject : HttpResult(101, "未找到指定对象")
class ErrorPassword : HttpResult(100, "用户名或密码错误")
class HaveSameObject : HttpResult(103, "已经存在相同的对象")
class NotOperatorPower : HttpResult(7, "无操作此功能的权限")
class NotAllowed : HttpResult(10, "不允许的操作")
class NeedConfirm : HttpResult(88, "再次确认")

class NotLogin : HttpResult(996, "请登录")
class NotSmsNull : HttpResult(997, "验证码失效，请重新获取")

class EXISTENCE : HttpResult(11, "对象已经存在")

class NotFoundException : HttpResult(201, "系统找不到指定的文件。")
class NotPSQLException : HttpResult(202, "数据插入有误并且回滚，请检查。")

class NotFoundCode : HttpResult(112, "验证码错误")

class NotMessage : HttpResult(301, "信息不一致，请查看。")
class NoSufficientBalance : HttpResult(401, "客户余额不足够")
class NotExcustCard : HttpResult(400, "信息有误")

/**
 * 此不返回操作,仅在返回二进制文件等操作当中使用
 */
class NoResult : HttpResult(9999, "不允许的操作")

/**
 * 多个对象不匹配.
 */
class Mismatch : HttpResult(8, "所取值不匹配")

