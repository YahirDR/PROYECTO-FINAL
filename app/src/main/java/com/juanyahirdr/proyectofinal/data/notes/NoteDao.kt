package com.juanyahirdr.proyectofinal.data.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.juanyahirdr.proyectofinal.utils.Constants

@Dao
interface NoteDao {
    @Query("SELECT * FROM ${Constants.note_database_table_name}")
    fun getAll(): List<NoteEntity>

    @Insert //SIRVE PARA INSERTAR LA NOTA A LA BASE DE DATOS
    fun insertAll(noteEntity: NoteEntity)

    @Delete //SIRVE PARA BORRAR LA NOTA EN LA BASE DE DATOS
    fun delete(noteEntity: NoteEntity)
}