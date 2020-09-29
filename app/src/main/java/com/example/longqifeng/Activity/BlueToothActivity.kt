package com.example.longqifeng.Activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.longqifeng.Adapter.BluetoothDeviceAdapter
import com.example.longqifeng.R
import com.example.longqifeng.Tool.BlueToothController
import kotlinx.android.synthetic.main.bluetooth_screen_layout.*


class BlueToothActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 0
    }

    private var mBondedDeviceList: MutableList<BluetoothDevice> = ArrayList()
    private var mDeviceList: MutableList<BluetoothDevice> = ArrayList()

    private val mController: BlueToothController = BlueToothController()

    private var mToast: Toast? = null
    private lateinit var mAdapter: BluetoothDeviceAdapter
    private var metric: DisplayMetrics = DisplayMetrics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_screen_layout)
        //初始化界面
        initUI()
        //注册广播
        registerBluetoothReceiver()
        //获取屏幕宽高
        windowManager.defaultDisplay.getMetrics(metric)
        val width: Int = metric.widthPixels
        val height: Int = metric.heightPixels
        initConstraintLayout(width, height)
        //为Switch注册监听事件
        bluetooth_turnOn_or_turnOff.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                requestTurnOnBlueTooth(buttonView)
            } else {
                turnOffBlueTooth(buttonView)
            }
        })
        //setListViewHeightBasedOnChildren(bondedBluetoothDevice_listView)
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
        registerReceiver(mReceiver, filter)
    }

    /**
     * 初始化界面
     */
    private fun initUI() {
        mAdapter = BluetoothDeviceAdapter(mDeviceList, this)
        bondedBluetoothDevice_listView.adapter = mAdapter
        bondedBluetoothDevice_listView.setOnItemClickListener(bondDeviceClick)
    }

    private val mReceiver = object: BroadcastReceiver() {
        /**
         * 创建广播监听搜索结果
         * @param context Context
         * @param intent Intent
         */
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                //setProgressBarIndeterminateVisibility(true)
                //初始化数据列表
                mDeviceList?.clear()
                mAdapter.notifyDataSetChanged()
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                //setProgressBarIndeterminateVisibility(false)
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                val device:BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                //找到一个添加一个
                mDeviceList?.add(device)
                mAdapter.notifyDataSetChanged()
            }
            else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {  //此处作用待细查
                val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0)
                //可查找的
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    setProgressBarIndeterminateVisibility(true)
                } else {
                    setProgressBarIndeterminateVisibility(false)
                }
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
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

    /**
     * 加载bluetooth页工具栏
     * @param menu Menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bluetooth, menu)
        return true
    }

    /**
     * 选择工具栏选项
     * @param item MenuItem
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enable_visibly -> mController.enableVisibly(this)
            R.id.search_device -> {
                mAdapter.refresh(mDeviceList)
                mController.findDevice()
                bondedBluetoothDevice_listView.setOnItemClickListener(bondDeviceClick)
            }
            R.id.bonded_device -> {
                mBondedDeviceList =
                    mController.getBondedDeviceList() as MutableList<BluetoothDevice>
                mAdapter.refresh(mBondedDeviceList)
                bondedBluetoothDevice_listView.setOnItemClickListener(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public fun isSupportBlueTooth(view: View?) {
        val ret = mController.isSupportBlueTooth()
        showToast("Support BlueTooth?" + ret)
    }

    public fun isBlueToothEnable(view: View?) {
        val ret = mController.getBlueToothStatus()
        showToast("BlueTooth enable?" + ret)
    }

    public fun requestTurnOnBlueTooth(view: View?) {
        mController.turnOnBlueTooth(this, REQUEST_CODE)
    }

    public fun turnOffBlueTooth(view: View?) {
        mController.turnOffBlueTooth()
    }

    private fun showToast(text: String) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(text)
        }
        mToast!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            showToast("打开成功")
        } else {
            showToast("打开失败")
        }
    }

    private fun initConstraintLayout(width: Int, height: Int) {
        //bluetooth_gridlayout.minimumHeight = width/2
    }

    private var bondDeviceClick = object: AdapterView.OnItemClickListener {
        /**
         * 获取蓝牙设备进行绑定
         * @param parent AdapterView<*>
         * @param view View
         * @param position Int
         * @param id Long
         */
        override fun onItemClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val device = mDeviceList[position]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                device.createBond()
            }
        }
    }
}