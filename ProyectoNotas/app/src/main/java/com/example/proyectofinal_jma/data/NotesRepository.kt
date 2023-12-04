package com.example.proyectofinal_jma.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    /** Retorna todos las notas de la fuente de datos dada.*/
    fun getAllNotesStream(): Flow<List<NotaEntity>>
    /** Retorna una note de la fuente de datos dada que concide con el id entregado*/
    fun getNoteStream(id: Int): Flow<NotaEntity>
    /** Insertar una nota en la fuente de datos*/
    suspend fun insertNote(notaEntity: NotaEntity):Long
    /**Borrar nota de la fuente de datos*/
    suspend fun deleteNote(notaEntity: NotaEntity)
    /**Actualizar nota de la fuente de datos**/
    suspend fun updateNote(notaEntity: NotaEntity)

    fun getAllImages(id: Int): Flow<List<String>>
    suspend fun deleteAllImages(id: Int)
    suspend fun insertImage(imageNotaEntity: ImageNotaEntity)
    suspend fun deleteImage(imageNotaEntity: ImageNotaEntity)
    suspend fun updateImage(imageNotaEntity: ImageNotaEntity)

    fun getAllVideos(id: Int): Flow<List<String>>
    suspend fun deleteAllVideos(id: Int)
    suspend fun insertVideo(videoNotaEntity: VideoNotaEntity)
    suspend fun deleteVideo(videoNotaEntity: VideoNotaEntity)
    suspend fun updateVideo(videoNotaEntity: VideoNotaEntity)

    fun getAllAudios(id: Int): Flow<List<String>>
    suspend fun deleteAllAudios(id: Int)
    suspend fun insertAudio(audioNotaEntity: AudioNotaEntity)
    suspend fun deleteAudio(audioNotaEntity: AudioNotaEntity)
    suspend fun updateAudio(audioNotaEntity: AudioNotaEntity)
}