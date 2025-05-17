package com.example.sports

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnTraining).setOnClickListener {
            startActivity(Intent(this, TrainingScheduleActivity::class.java))
        }

        findViewById<Button>(R.id.btnStatistics).setOnClickListener {
            startActivity(Intent(this, TeamStatisticsActivity::class.java))
        }

        findViewById<Button>(R.id.btnCommunication).setOnClickListener {
            startActivity(Intent(this, CommunicationActivity::class.java))
        }
    }
}
