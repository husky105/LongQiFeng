package com.example.longqifeng.blueToothScreen

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import com.example.longqifeng.R
import kotlinx.android.synthetic.main.bluetooth_screen_layout.*

class BlueToothActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_CODE = 0
    }

    private val mController:BlueToothController = BlueToothController()
    private var mToast:Toast? = null
    private var receiver:mBroadcastReceiver = mBroadcastReceiver()
    private var metric:DisplayMetrics = DisplayMetrics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_screen_layout)
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(receiver,filter)
        /**
         * 获取屏幕宽高
         */
        windowManager.defaultDisplay.getMetrics(metric)
        val width:Int = metric.widthPixels
        val height:Int = metric.heightPixels
        initConstraintLayout(width,height)
    }

    public fun isSupportBlueTooth(view: View?){
        val ret = mController.isSupportBlueTooth()
        showToast("Support BlueTooth?" + ret)
    }

    public fun isBlueToothEnable(view: View?){
        val ret = mController.getBlueToothStatus()
        showToast("BlueTooth enable?" + ret)
    }

    public fun requestTurnOnBlueTooth(view: View?){
        mController.turnOnBlueTooth(this, REQUEST_CODE)
        is_blue_tooth_enable.setBackgroundColor(Color.parseColor("#3ae374"))
    }

    public fun turnOffBlueTooth(view: View?){
        mController.turnOffBlueTooth()
        is_blue_tooth_enable.setBackgroundColor(Color.parseColor("#b2bec3"))
    }

    private fun showToast(text:String){
        if(mToast == null){
            mToast = Toast.makeText(this,text,Toast.LENGTH_SHORT)
        }else{
            mToast!!.setText(text)
        }
        mToast!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            showToast("打开成功")
        }else{
            showToast("打开失败")
        }
    }

    private fun initConstraintLayout(width:Int,height:Int){
        //bluetooth_gridlayout.minimumHeight = width/2
    }
}