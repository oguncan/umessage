package com.oguncan.umessage.utils

import android.util.Log
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class FileOperations {
    companion object{
        fun getFilesInFolder(folderName : String) : ArrayList<String>{
            var allFiles = ArrayList<String>()

            var file = File(folderName)

            var allFilesInFolder = file.listFiles()
            //control parameter
            if(allFilesInFolder != null){
                if(allFilesInFolder.size > 1){
                    Arrays.sort(allFilesInFolder, object : Comparator<File>{
                        override fun compare(p0: File?, p1: File?): Int {
                            if(p0!!.lastModified() > p1!!.lastModified()){
                                return -1
                            }
                            else{
                                return 1
                            }
                        }

                    })
                }
                for(i in 0..allFilesInFolder.size-1){
                    Log.e("Deneme", allFilesInFolder.size.toString())
                    if(allFilesInFolder[i].isFile){
                        var readingFilePath = allFilesInFolder[i].absolutePath
                        var fileType = readingFilePath.substring(readingFilePath.lastIndexOf("."))
                        if(fileType.equals(".png") ||
                            fileType.equals(".jpeg") ||
                            fileType.equals(".jpg") ||
                            fileType.equals(".mp4") ||
                            fileType.equals(".avi")){
                            allFiles.add(readingFilePath)
                        }
                    }
                }

            }


            return allFiles

        }
    }
}