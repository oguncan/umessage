package com.oguncan.umessage.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.nostra13.universalimageloader.core.ImageLoader
import com.oguncan.umessage.Login.LoginActivity
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import com.oguncan.umessage.utils.HomePagerAdapter
import com.oguncan.umessage.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"
    lateinit var mAuth : FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        initImageLoader()
        setupNavigationView()
        setupHomeViewPager()
    }

    fun setupHomeViewPager() {
        var homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.addFragment(CameraFragment())
        homePagerAdapter.addFragment(HomeFragment())
        homePagerAdapter.addFragment(MessageFragment())
        //viewPager bind adapter
        homeViewPager.adapter = homePagerAdapter
        //set home page of viewPager
        homeViewPager.setCurrentItem(1, true)
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

    fun setupAuthListener(){
        mAuthListener = FirebaseAuth.AuthStateListener {
            var user = FirebaseAuth.getInstance().currentUser
            //Toast.makeText(this@LoginActivity, user.toString(), Toast.LENGTH_LONG).show()
            if(user==null){
                var intent = Intent(this@HomeActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else{

            }
        }
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(homeBottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,homeBottomNavigationView)
        var menu = homeBottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    fun initImageLoader(){
        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)

    }
}
