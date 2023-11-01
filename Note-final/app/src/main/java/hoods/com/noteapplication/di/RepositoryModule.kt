package hoods.com.noteapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hoods.com.noteapplication.data.repository.NoteRepositoryImpl
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Este método abstracto se anota con @Binds y se utiliza para vincular (bind)
    // una implementación concreta de la interfaz Repository.
    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: NoteRepositoryImpl): Repository
}





