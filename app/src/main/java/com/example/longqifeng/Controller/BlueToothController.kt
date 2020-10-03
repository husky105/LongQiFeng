/*
*蓝牙控制
*@author 泷
*created at 2020/9/22 7:41
*/
package com.example.longqifeng.Controller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.example.longqifeng.Fragment.BluetoothPageFragment

public class BlueToothController {
    private var mAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun getAdapter(): BluetoothAdapter? {
        return mAdapter
    }

    /**
    *判断是否支持蓝牙
    * @return true支持 false不支持
    */
    public fun isSupportBlueTooth():Boolean = if(mAdapter!=null){ true }else{ false }

    /**
    *判断当前蓝牙状态
    * @return true 打开 false关闭
    */
    public fun getBlueToothStatus():Boolean = mAdapter?.isEnabled ?:false

    /**
     * 打开蓝牙
     * @param activity Activity
     * @param requestCode Int
     */
    public fun turnOnBlueTooth(activity: FragmentActivity, requestCode: Int){
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * 打开蓝牙可见性
     * @param context Context
     */
    public fun enableVisibly(context: BluetoothPageFragment){
        val discoverableIntent:Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300) //设置时长为5分钟
        context.startActivity(discoverableIntent)
    }

    /**
     * 查找设备
     */
    fun findDevice(context: BluetoothPageFragment){
        assert(mAdapter != null)
        mAdapter?.startDiscovery()
    }

    fun getBondedDeviceList():List<BluetoothDevice>{
        return object:ArrayList<BluetoothDevice>(mAdapter?.bondedDevices!!){}
    }

    /**
    *关闭蓝牙
    */
    public fun turnOffBlueTooth(){
        mAdapter?.disable()
    }
}