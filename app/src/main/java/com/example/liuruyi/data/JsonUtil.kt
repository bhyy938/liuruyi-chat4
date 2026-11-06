package com.example.liuruyi.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

/** 全局共享的 JSON 配置。 */
object JsonUtil {
    @OptIn(ExperimentalSerializationApi::class)
    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        coerceInputValues = true
    }
}
