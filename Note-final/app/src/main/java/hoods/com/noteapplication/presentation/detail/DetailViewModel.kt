package hoods.com.noteapplication.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.use_cases.AddUseCase
import hoods.com.noteapplication.domain.use_cases.AddUseCase_Factory
import hoods.com.noteapplication.domain.use_cases.GetNoteByIdUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
//AssistedInject permite la creación de ViewModels
// con parámetros específicos asistidos en un entorno de inyección de dependencias.
class DetailViewModel @AssistedInject constructor(
    private val addUseCase: AddUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    @Assisted private val noteId: Long,
) : ViewModel() {
    // El estado de la pantalla es mutable y se inicializa como DetailState vacío.
    var state by mutableStateOf(DetailState())
        private set
    // Comprueba si el formulario tiene campos no vacíos.
    val isFormNotBlank: Boolean
        get() = state.title.isNotEmpty() &&
                state.content.isNotEmpty()
    // Obtiene una representación de la nota actual desde el estado.
    private val note: Note
        get() = state.run {
            Note(
                id = id,
                title = title,
                content = content,
                createdDate = createdDate,
               isBookMarked = isBookmark
            )
        }
        // El bloque init se ejecuta al crear una instancia del ViewModel.
        init {
            initialize()
        }

    // Inicializa el ViewModel, verifica si se está actualizando una nota existente y carga la nota si es así.
    private fun initialize() {
        val isUpdatingNote = noteId != -1L
        state = state.copy(isUpdatingNote = isUpdatingNote)
        if (isUpdatingNote) {
            getNoteById()
        }
    }
    // Obtiene la nota por su ID.
    private fun getNoteById() = viewModelScope.launch {
        getNoteByIdUseCase(noteId).collectLatest { note ->
            state = state.copy(
                id = note.id,
                title = note.title,
                content = note.content,
                isBookmark = note.isBookMarked,
                createdDate = note.createdDate
            )
        }
    }
    // Actualiza el título de la nota en el estado.
    fun onTitleChange(title: String) {
        state = state.copy(title = title)
    }
    // Actualiza el contenido de la nota en el estado.
    fun onContentChange(content: String) {
        state = state.copy(content = content)
    }
    // Actualiza el estado de marcado/desmarcado de la nota.
    fun onBookMarkChange(isBookmark: Boolean) {
        state = state.copy(isBookmark = isBookmark)
    }
    // Agrega o actualiza la nota en la base de datos.
    fun addOrUpdateNote() = viewModelScope.launch {
        addUseCase(note = note)
    }


}
//clase de datos que representa el estado de la pantalla de detalle de notas.
data class DetailState(
    val id: Long = 0, //id de la nota
    val title: String = "", //titulo de la nota
    val content: String = "", //contenido de la nota
    val isBookmark: Boolean = false, // detectar si la nota esta marcada
    val createdDate: Date = Date(), //fecha de creacion
    val isUpdatingNote: Boolean = false, //indicador para verificar si se está actualizando una nota existente
)


//se utiliza para suprimir una advertencia del compilador en Kotlin.
// En este caso, se está utilizando para deshabilitar la advertencia "UNCHECKED_CAST".
@Suppress("UNCHECKED_CAST")
class DetailedViewModelFactory(// Se utiliza para crear instancias de ViewModels cuando se solicita un ViewModel específico.
    private val noteId: Long,
    private val assistedFactory: DetailAssistedFactory,
) : ViewModelProvider.Factory {
    // Esta función se utiliza para crear instancias de ViewModels cuando se solicita un ViewModel específico.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Llama al método `create` de la interfaz `assistedFactory` para crear una instancia de ViewModel.
        // El parámetro `noteId` se pasa a este método para que el ViewModel pueda inicializarse con un valor específico.
        return assistedFactory.create(noteId) as T
    }
}

@AssistedFactory
interface DetailAssistedFactory {
    // Esta interfaz define un método `create` que toma un parámetro `noteId` y crea una instancia de `DetailViewModel`.
    fun create(noteId: Long): DetailViewModel
}















