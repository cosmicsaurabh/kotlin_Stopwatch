package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lapAdapter: LapAdapter


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
        binding.lapButton.setOnClickListener {
            if (running) {
                val elapsed = SystemClock.elapsedRealtime() - binding.stopwatch.base
                val minutes = (elapsed / 1000) / 60
                val seconds = (elapsed / 1000) % 60
                val millis = (elapsed % 1000) / 10
                val formattedTime = "%02d:%02d.%02d".format(minutes, seconds, millis)
                lapAdapter.addLap(formattedTime)
            }
        }


        // START
        binding.startButton.setOnClickListener {
            if (!running) {
                binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
                binding.stopwatch.start()
                running = true
            }
        }

        // PAUSE
        binding.pauseButton.setOnClickListener {
            if (running) {
                offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
                binding.stopwatch.stop()
                running = false
            }
        }



        // RESET
        binding.resetButton.setOnClickListener {
            offset = 0
            binding.stopwatch.base = SystemClock.elapsedRealtime()
            binding.stopwatch.stop()
            running = false
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("offset", offset)
        outState.putBoolean("running", running)
        outState.putLong("base", binding.stopwatch.base)
    }
}
