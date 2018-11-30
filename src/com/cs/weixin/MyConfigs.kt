package com.cs.src.weixin

import com.common.units.Config
import com.cs.common.units.GENERIC
import com.github.wxpay.sdk.WXPayConfig
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class MyConfigs :WXPayConfig{
	private var certData: ByteArray
	init {
		val certPath = Config.getIns(GENERIC)!!.content["wx_file"].toString()
		val file = File(certPath)
		val certStream = FileInputStream(file)
		this.certData = ByteArray(file.length().toInt())
		certStream.read(this.certData)
		certStream.close()
	}

	override fun getAppID(): String {
		return "wxe795f972eb691695"
	}

	override fun getMchID(): String {
		return "1507820561"
	}

	override fun getKey(): String {
		return "39ab64c5f4b044e29f55704ff63ea1ab"
	}

	override fun getCertStream(): InputStream {
		return ByteArrayInputStream(this.certData)
	}

	override fun getHttpConnectTimeoutMs(): Int {
		return 8000
	}

	override fun getHttpReadTimeoutMs(): Int {
		return 10000
	}

}