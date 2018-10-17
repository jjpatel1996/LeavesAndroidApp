package com.jay.leavesandroid.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jay.leavesandroid.Classes.LeaveHistory
import com.jay.leavesandroid.Classes.LeaveType
import com.jay.leavesandroid.Classes.Util
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//val Context.database: LeavesDB
//    get() = LeavesDB.getInstance(applicationContext)

class LeavesDB(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {

        //private var instance: LeavesDB? = null

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "leavesDB.db"
        const val TABLE_LeaveHistory = "leave_history"

        const val COLUMN_ID = "id"
        const val COLUMN_LeaavCount = "leave_count"
        const val COLUMN_LeaveType = "leave_type"
        const val COLUMN_LeaveDescription = "leave_description"
        const val COLUMN_LeaveTakenOn = "leave_taken_on"
        const val COLUMN_LeaveCreatedDTM = "leave_created_dtm"
        const val COLUMN_LeaveModifiedDTM = "leave_modified_dtm"
        const val COLUMN_LeaveDead = "dead"

//        @Synchronized
//        fun getInstance(ctx: Context): LeavesDB {
//            if (instance == null) {
//                instance = LeavesDB(ctx.applicationContext)
//            }
//            return instance!!
//        }

    }

    fun getAllLeave() : HashMap<LeaveType,ArrayList<LeaveHistory>> {

        val db = this.readableDatabase

        val map = HashMap<LeaveType,ArrayList<LeaveHistory>>()
        map[LeaveType.Paid] = ArrayList()
        map[LeaveType.Sick] = ArrayList()

        try {

            val selectQuery = "select * from $TABLE_LeaveHistory where $COLUMN_LeaveDead = 0 order by $COLUMN_LeaveType"

            val c = db.rawQuery(selectQuery,null)

            if (c != null && c.moveToFirst()) {
                do {

                    val id = c.getInt(c.getColumnIndex(COLUMN_ID))
                    val leaveCount = c.getInt(c.getColumnIndex(COLUMN_LeaavCount))
                    val leavetype = c.getString(c.getColumnIndex(COLUMN_LeaveType))
                    val leaveDescription = c.getString(c.getColumnIndex(COLUMN_LeaveDescription))
                    val leaveDate = Util.toDateFromString(c.getString(c.getColumnIndex(COLUMN_LeaveTakenOn)))


                    if(leavetype == LeaveType.Paid.toString()){
                        map.getValue(LeaveType.Paid).add(LeaveHistory(id,leaveCount,leaveDate,leaveDescription,LeaveType.Paid,null,null))
                    }else{
                        map.getValue(LeaveType.Sick).add(LeaveHistory(id,leaveCount,leaveDate,leaveDescription,LeaveType.Sick,null,null))
                    }

                }while (c.moveToNext())
                c.close()
            }

        }catch(e: SQLException){
            print(e.message)
        }
        return map
    }

    fun insertLeave(leave:LeaveHistory) : Boolean {

        val values = ContentValues()
        values.put(COLUMN_LeaavCount,leave.leaveCount)
        values.put(COLUMN_LeaveType,leave.leaveType.toString())
        values.put(COLUMN_LeaveDescription,leave.leaveDescription)
        values.put(COLUMN_LeaveTakenOn,Util.toStringFromDate(leave.leaveDate))
        values.put(COLUMN_LeaveCreatedDTM,Util.toStringFromDate(leave.leaveCreatedDTM!!))
        values.put(COLUMN_LeaveModifiedDTM,Util.toStringFromDate(leave.leaveModifiedDTM!!))
        values.put(COLUMN_LeaveDead,0)

        val db = this.writableDatabase

        return try{
            val index = db.insert(TABLE_LeaveHistory,null,values)
            index > 0
        }catch (e: SQLException){
            print(e.message)
            false
        }

    }

    fun updateLeave(leave:LeaveHistory) {

        val values = ContentValues()

        values.put(COLUMN_LeaavCount,leave.leaveCount)
        values.put(COLUMN_LeaveType,leave.leaveType.toString())
        values.put(COLUMN_LeaveDescription,leave.leaveDescription)
        values.put(COLUMN_LeaveTakenOn,leave.leaveDate.toString())
        values.put(COLUMN_LeaveCreatedDTM,leave.leaveCreatedDTM.toString())
        values.put(COLUMN_LeaveModifiedDTM,leave.leaveModifiedDTM.toString())
        values.put(COLUMN_LeaveDead,0)

        val db = this.writableDatabase

        db.update(TABLE_LeaveHistory,values,"$COLUMN_ID = ?", arrayOf(leave.leaveID.toString()))

    }

    fun deleteLeave(leave:LeaveHistory) : Boolean {

        val db = this.writableDatabase

        return try{
            val affectRows = db.delete(TABLE_LeaveHistory,"$COLUMN_ID = ?", arrayOf(leave.leaveID.toString()))
            affectRows > 0
        }catch (e: SQLException){
            print(e.message)
            false
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createLeaveTable = "CREATE TABLE `$TABLE_LeaveHistory` ( `$COLUMN_ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `$COLUMN_LeaavCount` TEXT NOT NULL, `$COLUMN_LeaveType` TEXT NOT NULL, `$COLUMN_LeaveDescription` TEXT DEFAULT NULL, `$COLUMN_LeaveTakenOn` TEXT, `$COLUMN_LeaveCreatedDTM` TEXT NOT NULL, `$COLUMN_LeaveModifiedDTM` TEXT NOT NULL, `$COLUMN_LeaveDead` INTEGER NOT NULL DEFAULT 0 )"
        println(createLeaveTable)
        try{
            db!!.execSQL(createLeaveTable)
        }catch (e: SQLException){
            print(e.message)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }




}