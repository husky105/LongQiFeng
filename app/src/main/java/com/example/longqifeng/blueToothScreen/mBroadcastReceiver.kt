package com.example.longqifeng.blueToothScreen

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class mBroadcastReceiver:BroadcastReceiver() {
    /**
     * 广播当前蓝牙开启状态
     * @param context Context
     * @param intent Intent
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1)
        when(state){
            BluetoothAdapter.STATE_OFF->Toast.makeText(context,"STATE_OFF",Toast.LENGTH_SHORT).show()
            BluetoothAdapter.STATE_ON->Toast.makeText(context,"STATE_ON",Toast.LENGTH_SHORT).show()
            BluetoothAdapter.STATE_TURNING_OFF->Toast.makeText(context,"STATE_TURNING_OFF",Toast.LENGTH_SHORT).show()
            BluetoothAdapter.STATE_TURNING_ON->Toast.makeText(context,"STATE_TURNING_ON",Toast.LENGTH_SHORT).show()
            else->Toast.makeText(context,"Unkown STATE",Toast.LENGTH_SHORT).show()
        }
    }
}