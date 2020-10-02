package com.example.longqifeng.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class FragmentAdapter(private val mFragments: List<Fragment>, fm: FragmentManager?) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(i: Int): Fragment {
        return mFragments[i]
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}