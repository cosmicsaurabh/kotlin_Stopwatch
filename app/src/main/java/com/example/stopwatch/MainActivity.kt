package com.example.stopwatch

import LapData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lapAdapter: LapAdapter
    private var lastLapTime: Long = 0L


    private var running = false
    private var offset: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong("offset")
            running = savedInstanceState.getBoolean("running")
            binding.stopwatch.base = savedInstanceState.getLong("base")

            if (running) {
                binding.stopwatch.start()
            }
        }

        // RecyclerView setup
        lapAdapter = LapAdapter(mutableListOf())
        binding.lapRecyclerView.adapter = lapAdapter
        binding.lapRecyclerView.layoutManager = LinearLayoutManager(this)
        // LAP button
        binding.btnLapReset.setOnClickListener {
            if (running) {
                vibrateShort()
                val now = SystemClock.elapsedRealtime()
                val lapDuration = now - lastLapTime
                lastLapTime = now // reset for next lap

                val lapMinutes = (lapDuration / 1000) / 60
                val lapSeconds = (lapDuration / 1000) % 60
                val lapMillis = (lapDuration % 1000) / 10
                val formattedLapTime = "%02d:%02d.%02d".format(lapMinutes, lapSeconds, lapMillis)

                val overallMillis = now -  binding.stopwatch.base
                val overallMinutes = (overallMillis / 1000) / 60
                val overallSeconds = (overallMillis / 1000) % 60
                val overallMs = (overallMillis % 1000) / 10
                val formattedOverallTime = "%02d:%02d.%02d".format(overallMinutes, overallSeconds, overallMs)

                lapAdapter.addLap(LapData(formattedLapTime, lapDuration, formattedOverallTime))
            }
        }




        // START / PAUSE
        binding.btnStartStop.setOnClickListener {
            if (!running) {
                // START or RESUME
                vibrateShort()
                binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
                binding.stopwatch.start()
                running = true
                lastLapTime = SystemClock.elapsedRealtime()

                binding.btnStartStop.text = "Stop"
                binding.btnLapReset.isEnabled = true
                binding.btnLapReset.text = "Lap"
            } else {
                // STOP
                vibrateShort()
                offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
                binding.stopwatch.stop()
                running = false

                binding.btnStartStop.text = "Resume"
                binding.btnLapReset.text = "Reset"
            }
        }



        // Lap/Reset Button
        binding.btnLapReset.setOnClickListener {
            if (running) {
                // LAP
                vibrateShort()
                val now = SystemClock.elapsedRealtime()
                val lapDuration = now - lastLapTime
                lastLapTime = now

                val lapMinutes = (lapDuration / 1000) / 60
                val lapSeconds = (lapDuration / 1000) % 60
                val lapMillis = (lapDuration % 1000) / 10
                val formattedLapTime = "%02d:%02d.%02d".format(lapMinutes, lapSeconds, lapMillis)

                val overallMillis = now - binding.stopwatch.base
                val overallMinutes = (overallMillis / 1000) / 60
                val overallSeconds = (overallMillis / 1000) % 60
                val overallMs = (overallMillis % 1000) / 10
                val formattedOverallTime = "%02d:%02d.%02d".format(overallMinutes, overallSeconds, overallMs)

                lapAdapter.addLap(LapData(formattedLapTime, lapDuration, formattedOverallTime))
            } else {
                // RESET
                vibrateShort()
                offset = 0
                binding.stopwatch.base = SystemClock.elapsedRealtime()
                binding.stopwatch.stop()
                running = false
                lapAdapter.clearLaps()

                binding.btnStartStop.text = "Start"
                binding.btnLapReset.text = "Lap"
                binding.btnLapReset.isEnabled = false
            }
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("offset", offset)
        outState.putBoolean("running", running)
        outState.putLong("base", binding.stopwatch.base)
    }
    private fun vibrateShort(){
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else{
            vibrator.vibrate(5000);
        }
    }
}
