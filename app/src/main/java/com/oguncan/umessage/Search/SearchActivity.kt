package com.oguncan.umessage.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class SearchActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 1
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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
