package hoods.com.noteapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hoods.com.noteapplication.data.local.model.Note
import kotlinx.coroutines.flow.Flow
//Creacion del DAO CRUD que contrenda las funciones para usar
@Dao
interface NoteDao {
    // Consulta "getAllNotes" realiza una consulta  para obtener todas las notas de la tabla 'notes'
    // y las devuelve como una secuencia Flow)de listas de objetos Note, ordenadas por la columna por fecha.
    @Query("SELECT * FROM notes ORDER BY createdDate")
    fun getAllNotes(): Flow<List<Note>>
    //Consulta "getNoteById" para obtener una nota específica por su id
    // y la devuelve como una secuencia Flow de un solo objeto Note.
    @Query("SELECT * FROM notes WHERE id=:id ORDER BY createdDate")
    fun getNoteById(id: Long): Flow<Note>
    // Este método se encarga de realizar una  inserción en la base de datos,
    // insertando una nueva nota. En caso de conflicto (cuando ya existe una nota con el mismo 'id'), se reemplazará por la nueva nota.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)
    // Actualización en la base de datos, actualizando una nota existente.
    // En caso de conflicto (cuando ya existe una nota con el mismo 'id'), se reemplazará por la nota actualizada.
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Note)
    //Consulta para eliminar una nota
    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun delete(id: Long)
    // Este método realiza una consulta SQL para obtener todas las notas marcadas como favoritas (con 'isBookMarked' igual a 1)
    // y las devuelve como una secuencia (Flow) de listas de objetos Note,
    // ordenadas por 'createdDate' en orden descendente.
    @Query("SELECT * FROM notes WHERE isBookMarked = 1 ORDER BY createdDate DESC")
    fun getBookmarkedNotes(): Flow<List<Note>>

}











