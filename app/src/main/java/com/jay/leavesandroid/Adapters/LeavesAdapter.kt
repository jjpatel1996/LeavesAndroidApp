package com.jay.leavesandroid.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jay.leavesandroid.Classes.LeaveHistory
import com.jay.leavesandroid.R
import kotlinx.android.synthetic.main.list_leaves_card.view.*

class LeavesAdapter(var items : ArrayList<LeaveHistory>, val context: Context) : RecyclerView.Adapter<LeavesAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    fun setitemes(newItems : ArrayList<LeaveHistory>){
        items.clear()
        items = newItems
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_leaves_card, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder?.tvAnimalType?.text = items.get(position)

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val leaveCount = view.leave_count_textView_id
        var leaveDate = view.leave_date_textView_id
        var leaveDescription = view.leave_description_textView_id


}

}

