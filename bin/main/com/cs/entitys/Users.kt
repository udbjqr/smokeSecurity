package com.cs.entitys

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.common.units.Config
import com.cs.common.persistence.AbstractFactory
import com.cs.common.persistence.AbstractPersistence
import com.cs.common.units.ID
import com.cs.common.units.MENU
import com.cs.common.util.StringUtil

/**
 * 用户
 */
val powers = mutableSetOf<Int>()

class Users(factory: UsersFactory) : AbstractPersistence(factory) {

	val id: Long get() = this[ID]!!



	fun login(passWord: String): Boolean {
		return StringUtil.matchPasswd(passWord, get("passwd"))
	}


	fun getMenu(): JSONObject {

		val roles = get<Array<Int>>("roles")

		return if (roles != null) {
			println(powers)
			roles.forEach {
				powers.add(it)
			}

			println(powers)

			val aaa = "{\"menu\":${loadMenuByPower(
				Config.getIns(MENU)!!.content.getJSONArray("menu"),
				powers
			).toJSONString()}}"


			println(aaa)

			JSONObject.parseObject(aaa)

		} else {
			Config.getIns(MENU)!!.content
		}
	}


	private fun loadMenuByPower(menu: JSONArray, powers: Set<Int>, setMenu: JSONArray? = null): JSONArray {
		val thisMenu = setMenu ?: JSONArray()

		menu.forEach {
			val subMenu = (it as JSONObject).clone() as JSONObject

			//如果当前对象的权限存在
			if (powers.contains(subMenu.getIntValue(ID))) {
				thisMenu.add(subMenu)

				//为了于原有的对象进行脱离
				subMenu["operate"] = JSONObject()

				(it["operate"] as JSONObject).forEach { k, v ->
					run {
						if (powers.contains(v)) {
							(subMenu["operate"] as JSONObject)[k] = v
						}
					}
				}

				if (it.containsKey("children")) {
					val subMenuArray = subMenu.getJSONArray("children").clone() as JSONArray
					subMenu["children"] = subMenuArray
					subMenuArray.clear()

					loadMenuByPower(it.getJSONArray("children"), powers, subMenuArray)
				}
			}
		}

		return thisMenu
	}

}

object UsersFactory : AbstractFactory<Users>("users") {
	override fun createNewObject(): Users {
		return Users(this)
	}
}