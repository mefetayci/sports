package com.example.sports

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.adapter.TrainingAdapter
import com.example.sports.model.TrainingItem
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import android.util.Log


import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper




class TrainingScheduleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrainingAdapter
    private val itemList = mutableListOf<TrainingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_schedule)

        Log.d("TrainingFetch", "Activity başladı")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "match_alerts",  // Kanal ID'si
                "Match Alerts",  // Kanal ismi
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }


        recyclerView = findViewById(R.id.recyclerViewTraining)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TrainingAdapter(itemList)
        recyclerView.adapter = adapter

        fetchTrainingData()

        Handler(Looper.getMainLooper()).postDelayed({
            sendMatchReminder()
        }, 30_000) // 30 saniye sonra çalışır

    }

    private fun sendMatchReminder() {
        // Android 13+ için izin kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
                return
            }
        }

        val notification = NotificationCompat.Builder(this, "match_alerts")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Yaklaşan Maç!")
            .setContentText("Maça 5 dakika kaldı, hazırlan!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }



    private fun fetchTrainingData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:8079/sports/get_training.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TrainingFetch", "Bağlantı hatası: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    Log.d("TrainingFetch", "Gelen JSON: $it") // <-- 1

                    val jsonArray = JSONArray(it)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val title = obj.getString("title")
                        val date = obj.getString("date")
                        Log.d("TrainingFetch", "Veri: $title - $date") // <-- 2

                        itemList.add(TrainingItem(title, date))
                    }

                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                        Log.d("TrainingFetch", "Toplam eklenen: ${itemList.size}") // <-- 3
                    }
                }
            }
        })
    }

}
