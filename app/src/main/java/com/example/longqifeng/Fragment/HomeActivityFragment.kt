/*
*
*@author 泷
*created at 2020/9/11 17:03
*加载主页面控制列表布局
*/
package com.example.longqifeng.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.longqifeng.R
import com.example.longqifeng.Activity.ControlActivity
import com.example.longqifeng.Item.HomeItem
import kotlinx.android.synthetic.main.home_control_frag.*
import kotlinx.android.synthetic.main.home_screen_layout.*

class HomeActivityFragment:Fragment() {
    private var isTwoPane = false

    //数据源
    private val HomeItemList = ArrayList<HomeItem>()

    //加载布局
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_control_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.controlActivityLayout) != null
        initHomeItem() //读入初始化数据
        val layoutManager = LinearLayoutManager(activity)
        HomeRecyclerView.layoutManager = layoutManager
        val adapter = HomeItemAdapter(HomeItemList)
        HomeRecyclerView.adapter = adapter
    }

    inner class HomeItemAdapter(val HomeItemList:List<HomeItem>): RecyclerView.Adapter<HomeItemAdapter.ViewHolder>() {
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            val HomeItemImage: ImageView = view.findViewById(R.id.Home_Item_Image)
            val RoomName: TextView = view.findViewById(R.id.Room_Name)
            val DeviceStatus: TextView = view.findViewById(R.id.Device_Status)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.home_control_item,parent,false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val HomeItem = HomeItemList[holder.adapterPosition]
                if (isTwoPane){
                    //如果是双页模式，刷新ControlActivityFragment内容
                    val fragment = controlActivityFag as ControlActivityFragment
                    fragment.refresh(HomeItem.RoomName,HomeItem.DeviceStatus)
                }else{
                    //如果是单页模式，直接启动HomeActivity
                    ControlActivity.actionStart(parent.context,HomeItem.RoomName,HomeItem.DeviceStatus)
                }
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val HomeItem = HomeItemList[position] //获取当前HomeItem实例
            holder.RoomName.setText(HomeItem.RoomName)
            holder.DeviceStatus.setText(HomeItem.DeviceStatus)
            when(HomeItem.RoomName){
                "客厅" -> { holder.HomeItemImage.setImageResource(R.drawable.living_room) }
                "厨房" -> { holder.HomeItemImage.setImageResource(R.drawable.kitchen) }
                "主卧" -> { holder.HomeItemImage.setImageResource(R.drawable.master_bedroom) }
                "次卧" -> { holder.HomeItemImage.setImageResource(R.drawable.second_bedroom) }
                "洗手间" -> { holder.HomeItemImage.setImageResource(R.drawable.restroom) }
            }
        }

        override fun getItemCount() = HomeItemList.size
    }

    private fun initHomeItem() {
        HomeItemList.add(HomeItem("客厅","关闭"))
        HomeItemList.add(HomeItem("主卧","关闭"))
        HomeItemList.add(HomeItem("次卧","关闭"))
        HomeItemList.add(HomeItem("厨房","关闭"))
        HomeItemList.add(HomeItem("洗手间","关闭"))
    }
}