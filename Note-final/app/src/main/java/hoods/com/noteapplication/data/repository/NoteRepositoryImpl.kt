package hoods.com.noteapplication.data.repository

import hoods.com.noteapplication.data.local.NoteDao
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
// La clase `NoteRepositoryImpl` se implementa la interfaz `Repository`
// y se encarga de proporcionar métodos para interactuar con los datos de las notas.

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
) : Repository {
    // Esta función sobrescribe la función definida en la interfaz `Repository` para obtener todas las notas.
    override  fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }
    // Esta función sobrescribe la función definida en la interfaz `Repository` para obtener una nota por su ID.
    override fun getNoteById(id: Long): Flow<Note> {
        return noteDao.getNoteById(id)
    }
    // Esta función sobrescribe la función definida en la interfaz `Repository` para insertar una nota en la fuente de datos.
    override suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }
    // Esta función sobrescribe la función definida en la interfaz `Repository` para actualizar una nota en la fuente de datos.
    override suspend fun update(note: Note) {
        noteDao.update(note)
    }
    // Esta función sobrescribe la función definida en la interfaz `Repository` para eliminar una nota de la fuente de datos.
    override suspend fun delete(id: Long) {
        noteDao.delete(id)
    }
    // Esta función sobrescribe la función definida en la interfaz `Repository` para obtener todas las notas marcadas como favoritas.
    override fun getBookMarkedNotes(): Flow<List<Note>> {
        return noteDao.getBookmarkedNotes()
    }
}














