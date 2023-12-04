package com.example.proyectofinal_jma.viewModel

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navArgument
import com.example.proyectofinal_jma.data.AudioNotaEntity
import com.example.proyectofinal_jma.data.ImageNotaEntity
import com.example.proyectofinal_jma.data.NotaEntity
import com.example.proyectofinal_jma.data.NotesRepository
import com.example.proyectofinal_jma.data.VideoNotaEntity
import com.example.proyectofinal_jma.navigation.AppScreens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone

/**ViewModel para validar e insertar notas en la base de datos*/
class NoteEntryViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    var noteUiState by mutableStateOf(NoteUiState())
        private set

    fun updateUiState(noteDetails: NoteDetails) {
        noteUiState =
            NoteUiState(noteDetails = noteDetails, isEntryValid = validateInput(noteDetails))
    }

    suspend fun saveNote() {
        if (validateInput()) {
            noteUiState.noteDetails.tipo=optionNote
            var id=notesRepository.insertNote(noteUiState.noteDetails.toNote())
            saveImages(id)
            saveVideos(id)
            saveAudios(id)
        }
    }

    suspend fun saveImages(id:Long){
        urislist.forEach{uri->
            var imageNota=ImageNotaEntity(0, id.toInt(),""+uri)
            notesRepository.insertImage(imageNota)
        }
    }

    suspend fun saveVideos(id:Long){
        urisVideoslist.forEach{uri->
            var videoNota=VideoNotaEntity(0,id.toInt() ,""+uri)
            notesRepository.insertVideo(videoNota)
        }
    }

    suspend fun saveAudios(id:Long){
        urisAudiosList.forEach{uri->
            var audioNota=AudioNotaEntity(0, id.toInt(),""+uri)
            notesRepository.insertAudio(audioNota)
        }
    }

    private fun validateInput(uiState: NoteDetails = noteUiState.noteDetails): Boolean {
        return with(uiState) {
            titulo.isNotBlank() && contenido.isNotBlank() && fecha.isNotBlank()
        }
    }

    var isExpanded by mutableStateOf(false)
    var textSearch by mutableStateOf("")
    var optionNote by mutableStateOf("Nota")
    var showCancel by mutableStateOf(false)
    var recordatorios by mutableStateOf(false)
    var showOptionRecordatorios by mutableStateOf(false)
    var showReloj by mutableStateOf(false)
    var calcular by mutableStateOf(false)
    var hora by mutableStateOf("")
    var notificacion by mutableStateOf(false)
    var hour by mutableStateOf(0)
    var idNotificacion by mutableStateOf(0)
    var fileNumb by mutableStateOf(0)
    var minute by mutableStateOf(0)
    var uriMostrar by mutableStateOf(Uri.EMPTY)

    fun updateUriMostrar(uri: Uri?){
        uriMostrar=uri
    }

    fun updateIdNotificacion(){
        idNotificacion+=1
    }

    //IMAGENES
    var hasImage by mutableStateOf(false)
    var mostrarImagen by mutableStateOf(false)
    var cantidad by mutableStateOf(0)
    var urislist= mutableStateListOf<Uri?>()
    //VIDEOS
    var hasVideo by mutableStateOf(false)
    var mostrarVideo by mutableStateOf(false)
    var cantidadVideos by mutableStateOf(0)
    var urisVideoslist= mutableStateListOf<Uri>()
    //AUDIOS
    val urisAudiosList = mutableStateListOf<Uri?>()
    var hasAudio by mutableStateOf(false)
    var cantidadAudios by mutableStateOf(0)
    var mostrarAudio by mutableStateOf(false)
    var showOptionsAudio by mutableStateOf(false)

    fun updateShowOptionsAudio(boolean: Boolean){
        showOptionsAudio= boolean
    }

    fun updateFileNumb(int: Int){
        fileNumb=int
    }

    fun updateMostrarAudio(boolean: Boolean){
        mostrarAudio= boolean
    }

    fun updateShowCancel(boolean: Boolean){
        showCancel= boolean
    }
    fun updateHour(int: Int){
        hour= int
    }
    fun updateMinute(int: Int){
        minute= int
    }

    fun updateCalcular(boolean: Boolean){
        calcular= boolean
    }

    fun updateIsExpanded(boolean: Boolean){
        isExpanded= boolean
    }

    fun updateOptionNote(text: String ){
        optionNote=text
    }

    fun updateTextSearch(text: String ){
        textSearch=text
    }

    fun updateRecordatorios(boolean: Boolean){
        recordatorios= boolean
    }

    fun updateOptionsRecordatorios(boolean: Boolean){
        showOptionRecordatorios= boolean
    }
    fun updateShowReloj(boolean: Boolean){
        showReloj= boolean
    }

    fun updateHora(text: String){
        hora= text
    }

    fun updateNotificaciones(boolean: Boolean){
        notificacion= boolean
    }
    //IMAGENES
    fun updatehasImage(boolean: Boolean){
        hasImage= boolean
    }

    fun updateUrisList(uri: Uri?){
        urislist.add(uri)
        cantidad=urislist.size
    }

    fun deleteLastUri(){
        urislist.removeLast()
        cantidad=urislist.size
    }

    fun updateMostrarImagen(boolean: Boolean){
        mostrarImagen= boolean
    }

    //VIDEOS
    fun updatehasVideo(boolean: Boolean){
        hasVideo= boolean
    }

    fun updateUrisVideosList(uri: Uri){
        urisVideoslist.add(uri)
        cantidadVideos=urisVideoslist.size
    }

    fun deleteLastUriVideos(){
        urisVideoslist.removeLast()
        cantidadVideos=urisVideoslist.size
    }

    fun updateMostrarVideo(boolean: Boolean){
        mostrarVideo= boolean
    }

    //AUDIOS
    fun updateUrisAudiosList(uri: Uri?){
        urisAudiosList.add(uri)
        cantidadAudios=urisAudiosList.size
}

    fun deleteLastUriAudios(){
        urisAudiosList.removeLast()
        cantidadAudios=urisAudiosList.size
    }

    fun updateHasAudio(boolean: Boolean){
        hasAudio= boolean
    }
}

/**
 * Represents Ui State for an Item.
 */
data class NoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val isEntryValid: Boolean = false
)

data class NoteDetails(
    val id: Int = 0,
    val titulo: String = "",
    val contenido: String = "",
    val fecha: String = ""+ Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City")).get(Calendar.DAY_OF_MONTH)+
            "/"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"/"+Calendar.getInstance().get(Calendar.YEAR),
    var tipo:String=""
)

fun NoteDetails.toNote(): NotaEntity = NotaEntity(
    id = id,
    titulo = titulo,
    contenido = contenido,
    fecha = fecha,
    tipo=tipo
)

fun NotaEntity.toNoteUiState(isEntryValid: Boolean = false): NoteUiState = NoteUiState(
    noteDetails = this.toNoteDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun NotaEntity.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
    titulo = titulo,
    contenido = contenido,
    fecha = fecha,
    tipo= tipo
)