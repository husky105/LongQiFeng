package com.example.longqifeng.Connect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler

import java.io.IOException
import java.util.*

/**
 * 定义接收线程
 * @property mmServerSocket BluetoothServerSocket?
 * @property mBluetoothAdapter BluetoothAdapter
 * @property mHandler Handler
 * @property mConnectedThread ConnectedThread?
 * @constructor
 */
class AcceptThread(adapter: BluetoothAdapter, handler: Handler) :
    Thread() {
    companion object {
        private const val NAME = "BlueToothClass"
        private val MY_UUID =
            UUID.fromString(Constant.CONNECTTION_UUID)
    }

    //创建BluetoothServerSocket类
    private val mmServerSocket: BluetoothServerSocket?
    private val mBluetoothAdapter: BluetoothAdapter
    //private val mHandler: Handler
    private var mConnectedThread: ConnectedThread? = null

    init {
        // 使用一个临时对象，该对象稍后被分配给mmServerSocket，因为mmServerSocket是最终的
        mBluetoothAdapter = adapter
        //mHandler = handler
        var tmp: BluetoothServerSocket? = null
        try {
            // MY_UUID是应用程序的UUID，客户端代码使用相同的UUID
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID)
        } catch (e: IOException) { }
        mmServerSocket = tmp
    }

    //线程启动时运行
    override fun run() {
        var socket: BluetoothSocket? = null
        //持续监听，直到出现异常或返回socket
        while (true) {
            try {
                //mHandler.sendEmptyMessage(Constant.MSG_START_LISTENING) //进入监听模式
                //接受
                socket = mmServerSocket?.accept()
            } catch (e: IOException) {
//                mHandler.sendMessage(
//                    mHandler.obtainMessage(Constant.MSG_ERROR, e)
//                )
                break
            }
            // 如果一个连接被接受
            if (socket != null) {
                // 在单独的线程中完成管理连接的工作
                manageConnectedSocket(socket)
                try {
                    //关闭连接
                    mmServerSocket?.close()
                    //mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                break
            }
        }
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        //只支持同时处理一个连接
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
        }
        //mHandler.sendEmptyMessage(Constant.MSG_GOT_A_CLINET)
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread.run { start() }
    }

    /**
     * 取消监听socket，使此线程关闭
     */
    fun cancel() {
        try {
            mmServerSocket?.close()
            //mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING)
        } catch (e: IOException) { }
    }

//    fun sendData(data: ByteArray?) {
//        if (mConnectedThread != null) {
//            mConnectedThread!!.write(data)
//        }
//    }
}