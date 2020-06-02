package com.oguncan.umessage.Profile

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.oguncan.umessage.Login.LoginActivity
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettings : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG ="ProfileActivity"
    lateinit var mAuth : FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        setupNavigationView()
        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        setupToolBar()
        fragmentNavigations()
    }

    private fun setupToolBar() {
        imgProfileSettingsBackButton.setOnClickListener {
            onBackPressed()
        }
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
                var intent = Intent(this@ProfileSettings, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else{

            }
        }
    }

    private fun fragmentNavigations(){
        txtProfileSettingsEditProfile.setOnClickListener{
            profileSettingsRoute.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, ProfileEditFragment())
            transaction.addToBackStack("editProfileFragment")
            transaction.commit()
        }
        txtProfileSettingsSignOut.setOnClickListener{

            var dialogFragment = ProfileSignOutFragment()
            dialogFragment.show(supportFragmentManager, "signOutDialog")


        }
    }

    override fun onBackPressed() {
        profileSettingsRoute.visibility = View.VISIBLE
        super.onBackPressed()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(profileSettingsBottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, profileSettingsBottomNavigationView)
        var menu = profileSettingsBottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)

    }
}
