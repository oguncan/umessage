package com.oguncan.umessage.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.oguncan.umessage.Login.LoginActivity
import com.oguncan.umessage.Model.Account
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.BottomNavigationViewHelper
import com.oguncan.umessage.utils.EventBusDataEvents
import com.oguncan.umessage.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"
    lateinit var mAuth : FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    lateinit var mRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        setupToolBar()
        setupNavigationView()
        getAccountInfos()
        fragmentNavigations()
    }
    fun getAccountInfos(){
        //var accountInfo : Account? = null
        mRef.child("Accounts").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).addValueEventListener(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.getValue()!=null){
                        var accountInfo = p0.getValue(Account::class.java)

                        EventBus.getDefault().postSticky(EventBusDataEvents.SendAccountInfo(accountInfo!!))

                        txtProfileToolbarUsername.text = accountInfo!!.accountUsername.toString()
                        txtProfileRealName.text = accountInfo!!.accountNameAndSurname.toString()
                        txtProfileShareCount.text = accountInfo!!.accountDetails!!.detailsPosts.toString()
                        txtProfileFollower.text = accountInfo!!.accountDetails!!.detailsFollower.toString()
                        txtProfileFollow.text = accountInfo!!.accountDetails!!.detailsFollow.toString()
                        var imgURL: String = accountInfo!!.accountDetails!!.detailsProfilePicture.toString()
                        UniversalImageLoader.setImage(imgURL, imgProfilePhoto, progressBarProfile, "")
                        if(!accountInfo!!.accountDetails!!.detailsComment.toString().isNullOrEmpty() &&
                            !(accountInfo!!.accountDetails!!.detailsComment.toString() == "0")){
                            txtProfilePersonInfos.text = accountInfo!!.accountDetails!!.detailsComment.toString()
                        }
                        if(!accountInfo!!.accountDetails!!.detailsWebsite.toString().isNullOrEmpty() &&
                            !(accountInfo!!.accountDetails!!.detailsWebsite.toString() == "0")    ){
                            txtProfileWebsite.text = accountInfo!!.accountDetails!!.detailsWebsite.toString()
                        }

                    }
                }
            }
        )


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





}
