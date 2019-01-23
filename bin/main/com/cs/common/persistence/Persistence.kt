package com.cs.common.persistence

import com.alibaba.fastjson.JSONObject
import com.cs.common.dbHelper.ColumnType
import com.cs.common.dbHelper.Field
import com.cs.common.dbHelper.JDBCHelperFactory
import org.apache.logging.log4j.LogManager
import org.postgresql.jdbc.PgArray
import org.postgresql.util.PGobject
import java.math.BigDecimal
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

/**
 * 表明一个可以操作的实例化的对象,此对象与数据库表记录相对应.
 *
 * 实现此接口的对象，表明可以直接对持久层做读写操作.
 * 每一个实现此接口的对象代表一行记录。
 *
 * 此接口的实现类可自行处理对应的持久层保存/读取/更新。保证与持久层的数据记录一致。
 *
 * 实例对象已经实例化，新增加与获取从工厂类当中获取。
 */
interface Persistence {
	/**
	 * 得到指定名称的数据的值.
	 *
	 * @param name 指定返回值的名称。
	 * @param <T>  指定返回值的类型
	 * @return 如果名称未指定，返回null
	</T> */
	operator fun <T> get(name: String): T?

	/**
	 * 得到指定名称的数据的值.
	 *
	 * @param name         指定返回值的名称。
	 * @param defaultValue 如果得到的值为NULL.返回默认值。
	 * @param <T>          指定返回值的类型
	 * @return 如果名称未指定，返回null
	</T> */
	operator fun <T> get(name: String, defaultValue: T): T

	/**
	 * 将一个数据写入指定名称的值当中
	 *
	 * @param name  指定的列名称
	 * @param value 要更新的值
	 * @param <T>   值的类型。
	 */
	operator fun <T> set(name: String, value: T)

	/**
	 * 刷新数据至持久层.
	 *
	 *
	 * 此方法为底层方法。强制刷新持久层数据
	 *
	 * @return true 成功，false 失败。
	 */
	fun update(): Boolean

	/**
	 * 将数据保存至持久层当中
	 */
	fun save()

	/**
	 * 将此接口代表的数据删除.
	 *
	 *
	 * 具体的删除实现由实现类自己实现。
	 *
	 * @return 成功：true，失败，false
	 */
	fun delete(): Boolean

	/**
	 * 生成此工厂类对应表的索引值,放入对应的索引字段当中,并且将此值返回
	 *
	 * 此表必须设置了索引字段,并且有明确的索引获取方式
	 * 此表当中索引必须有且仅有一个
	 *
	 * 正常返回得到的索引值。如果无法获得索引时,返回值 < 0
	 */
	fun generateSequence(): Long

	/**
	 * 遍历所有字段的方法.
	 */
	fun foreach(black: (name: String, value: Any?) -> Unit)

	/**
	 * 读关联数据。并放置相关位置
	 * 需要设置工厂类的relationship属性
	 *
	 */
	fun loadAssociated()

	/**
	 * 给出此表是否存在从表数据。
	 * 只计算当前已经取出的数据
	 */
	val hasAssociated: Boolean

	/**
	 * 得到从表的数据列表
	 */
	val associated: ArrayList<out Persistence>?

	/**
	 * 返回此表的工厂类
	 */
	val factory: Factory<Persistence>

	/**
	 * 增加一个从表的数据
	 */
	fun addAssociated(ass: Persistence)

	/**
	 * 删除一个从表的数据
	 */
	fun deleteAssociated(ass: Persistence)

	/**
	 * 快速的设置一个对象的所有值
	 */
	fun setValues(data: JSONObject)
}


abstract class AbstractPersistence(final override val factory: AbstractFactory<AbstractPersistence>) : Persistence {
	companion object {
		val log = LogManager.getLogger(AbstractPersistence::class.java.name)!!
	}

	private val data = Array<Any?>(factory.fields.size) { null }
	private var isSaved = false
	private var associatedData: ArrayList<Persistence>? = null

