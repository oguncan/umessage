package com.oguncan.umessage.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.oguncan.umessage.Home.HomeActivity
import com.oguncan.umessage.Model.Account
import com.oguncan.umessage.R
import com.oguncan.umessage.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener{
    lateinit var manager : FragmentManager
    lateinit var mRef : DatabaseReference
    lateinit var mAuth : FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupAuthListener()

        mRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        manager = supportFragmentManager

        init()

        manager.addOnBackStackChangedListener {
            FragmentManager.OnBackStackChangedListener {
                onBackStackChanged()
            }
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
            if(user!=null){
                var intent = Intent(this@RegisterActivity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            } else{

            }
        }
    }

    private fun init(){
        txtRegisterSignIn.setOnClickListener {
            var intent = Intent(this@RegisterActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        txtRegisterMail.setOnClickListener{
            btnRegisterNext.isEnabled = false
            btnRegisterNext.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.shadow))
            btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background)
            //
            viewRegisterTelephone.visibility = View.INVISIBLE
            viewRegisterMail.setBackgroundColor(resources.getColor(R.color.black))
            txtRegisterMail.setTextColor(resources.getColor(R.color.black))
            txtRegisterTelephone.setTextColor(resources.getColor(R.color.shadow))
            viewRegisterMail.visibility = View.VISIBLE
            editTextRegisterTelephoneOrMail.setText("")
            editTextRegisterTelephoneOrMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            editTextRegisterTelephoneOrMail.setHint("E-Posta")
        }

        txtRegisterTelephone.setOnClickListener{
            btnRegisterNext.isEnabled = false
            btnRegisterNext.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.shadow))
            btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background)
            //
            viewRegisterTelephone.visibility = View.VISIBLE
            viewRegisterTelephone.setBackgroundColor(resources.getColor(R.color.black))
            txtRegisterTelephone.setTextColor(resources.getColor(R.color.black))
            txtRegisterMail.setTextColor(resources.getColor(R.color.shadow))
            viewRegisterMail.visibility = View.INVISIBLE
            editTextRegisterTelephoneOrMail.setText("+90")
            editTextRegisterTelephoneOrMail.inputType = InputType.TYPE_CLASS_NUMBER
            editTextRegisterTelephoneOrMail.setHint("Telefon")
        }

        editTextRegisterTelephoneOrMail.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0!!.length >= 11){
                    btnRegisterNext.isEnabled = true
                    btnRegisterNext.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.white))
                    btnRegisterNext.setBackgroundResource(R.drawable.register_button_active)
                }
                else{
                    btnRegisterNext.isEnabled = false
                    btnRegisterNext.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.shadow))
                    btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background)
                }
            }
        })
        btnRegisterNext.setOnClickListener {
            if(editTextRegisterTelephoneOrMail.hint.toString().equals("Telefon")){


                if(isValidTelephone(editTextRegisterTelephoneOrMail.text.toString())){
                    var isThereTelephoneNumber = false
                    mRef.child("Accounts").addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.getValue()!=null){
                                for(accounts in p0.children){
                                    var account = accounts.getValue(Account::class.java)
                                    if(account!!.accountTelephoneNumber.equals(editTextRegisterTelephoneOrMail.text.toString())){
                                        Toast.makeText(this@RegisterActivity, "Telefon numarası kullanılıyor.", Toast.LENGTH_LONG).show()
                                        isThereTelephoneNumber = true
                                        break
                                    }
                                }
                                if(!isThereTelephoneNumber){
                                    registerActivityRoute.visibility = View.GONE
                                    registerActivityContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.registerActivityContainer, ConfirmTelephoneCodeFragment())
                                    transaction.addToBackStack("telephoneCodeFragment")
                                    transaction.commit()
                                    if(editTextRegisterTelephoneOrMail.text.contains("+90")){
                                        EventBus.getDefault().postSticky(EventBusDataEvents.SendTelephoneOrMail(editTextRegisterTelephoneOrMail.text.toString(), null, null, null, false))
                                    }
                                    else{
                                        EventBus.getDefault().postSticky(EventBusDataEvents.SendTelephoneOrMail("+9"+editTextRegisterTelephoneOrMail.text.toString(), null, null, null, false))
                                    }
                                }
                            }
                        }

                    })



                }
                else{
                    Toast.makeText(this, "Telefon numaranızı uygun şekilde giriniz.", Toast.LENGTH_LONG).show()
                }
            }
            else{
                if(isValidEmail(editTextRegisterTelephoneOrMail.text.toString())){
                    var isUseThisEmail = false
                    mRef.child("Accounts").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.getValue()!=null){
                                for(user in p0.children){
                                    var account = user.getValue(Account::class.java)
                                    if(account!!.accountMailAddress.equals(editTextRegisterTelephoneOrMail.text.toString())){
                                        Toast.makeText(this@RegisterActivity, "E-mail adresi kullanılıyor.", Toast.LENGTH_LONG).show()
                                        isUseThisEmail = true
                                        break
                                    }
                                }
                                if(isUseThisEmail == false){
                                    registerActivityRoute.visibility = View.GONE
                                    registerActivityContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.registerActivityContainer, EmailUsernamePasswordFragment())
                                    transaction.addToBackStack("emailCodeFragment")
                                    transaction.commit()
                                    EventBus.getDefault().postSticky(EventBusDataEvents.SendTelephoneOrMail(null, editTextRegisterTelephoneOrMail.text.toString(),  null, null, true))
                                }
                            }
                        }

                    })

                    }
                else{
                    Toast.makeText(this, "Mail adresinizi uygun şekilde giriniz.", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onBackStackChanged() {

        if(manager.backStackEntryCount==-1){
            registerActivityRoute.visibility = View.VISIBLE
        }
        manager.popBackStackImmediate()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(this.supportFragmentManager.getBackStackEntryCount()==0){
            registerActivityRoute.visibility = View.VISIBLE
        }
    }

    fun isValidTelephone(telephone : String) : Boolean {
        if(telephone == null || telephone.length < 11 || telephone.length > 16){
            return false
        }
        return android.util.Patterns.PHONE.matcher(telephone).matches()
    }

    fun isValidEmail(email : String) : Boolean{
        if(email == null ){
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }




}
