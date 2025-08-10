package com.example.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapAdapter(private val laps: MutableList<String>) :
    RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapText: TextView = itemView.findViewById(R.id.lapTimeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lap, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.lapText.text = "Lap ${position + 1}: ${laps[position]}"
    }

    override fun getItemCount(): Int = laps.size

    fun addLap(lapTime: String) {
        laps.add(lapTime)
        notifyItemInserted(laps.size - 1)
    }
}
