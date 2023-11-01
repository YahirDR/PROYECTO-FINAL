package hoods.com.noteapplication.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    // El método nos ayudara a convertir un valor de tipo Long (almacenado en la base de datos) en un valor de tipo Date.
    @TypeConverter
    fun toDate(date: Long?): Date? {
        return date?.let { Date(it) }
    }
    // Este método convierte un valor de tipo
    // Date en un valor de tipo Long (que se almacenará en la base de datos).
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

}


