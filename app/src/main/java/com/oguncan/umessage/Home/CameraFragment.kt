package com.oguncan.umessage.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oguncan.umessage.R

class CameraFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_camera, container, false)
        Log.e("Merhaba","Merhaba")
        Log.e("Merhaba2","Merhaba2")
        Log.e("Merhaba3","Merhaba3")
        Log.e("Merhaba4","Merhaba4")
        Log.e("Merhaba44","Merhaba44")
        return view
    }
}