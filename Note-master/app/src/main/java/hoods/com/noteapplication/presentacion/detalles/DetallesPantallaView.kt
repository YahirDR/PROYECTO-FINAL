package hoods.com.noteapplication.presentacion.detalles

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
import hoods.com.noteapplication.domain.use_cases.GetNoteByIdUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
//biblioteca de Kotlin Coroutines.
//DetailViewModel
//Usamos @AssistedInject para ayudar en la inyección
// de dependencias cuando se tiene una dependencia que
// no es fácil de proporcionar de manera convencional.
class DetallesPantallaView @AssistedInject constructor(
    private  val addUseCase: AddUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    @Assisted   private val notaId: Long
): ViewModel() {
    // Creamos el estado de la pantalla
    var estado by mutableStateOf(detallesEstado())
        private set
    // Verifica si el formulario está en blanco
    val isFormEnBlanco:Boolean
        //Verificamos que no este en blanco el titulo ni el cuerpo
        get() =  estado.title.isNotEmpty() && estado.content.isNotEmpty()
    // Obtiene la nota desde el estado actual
    private val nota: Note
        get() = estado.run {
            Note(
                id,
                title,
                content,
                createdDate
            )
        }
    // Inicializador del ViewModel
    private fun inicializar(){
        // Verificar si se está actualizando una nota existente
        val isActualizandoNota = notaId != -1L
        estado = estado.copy(isActualizandoNota = isActualizandoNota)
        if(isActualizandoNota){
            getNotaId()
        }
    }
    // Obtiene la nota por su ID
    private fun getNotaId() = viewModelScope.launch {
        //Llamamos a nuestro caso de uso obtener la nota por su id
        getNoteByIdUseCase(notaId).collectLatest {nota ->
            estado = estado.copy(
                id = nota.id,
                title = nota.title,
                content = nota.content,
                isBookMarker = nota.isBookMarker,
                createdDate = nota.createdDate
            )
        }
    }
    // Funciones para modificar los atributos de la nota en el estado
    fun tituloCambiar(title: String){
        estado = estado.copy(title = title)
    }
    fun contenidoCambiar(content: String){
        estado = estado.copy(content = content)
    }
    fun notamMrcadaCambiar(isBookMarker: Boolean){
        estado = estado.copy(isBookMarker = isBookMarker)
    }
    fun agregarActualizadorNota() = viewModelScope.launch {
        addUseCase(note = nota)
    }

}

data class detallesEstado(
    //Parametros de nuesta entidad Note
    val id:Long =0,
    val title: String ="",
    val content:String = "",
    val isBookMarker: Boolean = false,
    val createdDate:Date = Date(),
    val isActualizandoNota: Boolean = false
)

//Creamos una clase que servira para recibir los datos que se
// usaran en el composable.
//Para ello se usara un Factory
//Esto permite en Jetpack Compose
// usar de forma flexibles
//  crear Composables personalizados
class detallasDePantallaF(
    //Agregamos los parametros
    private val notaId: Long,
    private val  ayudaF: DetallesAsistidosF
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ayudaF.create(notaId) as T
    }
}
//Usamos @AssistedFactory para
// crear fábricas que facilitan
// la creación de instancias de objetos
// con parámetros específicos en tiempo de ejecución en aplicaciones
@AssistedFactory
interface DetallesAsistidosF{
    fun create(notaId: Long): DetallesPantallaView
}