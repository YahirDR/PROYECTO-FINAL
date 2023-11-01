package hoods.com.noteapplication.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hoods.com.noteapplication.data.local.NoteDao
import hoods.com.noteapplication.data.local.NoteDatabase
import hoods.com.noteapplication.data.local.model.Note
import javax.inject.Singleton

@Module
//indica que este módulo está asociado al ámbito Singleton de Dagger Hilt,
// lo que significa que las instancias proporcionadas por estos métodos serán
// únicas durante toda la vida de la aplicación,
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // Este método proporciona una instancia de la interfaz NoteDao,
    // que se obtiene a partir de la instancia de la base de datos NoteDatabase.
    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao =
        database.noteDao
    // Este método proporciona una instancia de la base de datos NoteDatabase.
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): NoteDatabase = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "notes_db"
    )
        .build()

}



