package com.oguncan.umessage.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.oguncan.umessage.R
import kotlinx.android.synthetic.main.share_gridview_photo.view.*
import java.lang.Exception

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
        lateinit var timeText : TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = inflater.inflate(R.layout.share_gridview_photo, parent, false)
        viewHolder = ViewHolder()
        viewHolder.imageView = view.shareGridViewPhotos
        viewHolder.progressBar = view.shareGridViewProgressBar
        viewHolder.timeText = view.shareGridViewTime
        view.setTag(viewHolder)
        var imageOrVideoPath = filesInFolder.get(position)
        var fileType = imageOrVideoPath.substring(imageOrVideoPath.lastIndexOf("."))
        if(fileType.equals(".mp4")){
            viewHolder.timeText.visibility = View.VISIBLE
            var retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, Uri.parse("file://"+imageOrVideoPath))
            var timeOfVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var timeOfVideoLong = timeOfVideo.toLong()
            viewHolder.timeText.text = convertDuration(timeOfVideoLong).toString()
        }
        else{
            viewHolder.timeText.visibility = View.GONE
        }
        var imgURL = filesInFolder.get(position)
        UniversalImageLoader.setImage("file:/"+imgURL, viewHolder.imageView, viewHolder.progressBar, "file:/")
        return view
    }

    fun convertDuration(duration : Long) : String{
        var out : String? = null
        var hours : Long = 0
        try {
            hours = (duration / 3600000);
        }catch (e: Exception){
            e.printStackTrace();
            return out!!;
        }
        var remaining_minutes = (duration - (hours * 3600000)) / 60000;
        var minutes = remaining_minutes.toString()

        if (minutes.equals(0)) {
            minutes = "00"
        }
        var remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        var seconds = remaining_seconds.toString();
        if (seconds.length < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours.toString() + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;
    }

}