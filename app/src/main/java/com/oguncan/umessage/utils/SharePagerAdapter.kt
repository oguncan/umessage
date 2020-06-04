package com.oguncan.umessage.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SharePagerAdapter(fm : FragmentManager, tabItems : ArrayList<String>) : FragmentPagerAdapter(fm) {
    private var mFragmentList : ArrayList<Fragment> = ArrayList()
    private var mTabItems : ArrayList<String> = tabItems
    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun fragmentAdd(fragment : Fragment){
        mFragmentList.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTabItems.get(position)
    }
}