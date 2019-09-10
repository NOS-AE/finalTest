package org.fmod.finaltest.helper.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class JsonHelper {
    companion object {
        //默认转换器
        val gson: Gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        inline fun <reified T>convert(json: String): T {
            return gson.fromJson(json)
        }

        fun <T>toJson(bean: T): String {
            return gson.toJson(bean)
        }


        inline fun <reified T> Gson.fromJson(json: String?): T = this.fromJson(json, object: TypeToken<T>(){}.type)
    }
}