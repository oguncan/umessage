package com.oguncan.umessage.Share

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.oguncan.umessage.Model.Account

import com.oguncan.umessage.R
import com.oguncan.umessage.utils.EventBusDataEvents
import com.oguncan.umessage.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.net.URI

/**
 * A simple [Fragment] subclass.
 */
class ShareNextFragment : Fragment() {
    lateinit var image : Uri
    lateinit var mRef : DatabaseReference
    lateinit var mImage : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_share_next, container, false)
        mRef = FirebaseDatabase.getInstance().reference
        if(image != null){
            view.imgNewShareShareImage.setImageURI(image)
            Log.e("Merhaba", image.toString())
        }
        mRef.child("Accounts")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.getValue() != null){
                        var account = p0.getValue(Account::class.java)
                        UniversalImageLoader.setImage(account!!.accountDetails!!.detailsProfilePicture.toString(), view.imgNewShareAccountProfilePhoto, null, "")
                    }
                }

            })


        return view
    }

    @Subscribe(sticky = true)
    internal fun onEmailEvent(comingImage : EventBusDataEvents.SendShareImage){
        image = comingImage!!.image!!
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
