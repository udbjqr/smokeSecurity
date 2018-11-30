package com.cs.common.dbHelper

import org.apache.logging.log4j.LogManager
import java.sql.ResultSet

internal val log = LogManager.getLogger(Helper::class.java.name)

enum class Joint {
	SELECT,
	UPDATE,
	DELETE,
	FORM,
	WHERE,
	GROUP_BY,
	HAVING,
	ORDER_BY,
	LIMIT,
	OFFSET
}

internal val jointNum = Joint.values().size
//保存当前线程当中正在进行事务的对象
internal val traningHelper = ThreadLocal<Helper>()

/**
 * 数据库操作帮助类.
 *
 * 每个对象做为一个查询或修改使用。
 *
 * 当需要执行多次操作时,可持有此接口的实例,并且设置autoCommit为false.
 * 此后,必须在结束时手工调用commit或rollback方法。
 *
 */
interface Helper {
	fun getAutoCommit(): Boolean
	fun select(selectSql: String): Helper
	fun update(updateSql: String): Helper
	fun delete(deleteSql: String): Helper
	fun from(fromSql: String): Helper
	fun where(whereSql: String): Helper
	fun groupBy(groupBySql: String): Helper
	fun having(havingSql: String): Helper
	fun orderBy(orderBySql: String): Helper
	fun limit(limitNumber: Int): Helper
	fun offset(offsetNumber: Int): Helper

	/**
	 * 执行一条单独的查询语句，必须以select开头，并且将获得的第一行第一列的数据返回.
	 *
	 * @param sql 需要执行的sql语句
	 * @param then 成功后执行的操作
	 * @return 返回第一行第一列的值,无论获得多少.
	 */
	fun <T> queryWithOneValue(sql: String, then: (T) -> T = { it }): T?

	/**
	 * 执行一条单独的查询语句，并且将获得的第一行第一列的数据返回.
	 *
	 * 执行此操作必须先使用select()方法,设置了要查询的对象
	 * @param then 成功后执行的操作
	 * @return 返回第一行第一列的值,无论获得多少.
	 */
	fun <T> queryWithOneValue(then: (T) -> T = { it }): T?

	/**
	 * 执行一个无返回值的SQL操作.
	 *
	 * @param sql 需要执行的sql语句
	 * @param then 成功后执行的操作
	 * @return 返回影响的行数
	 */
	fun execute(sql: String, then: (Int) -> Int = { it }): Int

	/**
	 * 执行一个无返回值的SQL操作.
	 * 执行此操作必须先使用update()或delete()方法,设置了要处理的对象
	 *
	 * @param then 成功后执行的操作
	 * @return 返回影响的行数
	 */
	fun execute(then: (Int) -> Int = { it }): Int


	/**
	 * 执行查询语句，
	 *
	 * @param sql 需要执行的sql语句
	 * @param then 成功后执行的操作,query方法将只调用一次ResultSet.回调函数必需要再次写while循环方法
	 */
	fun queryBySet(sql: String, then: (ResultSet) -> Unit)

	/**
	 * 执行查询语句，
	 *
	 *  执行此操作必须先使用select()方法,设置了要查询的对象
	 * @param sql 需要执行的sql语句
	 * @param then 成功后执行的操作,query方法将循环ResultSet.回调函数不需要再次写while循环方法
	 */
	fun query(sql: String, then: (ResultSet) -> Unit)

	/**
	 * 执行查询语句. 需要执行此方法,传入的joint参数必须按Joint类顺序指定。
	 *
	 *  执行此操作必须先使用select()方法,设置了要查询的对象
	 * @param joint 存储sql语句的数组。存放顺序需要一致
	 * @param then 成功后执行的操作,query方法将循环ResultSet.回调函数不需要再次写while循环方法
	 */
	fun query(joint: Array<String>, then: (ResultSet) -> Unit)

	/**
	 * 执行查询语句，必须以select开头.
	 *
	 * @param then 成功后执行的操作
	 */
	fun query(then: (ResultSet) -> Unit)

	/**
	 * 开始一个事务.
	 *
	 * 此处事务将是单层的，即已经开始了一个事务，无法进行嵌套。
	 * 已经开始过的事务在调用此方法时将直接返回false
	 */
	fun beginTran()

	/**
	 * 提交数据操作
	 *
	 */
	fun commit()

	/**
	 * 回滚操作
	 *
	 */
	fun rollback()


	/**
	 * 使用format时一定要使用
	 *
	 * val helper = JDBCHelperFactory.helper
	 * helper.format()
	 * helper.xxxxxxxx
	 * 最后一定要有一句:helper.query()查询等操作.
	 *
	 * 或者使用JDBCHelperFactory.defaut.format()来使用format
	 *
	 */
	fun format(value: Any?): String

