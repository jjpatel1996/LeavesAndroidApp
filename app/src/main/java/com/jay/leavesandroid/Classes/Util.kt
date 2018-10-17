package com.jay.leavesandroid.Classes

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Util {

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun toStringFromDate(date: Date) : String {
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun toDateFromString(date: String) : Date {
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.parse(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTimeInString() : String {

        val current = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ssss")
        return formatter.format(current)

    }



}