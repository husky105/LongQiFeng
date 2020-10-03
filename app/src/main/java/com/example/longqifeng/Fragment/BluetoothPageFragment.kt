package com.example.longqifeng.Fragment

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.longqifeng.Adapter.BluetoothDeviceAdapter
import com.example.longqifeng.Controller.BlueToothController
import com.example.longqifeng.R
import com.example.longqifeng.connect.ConnectThread
import com.example.longqifeng.connect.Constant
import kotlinx.android.synthetic.main.bluetooth_screen_frag.*
import kotlinx.android.synthetic.main.bluetooth_screen_headview.*


class BluetoothPageFragment : Fragment() {
    companion object {
        const val REQUEST_CODE = 0
    }

    private var mBondedDeviceList: MutableList<BluetoothDevice> = ArrayList()
    private var mDeviceList: MutableList<BluetoothDevice> = ArrayList()

    private val mController: BlueToothController = BlueToothController()
    private val mUIHandler: Handler = MyHandler()

    private var mToast: Toast? = null
    private lateinit var mAdapter: BluetoothDeviceAdapter

    private var mConnectThread: ConnectThread? = null

    override fun onAttach(activity: Activity) {
        //注册广播
        registerBluetoothReceiver()
        super.onAttach(activity)
    }

    //加载布局
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bluetooth_screen_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //初始化界面
        initUI()
        //设置监听事件
        SetClickEvent()
    }

    private fun SetClickEvent() {
        //为Switch注册监听事件
        bluetooth_turnOn_or_turnOff?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //Toast.makeText(context, "打开蓝牙", Toast.LENGTH_SHORT).show()
                requestTurnOnBlueTooth(buttonView)
            } else {
                //Toast.makeText(context, "关闭蓝牙", Toast.LENGTH_SHORT).show()
                turnOffBlueTooth(buttonView)
            }
        })
        
        bluetooth_visibility_turnOn_or_turnOff?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener() { buttonView, isChecked ->
            if (isChecked) {
                requestTurnOnBlueToothVisibility(bluetooth_visibility_turnOn_or_turnOff)
            } else {
                Toast.makeText(context, "关闭蓝牙可见性", Toast.LENGTH_SHORT).show()
            }
        })

        find_bluetooth_device?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener() { buttonView, isChecked ->
            if (isChecked) {
                mAdapter.refresh(mDeviceList)
                mController.findDevice(this)
                BluetoothDevice_listView.onItemClickListener = bondDeviceClick
                bluetooth_textView.setText(R.string.available_bluetooth)
            } else {
                mBondedDeviceList =
                    mController.getBondedDeviceList() as MutableList<BluetoothDevice>
                mAdapter.refresh(mBondedDeviceList)
                BluetoothDevice_listView.onItemClickListener = bondedDeviceClick
                bluetooth_textView.setText(R.string.bonded_bluetooth)
            }
        })

        is_support_blue_tooth.setOnClickListener {
            isSupportBlueTooth(is_support_blue_tooth)
        }

        is_blue_tooth_enable.setOnClickListener {
            isBlueToothEnable(is_blue_tooth_enable)
        }
    }

    private fun registerBluetoothReceiver() {
        val filter = IntentFilter().apply {
            //开始查找
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            //结束查找
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            //查找设备
            addAction(BluetoothDevice.ACTION_FOUND)
            //设备扫描模式改变
            addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
            //绑定状态
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        activity?.applicationContext?.registerReceiver(receiver, filter)
    }

    /**
     * 初始化界面
     */
    private fun initUI() {
        //添加布局
        val headView: View =
            layoutInflater.inflate(R.layout.bluetooth_screen_headview, null)
        BluetoothDevice_listView?.addHeaderView(headView)

        mAdapter = BluetoothDeviceAdapter(mDeviceList, activity)
        BluetoothDevice_listView?.adapter = mAdapter
        //BluetoothDevice_listView?.setOnItemClickListener(bondDeviceClick)

        val footerView: View =
            layoutInflater.inflate(R.layout.bluetooth_screen_footerview, null)
        BluetoothDevice_listView?.addFooterView(footerView)
    }

    //注册广播监听搜索结果
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                //setProgressBarIndeterminateVisibility(true);
                //初始化数据列表
                mDeviceList.clear()
                mAdapter.notifyDataSetChanged()
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                //setProgressBarIndeterminateVisibility(false);
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                //找到一个添加一个
                mDeviceList.add(device)
                mAdapter.notifyDataSetChanged()
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED == action) {  //此处作用待细查
                val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0)
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    //setProgressBarIndeterminateVisibility(true)
                } else {
                    //setProgressBarIndeterminateVisibility(false)
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val remoteDevice =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (remoteDevice == null) {
                    showToast("无设备")
                    return
                }
                val status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0)
                if (status == BluetoothDevice.BOND_BONDED) {
                    showToast("已绑定" + remoteDevice.name)
                } else if (status == BluetoothDevice.BOND_BONDING) {
                    showToast("正在绑定" + remoteDevice.name)
                } else if (status == BluetoothDevice.BOND_NONE) {
                    showToast("未绑定" + remoteDevice.name)
                }
            }
        }
    }

    public fun isSupportBlueTooth(view: View?) {
        val ret = mController.isSupportBlueTooth()
        showToast("Support BlueTooth?" + ret)
    }

    public fun isBlueToothEnable(view: View?):Boolean {
        val ret = mController.getBlueToothStatus()
        showToast("BlueTooth enable?" + ret)
        return ret
    }

    public fun requestTurnOnBlueToothVisibility(view: View?){
        mController.enableVisibly(this)
    }

    public fun requestTurnOnBlueTooth(view: View?) {
        mController.turnOnBlueTooth(requireActivity(), REQUEST_CODE)
    }

    public fun turnOffBlueTooth(view: View?) {
        mController.turnOffBlueTooth()
    }

    private val bondDeviceClick =
        OnItemClickListener { adapterView, view, i, l ->
            val device = mDeviceList[l.toInt()]
            //Log.d("MyTest",i.toString()+" "+l.toString()+" "+(l.toInt()+1).toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                device.createBond()
            }
        }

    private val bondedDeviceClick =
        OnItemClickListener { _, _, i, l: Long ->
            val device = mBondedDeviceList[l.toInt()]
            //Log.d("MyTest",i.toString()+" "+l.toString()+" "+(l.toInt()+1).toString())
            if (mConnectThread != null) {
                mConnectThread!!.cancel()
            }
            mConnectThread = ConnectThread(device, mController.getAdapter())
            mConnectThread!!.start()
        }

    inner class MyHandler : Handler() {
        override fun handleMessage(message: Message) {
            super.handleMessage(message)
            when (message.what) {
                Constant.MSG_GOT_DATA -> showToast("data:" + message.obj.toString())
                Constant.MSG_ERROR -> showToast("error:" + message.obj.toString())
                Constant.MSG_CONNECTED_TO_SERVER -> showToast("连接到服务端")
                Constant.MSG_GOT_A_CLINET -> showToast("找到服务端")
            }
        }
    }

    private fun showToast(text: String) {
        if (mToast == null) {
            mToast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(text)
        }
        mToast!!.show()
    }
}