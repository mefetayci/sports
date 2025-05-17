package com.example.sports

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.adapter.MessageAdapter
import com.example.sports.model.MessageItem
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class CommunicationActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private val messageList = mutableListOf<MessageItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication)

        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<Button>(R.id.btnSend)

        recyclerView = findViewById(R.id.recyclerViewMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter

        fetchMessages()

        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString()
            if (messageText.isNotBlank()) {
                sendMessage("Player1", messageText) // örnek kullanıcı
                etMessage.text.clear()
            }
        }
    }

    private fun fetchMessages() {
        val request = Request.Builder()
            .url("http://10.0.2.2:8079/sports/get_messages.php")
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val jsonArray = JSONArray(it)
                    messageList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        messageList.add(MessageItem(obj.getString("sender"), obj.getString("content")))
                    }
                    runOnUiThread { adapter.notifyDataSetChanged() }
                }
            }
        })
    }

    private fun sendMessage(sender: String, content: String) {
        val json = JSONObject()
        json.put("sender", sender)
        json.put("content", content)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://10.0.2.2:8079/sports/send_message.php")
            .post(body)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                fetchMessages()
            }
        })
    }

}
