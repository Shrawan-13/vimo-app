package com.teamshryne.vimo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = findViewById<TextView>(R.id.homeStatus)
        val prefs = getSharedPreferences("vimo", MODE_PRIVATE)
        val last = prefs.getString("last_project", null)
        status.text = if (last != null) "Last project: $last" else "No recent project"

        findViewById<Button>(R.id.newProject).setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java).putExtra("new", true))
        }
        findViewById<Button>(R.id.resumeProject).setOnClickListener {
            if (last != null) {
                startActivity(Intent(this, EditorActivity::class.java).putExtra("project_path", last))
            } else {
                status.text = "No recent project to resume"
            }
        }
        findViewById<Button>(R.id.sampleProject).setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java).putExtra("sample", true))
        }
    }
}
