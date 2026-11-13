package com.teamshryne.vimo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var engine: Long = 0
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        status = findViewById(R.id.status)

        try {
            engine = VimoCore.nativeCreate(1280, 720, 48000, 2)
            status.text = "Engine ready: $engine"
        } catch (t: Throwable) {
            status.text = "Engine failed: ${t.message}"
            return
        }

        findViewById<Button>(R.id.newProject).setOnClickListener {
            val rc = VimoCore.nativeExecute(engine, CommandTag.NEW_PROJECT, 0, 0, 0, null, null)
            status.text = "New project: $rc"
        }
        findViewById<Button>(R.id.snapshot).setOnClickListener {
            val json = VimoCore.nativeSnapshot(engine)
            status.text = "Snapshot: ${json?.length ?: 0} bytes"
        }
        findViewById<Button>(R.id.stats).setOnClickListener {
            val s = VimoCore.nativeStats(engine)
            status.text = "Stats: ${s?.contentToString()}"
        }
    }

    override fun onDestroy() {
        if (engine != 0L) {
            VimoCore.nativeDestroy(engine)
            engine = 0
        }
        super.onDestroy()
    }
}
