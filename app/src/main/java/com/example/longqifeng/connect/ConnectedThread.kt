package com.example.longqifeng.connect

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread {
    //新建BluetoothSocket类
    private var mmSocket: BluetoothSocket? = null
    //新建输入流对象
    private var mmInStream: InputStream? = null
    //新建输出流对象
    private var mmOutStream: OutputStream? = null
    private var mHandler: Handler? = null

    constructor(socket: BluetoothSocket?) {
        //为BluetoothSocket赋初始值
        mmSocket = socket
        //输入流赋值为null
        var tmpIn: InputStream? = null
        //输出流赋值为null
        var tmpOut: OutputStream? = null
        //mHandler = handler
        // 使用临时对象获取输入和输出流，因为成员流是最终的
        try {
            //从BluetoothSocket中获取输入流
            tmpIn = socket?.inputStream
            //从BluetoothSocket中获取输出流
            tmpOut = socket?.outputStream
        } catch (e: IOException) {
        }
        //为输入流赋值
        mmInStream = tmpIn
        //为输出流赋值
        mmOutStream = tmpOut
    }


    fun run() {
        //流的缓冲大小
        val buffer = ByteArray(1024) // 用于流的缓冲存储
        //用于保存read()所读取的字节数
        var bytes: Int // 从read()返回bytes

        // 持续监听InputStream，直到出现异常
        while (true) {
            try {
                // 从InputStream读取数据
                bytes = mmInStream!!.read(buffer)
                // 将获得的bytes发送到UI层activity
                if (bytes > 0) {
                    val message = mHandler!!.obtainMessage(
                        Constant.MSG_GOT_DATA,
                        String(buffer, 0, bytes , charset("utf-8"))
                    )
                    mHandler!!.sendMessage(message)
                }
                Log.d("GOTMSG", "message size$bytes")
            } catch (e: IOException) {
                mHandler!!.sendMessage(mHandler!!.obtainMessage(Constant.MSG_ERROR, e))
                break
            }
        }
    }

    /**
     * 在main中调用此函数，将数据发送到远端设备中
     */
    fun write(bytes: ByteArray?) {
        try {
            mmOutStream?.write(bytes)
        } catch (e: IOException) { }
    }

    /**
     * 在main中调用此函数，断开连接
     */
    fun cancel() {
        try {
            mmSocket?.close()
        } catch (e: IOException) { }
    }
}