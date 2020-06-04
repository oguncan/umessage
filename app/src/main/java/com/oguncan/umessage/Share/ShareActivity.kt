package com.oguncan.umessage.Share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import com.oguncan.umessage.utils.SharePagerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 2
    private val TAG = "ShareActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupViewPagerItems()

    }

    private fun setupViewPagerItems() {

        var tabLayoutItemNames = ArrayList<String>()
        tabLayoutItemNames.add("GALERİ")
        tabLayoutItemNames.add("FOTOĞRAF")
        tabLayoutItemNames.add("VİDEO")


        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabLayoutItemNames)
        sharePagerAdapter.fragmentAdd(ShareGalleryFragment())
        sharePagerAdapter.fragmentAdd(ShareCameraFragment())
        sharePagerAdapter.fragmentAdd(ShareVideoFragment())
        shareViewPager.adapter = sharePagerAdapter
        shareTabLayout.setupWithViewPager(shareViewPager)


    }

    override fun onBackPressed() {
        shareLayoutRoot.visibility = View.VISIBLE
        shareLayoutContainer.visibility = View.GONE
        super.onBackPressed()
    }


}
