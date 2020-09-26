/*
*蓝牙控制
*@author 泷
*created at 2020/9/22 7:41
*/
package com.example.longqifeng.blueToothScreen

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent

public class BlueToothController {
    private var mAdapter: BluetoothAdapter?

    init {
        //获取蓝牙适配器
        mAdapter = BluetoothAdapter.getDefaultAdapter()
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
    public fun getBlueToothStatus():Boolean = mAdapter?.isEnabled()?:false

    /**
     * 打开蓝牙
     * @param activity Activity
     * @param requestCode Int
     */
    public fun turnOnBlueTooth(activity: Activity,requestCode:Int){
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
    *关闭蓝牙
    */
    public fun turnOffBlueTooth(){
        mAdapter?.disable()
    }
}