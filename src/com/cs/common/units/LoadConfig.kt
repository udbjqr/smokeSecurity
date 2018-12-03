package com.common.units

import com.cs.common.units.DBCONFIG
import com.cs.common.units.GENERIC
import com.cs.common.units.GENERIC_CONFIG_FILE_NAME

/**
 *
 * create by 2018/11/29.
 * @author lipo
 */

fun loadConfig() {
    setDefaultConfig(Config.createConfig(GENERIC, GENERIC_CONFIG_FILE_NAME))
    Config.createConfig(DBCONFIG, "db.json")
}