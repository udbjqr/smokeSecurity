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
class AlarmCause(factory: AlarmCauseFactory) : AbstractPersistence(factory) {
	val id: Long get() = this[ID]!!
}


object AlarmCauseFactory : AbstractFactory<AlarmCause>("alarm_cause") {
	override fun createNewObject(): AlarmCause {
		return AlarmCause(this)
	}
}