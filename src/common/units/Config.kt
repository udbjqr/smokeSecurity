package com.example.common.units

import com.alibaba.fastjson.JSONObject
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.util.Loader

/**
 *
 * create by 2018/11/29.
 * @author lipo
 */


class Config(fileName: String) {
    private val separator = "."

    /**
     * 得到此配置的所有内容
     */
    private val content: JSONObject
    private var configName = ""
    private val strings: String = getProperties(Loader.getResource(fileName, Config::class.java.classLoader), "UTF-8")

    init {

        content = JSONObject.parseObject(strings)
    }

    override fun toString(): String {
        return strings
    }


    /**
     * 返回指定名称的配置项.
     *
     * @param name         指定的配置名称.
     * @param defaultValue 当未定义值时使用的默认值
     * @return 配置项的值
     */
    operator fun <T> get(name: String, defaultValue: T): T {
        val value = get<T>(name)

        return value ?: defaultValue
    }


    /**
     * 返回指定名称的配置项.
     *
     * @param name     指定的配置名称.
     * @param notFound 当名称在配置文件中未定义时，回调。
     * @return 配置项的值
     */
    operator fun <T> get(name: String, notFound: NotParaCallback): T? {
        val value = get<T>(name)

        if (value == null) {
            logger.error("未找到配置的名称项，配置名:{}，项目名:{}", configName, name)
            notFound.callBack("$configName->$name\n")
        }

        return value
    }

    /**
     * 返回指定名称的配置项.
     *
     * @param name 指定的配置名称.
     * @return 配置项的值
     */
    operator fun <T> get(name: String): T? {

        val value = locate(name)!![getLastName(name)]
        if (value == null) {
            logger.warn("未找到指定名称:{}的配置项。", name)
            return null
        }

        return try {
            value as T
        } catch (e: Exception) {
            logger.error("配置项值与请求配置值不相符。名称:{}.返回空", name)
            null
        }

    }

    /**
     * 校验值是否存在.
     *
     * @param names 需要校验的名称
     * @return 当找到第一个未存在的名称时返回。
     */
    fun checkContent(vararg names: String): String? {
        for (name in names) {

            if (!locate(name)!!.containsKey(getLastName(name))) {
                return name
            }
        }

        return null
    }


    private fun locate(key: String): JSONObject? {
        if (!key.contains(separator)) {
            //如果未有"."分隔符
            return content
        }

        var temp: JSONObject? = content
        var index = 0
        var endIndex = key.indexOf(separator, index)
        while (endIndex > 0) {
            temp = getMap(key.substring(index, endIndex), temp!!)
            if (temp == null) {
                return null
            }
            index = endIndex + 1
            endIndex = key.indexOf(separator, index)
        }

        return temp
    }

    private fun getMap(key: String, content: JSONObject): JSONObject? {
        val obj = content[key]
        return if (obj is JSONObject) {
            obj
        } else null

    }

    private fun getLastName(key: String): String {
        val index = key.lastIndexOf(separator)
        return if (index == -1) {
            key
        } else key.substring(index + 1, key.length)

    }

    companion object {
        private val logger = LogManager.getLogger(Config::class.java.name)
        private val configs = HashMap<String, Config>()

        var defaultConfig: Config? = null

        /**
         * 从指定的文件名读一个配置文件，并根据指定名称存放.
         *
         *
         * 如多次调用此方法，会重新读指定文件名内容。
         *
         *
         * 如每次指定name 不同，将会多次存放文件的多个副本.
         *
         * @param name     在此配置文件内的名称
         * @param fileName 需要读取的文件名
         * @return 新创建的配置文件
         */
        fun createConfig(name: String, fileName: String): Config {
            if (configs.containsKey(name)) {
                logger.warn("已经存在同名对象.直接返回原有对象")
                return configs[name]!!
            }

            logger.trace("新增加一个配置文件，名称:{}，读取文件名:{}", name, fileName)
            val config = Config(fileName)

            synchronized(configs) {
                configs[name] = config
                config.configName = name

                if (configs.size == 1) {
                    defaultConfig = config
                }
            }

            return config
        }

        /**
         * 找到指定名称的配置对角
         *
         * @param name 指定的名称，此名称为创建对象时指定的名称
         * @return Confing对象。如果未找到返回null
         */
        fun getIns(name: String): Config? {
            return configs[name]
        }
    }

}


interface NotParaCallback {
    /**
     * 未找到配置文件指定名称的值时的回调.
     *
     * @param name 未找到的名称值
     */
    fun callBack(name: String)
}


fun setDefaultConfig(defaultConfig: Config) {
    Config.defaultConfig = defaultConfig
}

fun getDefaultConfig(): Config {
    return Config.defaultConfig!!
}