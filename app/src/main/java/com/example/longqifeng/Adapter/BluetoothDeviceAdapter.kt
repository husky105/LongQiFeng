package com.example.longqifeng.Adapter

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.longqifeng.R

class BluetoothDeviceAdapter(
    private var mData: List<BluetoothDevice>,
    private var mContext: FragmentActivity?
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view:View
        //复用view，优化性能
        if (convertView == null) {
            view = LayoutInflater.from(mContext)
                .inflate(R.layout.bluetooth_screen_item, parent, false)
        }else{
            view = convertView
        }
        val BluetoothName:TextView = view.findViewById(R.id.bluetooth_device_name)
        val BluetoothAddress:TextView = view.findViewById(R.id.bluetooth_address)
        BluetoothName.setTextColor(Color.BLACK)
        BluetoothAddress.setTextColor(Color.BLACK)

        //获取对应的蓝牙设备
        val device = getItem(position) as BluetoothDevice

        //显示设备名称
        BluetoothName.text = device.name
        //显示设备地址
        BluetoothAddress.text = device.address
        return view
    }

    override fun getCount() = mData.size

    override fun getItem(position: Int): BluetoothDevice {
        return mData[position]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    //刷新列表，防止搜索结果重复出现
    fun refresh(data: List<BluetoothDevice>) {
        mData = data
        notifyDataSetChanged()
    }
}