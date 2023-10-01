package com.juanyahirdr.proyectofinal.data.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//DATACLASS ES USADO POR KOTLIN PARA GUARDAR DATOS
@Entity(tableName= "note")  //specificar el nombre de la tabla en la que se almacenarán los objetos de esta entidad en la base de datos.
data class NoteEntity( //Se esta creando una entidad para las notas
    @PrimaryKey val uid: Int,   //ID PARA SABER QUE NOTA ES
    //columnas:
    @ColumnInfo(name = "Title") val Title: String?, //Titulo DE LA NOTA
    @ColumnInfo(name = "Body") val Body: String?    //Cuerdo DE LA NOTA
)
