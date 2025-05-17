package com.example.sports

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.adapter.StatsAdapter
import com.example.sports.model.StatsItem
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class TeamStatisticsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StatsAdapter
    private val statsList = mutableListOf<StatsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_statistics)

        recyclerView = findViewById(R.id.recyclerViewStats)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StatsAdapter(statsList)
        recyclerView.adapter = adapter

        fetchStats()
    }

    private fun fetchStats() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:8079/sports/get_stats.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val jsonArray = JSONArray(it)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val name = obj.getString("player_name")
                        val goals = obj.getInt("goals")
                        val assists = obj.getInt("assists")
                        statsList.add(StatsItem(name, goals, assists))
                    }
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
