package com.macro.bot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var roomAdapter: ArrayAdapter<String>
    private val roomList = ArrayList<String>()

    private val roomReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val rooms = intent?.getStringArrayListExtra("room_list")
            if (rooms != null) {
                roomList.clear()
                roomList.addAll(rooms)
                roomAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPermission = findViewById<Button>(R.id.btnPermission)
        val roomListView = findViewById<ListView>(R.id.roomListView)

        roomAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, roomList)
        roomListView.adapter = roomAdapter

        btnPermission.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.macro.bot.UPDATE_ROOMS")
        registerReceiver(roomReceiver, filter, RECEIVER_EXPORTED)
        BotService.requestRoomList(this)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(roomReceiver)
    }
}
