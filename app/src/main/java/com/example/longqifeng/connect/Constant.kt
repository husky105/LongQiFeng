package com.example.longqifeng.connect

class Constant {
    companion object {
        val CONNECTTION_UUID = "00001106-0000-1000-8000-00805F9B34FB"

        /**
         * 开始监听
         */
        val MSG_START_LISTENING = 1

        /**
         * 结束监听
         */
        val MSG_FINISH_LISTENING = 2

        /**
         * 有客户端连接
         */
        val MSG_GOT_A_CLINET = 3

        /**
         * 连接到服务器
         */
        val MSG_CONNECTED_TO_SERVER = 4

        /**
         * 获取到数据
         */
        val MSG_GOT_DATA = 5

        /**
         * 出错
         */
        val MSG_ERROR = -1
    }
}