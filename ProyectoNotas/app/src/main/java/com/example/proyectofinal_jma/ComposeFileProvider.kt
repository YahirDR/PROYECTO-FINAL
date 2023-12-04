package com.example.proyectofinal_jma

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
){
    companion object {
        fun getImageUri(context: Context): Uri {
            // 1
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            // 2
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            // 3
            val authority = context.packageName + ".fileprovider"
            // 4
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
        fun getVideoUri(context: Context): Uri {
            // 1
            val directory = File(context.cacheDir, "videos")
            directory.mkdirs()
            // 2
            val file = File.createTempFile(
                "captured_video_",
                ".mp4",
                directory
            )
            // 3
            val authority = context.packageName + ".fileprovider"
            // 4
            return getUriForFile(
                context,
                authority,
                file,
            )
        }

    }
}