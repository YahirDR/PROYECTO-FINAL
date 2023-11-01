package hoods.com.noteapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hoods.com.noteapplication.data.local.converters.DateConverter
import hoods.com.noteapplication.data.local.model.Note
//Indicamos  que se deben utilizar los convertidores definidos en la clase DateConverter
// para tratar los campos de tipo Date en la base de datos.
@TypeConverters(value = [DateConverter::class])
@Database(
    // La lista de entidades asociadas a esta base de datos.
    // Para esta app solo se usara la entidad: Note.
    entities = [Note::class],
    // La versión de la base de datos.
    version = 1,
    //Evitar la exportación de esquemas de base de datos en tiempo de compilación.
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}











