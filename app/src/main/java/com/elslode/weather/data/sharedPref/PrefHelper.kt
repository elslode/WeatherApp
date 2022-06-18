package com.elslode.weather.data.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.elslode.weather.R
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefHelper (context: Context) {

    private val PREFS_NAME = "weatherApp_preferences"
    private var sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val gson = Gson()

    init {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String?) {
        editor.putString(key, value)
            .apply()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun put(key: String, value: Float?) {
        if (value != null) {
            editor.putFloat(key, value)
                .apply()
        }
    }

    fun put(key: String, value: Int) {
        editor.putInt(key, value)
            .apply()
    }

    fun existsFloat(key: String): Boolean {
        val floatValue = sharedPref.getFloat(key, 0f)
        return floatValue > 0f
    }

    fun existsInt(key: String): Boolean {
        val intValue = sharedPref.getInt(key, 0)
        return intValue > 0
    }

    fun existsString(key: String): Boolean {
        val stringValue = sharedPref.getString(key, null)
        return stringValue != null
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun getFloat(key: String): Float {
        return sharedPref.getFloat(key, 0f)
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, -1)
    }

    fun clear() {
        editor.clear()
            .apply()
    }

    fun cityClear(){
        editor.putString(PrefKeys.CITY_KEY, null)
    }

    fun getTemperature(): String {
        return when (get<Int>(PrefKeys.TEMPERATURE, Int::class.java)) {
            R.id.radioButtonF -> PrefKeys.f
            R.id.radioButtonC -> PrefKeys.c
            else -> "ERROR"
        }
    }

    fun getTemperatureId(): Int {
        return get(PrefKeys.TEMPERATURE, Int::class.java)
    }

    fun <T> get(key: String, type: Type): T {
        val intValue = sharedPref.getInt(key, -1) ?: throw Exception()
        return gson.fromJson(intValue.toString(), type) ?: throw Exception()
    }

}