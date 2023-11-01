package hoods.com.noteapplication.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.use_cases.DeleteNoteUseCase
import hoods.com.noteapplication.domain.use_cases.FilteredBookmarkNotes
import hoods.com.noteapplication.domain.use_cases.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    // El constructor se inyecta con instancias de casos de uso necesarios y una instancia de ViewModel proporcionada por Hilt.
    private val updateNoteUseCase: UpdateNoteUseCase, // Caso de uso para actualizar una nota
    private val filteredBookmarkNotes: FilteredBookmarkNotes, // Caso de uso para obtener notas marcadas como favoritas
    private val deleteNoteUseCase: DeleteNoteUseCase // Caso de uso para eliminar una nota
) : ViewModel() {
    // Se crea un MutableStateFlow que almacena el estado de la vista de la pantalla de notas favoritas
    private val _state: MutableStateFlow<BookmarkState> = MutableStateFlow(BookmarkState())
    // Se expone el estado de la vista como un StateFlow que los componentes de la interfaz de usuario pueden observar.
    val state: StateFlow<BookmarkState> = _state.asStateFlow()
    // El constructor de la ViewModel se inicializa llamando a la función getBookMarkedNotes() para
    // obtener las notas favoritas.
    init {
        getBookMarkedNotes()
    }

    // Obtiene las notas marcadas como favoritas y actualiza el estado
    private fun getBookMarkedNotes() {
        filteredBookmarkNotes().onEach {
            _state.value = BookmarkState(
                notes = ScreenViewState.Success(it)
            )
        }
            // En caso de error, se actualiza el estado con un estado de error y se proporciona un mensaje de error.
            .catch {
                _state.value = BookmarkState(notes = ScreenViewState.Error(it.message))
            }
            .launchIn(viewModelScope)
    }
    // Cambia el estado de favorito de una nota y actualiza la base de datos
    fun onBookmarkChange(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(
                note.copy(
                    isBookMarked = !note.isBookMarked
                )
            )
        }
    }
    // Elimina una nota de la base de datos
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }


}
//Esta clase tiene  La finalidad de crear una estructura de datos
// además de proporcionar un lugar centralizado para almacenar y
// gestionar el estado de la vista de la pantalla de notas favoritas.
data class BookmarkState(
    val notes: ScreenViewState<List<Note>> = ScreenViewState.Loading,
)