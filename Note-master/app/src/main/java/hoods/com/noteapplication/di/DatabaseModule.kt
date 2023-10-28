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
import javax.inject.Singleton
/*
este módulo proporciona instancias de NoteDao y NoteDatabase
que son necesarias para la interacción con la base de datos
local de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase):NoteDao =
        database.noteDao
    @Provides
    @Singleton
    fun  provideDatabase(
        @ApplicationContext context: Context
    ):NoteDatabase = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "notes_dn"
    ).build()
}
/*
Dagger Hilt se encargará de inyectar estas instancias en
otras partes de la aplicación donde se necesiten, como en
los casos de uso o en las capas de acceso a datos.
La anotación @Singleton garantiza que las instancias
sean únicas y vivan durante
toda la vida de la aplicación
* */