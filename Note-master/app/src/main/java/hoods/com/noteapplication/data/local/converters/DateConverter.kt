package hoods.com.noteapplication.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    //CONVERSION DE LA FECHA
    @TypeConverter
    fun toDate(date:Long?): Date?{
        return date?.let { Date(it) }
    }
    //OBTENER DATE
    @TypeConverter
    fun fromDate(date:Date?): Long?{
        return date?.time
    }
}