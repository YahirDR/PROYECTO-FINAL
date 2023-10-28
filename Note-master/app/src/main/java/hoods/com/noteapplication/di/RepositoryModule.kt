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
    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: NoteRepositoryImpl):Repository
}
/*
*Este módulo RepositoryModule se
* utiliza para definir cómo Dagger Hilt
* debe proporcionar instancias de la interfaz Repository.
* Vincula la interfaz con su implementación concreta, NoteRepositoryImpl,
* y especifica que esta implementación debe tratarse como un singleton.
* Luego, Dagger Hilt se encargará de inyectar instancias de NoteRepositoryImpl
* donde sea necesario en la aplicación.
* */