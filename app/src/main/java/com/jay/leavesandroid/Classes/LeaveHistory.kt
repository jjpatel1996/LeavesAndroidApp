package com.jay.leavesandroid.Classes

import java.util.*

class LeaveHistory(var leaveID:Int, var leaveCount:Int, var leaveDate:Date, var leaveDescription:String, var leaveType:LeaveType, var leaveCreatedDTM:Date?, var leaveModifiedDTM:Date?) {

    override fun toString(): String {
        return "LeaveHistory(leaveID=$leaveID, leaveCount=$leaveCount, leaveDate=$leaveDate, leaveDescription='$leaveDescription', leaveType='$leaveType', leaveCreatedDTM=$leaveCreatedDTM, leaveModifiedDTM=$leaveModifiedDTM)"
    }
}