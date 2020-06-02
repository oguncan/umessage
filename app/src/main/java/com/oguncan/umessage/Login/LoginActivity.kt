package com.oguncan.umessage.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.oguncan.umessage.Home.HomeActivity
import com.oguncan.umessage.Model.Account
import com.oguncan.umessage.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var mRef : DatabaseReference
    lateinit var mAuth : FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupAuthListener()
        mRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        init()
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

    fun setupAuthListener() {
        mAuthListener = FirebaseAuth.AuthStateListener {
            var user = FirebaseAuth.getInstance().currentUser
            //Toast.makeText(this@LoginActivity, user.toString(), Toast.LENGTH_LONG).show()
            if(user!=null){
                var intent = Intent(this@LoginActivity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else{

            }
        }
    }

    fun init(){
        progressBarLogin.visibility = View.GONE

//        if(mAuth.currentUser != null){
//            mAuth.signOut()
//        }
        btnLoginSignInButton.setOnClickListener {
            controlLoginUser(editTextLoginName.text.toString(), editTextLoginPassword.text.toString())
        }
        editTextLoginName.addTextChangedListener(watcher)
        editTextLoginPassword.addTextChangedListener(watcher)
        txtLoginSignUpText.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

    }
    fun controlLoginUser(userName : String, password: String){
        var isFoundUser = false
        mRef.child("Accounts").orderByChild("accountMailAddress").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(users in p0.children){
                    var user = users.getValue(Account::class.java)
                    if(user!!.accountMailAddress.toString().equals(userName)){
                        openAccountMember(user, password, false)
                        isFoundUser = true
                        break

                    }
                    else if(user!!.accountUsername.toString().equals(userName)){
                        openAccountMember(user, password, false)
                        isFoundUser = true
                        break

                    }
                    else if(user!!.accountTelephoneNumber.toString().equals(userName)){
                        openAccountMember(user, password, true)
                        isFoundUser = true
                        break
                    }

                }
                if(!isFoundUser) {
                    Toast.makeText(this@LoginActivity, "Kullanıcı Bulunamadı!", Toast.LENGTH_LONG).show()
                }

            }

        })
    }
    fun openAccountMember(user : Account, password : String, isSignWithTelephone : Boolean){
        //Toast.makeText(this@LoginActivity, user.accountMailAddress+user.accountPassword, Toast.LENGTH_LONG).show()
        var mailForSignIn = ""
        if(isSignWithTelephone){
            mailForSignIn = user.accountTelephoneNumber.toString()
        }
        else{
            mailForSignIn = user.accountMailAddress.toString()
        }
//        Toast.makeText(this@LoginActivity, mailForSignIn+""+ password, Toast.LENGTH_LONG).show()
        mAuth.signInWithEmailAndPassword(mailForSignIn, password).addOnCompleteListener(this) {
            task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Hoşgeldin! ${user.accountNameAndSurname}", Toast.LENGTH_LONG).show()
//                var intent = Intent(this@LoginActivity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Eksik veya Yanlış veri girdiniz!", Toast.LENGTH_LONG).show()
            }


        }

    }
    var watcher = object : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if(editTextLoginName.text.toString().length > 5 && editTextLoginPassword.text.toString().length > 5){
                btnLoginSignInButton.isEnabled = true
                btnLoginSignInButton.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
                btnLoginSignInButton.setBackgroundResource(R.drawable.register_button_active)
            }
            else{
                btnLoginSignInButton.isEnabled = false
                btnLoginSignInButton.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.shadow))
                btnLoginSignInButton.setBackgroundResource(R.drawable.edit_profile_button_background)
            }
        }

        }

    }

