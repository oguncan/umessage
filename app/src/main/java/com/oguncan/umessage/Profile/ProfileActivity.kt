package com.oguncan.umessage.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.oguncan.umessage.Home.HomeActivity
import com.oguncan.umessage.Login.LoginActivity
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import com.oguncan.umessage.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"
    lateinit var mAuth : FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    //lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        setupToolBar()
        setupNavigationView()
        fragmentNavigations()
        setupProfileImage()
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(profileBottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,profileBottomNavigationView)
        var menu = profileBottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    override fun onStart() {
        super.onStart()
        //FirebaseAuth.getInstance().addAuthStateListener(mAuthListener)
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
                var intent = Intent(this@ProfileActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else{

            }
        }
    }

    fun setupToolBar() {
        imgProfileToolbarSettings.setOnClickListener{
            var intent = Intent(this@ProfileActivity, ProfileSettings::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    private fun fragmentNavigations(){
        txtProfileEditProfile.setOnClickListener{
            profileActivityRoute.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileActivityContainer, ProfileEditFragment())
            transaction.addToBackStack("editProfileFragment")
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        profileActivityRoute.visibility = View.VISIBLE
        super.onBackPressed()
    }

    fun setupProfileImage(){
        var imageURL = "https://i.pinimg.com/originals/60/ac/92/60ac920f4efedd79f815c206e654275f.jpg"
        UniversalImageLoader.setImage(imageURL, imgRegisterPhoto, progressBarProfile, null)
    }



}
