package com.juanyahirdr.proyectofinal.data.notes

import androidx.room.Database
import androidx.room.RoomDatabase

//NUESTA UNICA ENTIDAD ES NoteEntity
@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun NoteDao(): NoteDao
}