package com.mio.base.utils

import android.content.Context
import android.content.SharedPreferences
import com.mio.base.extension.toBean
import com.mio.base.extension.toJson

object KvHelper {
    private lateinit var sharedPreferences: SharedPreferences
    private const val PREF_NAME = "AppPreferences"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 存储字符串值
    fun saveString(key: String?, value: String?) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    // 读取字符串值
    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    // 存储整数值
    fun saveInt(key: String?, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    // 读取整数值
    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // float
    fun saveFloat(key: String?, value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    // long
    fun saveLong(key: String?, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    // 存储布尔值
    fun saveBoolean(key: String?, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    // 读取布尔值
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // 清除指定键的值
    fun remove(key: String?) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    // 清除所有存储的数据
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    // 存储自定义数据类型 存储对应的json
    fun <T : Any> saveObject(key: String?, value: T) {
        val json = value.toJson()
        saveString(key, json)
    }

    fun <T : Any> getObject(key: String?, clazz: Class<T>): T? {
        val json = getString(key, null)
        return json?.toBean(clazz)
    }
}