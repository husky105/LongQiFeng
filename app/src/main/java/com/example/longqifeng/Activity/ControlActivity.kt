package com.example.longqifeng.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.longqifeng.R
import com.example.longqifeng.Fragment.ControlPageFragment
import kotlinx.android.synthetic.main.control_screen_layout.*


class ControlActivity : AppCompatActivity() {
    companion object{
        fun actionStart(context: Context, RoomName:String, DeviceStatus:String){
            val intent = Intent(context, ControlActivity::class.java).apply {
                putExtra("roomName",RoomName)
                putExtra("deviceStatus",DeviceStatus)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_screen_layout)
        val roomName = intent.getStringExtra("roomName")
        val deviceStatus = intent.getStringExtra("deviceStatus")
        if (roomName!=null&&deviceStatus!=null){
            val fragment = controlActivityFag as ControlPageFragment
            fragment.refresh(roomName,deviceStatus)
        }
    }
}