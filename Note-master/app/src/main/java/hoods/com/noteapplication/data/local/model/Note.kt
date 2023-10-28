package hoods.com.noteapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
//CREAMOS NUESTRA BASE DE DATOS NOTES
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Long =0,
    val title:String,
    val content:String,
    val createdDate: Date,
    val isBookMarker:Boolean = false

)