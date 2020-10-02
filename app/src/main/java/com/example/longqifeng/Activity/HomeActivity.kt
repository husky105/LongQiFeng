package com.example.longqifeng.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.longqifeng.Adapter.FragmentAdapter
import com.example.longqifeng.Fragment.BluetoothPageFragment
import com.example.longqifeng.Fragment.ControlPageFragment
import com.example.longqifeng.Fragment.HomePageFragment
import com.example.longqifeng.R
import kotlinx.android.synthetic.main.home_screen_layout.*


class HomeActivity : AppCompatActivity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen_layout)

        //添加主界面Fragment
        val fragments = ArrayList<Fragment>().apply {
            add(HomePageFragment())
            add(BluetoothPageFragment())
            add(Fragment())
        }


        val adapter:FragmentAdapter = FragmentAdapter(fragments, supportFragmentManager)

        view_pager.adapter = adapter

        //BottomNavigationView 点击事件监听
        bottom_nav_view.setOnNavigationItemSelectedListener { menuItem ->
            val menuId = menuItem.itemId
            when (menuId) {
                R.id.tab_one -> view_pager.setCurrentItem(0)
                R.id.tab_two -> view_pager.setCurrentItem(1)
                R.id.tab_three -> view_pager.setCurrentItem(2)
            }
            false
        }

        view_pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                //将滑动到的页面对应的 menu 设置为选中状态
                bottom_nav_view.getMenu().getItem(i).setChecked(true)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    /**
     * Home页工具栏
     * @param menu Menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * Home页工具栏选项选择
     * @param item MenuItem
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            else->{Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()}
        }
        return true
    }
}