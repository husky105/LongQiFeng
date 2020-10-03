package com.example.longqifeng.connect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import java.io.IOException
import java.util.*

/**
 * 定义Bluetooth连接线程
 * @property MY_UUID (java.util.UUID..java.util.UUID?)
 * @property mmSocket BluetoothSocket?
 * @property mmDevice BluetoothDevice?
 * @property mBluetoothAdapter BluetoothAdapter?
 * @property mHandler Handler?
 * @property mConnectedThread ConnectedThread?
 */
class ConnectThread:Thread {
    private val MY_UUID = UUID.fromString(Constant.CONNECTTION_UUID)
    //新建BluetoothSocket类
    private var mmSocket: BluetoothSocket? = null
    //新建BluetoothDevice对象
    private var mmDevice: BluetoothDevice? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    //private var mHandler: Handler? = null
    private var mConnectedThread: ConnectedThread? = null

    constructor(device: BluetoothDevice,adapter: BluetoothAdapter?) {
        // U将一个临时对象分配给mmSocket，因为mmSocket是最终的
        var tmp: BluetoothSocket? = null
        //赋值给设备
        mmDevice = device
        mBluetoothAdapter = adapter
        //mHandler = handler
        // 用BluetoothSocket连接到给定的蓝牙设备
        try {
            // MY_UUID是应用程序的UUID，客户端代码使用相同的UUID
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID)
        } catch (e: IOException) { }
        //赋值给BluetoothSocket
        mmSocket = tmp
    }

    override fun run() {
        // 搜索占用资源大，关掉提高速度
        mBluetoothAdapter?.cancelDiscovery()
        try {
            // 通过socket连接设备，阻塞运行直到成功或抛出异常时
            mmSocket?.connect()
        } catch (connectException: Exception) {
            //mHandler!!.sendMessage(mHandler?.obtainMessage(Constant.MSG_ERROR, connectException))
            // 如果无法连接则关闭socket并退出
            try {
                mmSocket?.close()
            } catch (closeException: IOException) { }
            return
        }
        // 在单独的线程中完成管理连接的工作
        manageConnectedSocket(mmSocket)
    }

    private fun manageConnectedSocket(mmSocket: BluetoothSocket?) {
        //mHandler!!.sendEmptyMessage(Constant.MSG_CONNECTED_TO_SERVER)
        mConnectedThread = ConnectedThread(mmSocket)
        mConnectedThread.run { start() }
    }

    /**
     * 取消正在进行的连接并关闭socket
     */
    fun cancel() {
        try {
            //关闭BluetoothSocket
            mmSocket?.close()
        } catch (e: IOException) { }
    }

    /**
     * 发送数据
     */
//    fun sendData(data: ByteArray?) {
//        if (mConnectedThread != null) {
//            mConnectedThread!!.write(data)
//        }
//    }
}