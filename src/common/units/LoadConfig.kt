package com.example.common.units

import common.units.DBCONFIG
import common.units.GENERIC
import common.units.GENERIC_CONFIG_FILE_NAME

/**
 *
 * create by 2018/11/29.
 * @author lipo
 */

fun loadConfig() {
    setDefaultConfig(Config.createConfig(GENERIC, GENERIC_CONFIG_FILE_NAME))
    Config.createConfig(DBCONFIG, "db.json")
}