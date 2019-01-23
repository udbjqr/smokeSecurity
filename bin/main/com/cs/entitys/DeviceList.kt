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
 * 设备列表
 */
class DeviceList(factory: DeviceListFactory) : AbstractPersistence(factory) {
    val id: Long get() = this[ID]!!
}


object DeviceListFactory : AbstractFactory<DeviceList>("device_list") {
    override fun createNewObject(): DeviceList {
        return DeviceList(this)
    }
}