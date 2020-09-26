package com.example.longqifeng.homeScreen

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.longqifeng.R
import com.example.longqifeng.blueToothScreen.BlueToothActivity
import tech.gujin.toast.ToastUtil


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen_layout)
    }

    /**
     * Home页工具栏
     * @param menu Menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    /**
     * Home页工具栏选项选择
     * @param item MenuItem
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bluetooth->{
                val intent = Intent(this,BlueToothActivity::class.java)
                startActivityForResult(intent,1)
            }
            else->{Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()}
        }
        return true
    }
}