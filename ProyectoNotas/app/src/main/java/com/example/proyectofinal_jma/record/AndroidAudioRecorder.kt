package com.example.proyectofinal_jma.record

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AndroidAudioRecorder(
    private val context: Context
) {
    private var recorder: MediaRecorder? = null
    private var audioFile: File? = null // Variable para guardar el archivo actual

    private fun createRecorder(): MediaRecorder{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        }else MediaRecorder()
    }

    fun start(outputFile: File,fileNumber:Int) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            // Generar un nombre de archivo con el número de archivo
            val fileName = "audio${fileNumber}.mp4"
            // Crear el archivo en la carpeta definida en el file provider
            audioFile = File(context.cacheDir, fileName)
            setOutputFile(audioFile?.absolutePath)
            prepare()
            start()
            recorder = this
        }
    }

    fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }

    // Método para obtener la URI de contenido del archivo actual
    fun getContentUri(): Uri? {
        return audioFile?.let {
           it.path.toUri()
        }
    }
}