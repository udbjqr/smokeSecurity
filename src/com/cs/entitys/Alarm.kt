package com.cs.entitys

import com.cs.common.persistence.AbstractFactory
import com.cs.common.persistence.AbstractPersistence
import com.cs.common.units.ID

/**
 *
 * create by 2018/12/4.
 * @author lipo
 */


/**
 * 报警记录
 */
class Alarm(factory: AlarmFactory) : AbstractPersistence(factory) {
	val id: Long get() = this[ID]!!
}


object AlarmFactory : AbstractFactory<Alarm>("alarm") {
	override fun createNewObject(): Alarm {
		return Alarm(this)
	}
}