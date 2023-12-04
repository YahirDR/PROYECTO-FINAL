package com.example.proyectofinal_jma.playback

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
) {
    private var player: MediaPlayer? = null

    fun playFile(uri: Uri) {
        MediaPlayer.create(context, uri).apply {
            player = this
            start()
        }
    }

    fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}