package hoods.com.noteapplication.presentacion.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import hoods.com.noteapplication.data.local.model.Note
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hoods.com.noteapplication.common.pantallaVista
import org.w3c.dom.Text
import java.util.Date


@Composable
fun pantallaInicio(
    modificador: Modifier = Modifier,
    estado: inicioEstado,
    marcadorCambio:(note:Note) ->Unit, //onBookMarkChange
    borrarNota:(Long) -> Unit,          //onDeleteNote
    notaSeleccionada:(Long) ->Unit      //onNoteClicked
){
    when(estado.notes){
        is pantallaVista.Cargar ->{
            CircularProgressIndicator()
        }
        is pantallaVista.Exito ->{
            val notes = estado.notes.data
            inicioDetalles(
                notas = notes,
                modifier = modificador,
                marcadorCambio = marcadorCambio,
                borrarNota = borrarNota,
                notaSeleccionada = notaSeleccionada
            )
        }
        is pantallaVista.Error ->{
            Text(
                text = estado.notes.message ?: "A ocurrido un error ",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
//Home Detail
@Composable
private fun inicioDetalles(
    notas:List<Note>,
    modifier: Modifier,
    marcadorCambio:(note:Note) ->Unit, //onBookMarkChange
    borrarNota:(Long) -> Unit,          //onDeleteNote
    notaSeleccionada:(Long) ->Unit      //onNoteClicked
) {
   LazyVerticalStaggeredGrid(
       columns = StaggeredGridCells.Fixed(count = 2),
       contentPadding = PaddingValues(4.dp),
       modifier = modifier
   ){
        itemsIndexed(notas){index, note ->
            notaCarta(
                index = index,
                note = note,
                marcadorCambio = marcadorCambio,
                borrarNota = borrarNota,
                notaSeleccionada = notaSeleccionada
            )
        }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun notaCarta(
    index:Int,
    note:Note,
    marcadorCambio:(note:Note) ->Unit, //onBookMarkChange
    borrarNota:(Long) -> Unit,          //onDeleteNote
    notaSeleccionada:(Long) ->Unit

){
    val isIndex = index % 2 == 0
    val forma = when{
        isIndex ->{
            RoundedCornerShape(
                topStart = 50f,
                bottomEnd = 50f
            )
        }
        else -> {
            RoundedCornerShape(
                topEnd = 50f,
                bottomStart = 50f
            )
        }
    }
    val icono = if (note.isBookMarker) Icons.Default.BookmarkRemove
    else Icons.Outlined.BookmarkAdd
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape= forma,
        onClick = {notaSeleccionada(note.id)}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = note.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //BOTONES EN LA CARTA
                IconButton(onClick = { borrarNota(note.id)}) {
                    Icon(
                        imageVector = Icons.Default.Delete ,
                        contentDescription = null,
                    )
                }
                IconButton(onClick = { marcadorCambio(note)}) {
                    Icon(
                        imageVector = icono ,
                        contentDescription = null,
                    )
                }
            }
        }

    }
}
@Preview(showSystemUi = true)
@Composable
fun previewInicio(){
    pantallaInicio(
        estado = inicioEstado(
            notes = pantallaVista.Exito(notes)
        ),
        marcadorCambio = {},
        borrarNota = {},
        notaSeleccionada = {}
    )
}

val placeHolderText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque varius erat at lacus alique"

val notes = listOf(
    Note(
        title = "Nota 1",
        content = placeHolderText + placeHolderText,
        createdDate = Date()
    ),
    Note(
        title = "Nota 2",
        content = "Probando",
        isBookMarker = true,
        createdDate = Date()
    ),
    Note(
        title = "Nota 3",
        content = "El amor es caca ",
        createdDate = Date()
    )
)