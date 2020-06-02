package com.oguncan.umessage.Login

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import com.oguncan.umessage.R
import com.oguncan.umessage.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_confirm_telephone_code.*
import kotlinx.android.synthetic.main.fragment_confirm_telephone_code.view.*
import kotlinx.android.synthetic.main.fragment_confirm_telephone_code.view.editTextConfirmTelephoneActivationCode
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit


class ConfirmTelephoneCodeFragment : Fragment() {
    var comingTelephoneNoValue : String = ""
    lateinit var mCallbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationID = ""
    var telephoneCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_confirm_telephone_code, container,false)
        view.txtConfirmTelephone.text = comingTelephoneNoValue
        setupCallBack()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(comingTelephoneNoValue, 60, TimeUnit.SECONDS, this.activity!!, mCallbacks)
//        view.txtConfirmSignInButton.setOnClickListener {
//            var transaction = activity!!.supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.registerActivityContainer, EmailUsernamePasswordFragment())
//            transaction.addToBackStack("confirmTelephoneFragment")
//            transaction.commit()
//        }
        view.btnConfirmTelephoneNextButton.setOnClickListener {
            if(telephoneCode.equals(view.editTextConfirmTelephoneActivationCode.text.toString())){
                EventBus.getDefault().postSticky(EventBusDataEvents.SendTelephoneOrMail(comingTelephoneNoValue, null, verificationID, telephoneCode, false))
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.registerActivityContainer, EmailUsernamePasswordFragment())
                transaction.addToBackStack("confirmCodeFragment")
                transaction.commit()
            }
            else {

            }
        }

        view.txtConfirmRetryTelephoneSendCode.setOnClickListener {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(comingTelephoneNoValue, 60, TimeUnit.SECONDS, this.activity!!, mCallbacks)
        }

        return view
    }



    @Subscribe(sticky = true)
    internal fun onTelephoneNoEvent(telephoneNo : EventBusDataEvents.SendTelephoneOrMail){
        comingTelephoneNoValue = telephoneNo.telNo!!


    }

    fun setupCallBack(){
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if(!credential.smsCode.isNullOrEmpty())
                    telephoneCode = credential.smsCode!!
            }
            override fun onVerificationFailed(e: FirebaseException) {
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                verificationID = verificationId
                if(verificationID != null){
                    progressBarConfirmTelephoneCode.visibility = View.GONE
                }
            }
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
