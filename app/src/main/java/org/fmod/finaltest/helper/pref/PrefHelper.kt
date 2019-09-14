package org.fmod.finaltest.helper.pref

import android.content.SharedPreferences
import android.util.Base64
import java.io.*
import java.lang.Exception
import kotlin.reflect.KProperty

/**
 * 提供键值对的存取方式
 */
abstract class PrefHelper<T>(private val default: T) {

    abstract fun getSharePreference(): SharedPreferences

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = property.name
        return with(getSharePreference()) {
            when (default) {
                is String -> getString(name, default)
                is Int -> getInt(name, default)
                is Boolean -> getBoolean(name, default)
                is Float -> getFloat(name, default)
                is Long -> getLong(name, default)
                is Serializable -> str2obj(getString(name, "")) ?: default
                else -> getString(name, null)
            } as T
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val name = property.name
        with(getSharePreference().edit()) {
            when (value) {
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                is Long -> putLong(name, value)
                is Serializable -> putString(name, obj2str(value))
                else -> putString(name, null)
            }
            apply()
        }
    }

    private fun obj2str(obj: Any?): String? {
        //创建字节输出流
        val baos = ByteArrayOutputStream()
        //创建字节对象输出流
        var oos: ObjectOutputStream? = null
        try {
            //将对象转成base64字符串
            oos = ObjectOutputStream(baos)
            oos.writeObject(obj)
            return String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                baos.close()
                oos?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    @Suppress("UNCHECKED_CAST")
    private fun str2obj(string: String?): T? {
        val buffer = Base64.decode(string, Base64.DEFAULT)
        //创建字节输入流
        val bais = ByteArrayInputStream(buffer)
        //创建字节对象输入流
        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(bais)
            return ois.readObject() as T
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bais.close()
                ois?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
}