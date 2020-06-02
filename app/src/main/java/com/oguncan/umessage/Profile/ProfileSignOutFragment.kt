package com.oguncan.umessage.Profile

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth

import com.oguncan.umessage.R

class ProfileSignOutFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialog = AlertDialog.Builder(this!!.activity!!)
            .setTitle("Çıkış Yap")
            .setMessage("Emin misiniz?")
            .setPositiveButton("Onayla") { p0, p1 ->
                FirebaseAuth.getInstance().signOut()
                this.activity!!.finish()

//                activity!!.finish()
            }
            .setNegativeButton("İptal"
            ) { p0, p1 -> dismiss() }.create()


        return alertDialog
    }

}
