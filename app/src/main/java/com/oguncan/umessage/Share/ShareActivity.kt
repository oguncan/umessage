package com.oguncan.umessage.Share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class ShareActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 2
    private val TAG = "ShareActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupNavigationView()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(homeBottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,homeBottomNavigationView)
        var menu = homeBottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
