package com.example.stopwatch

import LapData
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapAdapter(private val laps: MutableList<LapData>) :
    RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    private var minLapMillis: Long? = null
    private var maxLapMillis: Long? = null
    private val expandedPositions = mutableSetOf<Int>() // store which items are expanded

    init {
        updateMinMaxTimes()
    }

    private fun updateMinMaxTimes() {
        if (laps.isEmpty()) {
            minLapMillis = null
            maxLapMillis = null
        } else {
            minLapMillis = laps.minOfOrNull { it.lapTimeMillis }
            maxLapMillis = laps.maxOfOrNull { it.lapTimeMillis }
        }
    }

    class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapNumber: TextView = itemView.findViewById(R.id.lapNumberText)
        val lapTime: TextView = itemView.findViewById(R.id.lapTimeText)
        val overallTime: TextView = itemView.findViewById(R.id.overallTimeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lap, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val lap = laps[position]
        holder.lapNumber.text = "Lap ${position + 1}"
        holder.lapTime.text = lap.lapTimeText
        holder.overallTime.text = lap.overallTimeText

        // Alternate background color
        holder.itemView.setBackgroundColor(
            if (position % 2 == 0) Color.parseColor("#F9F9F9")
            else Color.WHITE
        )

        // Min/max colors
        when (lap.lapTimeMillis) {
            minLapMillis -> holder.lapTime.setTextColor(Color.parseColor("#2E7D32")) // Green
            maxLapMillis -> holder.lapTime.setTextColor(Color.parseColor("#C62828")) // Red
            else -> holder.lapTime.setTextColor(Color.BLACK)
        }



       }

    override fun getItemCount(): Int = laps.size

    fun addLap(lapData: LapData) {
        laps.add(lapData)
        updateMinMaxTimes()
        notifyItemInserted(laps.size - 1)
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        val ms = (millis % 1000) / 10
        return "%02d:%02d.%02d".format(minutes, seconds, ms)
    }

    fun clearLaps() {
        laps.clear()
        notifyDataSetChanged()
    }
}
