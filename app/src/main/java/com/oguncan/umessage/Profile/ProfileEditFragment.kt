package com.oguncan.umessage.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.oguncan.umessage.R
import com.oguncan.umessage.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileEditFragment : Fragment() {
    lateinit var circleImageViewInFragment : CircleImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_profile_edit, container, false)
        circleImageViewInFragment= view.findViewById(R.id.imgRegisterPhoto)

        setupProfileImage(view)
        view.imgEditProfileClose.setOnClickListener {
            activity?.onBackPressed()
        }

        view.imgEditProfileDone.setOnClickListener {
            activity?.onBackPressed()
        }





        return view
    }

    fun setupProfileImage(view : View){
        val imageURL = "i.pinimg.com/originals/60/ac/92/60ac920f4efedd79f815c206e654275f.jpg"
        UniversalImageLoader.setImage(imageURL, circleImageViewInFragment, null, "https://")
    }



}