	/**
	 * 操作完成后调用此操作来结束此对象的使用.并将资源返回以后使用.
	 *
	 * 在autoCommit 为true时,调用query()\execute()\queryWithOneValue()时,均会自动调用此方法。
	 * 在autoCommit 为false时,在调用commit()\rollback()时,将会自动调用此方法。以结束一次使用。
	 *
	 * 如果事务开启时直接调用此方法,将会执行rollback()方法后结束对象。
	 * 此方法在调用后再次使用此对象任何方法均将获得一个OperatorIsEnd的异常。
	 */
	fun close()

	/**
	 * 给定一个表名,返回一个表结构对象
	 */
	fun getTableInfo(tableName: String): MutableList<Field>

	/**
	 * 得到当前正在事务当中的helper对象
	 * 如无正在使用的事务，返回null
	 */
	fun getTraningHelper(): Helper?
}

enum class ColumnType {
	UNKNOWN, INT4, INT8, INT2, JSON, VARCHAR, TIMESTAMP, MONEY, VARCHAR_ARRAY, INT4_ARRAY, NUMERIC, BIT, BOOLEAN, UUID, INT2_ARRAY, INT8_ARRAY
}

data class Field(val name: String, val type: ColumnType, private val notNull: Boolean, val defaultValue: String, private val description: String, val isPrimary: Boolean, val isSequence: Boolean, val isView: Boolean = false) {
	override fun toString(): String {
		return "列名:$name\t\t类型:$type\t\t必填:$notNull\t\t默认值:$defaultValue\t\t描述:$description\t\t主键:$isPrimary\t\t序列:$isSequence"
	}
}

abstract class AbstractJDBCHelper(private val connection: Connection) : Helper {
	private var jointSql = Array(jointNum) { "" }
	private var isUsed = false

	private fun checkOperator() {
		if (isUsed || connection.isClosed) {
			throw OperatorIsEnd("此对象的操作已经过期,请重新申请对象。isUser:$isUsed,连接状态:${connection.isClosed}")
		}
	}

	override fun getTableInfo(tableName: String): MutableList<Field> {
		TODO("需要实现类自己实现的方法。")
	}

	override fun getAutoCommit(): Boolean {
		return connection.autoCommit
	}

	override fun select(selectSql: String): Helper {
		jointSql[Joint.SELECT.ordinal] = selectSql
		return this
	}

	override fun update(updateSql: String): Helper {
		jointSql[Joint.UPDATE.ordinal] = updateSql
		return this
	}

	override fun delete(deleteSql: String): Helper {
		jointSql[Joint.DELETE.ordinal] = deleteSql
		return this
	}

	override fun from(fromSql: String): Helper {
		jointSql[Joint.FORM.ordinal] = fromSql
		return this
	}

	override fun where(whereSql: String): Helper {
		jointSql[Joint.WHERE.ordinal] = whereSql
		return this
	}

	override fun groupBy(groupBySql: String): Helper {
		jointSql[Joint.GROUP_BY.ordinal] = groupBySql
		return this
	}

	override fun having(havingSql: String): Helper {
		jointSql[Joint.HAVING.ordinal] = havingSql
		return this
	}

	override fun orderBy(orderBySql: String): Helper {
		jointSql[Joint.ORDER_BY.ordinal] = orderBySql
		return this
	}

	override fun limit(limitNumber: Int): Helper {
		jointSql[Joint.LIMIT.ordinal] = limitNumber.toString()
		return this
	}

	override fun offset(offsetNumber: Int): Helper {
		jointSql[Joint.OFFSET.ordinal] = offsetNumber.toString()
		return this
	}

	override fun <T> queryWithOneValue(sql: String, then: (T) -> T): T? {
		checkOperator()

		log.trace("执行sql语句:$sql")
		var result: T? = null
		try {
			connection.createStatement().use {
				it.executeQuery(sql).use {
					@Suppress("UNCHECKED_CAST")
					result = if (it.next()) then(it.getObject(1) as T) else null
				}
			}
		} catch (e: Exception) {
			log.error("执行SQL语句出现异常:$sql", e)
			close()
			throw e
		}

		if (connection.autoCommit) {
			close()
		}

		return result
	}

	override fun <T> queryWithOneValue(then: (T) -> T): T? {
		return queryWithOneValue(generateQuerySql(), then)
	}

	override fun execute(sql: String, then: (Int) -> Int): Int {
		checkOperator()

		var result = -1
		log.trace("执行sql语句: $sql")

		try {
			connection.createStatement().use { result = then(it.executeUpdate(sql)) }
		} catch (e: Exception) {
			log.error("执行SQL语句出现异常:$sql ", e)
			close()
			throw e
		}

		if (connection.autoCommit) {
			close()
		}

		return result
	}

	override fun execute(then: (Int) -> Int): Int {
		return execute(generateExecuteSql(), then)
	}

	override fun queryBySet(sql: String, then: (ResultSet) -> Unit) {
		checkOperator()

		log.debug("sql:$sql")
		try {
			connection.createStatement().use {
				it.executeQuery(sql).use {
					then(it)
				}
			}
		} catch (e: Exception) {
			log.error("执行SQL语句出现异常:", e)
			close()
			throw e
		}

		if (connection.autoCommit) {
			close()
		}
	}

