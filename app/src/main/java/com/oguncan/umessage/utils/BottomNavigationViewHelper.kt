package com.oguncan.umessage.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.oguncan.umessage.Heart.HeartActivity
import com.oguncan.umessage.Home.HomeActivity
import com.oguncan.umessage.Profile.ProfileActivity
import com.oguncan.umessage.R
import com.oguncan.umessage.Search.SearchActivity
import com.oguncan.umessage.Share.ShareActivity

class BottomNavigationViewHelper {
    companion object{
        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx){

            bottomNavigationViewEx.enableAnimation(false)
            bottomNavigationViewEx.enableShiftingMode(true)
            bottomNavigationViewEx.enableItemShiftingMode(true)
            bottomNavigationViewEx.setTextVisibility(false)
        }
        fun setupNavigation(context: Context, bottomNavigationViewEx: BottomNavigationViewEx){
            bottomNavigationViewEx.onNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                    when(p0.itemId){
                        R.id.bottomMenuHome -> {
                            val intent = Intent(context, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.bottomMenuSearch -> {
                            val intent = Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.bottomMenuShare -> {
                            val intent = Intent(context, ShareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.bottomMenuLike -> {
                            val intent = Intent(context, HeartActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.bottomMenuProfile -> {
                            val intent = Intent(context, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                    }
                    return false
                }
            }
        }
    }
}