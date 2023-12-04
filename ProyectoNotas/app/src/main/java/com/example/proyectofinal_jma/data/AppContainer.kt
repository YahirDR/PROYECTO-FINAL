package com.example.proyectofinal_jma.data

import android.content.Context

//definir los repositorys que utilizaran los viewmodels
interface AppContainer {
    val notesRepository: NotesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val notesRepository: NotesRepository by lazy {
        OfflineNotesRepository(NotesDatabase.getDatabase(context).notaDAO())
    }
}