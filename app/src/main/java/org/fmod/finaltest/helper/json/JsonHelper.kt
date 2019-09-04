package org.fmod.finaltest.helper.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonHelper: IJsonHelper {
    companion object {
        //默认转换器
        val gson = Gson()

        inline fun <reified T>convert(json: String): T {
            return gson.fromJson(json)
        }

        inline fun <reified T> Gson.fromJson(json: String?): T = this.fromJson(json, object: TypeToken<T>(){}.type)
    }
}