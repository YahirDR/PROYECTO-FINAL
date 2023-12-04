package com.example.proyectofinal_jma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imagenes")
data class ImageNotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0 ,
    val idNota:Int =0 ,
    val uriImagen:String=""
)

@Entity(tableName = "videos")
data class VideoNotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0 ,
    val idNota:Int =0 ,
    val uriVideo:String=""
)

@Entity(tableName = "audios")
data class AudioNotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0 ,
    val idNota:Int =0 ,
    val uriAudio:String=""
)