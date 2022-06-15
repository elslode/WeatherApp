package com.elslode.weather.utils


import android.os.Build
import java.time.LocalDate

object DataExtensions {

    fun String.changeData(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(this).dayOfMonth.toString()
        } else {
            this
        }
    }
}
