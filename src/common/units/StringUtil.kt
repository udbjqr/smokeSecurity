package com.example.common.units

import org.apache.logging.log4j.LogManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 *
 * create by 2018/11/30.
 * @author lipo
 */


object InternalConstant {
    /**
     * 日期数据转换的格式字符串
     */
    val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    var TOKEN_SPLIT = "&"

    val TOKEN = "token"

    val OPENID = "openid"

}


object StringUtil {
    val logger = LogManager.getLogger(StringUtil::class.java.name)
    val NEWLINE: String
    private val PACKAGE_SEPARATOR_CHAR = '.'


    init {
        // Determine the newline character of the current platform.
        var newLine: String

        val formatter = Formatter()
        try {
            newLine = formatter.format("%n").toString()
        } catch (e: Exception) {
            // Should not reach here, but just in case.
            newLine = "\n"
        } finally {
            formatter.close()
        }

        NEWLINE = newLine
    }

    /**
     * The shortcut to [simpleClassName(o.getClass())][.simpleClassName].
     */
    fun simpleClassName(o: Any?): String {
        return if (o == null) {
            "null_object"
        } else {
            simpleClassName(o.javaClass)
        }
    }

//    /**
//     * Generates a simplified name from a [Class].  Similar to [Class.getSimpleName], but it works fine
//     * with anonymous classes.
//     */
//    fun simpleClassName(clazz: Class<*>): String {
//        val className = ObjectUtil.checkNotNull(clazz, "clazz").name
//        val lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR.toInt())
//        return if (lastDotIdx > -1) {
//            className.substring(lastDotIdx + 1)
//        } else className
//    }

    /**
     * 判断明文加密之后与密码是否匹配,
     *
     * @param plaintext 明文
     * @param passwd    密码
     * @return 匹配返回true, 不匹配返回false
     */
    fun matchPasswd(plaintext: String, passwd: String): Boolean {
        return encrypt(plaintext) == passwd
    }

    /**
     * 加密码一个明文,此过程返回对应的密码,如出现错误,返回NULL
     *
     * @param plaintext 需要加密的文本
     * @return
     */
    fun encrypt(plaintext: String): String? {
        val en: MessageDigest
        try {
            en = MessageDigest.getInstance("SHA")
            en.update(plaintext.toByteArray())
            val pass = Base64.getEncoder().encodeToString(en.digest())
            return pass.trim { it <= ' ' }
        } catch (e: NoSuchAlgorithmException) {
            logger.error("加密出错.", e)
        }

        return null
    }




    fun isEmpty(value: String?): Boolean {
        return value == null || value.isEmpty()
    }

    fun isEmptyAndUndefined(value: String?): Boolean {
        return value == null || value.isEmpty() || "undefined" == value
    }


    fun trim(original: String, removed: String): String? {
        if (isEmpty(original)) {
            return original
        }

        var begin = 0
        var end = original.length
        if (original.startsWith(removed)) {
            begin = removed.length
        }

        if (original.endsWith(removed)) {
            end = original.length - removed.length
        }

        return original.substring(begin, end)
    }


}