package com.example.common.units

import java.text.DecimalFormat

/**
 *
 * create by 2018/11/30.
 * @author lipo
 */

object ObjectUtil {
    /**
     * Checks that the given argument is strictly positive. If it is, throws [IllegalArgumentException].
     * Otherwise, returns the argument.
     */
    fun checkPositive(i: Int, name: String): Int {
        if (i <= 0) {
            throw IllegalArgumentException("$name: $i (expected: > 0)")
        }
        return i
    }

    /**
     * Checks that the given argument is not null. If it is, throws [NullPointerException].
     * Otherwise, returns the argument.
     */
    fun <T> checkNotNull(arg: T?, text: String): T {
        if (arg == null) {
            throw NullPointerException(text)
        }
        return arg
    }

    /**
     * Checks that the given argument is strictly positive. If it is, throws [IllegalArgumentException].
     * Otherwise, returns the argument.
     */
    fun checkPositive(i: Long, name: String): Long {
        if (i <= 0) {
            throw IllegalArgumentException("$name: $i (expected: > 0)")
        }
        return i
    }

    /**
     * Checks that the given argument is positive or zero. If it is, throws [IllegalArgumentException].
     * Otherwise, returns the argument.
     */
    fun checkPositiveOrZero(i: Int, name: String): Int {
        if (i < 0) {
            throw IllegalArgumentException("$name: $i (expected: >= 0)")
        }
        return i
    }

    /**
     * Checks that the given argument is neither null nor empty.
     * If it is, throws [NullPointerException] or [IllegalArgumentException].
     * Otherwise, returns the argument.
     */
    fun <T> checkNonEmpty(array: Array<T>, name: String): Array<T> {
        checkNotNull(array, name)
        checkPositive(array.size, "$name.length")
        return array
    }

    /**
     * Resolves a possibly null Integer to a primitive int, using a default value.
     * @param wrapper the wrapper
     * @param defaultValue the default value
     * @return the primitive value
     */
    fun intValue(wrapper: Int?, defaultValue: Int): Int {
        return wrapper ?: defaultValue
    }

    /**
     * Resolves a possibly null Long to a primitive long, using a default value.
     * @param wrapper the wrapper
     * @param defaultValue the default value
     * @return the primitive value
     */
    fun longValue(wrapper: Long?, defaultValue: Long): Long {
        return wrapper ?: defaultValue
    }

    /***
     * 四舍五入
     * @param value
     * @return
     */
    fun convert(value: Double): Double {
        val df = DecimalFormat("#.##")
        return java.lang.Double.valueOf(df.format(value))
    }
}
