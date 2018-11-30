package com.cs.common.units

import com.cs.common.dbHelper.Helper
import com.cs.common.dbHelper.JDBCHelperFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.util.Loader
import java.io.File
import java.math.BigDecimal
import java.nio.charset.Charset

val logger = LogManager.getLogger("com.cs.com.cs.common.units")!!

/**
 * 根据给定的编码，从文件中读取字符串.
 *
 * 如果文件未找到，将记录一个异常，但直接返回"".
 * 而不是弹出异常
 *
 * @param fileName 文件名
 * @param charset      指定文件的编码格式
 * *
 * @RETURN 读取到的字符串
 */
fun readFileContentByString(fileName: String, charset: Charset = Charsets.UTF_8): String {
	logger.debug("以 $charset 格式读文件: $fileName。")

	return try {
		val fullPath = Loader.getResource(fileName, Loader::class.java.classLoader).file
		File(fullPath).readText(charset)
	} catch (e: Exception) {
		logger.error("读文件$fileName 出现异常，原因：", e)
		""
	}
}


/**
 *
 * 此方法将开启一个事务，所有调用此方法的exec\query\format等方法将在同一个事务内执行。
 */
inline fun <T> tranExec(noinline errorHandle: ((Exception) -> T)? = null, crossinline block: Helper.() -> T): T {
	val helper = JDBCHelperFactory.helper
	return try {
		helper.beginTran()
		val result = helper.block()
		helper.commit()
		result
	} catch (e: Exception) {
		helper.rollback()
		if (errorHandle != null) {
			errorHandle(e)
		} else {
			logger.error("执行操作出现异常，回滚，异常：", e)
			throw e
		}
	}
}


/**
 * 从其他类型得到Double类型
 */
fun getDouble(value: Any?): Double? {
	if (value == null) return null

	return when (value) {
		is Double -> value
		is Int -> value.toDouble()
		is BigDecimal -> value.toDouble()
		is Long -> value.toDouble()
		is String -> value.toDouble()
		else -> value.toString().toDouble()
	}
}
