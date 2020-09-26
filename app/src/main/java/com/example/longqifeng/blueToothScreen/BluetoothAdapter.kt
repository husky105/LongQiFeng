package com.example.longqifeng.blueToothScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.longqifeng.R

class BluetoothAdapter(val BluetoothList:List<BlueToothItem>):RecyclerView.Adapter<BluetoothAdapter.ViewHolder>() {
    /**
     * 加载控件
     * @constructor
     */
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val BluetoothName:TextView = view.findViewById(R.id.bluetooth_device_name)
        val BluetoothDeviceStatus:TextView = view.findViewById(R.id.bluetooth_device_connection)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_screen_item,parent,false)
        val holder = ViewHolder(view)
        return holder
    }

    /**
     * 用于为BlueTooth赋值
     * @param holder ViewHolder
     * @param position Int
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val BlueToothItem = BluetoothList[position]
        holder.BluetoothName.setText(BlueToothItem.Bluetooth_Device_Name)
        holder.BluetoothDeviceStatus.setText(BlueToothItem.Bluetooth_Connection_Status)
    }

    override fun getItemCount() = BluetoothList.size
}