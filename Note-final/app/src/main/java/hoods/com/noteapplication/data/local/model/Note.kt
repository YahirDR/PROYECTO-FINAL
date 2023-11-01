package hoods.com.noteapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
//Declaramos nuestra entidad que se usara en nuestra base de datos
//Su objetivo es el de almacenar los datos de una nota
@Entity(tableName = "notes")
//Definimos nuestra tabla juntos a sus valores
data class Note(
    //Declaramos un id que servira  para trabajar con las notas, facilitando identificarlas
    @PrimaryKey(autoGenerate = true) //Declaramos que el id sea autoincremental
    val id: Long = 0,
    //Declaramos la seccion en la tabla para el titulo de la nota
    val title: String,
    //Declaramos la seccion en la tabla para contenido o body de nuesta nota
    val content: String,
    //Declaramos la seccion en la tabla para la fecha que se creo
    val createdDate: Date,
    //Declaramos la seccion en la tabla para detectar si una nota esta marcada como favorita
    val isBookMarked: Boolean = false,
)