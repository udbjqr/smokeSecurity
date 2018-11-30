package com.example.common.units

import com.cs.common.units.logger
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.util.Loader
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 *
 * create by 2018/11/29.
 * @author lipo
 */


private val logger = LogManager.getLogger(FileUtil::class.java.name)
val NEWINE: String = newLine()


fun newLine(): String {
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

    return newLine
}

class FileUtil {


    /**
     * 返回指定文件名的流
     *
     * @param fileName 文件名
     * @return 输入流
     */

    fun readFile(fileName: String): InputStream? {
        val uConn: URLConnection
        try {
            uConn = Loader.getResource(fileName, FileUtil::class.java.classLoader).openConnection()
            uConn.useCaches = false
            return uConn.getInputStream()
        } catch (e: Exception) {
            logger.error("读取文件{} 发生异常。", fileName, e)
            return null
        }

    }


    fun getPropertiesFromString(config: String): Properties {
        val properties = Properties()
        try {
            properties.load(StringReader(config))
        } catch (e: Exception) {
            logger.error("配置转换出错：", e)
        }

        return properties
    }


    /**
     * 获得可上传的文件类型
     */
    fun getFileTypeList(): List<String> {
        val fileTypeList = ArrayList<String>()
        fileTypeList.add("jpg")
        fileTypeList.add("jpeg")
        fileTypeList.add("bmp")
        fileTypeList.add("png")
        fileTypeList.add("gif")
        return fileTypeList
    }

    /****
     * 文件转Base64
     */
    @Throws(IOException::class)
    internal fun readBase64FromFile(url: String): String {
        val format = url.substring(url.lastIndexOf(".") + 1)
        val buffer = Files.readAllBytes(Paths.get(url))

        return "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(buffer)
    }

}


/**
 * 根据给定的编码，从文件中读取字符串
 *
 * @param configURL URL地址
 * @param code      指定文件的编码格式
 * @return 读取到的字符串
 */
fun getProperties(configURL: URL, code: String): String {
    logger.debug("Reading configuration from URL {}", configURL)
    val stream: InputStream
    val uConn: URLConnection
    val reader: BufferedReader
    val builder = StringBuilder()

    try {
        uConn = configURL.openConnection()
        uConn.useCaches = false
        stream = uConn.getInputStream()


        val inputStreamReader = InputStreamReader(stream, code)
        reader = BufferedReader(inputStreamReader)


        val tempString: String = reader.readText()

        reader.close()

        return tempString
    } catch (e: Exception) {
        logger.error("{} error", configURL, e)
        return ""
    }

}