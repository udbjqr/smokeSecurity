package com.cs.entitys

import com.cs.common.persistence.AbstractFactory
import com.cs.common.persistence.AbstractPersistence
import com.cs.common.units.ID

/**
 *
 * create by 2018/12/3.
 * @author lipo
 */


/**
 * 设备
 */
class Device(factory: DeviceFactory) : AbstractPersistence(factory) {
    val id: Long get() = this[ID]!!
}


object DeviceFactory : AbstractFactory<Device>("device") {
    override fun createNewObject(): Device {
        return Device(this)
    }
}