package hoods.com.noteapplication.presentacion.notasMarcadas

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hoods.com.noteapplication.common.pantallaVista
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.presentacion.inicio.inicioEstado
import hoods.com.noteapplication.presentacion.inicio.notaCarta
import hoods.com.noteapplication.presentacion.inicio.notes
import hoods.com.noteapplication.presentacion.inicio.pantallaInicio


@Composable
fun notaMarcadaPantall(
    estado: notaMarcadaEstado,   // El estado de la pantalla
    modificador: Modifier = Modifier,  // Modificador para personalizar la apariencia
    obtenerNotaCambiada:(nota: Note) -> Unit, // Función para obtener una nota cambiada
    borrar:(Long) ->Unit,   // Función para borrar una nota
    notaSeleccionada:(Long) ->Unit // Función para seleccionar una nota
){
    when(estado.notas){
        is pantallaVista.Cargar ->{
            CircularProgressIndicator() //permite indicar que una tarea en segundo plano
        }
        // Cuando se obtienen con éxito las notas, se muestra la lista de notas
        is pantallaVista.Exito ->{
            val notas = estado.notas.data
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                modifier = modificador,
                ){
                    itemsIndexed(notas){index: Int, note: Note ->
                        //Llamamos notaCarda para crear la carta de la nota
                        notaCarta(
                            index = index,
                            note = note,
                            marcadorCambio = obtenerNotaCambiada,
                            borrarNota = borrar,
                            notaSeleccionada = notaSeleccionada
                        )
                    }
            }
        }
        // En caso de error, se muestra un mensaje de error
        is pantallaVista.Error ->{
            Text(
                text = estado.notas.message ?: "A ocurrido un error ",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewNotaMarcada(){
    //Agregamos notas de ejemplo
    notaMarcadaPantall(
        estado = notaMarcadaEstado(
            notas = pantallaVista.Exito(notes)
        ),
        obtenerNotaCambiada = {},
        borrar = {},
        notaSeleccionada = {}
    )
}