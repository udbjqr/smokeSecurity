package com.cs.entitys

import com.cs.common.persistence.AbstractFactory
import com.cs.common.persistence.AbstractPersistence
import com.cs.common.units.ID
import com.cs.common.util.StringUtil

/**
 * 用户
 */
class Users(factory: UsersFactory) : AbstractPersistence(factory) {

    val id: Long get() = this[ID]!!

    fun login(passWord: String): Boolean {
        return StringUtil.matchPasswd(passWord, get("passwd"))
    }
}


object UsersFactory : AbstractFactory<Users>("users") {
    override fun createNewObject(): Users {
        return Users(this)
    }
}