package com.example.longqifeng.controlScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.longqifeng.R
import kotlinx.android.synthetic.main.control_control_frag.*

class ControlActivityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.control_control_frag, container, false)
    }

    fun refresh(RoomName:String, DeviceStatus:String){
        controlLayout.visibility = View.VISIBLE //设置为可见
        Room_Name_Of_ControlPage.text = RoomName //显示房间名称
        Device_Status_Of_ControlPage.text = DeviceStatus //设备状态
    }
}