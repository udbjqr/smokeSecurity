package com.cs.common.units

import com.common.units.getDefaultConfig


const val GENERIC = "generic"
const val GENERIC_CONFIG_FILE_NAME = "generic_config.json"
const val WRITER = "writer"
const val HTTP_REQUEST_TYPE = "_type"
const val REQUEST = "request"
const val RESPONSE = "response"
const val JSON_DATA = "jsonData"
const val OPENID = "openid"

const val NAME = "name"
const val ID = "id"
const val FLAG = "flag"
const val MESSAGE = "message"
const val MSISDN = "msisdn"
const val ICCID = "iccid"
const val SUPERIOR_ID = "superior_id"
const val TS_ID = "ts_id"
const val CODE = "code"
const val CUSTOM_ID = "custom_id"
const val FILE_NAME = "file_name"
const val PAGE_NUMBER = "page_number"
const val PAGE_COUNT = "page_count"
const val PACKAGE_ID = "package_id"
const val NUMBER = "number"
const val TYPE = "type"
const val POOL_ID = "pool_id"
const val FORM_ID = "form_id"


const val CONDITION = "condition"


const val DEBUG = "debug"
const val ALL_SUPPER_MAN = "allSupperMan"
const val IS_ENCRYPT = "is_encrypt"
const val LOGIN_KEEP_TIME = "loginKeepTime"
const val USER_TYPE = "userType"
const val USER = "user"

const val OPERATOR_MENU = "operatorMenu"
const val DBCONFIG = "dbConfig"
const val CUSTOMER_MENU = "customerMenu"
const val AGENT_MENU = "AgentMenu"

const val EMPTY_JSON = "{}"


val UPLOAD_DIRECTORY = getDefaultConfig()["resourcesRootPath", "/"]
val RESOURCES_URL = getDefaultConfig()["resourcesURL", "/resources"]
const val MEMORY_THRESHOLD = 1024 * 1024 * 3  // 3MB
const val MAX_REQUEST_SIZE = 1024 * 1024 * 50L // 50MB
const val MAX_FILE_SIZE = 1024 * 1024 * 40L // 40MB

val stopCarkLock = Any()