	override fun setValues(data: JSONObject) {
		for (i in factory.fields.indices) {
			if (data[factory.fields[i].name] != null)
				this.data[i] = data[factory.fields[i].name]
		}
	}

	override fun <T> get(name: String): T? {
		val index = factory.find(name)
		if (index < 0) {
			log.error("未找到指定名称的字段。name:$name")
			return null
		}

		@Suppress("UNCHECKED_CAST")
		return data[index] as? T
	}

	override fun <T> get(name: String, defaultValue: T): T {
		return get(name) ?: defaultValue
	}

	override fun <T> set(name: String, value: T) {
		val index = factory.find(name)
		if (index < 0) {
			log.error("未找到指定名称的字段。name:$name")
			return
		}

		if (isSaved && (factory.fields[index].isPrimary || factory.fields[index].isSequence)) {
			log.error("已经保存进数据库的无法修改序列与主键值")
		}

		@Suppress("UNCHECKED_CAST")
		data[index] = when (factory.fields[index].type) {
			ColumnType.NUMERIC -> {
				when (value) {
					is BigDecimal -> value.toDouble()
					is Int -> value.toDouble()
					is Long -> value.toDouble()
					is String -> value.toDouble()

					else -> value
				}
			}
			ColumnType.INT4_ARRAY -> {
				when (value) {
					is PgArray -> (value as PgArray).array as Array<Int>?
					is Array<*> -> value
					else -> throw ClassCastException("不支持的转换")
				}
			}

			ColumnType.VARCHAR_ARRAY -> {
				when (value) {
					is PgArray -> (value as PgArray).array as Array<String>?
					is Array<*> -> value
					else -> throw ClassCastException("不支持的转换")
				}
			}

			ColumnType.JSON -> {
				if (value is PGobject && (value.type == "jsonb" || value.type == "json"))
					JSONObject.parseObject(value.value)
				else if (value is String) {
					JSONObject.parseObject(value)
				} else if (value is JSONObject) {
					value
				} else {
					JSONObject.parseObject(value.toString())
				}
			}
			else -> value
		}
	}

	override fun save() {
		val helper = JDBCHelperFactory.helper
		if (factory.sequence != null) {
			this.generateSequence()
		}

		val insertBuild = StringBuilder("insert into ${factory.tableName}(")
		val valuesBuild = StringBuilder(" values(")

		for (i in factory.fields.indices) {
			val field = factory.fields[i]

			if (field.isView) continue

			insertBuild.append(field.name).append(",")
			when {
				data[i] != null -> valuesBuild.append(helper.format(data[i]))
				field.defaultValue != "" -> valuesBuild.append(field.defaultValue)
				else -> valuesBuild.append("null")
			}
			valuesBuild.append(",")
		}

		insertBuild.replace(insertBuild.length - 1, insertBuild.length, ")")
		valuesBuild.replace(valuesBuild.length - 1, valuesBuild.length, ")")

		val success = helper.execute(insertBuild.toString() + valuesBuild.toString()) > 0

		isSaved = true

		afterSave(success)
	}

	protected open fun afterSave(success: Boolean) {
		afterUpdateDB(success)
	}

	protected open fun afterUpdate(success: Boolean) {
		afterUpdateDB(success)
	}

	protected open fun afterDelete(success: Boolean) {
		afterUpdateDB(success)
	}

	protected open fun afterUpdateDB(success: Boolean) {}

	override fun update(): Boolean {
		val helper = JDBCHelperFactory.helper

		val build = StringBuilder("update ${factory.tableName} set ")

		for (i in factory.fields.indices) {
			val field = factory.fields[i]
			if (field.isView) continue

			build.append(field.name).append(" = ")
			when {
				data[i] != null -> build.append(helper.format(data[i]))
				field.defaultValue != "" -> build.append(field.defaultValue)
				else -> build.append("null")
			}
			build.append(",")
		}
		build.delete(build.length - 1, build.length)

		build.append(" where ")
		for (field in factory.pks) {
			build.append(field.name).append(" = ")
				.append(helper.format(data[factory.fields.indexOf(field)])).append(" and ")
		}
		build.delete(build.length - 5, build.length)

		val result = helper.execute(build.toString()) > 0

		afterUpdate(result)
		return result
	}

