package com.oguncan.umessage.Share

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.oguncan.umessage.R
import com.oguncan.umessage.utils.FileOperations
import com.oguncan.umessage.utils.ShareActivityGridViewAdapter
import com.oguncan.umessage.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.spinnerFileNames

/**
 * A simple [Fragment] subclass.
 */
class ShareGalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_share_gallery, container, false)

        var filesPaths = ArrayList<String>()

        var root = Environment.getExternalStorageDirectory().path
        var cameraPhotos = root+"/DCIM/Camera"
        var screenshotsPhotos = root+"/Pictures/Screenshots"
        var whatsAppPhotos = root+"/WhatsApp/Media/WhatsApp Images"

        filesPaths.add(cameraPhotos)
        filesPaths.add(screenshotsPhotos)
        filesPaths.add(whatsAppPhotos)

        var filesNames = ArrayList<String>()
        filesNames.add("Kamera")
        filesNames.add("Ekran Görüntüleri")
        filesNames.add("WhatsApp Resimleri")

        var spinnerArrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, filesNames)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        view.spinnerFileNames.adapter = spinnerArrayAdapter



        view.spinnerFileNames.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                setupGridView(FileOperations.getFilesInFolder(filesPaths.get(p2)))
            }

        }


        return view
    }


    fun setupGridView(allFilesInFolder : ArrayList<String>){
        var gridViewAdapter = ShareActivityGridViewAdapter(activity!!, R.layout.share_gridview_photo, allFilesInFolder)
        gridViewShareGallery.adapter = gridViewAdapter
        gridViewShareGallery.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                UniversalImageLoader.setImage("file:/"+allFilesInFolder.get(p2), imgShareGalleryImage, null, "")
            }

        })


    }

}
