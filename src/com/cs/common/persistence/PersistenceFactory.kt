package com.cs.common.persistence

import com.cs.common.dbHelper.ColumnType
import com.cs.common.dbHelper.Field
import com.cs.common.dbHelper.JDBCHelperFactory
import com.cs.common.dbHelper.JDBCHelperFactory.helper
import org.apache.logging.log4j.LogManager
import java.sql.ResultSet


/**
 * 持久化对象实例的工厂接口.
 * <p>
 * 每一个对象均会实现一个继承类，用以获得对象实例。
 * <p>
 * 实现类做为一个单例存在，并且在构造函数当中设置对象类的表名/字段名等信息。并且实现{@link #createObject(User)}方法，
 * 并返回相对应的对象实例。
 * <p>
 * 使用此对象设置的表必须有主键，并且使用{@link #addField(String, Class, Object, boolean, boolean)}方法设置字段主键。
 * <p>
 * 继承类可修改对象操作过程当中执行的语句，{@link #setWherePrimaryKeys()}方法默认被其他方法调用。
 * <p>
 * 调用{@link #getObject(String, Object)}方法得到一个存在的对象。如此对象不存在，将返回null.调用{@link #getNewObject(User)}
 * 方法得到一个新的对象。
 * <p>
 * 每次获得一个对象时会将此对象进行缓存，使用{@link #setIsCheck(boolean)}可设置是否需要缓存，默认为true.
 */
interface Factory<out T : Persistence> {
	val tableName: String
	val relationship: Relationship?

	fun createQuery(): Query<T>

	/**
	 * 根据给定的列名与值,给出相应的对象。
	 */
	fun get(name: String, value: Any): T?

	/**
	 * 根据给定的列名与值,给出相应的对象。
	 */
	fun get(values: Map<String, Any>): T?

	/**
	 * 返回一个新的对象,此对象为空对象
	 */
	fun getNewObject(): T

	/**
	 * 设置此对象是否可以被缓存,默认不进行缓存
	 */
	fun setCacheObject(cache: Boolean)

	/**
	 * 得到是否缓存对象
	 */
	fun isCacheObject(): Boolean

	/**
	 *根据指定名称,返回此列的信息
	 */
	fun getField(name: String): Field
}

abstract class AbstractFactory<out T : AbstractPersistence>(final override val tableName: String, final override val relationship: Relationship?) : Factory<T> {
	val fields: MutableList<Field> = JDBCHelperFactory.helper.getTableInfo(tableName)
	internal val pks: ArrayList<Field> = ArrayList()
	internal var sequence: Field? = null
	private val data = ArrayList<T>()
	private var needCache = false
	protected var selectSql: String = ""


	companion object {
		val log = LogManager.getLogger(AbstractFactory::class.java.name)!!
	}

	constructor(tableName: String) : this(tableName, null)

	init {
		for (field in fields) {
			if (field.isPrimary) {
				pks.add(field)
			}
			if (field.isSequence) {
				sequence = field
			}
		}

		reSetSelectSql()
	}


	private fun reSetSelectSql() {
		val builder = StringBuilder("select ")

		for (field in fields) {
			builder.append(field.name).append(",")
		}

		selectSql = builder.delete(builder.length - 1, builder.length).append(" from $tableName  t where ").toString()
	}

	fun addViewField(name: String, type: ColumnType) {
		fields.add(Field(name, type, false, "", "", false, false, true))
	}

	override fun setCacheObject(cache: Boolean) {
		this.needCache = cache
	}

	override fun isCacheObject(): Boolean {
		return this.needCache
	}

	internal fun find(name: String): Int {
		return fields.indices.firstOrNull { fields[it].name == name } ?: -1
	}

	private fun findProduct(name: String, value: Any): T? {
		if (needCache) {
			data.forEach {
				if (value == it[name])
					return it
			}
		}

		return null
	}


	internal fun findProduct(set: ResultSet): T? {
		var found = false

		if (needCache) {
			data.forEach {
				for (i in pks.indices) {
					if (set.getObject(pks[i].name) != it[pks[i].name]) {
						continue
					}

					found = true
					break
				}

				if (found)
					return it
			}
		}

		return null
	}

	override fun get(values: Map<String, Any>): T? {
		val where = buildString {
			values.forEach {

				if (length > 0) append(" and ")
				append(" t.").append(it.key).append(" = ").append(it.value)
			}
		}

		var found = false
		var result: T? = null
		helper.query("$selectSql $where") {
			result = findProduct(it)
			if (result != null) {
				found = true
				return@query
			} else {
				result = createNewObject()
				result!!.setFieldDataByDB(it)
			}
		}

		if (relationship != null) {
			result!!.loadAssociated()
		}

		return if (result == null) null else if (found) result else holdProduct(result!!)
	}

	override fun get(name: String, value: Any): T? {
		if (find(name) < 0) {
			log.error("在数据库表:$tableName 中未定义此字段名:$name")
		}

		var result = findProduct(name, value)
		if (result != null)
			return result

		val helper = JDBCHelperFactory.helper
		helper.query("$selectSql t.$name = ${helper.format(value)}") {
			result = createNewObject()
			result!!.setFieldDataByDB(it)
		}

		if (relationship != null) {
			result!!.loadAssociated()
		}

		return if (result == null) null else holdProduct(result!!)
	}

	private fun holdProduct(product: T): T {
		if (needCache && !data.contains(product)) {
			data.add(product)
		}

		return product
	}

	fun getDatas():ArrayList<out T>{
		return data
	}

	override fun getNewObject(): T {
		return createNewObject()
	}

	protected abstract fun createNewObject(): T


	override fun createQuery(): Query<T> {
		return Query(this)
	}

	override fun getField(name: String): Field {
		return fields[find(name)]
	}


}

