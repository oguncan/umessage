package com.oguncan.umessage.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.oguncan.umessage.Model.Account

import com.oguncan.umessage.R
import com.oguncan.umessage.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_email_username_password.*
import kotlinx.android.synthetic.main.fragment_email_username_password.view.*
import kotlinx.android.synthetic.main.fragment_email_username_password.view.btnNameAndPasswordNextButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class EmailUsernamePasswordFragment : Fragment() {
    var telNo = ""
    var verificiationID = ""
    var comingEmailValue = ""
    var comingTelephoneCode = ""
    var mailRegisterProcess = true
    lateinit var mAuth : FirebaseAuth
    lateinit var mRef : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_email_username_password, container, false)
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().getReference()
        if(mAuth.currentUser != null){
            mAuth.signOut()
        }
        view.editTextNameAndPasswordNameAndSurname.addTextChangedListener(watcher)
        view.editTextNameAndPasswordPassword.addTextChangedListener(watcher)
        view.editTextNameAndPasswordUsername.addTextChangedListener(watcher)

        view.txtNameAndPasswordSignIn.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        view.btnNameAndPasswordNextButton.setOnClickListener {
            var isThereUsername = false
            mRef.child("Accounts").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.getValue() != null){
                        for(usernames in p0.children){
                            var username = usernames.getValue(Account::class.java)
                            if(username!!.accountUsername.equals(view.editTextNameAndPasswordUsername.text.toString())
                                || username!!.accountUsername.equals(view.editTextNameAndPasswordUsername.text.toString().toLowerCase())){
                                Toast.makeText(activity!!, "Bu kullanıcı adı kullanılıyor!", Toast.LENGTH_LONG).show()
                                isThereUsername = true
                                break
                            }
                        }
                        if(!isThereUsername){
                            view.progressBarNameAndPassword.visibility = View.VISIBLE
                            //User want to with ur mail address
                            if(mailRegisterProcess){
                                var password = view.editTextNameAndPasswordPassword.text.toString()
                                mAuth.createUserWithEmailAndPassword(comingEmailValue, password)
                                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                                        override fun onComplete(p0: Task<AuthResult>) {
                                            if(p0.isSuccessful){
                                                //Toast.makeText(activity!!, comingEmailValue+" "+password , Toast.LENGTH_LONG).show()
                                                view.progressBarNameAndPassword.visibility = View.GONE
                                                //Toast.makeText(activity!!, "Kullanıcı kaydı gerçekleştirildi!", Toast.LENGTH_LONG).show()
                                                //Save information of user who sign in success
                                                var saveAccount = Account(comingEmailValue,
                                                    view.editTextNameAndPasswordNameAndSurname.text.toString(),
                                                    view.editTextNameAndPasswordPassword.text.toString(),
                                                    view.editTextNameAndPasswordUsername.text.toString().toLowerCase(),
                                                    "",
                                                    "",
                                                    mAuth.currentUser!!.uid.toString())
                                                mRef.child("Accounts").child(mAuth.currentUser!!.uid).setValue(saveAccount)
                                                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if(p0.isSuccessful){

                                                            }
                                                            else{
                                                            }
                                                        }
                                                    })
                                            }
                                            else{
                                                mAuth.currentUser!!.delete()
                                                    .addOnCompleteListener { object: OnCompleteListener<Void> {
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if(p0.isSuccessful){
                                                                Toast.makeText(activity!!, "Kullanıcı Kayıt Edilemedi!", Toast.LENGTH_LONG).show()
                                                            }
                                                        }
                                                    }}
                                            }
                                        }
                                    })
                            }
                            //User want to with ur telephone number
                            else{
                                var password = view.editTextNameAndPasswordPassword.text.toString()
                                var fakeMailAddress = telNo + "@umessage.com"
                                mAuth.createUserWithEmailAndPassword(fakeMailAddress, password)
                                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                                        override fun onComplete(p0: Task<AuthResult>) {
                                            if(p0.isSuccessful){
                                                view.progressBarNameAndPassword.visibility = View.GONE
                                                //Toast.makeText(activity!!, "Oturum Açıldı!", Toast.LENGTH_LONG).show()
                                                //Save information of user who sign in success
                                                var saveAccount = Account("",
                                                    view.editTextNameAndPasswordNameAndSurname.text.toString(),
                                                    view.editTextNameAndPasswordPassword.text.toString(),
                                                    view.editTextNameAndPasswordUsername.text.toString().toLowerCase(),
                                                    telNo,
                                                    fakeMailAddress,
                                                    mAuth.currentUser!!.uid.toString())
                                                mRef.child("Accounts").child(mAuth.currentUser!!.uid).setValue(saveAccount)
                                                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if(p0.isSuccessful){

                                                            }
                                                            else{
                                                                mAuth.currentUser!!.delete().addOnCompleteListener { object: OnCompleteListener<Void> {
                                                                    override fun onComplete(p0: Task<Void>) {
                                                                        if(p0.isSuccessful){
                                                                            Toast.makeText(activity!!, "Kullanıcı Kayıt Edilemedi!", Toast.LENGTH_LONG).show()
                                                                        }
                                                                    }

                                                                }}
                                                            }
                                                        }
                                                    })
                                            }
                                            else{
                                                Toast.makeText(activity!!, "Oturum Açılamadı!", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    })
                            }
                        }

                    }
                }

            })
        }
        return view
    }
    var watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(p0!!.length >= 5){
                if(editTextNameAndPasswordNameAndSurname.text.length > 5 && editTextNameAndPasswordUsername.text.length > 5 && editTextNameAndPasswordPassword.text.length > 5){
                    btnNameAndPasswordNextButton.isEnabled = true
                    btnNameAndPasswordNextButton.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    btnNameAndPasswordNextButton.setBackgroundResource(R.drawable.register_button_active)
                }
                else{
                    btnNameAndPasswordNextButton.isEnabled = false
                    btnNameAndPasswordNextButton.setTextColor(ContextCompat.getColor(activity!!, R.color.shadow))
                    btnNameAndPasswordNextButton.setBackgroundResource(R.drawable.edit_profile_button_background)
                }
            }
            else{
                btnNameAndPasswordNextButton.isEnabled = false
                btnNameAndPasswordNextButton.setTextColor(ContextCompat.getColor(activity!!, R.color.shadow))
                btnNameAndPasswordNextButton.setBackgroundResource(R.drawable.edit_profile_button_background)
            }
        }

    }
    @Subscribe(sticky = true)
    internal fun onEmailEvent(generallyInfo : EventBusDataEvents.SendTelephoneOrMail){
        if(generallyInfo.isMail!!){
            mailRegisterProcess = true
            comingEmailValue = generallyInfo.email!!
        }
        else{
            mailRegisterProcess = false
            telNo = generallyInfo.telNo!!
            verificiationID = generallyInfo.verificationID!!
            comingTelephoneCode = generallyInfo.code!!
        }


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }



}
