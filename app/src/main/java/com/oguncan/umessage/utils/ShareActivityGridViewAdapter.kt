package com.oguncan.umessage.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.oguncan.umessage.R
import kotlinx.android.synthetic.main.share_gridview_photo.view.*

class ShareActivityGridViewAdapter(context: Context, resource: Int, var filesInFolder: ArrayList<out String>) :
    ArrayAdapter<String>(context, resource, filesInFolder) {
    var inflater : LayoutInflater
    lateinit var viewHolder : ViewHolder
    init{
        inflater = LayoutInflater.from(context)
    }

    inner class ViewHolder(){
        lateinit var imageView : ImageViewController
        lateinit var progressBar : ProgressBar
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = inflater.inflate(R.layout.share_gridview_photo, parent, false)
        viewHolder = ViewHolder()
        viewHolder.imageView = view.shareGridViewPhotos
        viewHolder.progressBar = view.shareGridViewProgressBar
        view.setTag(viewHolder)
        var imgURL = filesInFolder.get(position)
        UniversalImageLoader.setImage("file:/"+imgURL, viewHolder.imageView, viewHolder.progressBar, "file:/")
        return view
    }
}