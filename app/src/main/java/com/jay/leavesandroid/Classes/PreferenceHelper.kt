package com.jay.leavesandroid.Classes
import android.content.Context

class PreferenceHelper(private val context:Context) {

    fun setLeave(leaveTag:LeavesTags, value:Int){
        val sharedPref = context.getSharedPreferences("Leave",Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(leaveTag.toString(),value)
        editor.apply()
    }

    fun getLeave(leaveTag: LeavesTags) : Int {
        val sharedPref = context.getSharedPreferences("Leave",Context.MODE_PRIVATE)
        return sharedPref.getInt(leaveTag.toString(),0)
    }

}



enum class LeavesTags {

    PaidLeave,
    SickLeave,
    PaidRemainLeave,
    SickRemainLeave;

    override fun toString() : String {
        return when {
            this == LeavesTags.PaidLeave -> "PaidLeave"
            this == LeavesTags.SickLeave -> "SickLeave"
            this == LeavesTags.PaidRemainLeave -> "PaidRemainLeave"
            this == LeavesTags.SickRemainLeave -> "SickRemainLeave"
            else -> super.toString()
        }
    }

}

enum class LeaveType {

    Sick,
    Paid;

    override fun toString() : String {
        return when {
            this == LeaveType.Sick -> "Sick"
            this == LeaveType.Paid -> "Working"
            else -> super.toString()
        }
    }

}
