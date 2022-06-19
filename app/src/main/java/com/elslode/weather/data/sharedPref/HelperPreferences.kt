package com.elslode.weather.data.sharedPref

import android.content.SharedPreferences
import com.elslode.weather.R
import com.elslode.weather.data.sharedPref.PrefKeys.CITY_KEY
import com.elslode.weather.data.sharedPref.PrefKeys.TEMPERATURE
import java.lang.reflect.Type

object HelperPreferences {

    inline fun <reified T> SharedPreferences.put(key: String, value: T) {
        val editor = this.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            Float::class -> editor.putFloat(key, value as Float)
            Int::class -> editor.putInt(key, value as Int)
            Long::class -> editor.putLong(key, value as Long)
            String::class -> editor.putString(key, value as String)
            else -> {
                if (value is Set<*>) {
                    editor.putStringSet(key, value as Set<String>)
                }
            }
        }
        editor.apply()
    }

    inline fun <reified T: Any> SharedPreferences.exist(key: String): Boolean {
      return when (T::class) {
            String::class -> getValue<T>(key) != ""
            Int::class -> getValue<T>(key) != 0
            Float::class -> getValue<T>(key) != 0f
            Long::class -> getValue<T>(key) != 0L
            Boolean::class -> getValue<T>(key) != false
            else -> throw ClassCastException()
        }
    }

    inline fun <reified T: Any> SharedPreferences.getValue(key: String): T {
        return when (T::class) {
            Boolean::class -> getBoolean(key, false) as T
            Float::class -> getFloat(key, 0f) as T
            Int::class -> getInt(key, 0) as T
            Long::class -> getLong(key, 0L) as T
            String::class -> getString(key, "") as T
            else -> {
                throw ClassCastException("Unknown this class with key $key which you want get")
            }
        }
    }

    fun SharedPreferences.getTemperature(): String {
        return when (this.getValue<Int>(TEMPERATURE)) {
            R.id.radioButtonF -> PrefKeys.f
            R.id.radioButtonC -> PrefKeys.c
            else -> PrefKeys.c
        }
    }

    fun SharedPreferences.clearCity() {
        this.edit().putString(CITY_KEY, null).apply()
    }

}