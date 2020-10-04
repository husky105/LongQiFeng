package com.example.longqifeng.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.longqifeng.R
import com.example.longqifeng.Utils.DBUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.ref.WeakReference
import java.sql.*


class LoginActivity : AppCompatActivity() {
    class MyHandler(activity: LoginActivity) : Handler() {
        //注意下面的“PopupActivity”类是MyHandler类所在的外部类，即所在的activity
        private lateinit var mActivity:WeakReference<LoginActivity>

        init{
            mActivity = WeakReference<LoginActivity>(activity);
        }

        override fun handleMessage(msg: Message?) {
            val theActivity: LoginActivity? = mActivity.get()
            when(msg?.what) {
                //此处可以根据what的值处理多条信息
                0->{
                    val count = msg.obj
                    theActivity?.find_count_res?.setText("用户数量为"+count)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val handler:MyHandler = MyHandler(this)

        find_count.setOnClickListener {
            Thread(object : Runnable{
                override fun run(){
                    val count:Int = DBUtils.getConnection()
                    val msg:Message = Message.obtain()
                    msg.what = 0
                    msg.obj = count
                    //向主线程发送数据
                    handler.sendMessage(msg)
                }
            }).start()
        }
    }
}