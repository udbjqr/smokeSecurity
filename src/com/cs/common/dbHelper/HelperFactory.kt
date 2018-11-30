package com.cs.common.dbHelper

import com.alibaba.fastjson.JSONObject
import com.cs.common.units.readFileContentByString
import org.apache.logging.log4j.LogManager

internal const val DB_SERVER_ADD = "DBServerAdd"
internal const val DB_SERVER_PORT = "DBServerPort"
internal const val DB_DATA_BASE_NAME = "DBDataBaseName"
internal const val DB_PASSWORD = "DBPassword"
internal const val DB_USER_NAME = "DBUserName"
internal const val CONNECTION_TIMEOUT = "ConnectionTimeout"
internal const val DB_POOL_NUM = "DBPoolNum"

/**
 * 数据库操作对象入口.
 *
 * create by 2017/6/1.
 * @author yimin
 */
object JDBCHelperFactory {
	private val log = LogManager.getLogger(JDBCHelperFactory::class.java.name)
	private val config: JSONObject
	private val connectionPools: HashMap<String, ConnectionPool> = HashMap()
	val default: ConnectionPool

	init {
		log.trace("开始初始化数据库帮助管理对象.")
		config = JSONObject.parseObject(readFileContentByString("db.json"))
		config.forEach { t, u -> connectionPools[t] = createHelper(u as JSONObject) }

		default = connectionPools["default"]!!
	}

	private fun createHelper(config: JSONObject): ConnectionPool {
		return when (config["DBType"]) {
			"MSSQL" -> createMSSQLServerPools(config)
			"ORACLE" -> createOraclePools(config)
			"PGSQL" -> createPostgresqlPools(config)
			"MYSQL" -> createMySqlPools(config)
			"H2" -> createH2Pools(config)
			else -> throw IllegalArgumentException("类型都乱指定，你想闹哪样：${config["DBType"]}，去看db.json文件。")
		}
	}

	private fun createH2Pools(@Suppress("UNUSED_PARAMETER") config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createMySqlPools(@Suppress("UNUSED_PARAMETER") config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createOraclePools(@Suppress("UNUSED_PARAMETER") config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createMSSQLServerPools(@Suppress("UNUSED_PARAMETER") config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createPostgresqlPools(config: JSONObject): ConnectionPool {
		Class.forName("org.postgresql.Driver")

		val connectionUrl = "jdbc:postgresql://${config[DB_SERVER_ADD]}:${config[DB_SERVER_PORT]}/${config[DB_DATA_BASE_NAME]}"
		return ConnectionPool(connectionUrl, config.getString(DB_USER_NAME), config.getString(DB_PASSWORD), config.getLong(CONNECTION_TIMEOUT), config.getInteger(DB_POOL_NUM)) {
			PostgreSql(config["trim"] as String, it)
		}
	}


	val helper get() = traningHelper.get() ?: default.getFreeConn().helper


	/**
	 * 注意不能使用helper的format方法
	 */
	fun getHelper(name: String): Helper {
		return connectionPools[name]!!.getFreeConn().helper
	}
}
