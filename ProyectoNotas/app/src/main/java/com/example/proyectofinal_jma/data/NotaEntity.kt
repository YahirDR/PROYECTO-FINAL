package com.example.proyectofinal_jma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class NotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0 ,
    val titulo:String,
    val contenido:String,
    val fecha:String,
    val tipo:String=""
)
