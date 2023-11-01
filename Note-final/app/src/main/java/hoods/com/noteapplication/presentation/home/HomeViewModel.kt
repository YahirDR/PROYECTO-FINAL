package hoods.com.noteapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.use_cases.DeleteNoteUseCase
import hoods.com.noteapplication.domain.use_cases.GetAllNotesUseCase
import hoods.com.noteapplication.domain.use_cases.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
// En la clase se usa un ViewModel con con @HiltViewModel, lo que permite que Hilt gestione la inyección de dependencias.
// El constructor se inyecta con instancias de casos de uso necesarios y una instancia de ViewModel proporcionada por Hilt.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
) : ViewModel() {
    // Se crea un MutableStateFlow que almacena el estado de la vista de la pantalla de inicio.
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())

    // Se expone el estado de la vista como un StateFlow que los componentes de la interfaz de usuario pueden observar.
    val state: StateFlow<HomeState> = _state.asStateFlow()

    // El constructor de la ViewModel se inicializa llamando a la función getAllNotes() para obtener todas las notas.
    init {
        getAllNotes()
    }
    // Función privada para obtener todas las notas desde la fuente de datos utilizando el caso de uso.
    private fun getAllNotes() {
        //Se llama el caso del uso getAllNotesUseCase
        //para obtener las notas
        getAllNotesUseCase()
            // Cuando se obtienen las notas con éxito, se actualiza el estado con un estado de éxito y las notas.
            .onEach {
                _state.value = HomeState(notes = ScreenViewState.Success(it))
            }
            // En caso de error, se actualiza el estado con un estado de error y se proporciona un mensaje de error.
            .catch {
                _state.value = HomeState(notes = ScreenViewState.Error(it.message))
            }
            .launchIn(viewModelScope)
    }
    // Función  para eliminar una nota por su ID.
    fun deleteNote(noteId:Long) = viewModelScope.launch {
        //Se llama el caso de uso eliminar
        //para eliminar la nota
        deleteNoteUseCase(noteId)
    }
    // Función  para cambiar el estado de favorito de una nota.
    fun onBookMarkChange(note:Note){
        viewModelScope.launch {
            // Se actualiza la nota con un cambio en su estado de favorito
            // y se llama al caso de uso para actualizarla en la fuente de datos.
            updateNoteUseCase(note.copy(isBookMarked = !note.isBookMarked))
        }
    }
}
//Esta clase tiene  La finalidad de crear una estructura de datos
// además de proporcionar un lugar centralizado para almacenar y
// gestionar el estado de la vista de la pantalla de inicio.
data class HomeState(
    //se inicializa con el valor Loading, lo que indica que inicialmente se considera que la pantalla está en un estado de carga.
    val notes: ScreenViewState<List<Note>> = ScreenViewState.Loading,
)











