package com.jay.leavesandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.jay.leavesandroid.Adapters.LeavesAdapter
import com.jay.leavesandroid.Classes.LeaveHistory
import com.jay.leavesandroid.Classes.LeaveType
import com.jay.leavesandroid.Classes.LeavesTags
import com.jay.leavesandroid.Classes.PreferenceHelper
import com.jay.leavesandroid.Database.LeavesDB
import kotlinx.android.synthetic.main.activity_leaves.*

class LeavesActivity : AppCompatActivity(), View.OnTouchListener {


    var leaveList = HashMap<LeaveType, ArrayList<LeaveHistory>>()

    lateinit var adapter: LeavesAdapter

    private var leaveDB: LeavesDB = LeavesDB(this)

    private val pref = PreferenceHelper(this)

    var totalSickLeave = 0
    var totalPaidLeave = 0
    var totalRemainSickLeave = 0
    var totalRemainPaidLeave = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaves)

        leaveDB = LeavesDB(this)
        setupRecycleView()

        fab.setOnClickListener { _ ->
            createNewLeave()
        }

        linear_layout_1.setOnTouchListener(this)
    }

    override fun onResume() {
        super.onResume()
        fetchTotalLeaves()
        setup()
        getLeavesFromDB()
    }

    fun setup() {

        if (totalPaidLeave == 0 && totalSickLeave == 0) {
            linear_layout_1.visibility = View.GONE
            askForTotalLeaves(false)
        } else {
            linear_layout_1.visibility = View.VISIBLE

        }

    }

    fun fetchTotalLeaves() {

        totalSickLeave = pref.getLeave(LeavesTags.SickLeave)
        totalPaidLeave = pref.getLeave(LeavesTags.PaidLeave)
        totalRemainSickLeave = pref.getLeave(LeavesTags.SickRemainLeave)
        totalRemainPaidLeave = pref.getLeave(LeavesTags.PaidRemainLeave)

        paid_textView.text = "${totalPaidLeave - totalRemainPaidLeave} taken | $totalRemainPaidLeave remain"
        sick_textView.text = "${totalSickLeave - totalRemainSickLeave} taken | $totalRemainSickLeave remain"

    }

    private fun setupRecycleView() {

        leaveList = leaveDB.getAllLeave()
        leaves_recycle_id.layoutManager = LinearLayoutManager(this)
        adapter = LeavesAdapter(leaveList[LeaveType.Paid]!!, this)
        leaves_recycle_id.adapter = adapter

    }

    private fun getLeavesFromDB() {
        leaveList = leaveDB.getAllLeave()
        adapter.setitemes(leaveList[LeaveType.Paid]!!)
        adapter.notifyDataSetChanged()

    }

    private fun createNewLeave() {
        //Get New Leave

        val intent = Intent(this, AddEditLeaveActivity::class.java)
        intent.putExtra("isForEdit", false)
        startActivity(intent)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == linear_layout_1) {
            //askForTotalLeaves(true)
        }
        return true
    }

    private fun askForTotalLeaves(isEdit: Boolean) {

        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Setup total leaves")
        alertBuilder.setCancelable(false)

        val viewInflate = layoutInflater.inflate(R.layout.popup_view_get_total_leave, null)
        alertBuilder.setView(viewInflate)

        val paidSeekBar = viewInflate.findViewById<SeekBar>(R.id.paid_leave_bar)
        val sickSeekBar = viewInflate.findViewById<SeekBar>(R.id.sick_leave_bar)
        val paid_total_textView = viewInflate.findViewById<TextView>(R.id.paid_total_textView)
        val sick_total_textView = viewInflate.findViewById<TextView>(R.id.sick_total_textView)

        paidSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                paid_total_textView.text = getString(R.string.enter_the_number_of_total_paid_leave) + " ($progress)"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        sickSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sick_total_textView.text = getString(R.string.enter_the_number_of_total_sick_leave) + " ($progress)"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        alertBuilder.setPositiveButton("Save") { _, _ ->
            pref.setLeave(LeavesTags.PaidLeave, paidSeekBar.progress)
            pref.setLeave(LeavesTags.SickLeave, sickSeekBar.progress)

            if (!isEdit) {
                pref.setLeave(LeavesTags.PaidRemainLeave, paidSeekBar.progress)
                pref.setLeave(LeavesTags.SickRemainLeave, sickSeekBar.progress)
            }

            fetchTotalLeaves()
            setup()
        }

        alertBuilder.setNegativeButton("Cancel") { _, _ ->
            fetchTotalLeaves()
            setup()
        }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }


}