	override fun query(sql: String, then: (ResultSet) -> Unit) {
		checkOperator()

		log.debug("sql:$sql")
		try {
			connection.createStatement().use { it ->
				it.executeQuery(sql).use {
					while (it.next()) {
						then(it)
					}
				}
			}
		} catch (e: Exception) {
			log.error("执行SQL语句出现异常:", e)
			close()
			throw e
		}

		if (connection.autoCommit) {
			close()
		}
	}

	override fun query(then: (ResultSet) -> Unit) {
		query(generateQuerySql(), then)
	}

	override fun query(joint: Array<String>, then: (ResultSet) -> Unit) {
		this.jointSql = joint
		query(generateQuerySql(), then)
	}

	private fun generateQuerySql(): String {
		return buildString {
			append("select ").append(if (jointSql[Joint.SELECT.ordinal] != "") jointSql[Joint.SELECT.ordinal] else "*")
			if (jointSql[Joint.FORM.ordinal] != "") append(" from ").append(jointSql[Joint.FORM.ordinal])
			if (jointSql[Joint.WHERE.ordinal] != "") append(" where ").append(jointSql[Joint.WHERE.ordinal])
			if (jointSql[Joint.GROUP_BY.ordinal] != "") append(" group by ").append(jointSql[Joint.GROUP_BY.ordinal])
			if (jointSql[Joint.HAVING.ordinal] != "") append(" having ").append(jointSql[Joint.HAVING.ordinal])
			if (jointSql[Joint.ORDER_BY.ordinal] != "") append(" order by ").append(jointSql[Joint.ORDER_BY.ordinal])
			if (jointSql[Joint.OFFSET.ordinal] != "") append(" offset ").append(jointSql[Joint.OFFSET.ordinal])
			if (jointSql[Joint.LIMIT.ordinal] != "") append(" limit ").append(jointSql[Joint.LIMIT.ordinal])

			append(";")
		}
	}

	private fun generateExecuteSql(): String {
		return buildString {
			if (jointSql[Joint.UPDATE.ordinal] != "")
				append("update ").append(jointSql[Joint.UPDATE.ordinal])
			else
				append("delete ").append(jointSql[Joint.DELETE.ordinal])

			if (jointSql[Joint.FORM.ordinal] != "") append(" from ").append(jointSql[Joint.FORM.ordinal])
			if (jointSql[Joint.WHERE.ordinal] != "") append(" where ").append(jointSql[Joint.WHERE.ordinal])
			if (jointSql[Joint.GROUP_BY.ordinal] != "") append(" group by ").append(jointSql[Joint.GROUP_BY.ordinal])
			if (jointSql[Joint.HAVING.ordinal] != "") append(" having ").append(jointSql[Joint.HAVING.ordinal])
			if (jointSql[Joint.ORDER_BY.ordinal] != "") append(" order by ").append(jointSql[Joint.ORDER_BY.ordinal])
			if (jointSql[Joint.OFFSET.ordinal] != "") append(" offset ").append(jointSql[Joint.OFFSET.ordinal])
			if (jointSql[Joint.LIMIT.ordinal] != "") append(" limit ").append(jointSql[Joint.LIMIT.ordinal])
			append(";")
		}
	}


	override fun beginTran() {
		checkOperator()

		if (!connection.autoCommit) {
			log.error("事务已经开始,不能再次开始")
			throw NestedTransactionException("事务已经开始")
		} else {
			//将自己增加进线程的本地存储当中

			traningHelper.set(this)

			connection.autoCommit = false
		}
	}

	override fun commit() {
		checkOperator()

		if (!connection.autoCommit) {
			isUsed = true
			connection.commit()
			connection.autoCommit = true
			close()

			//结束后关闭事务
			traningHelper.set(null)

			log.trace("事务正常结束")
		} else {
			log.warn("没有需要结束的事务")
		}
	}

	override fun rollback() {
		checkOperator()

		if (!connection.autoCommit) {
			isUsed = true
			connection.rollback()
			connection.autoCommit = true
			close()

			traningHelper.set(null)

			log.trace("事务已经提交回滚")
		} else {
			log.warn("没有需要结束的事务")
		}
	}

	override fun getTraningHelper(): Helper? {
		return traningHelper.get()
	}


	override fun close() {
		if (connection.autoCommit) {
			isUsed = true
			connection.close()
		}
	}
}


@Suppress("unused")
class MSSQLServer(connection: Connection) : AbstractJDBCHelper(connection) {
	override fun format(value: Any?): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}

@Suppress("unused")
class MySql(connection: Connection) : AbstractJDBCHelper(connection) {
	override fun format(value: Any?): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}

@Suppress("unused")
class H2(connection: Connection) : AbstractJDBCHelper(connection) {
	override fun format(value: Any?): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}

@Suppress("unused")
class OracleHelper(connection: Connection) : AbstractJDBCHelper(connection) {
	override fun format(value: Any?): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}
