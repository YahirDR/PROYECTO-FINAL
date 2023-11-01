package hoods.com.noteapplication.domain.repository

import hoods.com.noteapplication.data.local.model.Note
import kotlinx.coroutines.flow.Flow
//El REPOSITORY se usara para  crear un conjunto
//De métodos que proporcionan operaciones relacionadas con las notas
interface Repository {
    // Este método devuelve un flujo Flow de listas de objetos Note que representan todas las notas disponibles en la aplicación.
    fun getAllNotes(): Flow<List<Note>>

    // Este método devuelve un flujo Flow de un solo objeto Note que representa una nota específica identificada por su 'id'.
    fun getNoteById(id: Long): Flow<Note>

    // Este método se utiliza para insertar una nueva nota en la aplicación de manera asíncrona.
    suspend fun insert(note: Note)

    // Este método se utiliza para actualizar una nota existente en la aplicación de manera asíncrona.
    suspend fun update(note: Note)

    // Este método se utiliza para eliminar una nota identificada por su 'id' de manera asíncrona.
    suspend fun delete(id: Long)

    // Este método devuelve un flujo Flow de listas de objetos Note que representan todas las notas marcadas como favoritas en la aplicación.
    fun getBookMarkedNotes(): Flow<List<Note>>
}