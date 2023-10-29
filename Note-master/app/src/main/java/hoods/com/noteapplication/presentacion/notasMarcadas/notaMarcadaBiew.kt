package hoods.com.noteapplication.presentacion.notasMarcadas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.common.pantallaVista
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


//BookmarkViewModel
@HiltViewModel
class notaMarcadaBiew @Inject constructor(
    //Agregamos los casos de uso que creamos
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val filteredBookmarkNotes: FilteredBookmarkNotes,
    private val deleteNoteUseCase: DeleteNoteUseCase
): ViewModel() {
    // Agregamos  MutableStateFlow para mantener el estado de la pantalla
    private val estadoUno : MutableStateFlow<notaMarcadaEstado>
    = MutableStateFlow(notaMarcadaEstado())
    val estadoDos: StateFlow<notaMarcadaEstado> = estadoUno.asStateFlow()
    // Función para obtener notas marcadas
    private fun obtenerNotaMarcada(){
        filteredBookmarkNotes()
            // Cuando se obtienen con éxito las notas marcadas, se actualiza el estado con las notas obtenidas.
            .onEach {
            estadoUno.value = notaMarcadaEstado(
                notas = pantallaVista.Exito(it)
            )
        }    // En caso de error al obtener las notas marcadas, se actualiza el estado con un mensaje de error.
            .catch {
                estadoUno.value = notaMarcadaEstado(notas = pantallaVista.Error(it.message))
            }
            .launchIn(viewModelScope)
    }
    // Función para obtener una nota cambiada
    fun obtenerNotaCambiada(nota: Note){
        viewModelScope.launch {
            updateNoteUseCase(nota.copy(
                isBookMarker = nota.isBookMarker
            ))
        }
    }
    // Función para borrar una nota
    fun borrarNota(notaId: Long){
        viewModelScope.launch {
            deleteNoteUseCase(notaId)
        }
    }
}
//BookmarkState
data class notaMarcadaEstado(
    val notas:pantallaVista<List<Note>> = pantallaVista.Cargar
)