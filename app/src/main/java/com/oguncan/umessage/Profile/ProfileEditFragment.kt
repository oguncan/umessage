package com.oguncan.umessage.Profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.oguncan.umessage.Model.Account

import com.oguncan.umessage.R
import com.oguncan.umessage.utils.EventBusDataEvents
import com.oguncan.umessage.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.imgProfilePhoto
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ProfileEditFragment : Fragment() {
    lateinit var circleImageViewInFragment : CircleImageView
    lateinit var accountInfo : Account
    lateinit var mDatabase : DatabaseReference
    lateinit var mStorageRef : StorageReference
    val CHANGE_PHOTO = 100
    var profilePhotoURI : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater!!.inflate(R.layout.fragment_profile_edit, container, false)
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference
        setupAccountInfos(view)

        view.txtEditProfileChangePhoto.setOnClickListener {
            var intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, CHANGE_PHOTO)
        }

        view.imgEditProfileClose.setOnClickListener {
            activity?.onBackPressed()
        }

        view.imgEditProfileDone.setOnClickListener {
            if(profilePhotoURI != null) {
                var dialogUploading = ProfilePhotoLoadingFragment()
                dialogUploading.show(activity!!.supportFragmentManager, "uploadingPhotoFragment")
                dialogUploading.isCancelable = false
                var uploadTask = mStorageRef.child("Accounts")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                    .child(profilePhotoURI!!.lastPathSegment.toString())
                    .putFile(profilePhotoURI!!).addOnSuccessListener(
                        object : OnSuccessListener<UploadTask.TaskSnapshot> {
                            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                                p0!!.storage
                                    .downloadUrl
                                    .addOnSuccessListener(object  : OnSuccessListener<Uri>{
                                        override fun onSuccess(p0: Uri?) {
                                            Toast.makeText(activity!!, "Resim Yüklendi.", Toast.LENGTH_LONG)
                                            mDatabase.child("Accounts").child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                                                .child("accountDetails").child("detailsProfilePicture")
                                                .setValue(p0.toString())
                                            dialogUploading.dismiss()
                                            updateAccountUserName(view, true)
                                        }
                                    }).addOnFailureListener(object : OnFailureListener{
                                        override fun onFailure(p0: Exception) {
                                            updateAccountUserName(view, false)
                                        }
                                    })
                            }
                        }
                    )
            }
            else{
                updateAccountUserName(view, null)
            }
        }
        return view
    }
    fun updateAccountUserName(view : View, isChangedProfilePhoto : Boolean?){
        if(!(view.edtTextProfileEditUsername.text.toString() == accountInfo!!.accountUsername!!.toString())) {
                mDatabase.child("Accounts").orderByChild("accountUsername")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            var isUsernameUse = false
                            for(accounts in p0.children){
                                var account = accounts.getValue(Account::class.java)!!.accountUsername.toString()
                                if(account!!.equals(view.edtTextProfileEditUsername.text.toString())){
                                    isUsernameUse = true
                                    updateProfileInfo(view, isChangedProfilePhoto, false)
                                    break
                                }
                            }
                            if(!isUsernameUse){
                                mDatabase.child("Accounts").child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                                    .child("accountUsername").setValue(view.edtTextProfileEditUsername.text.toString())
                                updateProfileInfo(view, isChangedProfilePhoto, true)
                            }
                        }
                    })
            }
        else{
            updateProfileInfo(view, isChangedProfilePhoto, null)
        }
    }
    fun updateProfileInfo(view : View, isChangedProfilePhoto : Boolean?, isChangedProfileUsername : Boolean?){
        var isUpdatedProfile : Boolean? = null
        if(!(view.edtTextEditProfileName.text.toString() == accountInfo!!.accountNameAndSurname!!.toString())){
            mDatabase.child("Accounts").child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                .child("accountNameAndSurname").setValue(view.edtTextEditProfileName.text.toString())
            isUpdatedProfile = true
        }
        if(!(view.edtTextProfileEditWebsite.text.toString() == accountInfo!!.accountDetails!!.detailsWebsite.toString())) {
            mDatabase.child("Accounts")
                .child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                .child("accountDetails").child("detailsWebsite")
                .setValue(view.edtTextProfileEditWebsite.text.toString())
            isUpdatedProfile = true
        }

        if(!(view.edtTextProfileEditBio.text.toString() == accountInfo!!.accountDetails!!.detailsComment.toString())) {
            mDatabase.child("Accounts")
                .child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                .child("accountDetails").child("detailsComment")
                .setValue(view.edtTextProfileEditBio.text.toString())
            isUpdatedProfile = true
        }
        if(isChangedProfilePhoto==null && isChangedProfileUsername==null && isUpdatedProfile==null){
            Toast.makeText(activity!!, "Değişiklik yapılmadı.", Toast.LENGTH_LONG).show()
        }
        else if(isChangedProfileUsername == false && (isUpdatedProfile==true || isChangedProfilePhoto==true )){
            Toast.makeText(activity!!, "Bilgiler güncellendi fakat kullanıcı adı kullanılıyor.", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(activity!!, "Kullanıcı bilgileri güncellendi.", Toast.LENGTH_LONG).show()
            activity!!.onBackPressed()
        }




    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CHANGE_PHOTO && resultCode== AppCompatActivity.RESULT_OK && data!!.data!=null){
            profilePhotoURI = data!!.data
            view!!.imgProfilePhoto.setImageURI(profilePhotoURI)
        }

    }

    fun setupAccountInfos(view:View?){
        view!!.edtTextEditProfileName.setText(accountInfo!!.accountNameAndSurname.toString())
        view!!.edtTextProfileEditUsername.setText(accountInfo!!.accountUsername.toString())
        val imageURL = accountInfo!!.accountDetails!!.detailsProfilePicture.toString()
        UniversalImageLoader.setImage(imageURL, view.imgProfilePhoto, null, "")
        if(!accountInfo!!.accountDetails!!.detailsComment.toString().isNullOrEmpty() &&
            !(accountInfo!!.accountDetails!!.detailsComment.toString() == "0")){
            view!!.edtTextProfileEditBio
                .setText(accountInfo!!.accountDetails!!.detailsComment.toString())
        }
        else{
            view!!.edtTextProfileEditBio.setText("")
        }
        if(!accountInfo!!.accountDetails!!.detailsWebsite.toString().isNullOrEmpty() &&
            !(accountInfo!!.accountDetails!!.detailsWebsite.toString() == "0")    ){
            view!!.edtTextProfileEditWebsite.setText(accountInfo!!.accountDetails!!.detailsWebsite.toString())
        }
        else{
            view!!.edtTextProfileEditWebsite.setText("")
        }
    }

    @Subscribe(sticky = true)
    internal fun onEmailEvent(account : EventBusDataEvents.SendAccountInfo){
        accountInfo = account!!.account!!
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
