package hoods.com.noteapplication.presentacion.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import hoods.com.noteapplication.common.pantallaVista
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

@HiltViewModel
class InicioView @Inject constructor(
    //Llamar los casos de uso que se crearon
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private  val deleteNoteUseCase: DeleteNoteUseCase,
    private val  updateNoteUseCase: UpdateNoteUseCase
):ViewModel(){
    // Declaramon  un MutableStateFlow para mantener el estado de la pantalla
    private val estadoUno:MutableStateFlow<inicioEstado> = MutableStateFlow(inicioEstado())
    // Declaración de un StateFlow que proporciona acceso de solo lectura al estado
    val estadoDos:StateFlow<inicioEstado> = estadoUno.asStateFlow()

    init {
        // Cuando se crea una instancia del ViewModel,
        // se llama a la función "obtenerNotas()"
        obtenerNotas()
    }
    // Se utiliza "viewModelScope.launch" para realizar esta operación en un hilo secundario.
    private fun obtenerNotas(){
        // Llamamos "getAllNotesUseCase()" para obtener una lista de notas.
        getAllNotesUseCase()
            //se actualiza el estado con las notas obtenidas
            .onEach {
            estadoUno.value = inicioEstado(notes = pantallaVista.Exito(it))
        }
            //se actualiza el estado con un mensaje de error.
            .catch {
                estadoUno.value = inicioEstado(notes = pantallaVista.Error(it.message))
            }
            .launchIn(viewModelScope)
    }
    //DeleteNote
    fun borrarNota(notaId:Long)= viewModelScope.launch {
        // Llamamos a "deleteNoteUseCase()" para borrar una nota con el ID proporcionado.
        deleteNoteUseCase(notaId)
    }
    //onBookMarkChange
    fun marcadorCambio(nota:Note){
        viewModelScope.launch {
            // Llamamos  a "updateNoteUseCase()" para cambiar el marcador (bookmarked) de una nota.
            updateNoteUseCase(nota.copy(isBookMarker = !nota.isBookMarker))
        }
    }
}
//HomeState
data class inicioEstado(
    val notes:pantallaVista<List<Note>> = pantallaVista.Cargar,
)