package com.jay.leavesandroid

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import com.jay.leavesandroid.Classes.*
import com.jay.leavesandroid.Database.LeavesDB
import kotlinx.android.synthetic.main.activity_add_edit_leave.*
import kotlinx.android.synthetic.main.list_leaves_card.*
import java.text.SimpleDateFormat
import java.util.*

class AddEditLeaveActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {


    var isForEdit: Boolean = false

    lateinit var leave: LeaveHistory

    lateinit var selectedDate: Date

    val leaveDB = LeavesDB(this, null, null, 1)

    var totalRemainPaidLeaves = 0
    var totalRemainSickLeaves = 0

    private val pref = PreferenceHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_leave)

        isForEdit = intent.getBooleanExtra("isForEdit", false)

        select_date_for_leave.setOnClickListener({
            showCalender()
        })

        leave_selector_seekbar.setOnSeekBarChangeListener(this)

        leave_type_radioGroup.setOnCheckedChangeListener(this)

        totalRemainPaidLeaves = pref.getLeave(LeavesTags.PaidRemainLeave)
        totalRemainSickLeaves = pref.getLeave(LeavesTags.SickRemainLeave)

        if (isForEdit) {
            if (leave.leaveType == LeaveType.Paid) {
                leave_selector_seekbar.max = totalRemainPaidLeaves
            } else {
                leave_selector_seekbar.max = totalRemainSickLeaves
            }
            leave_selector_seekbar.progress = leave.leaveCount
            leave_description_textView_id.text = leave.leaveDescription
            number_of_leave_textView.text = "Number of leaves: $leave.leaveCount"
            select_date_for_leave.text = Util.toStringFromDate(leave.leaveDate)

        } else {
            leave_selector_seekbar.max = totalRemainPaidLeaves
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        number_of_leave_textView.text = "Number of leaves: $progress"
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (group!! == paid_radio_button && paid_radio_button.isChecked) {
            leave_selector_seekbar.max = totalRemainSickLeaves
        } else {
            leave_selector_seekbar.max = totalRemainPaidLeaves
        }
    }

    private fun showCalender() {

        val canlender = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, this, canlender.get(Calendar.YEAR), canlender.get(Calendar.MONTH), canlender.get(Calendar.DAY_OF_MONTH))
        datePicker.show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_leaves, menu)
        if (menu != null)
        menu.findItem(0).title = if (isForEdit) "Update" else "Save"
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        tappedOnSaveUpdate()
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    private fun tappedOnSaveUpdate() {

        val leaveCount = leave_selector_seekbar.progress
        val leaveDescription = leave_description_textView_id.text.toString()
        val leaveType: LeaveType = if (paid_radio_button.isChecked) LeaveType.Paid else LeaveType.Sick

        //Do Two task save or update
        if (isForEdit) {

            val current = Date()
            leave = LeaveHistory(leave.leaveID, leaveCount, selectedDate, leaveDescription, leaveType, leave.leaveCreatedDTM, current)

            leaveDB.updateLeave(leave)
            Toast.makeText(this, "Updated current leave", Toast.LENGTH_LONG).show()

        } else {

            val current = Date()
            leave = LeaveHistory(-1, leaveCount, selectedDate, leaveDescription, leaveType, current, current)

            if (leaveDB.insertLeave(leave)) {
                Toast.makeText(this, "New leave added", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Sorry, Something went wrong", Toast.LENGTH_LONG).show()
            }

        }
        finish()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val calender = Calendar.getInstance()
        print("Tapped Save")
        //select_date_for_leave.text = "$dayOfMonth-$month-$year"

        selectedDate = Util.toDateFromString("$dayOfMonth-$month-$year")
        println(selectedDate)


        calender.set(year, month, dayOfMonth);
        select_date_for_leave.text = (SimpleDateFormat("dd-MM-yyyy").format(calender.time))


    }

}
