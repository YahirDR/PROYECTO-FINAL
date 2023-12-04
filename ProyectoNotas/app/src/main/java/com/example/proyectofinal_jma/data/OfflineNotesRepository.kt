package com.example.proyectofinal_jma.data

import kotlinx.coroutines.flow.Flow

class OfflineNotesRepository(private val notaDao: NotaDAO) : NotesRepository{
    override fun getAllNotesStream(): Flow<List<NotaEntity>> = notaDao.getAllItems()
    override fun getNoteStream(id: Int): Flow<NotaEntity> = notaDao.getItem(id)
    override suspend fun insertNote(item: NotaEntity):Long = notaDao.insert(item)
    override suspend fun deleteNote(item: NotaEntity) = notaDao.delete(item)
    override suspend fun updateNote(item: NotaEntity) = notaDao.update(item)
    override fun getAllImages(id: Int): Flow<List<String>> = notaDao.getAllImages(id)
    override suspend fun deleteAllImages(id: Int)= notaDao.deleteAllImages(id)
    override suspend fun insertImage(imageNotaEntity: ImageNotaEntity) = notaDao.insert(imageNotaEntity)
    override suspend fun deleteImage(imageNotaEntity: ImageNotaEntity) = notaDao.delete(imageNotaEntity)
    override suspend fun updateImage(imageNotaEntity: ImageNotaEntity) = notaDao.update(imageNotaEntity)
    override fun getAllVideos(id: Int): Flow<List<String>> = notaDao.getAllVideos(id)
    override suspend fun deleteAllVideos(id: Int)= notaDao.deleteAllVideos(id)
    override suspend fun insertVideo(videoNotaEntity: VideoNotaEntity) = notaDao.insert(videoNotaEntity)
    override suspend fun deleteVideo(videoNotaEntity: VideoNotaEntity) = notaDao.delete(videoNotaEntity)
    override suspend fun updateVideo(videoNotaEntity: VideoNotaEntity) = notaDao.update(videoNotaEntity)
    override fun getAllAudios(id: Int): Flow<List<String>> = notaDao.getAllAudios(id)
    override suspend fun deleteAllAudios(id: Int)= notaDao.deleteAllAudios(id)
    override suspend fun insertAudio(audioNotaEntity: AudioNotaEntity) = notaDao.insert(audioNotaEntity)
    override suspend fun deleteAudio(audioNotaEntity: AudioNotaEntity) = notaDao.delete(audioNotaEntity)
    override suspend fun updateAudio(audioNotaEntity: AudioNotaEntity) = notaDao.update(audioNotaEntity)
}