	override fun delete(): Boolean {
		if (!isSaved) {
			log.warn("未写入持久层,无法调用删除方法")
			return false
		}

		val helper = JDBCHelperFactory.helper
		val where = buildString {
			if (!factory.pks.isEmpty()) {
				factory.pks.forEach {
					append(" ${it.name} = ")
					append(helper.format(data[factory.find(it.name)]))
					append(" and ")
				}

				delete(this.length - 5, this.length)
			} else {
				factory.fields.forEach {
					if (!it.isView) {
						append(" ${it.name} ")

						val value = helper.format(data[factory.find(it.name)])
						if (value == "null") {
							append(" isnull ")
						} else {
							append(" = ")
							append(value)
						}

						append(" and ")
					}
				}

				delete(this.length - 5, this.length)
			}
		}

		val result = helper.execute("delete from ${factory.tableName} where $where") > 0

		afterDelete(result)

		return result
	}

	override fun generateSequence(): Long {
		val sequence: Field = factory.sequence ?: return -1

		val seq = factory.find(sequence.name)

		data[seq] = JDBCHelperFactory.helper.queryWithOneValue("select ${sequence.defaultValue}")!!

		return data[seq] as Long
	}

	override fun equals(other: Any?): Boolean {
		if (other != null && other is AbstractPersistence && this.factory != other.factory) {
			for (f in this.factory.pks) {
				val v: Any? = this[f.name]
				if (v == null || v != other[f.name])
					return false
			}
		} else {
			return false
		}

		return true
	}

	override fun hashCode(): Int {
		return Arrays.hashCode(data)
	}

	internal fun setFieldDataByDB(set: ResultSet) {
		for (f in factory.fields) {
			if (!(f.isView || set.getObject(f.name) == null))
				set(f.name, set.getObject(f.name))
		}

		completeReadFromDB()
		isSaved = true
	}

	/**
	 * 此操作为从数据库读出数据后调用的方法.
	 * 当写入数据库(插入与修改)后,也会调用此方法.
	 */
	protected open fun completeReadFromDB() {}

	override fun toString(): String {
		val fields = factory.fields
		return buildString {
			for (i in fields.indices) {
				append("${fields[i].name}:${data[i]} \t\t")
			}
		}
	}

	override fun foreach(black: (name: String, value: Any?) -> Unit) {
		val fields = factory.fields
		for (index in fields.indices) {
			black(fields[index].name, data[index])
		}
	}

	override fun loadAssociated() {
		val rel = factory.relationship!!
		val query = rel.rTableFactory.createQuery()

		val where = buildString {
			for (a in rel.Fields) {
				if (length > 0) append(" and ")
				append(a.second).append(" = " + get(a.first))
			}
		}

		query.exec("select * from ${rel.rTableFactory.tableName} where $where").forEach {
			addAssociated(it)
		}
	}


	/**
	 * 增加一个从表的数据
	 */
	override fun addAssociated(ass: Persistence) {
		val rel = factory.relationship!!
		if (ass.factory != rel.rTableFactory) {
			log.error("加入的子表数据与指定的不匹配,插入的${ass.factory},指定的${rel.rTableFactory}")
		}

		if (associatedData == null) {
			associatedData = ArrayList()
		}

		associatedData!!.add(ass)
	}

	/**
	 * 删除一个从表的数据
	 */
	override fun deleteAssociated(ass: Persistence) {
		if (associatedData == null) {
			log.warn("关联对象未加入任何数据")
		} else
			associatedData!!.remove(ass)
	}

	override val hasAssociated: Boolean
		get() = associatedData != null

	override val associated: ArrayList<Persistence>?
		get() = associatedData

}