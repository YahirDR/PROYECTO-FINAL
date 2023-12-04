package com.example.proyectofinal_jma.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDAO {
    @Insert
    suspend fun insert(notaEntity:NotaEntity):Long
    @Update
    suspend fun update(notaEntity:NotaEntity)
    @Delete
    suspend fun delete(notaEntity:NotaEntity)

    @Query("SELECT * from Notas WHERE id = :id")
    fun getItem(id: Int): Flow<NotaEntity>

    @Query("SELECT * from Notas ORDER BY fecha ASC")
    fun getAllItems(): Flow<List<NotaEntity>>

    //IMAGENES
    @Query("SELECT uriImagen from imagenes WHERE idNota= :id")
    fun getAllImages(id: Int): Flow<List<String>>

    @Query("DELETE from imagenes WHERE idNota= :id")
    suspend fun deleteAllImages(id: Int)

    @Insert
    suspend fun insert(imageNotaEntity: ImageNotaEntity)

    @Update
    suspend fun update(imageNotaEntity: ImageNotaEntity)

    @Delete
    suspend fun delete(imageNotaEntity: ImageNotaEntity)

    //VIDEOS
    @Query("SELECT uriVideo from videos WHERE idNota= :id")
    fun getAllVideos(id: Int): Flow<List<String>>

    @Query("DELETE from videos WHERE idNota= :id")
    suspend fun deleteAllVideos(id: Int)

    @Insert
    suspend fun insert(videoNotaEntity: VideoNotaEntity)

    @Update
    suspend fun update(videoNotaEntity: VideoNotaEntity)

    @Delete
    suspend fun delete(videoNotaEntity: VideoNotaEntity)

    //Audios
    @Query("SELECT uriAudio from audios WHERE idNota= :id")
    fun getAllAudios(id: Int): Flow<List<String>>

    @Query("DELETE from audios WHERE idNota= :id")
    suspend fun deleteAllAudios(id: Int)

    @Insert
    suspend fun insert(audioNotaEntity: AudioNotaEntity)

    @Update
    suspend fun update(audioNotaEntity: AudioNotaEntity)

    @Delete
    suspend fun delete(audioNotaEntity: AudioNotaEntity)
}