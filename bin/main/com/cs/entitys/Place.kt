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
class Place(factory: PlaceFactory) : AbstractPersistence(factory) {
    val id: Long get() = this[ID]!!
}


object PlaceFactory : AbstractFactory<Place>("place") {
    override fun createNewObject(): Place {
        return Place(this)
    }
}