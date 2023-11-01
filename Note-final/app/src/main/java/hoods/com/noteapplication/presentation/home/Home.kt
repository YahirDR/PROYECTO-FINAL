package hoods.com.noteapplication.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import java.util.Date
// Con esta función se representa la pantalla de inicio de la aplicación.
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onBookmarkChange: (note: Note) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onNoteClicked: (Long) -> Unit,
) {
    // Usamos when para manejar los diferentes estados de vista de acuerdo al estado actual.
    when (state.notes) {
        is ScreenViewState.Loading -> {
            // Si el estado es de carga, se muestra un indicador de progreso.
            CircularProgressIndicator()
        }
        // Si el estado es de éxito, se obtienen las notas marcadas y se muestran en una lista.
        is ScreenViewState.Success -> {
            val notes = state.notes.data
            HomeDetail(
                notes = notes,
                modifier = modifier,
                onBookmarkChange = onBookmarkChange,
                onDeleteNote = onDeleteNote,
                onNoteClicked = onNoteClicked
            )
        }
        // Si el estado es de error, se muestra un mensaje de error o "Error" si no hay mensaje.
        is ScreenViewState.Error -> {
            Text(
                text = state.notes.message ?: "Error",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

// La función 'HomeDetail' muestra una lista de notas en un diseño de cuadrícula escalonada verticalmente.
@Composable
private fun HomeDetail(
    //PARAMETROS
    notes: List<Note>, //Lista de objetos Note, que representa las notas que se mostrarán en la lista.
    modifier: Modifier,
    onBookmarkChange: (note: Note) -> Unit, // función de devolución de llamada que se invoca cuando se cambia el estado de favorito de una nota.
    onDeleteNote: (Long) -> Unit,//función de devolución de llamada que se invoca cuando se elimina una nota por su ID.
    onNoteClicked: (Long) -> Unit,  // función de devolución de llamada que se invoca cuando se hace clic en una nota para ver más detalles.
) {
    //Usamos LazyVerticalStaggeredGrid
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),// Se especifica que la cuadrícula tendrá 2 columnas fijas.
        contentPadding = PaddingValues(4.dp),   // Se aplican rellenos al contenido de la cuadrícula.
        modifier = modifier
    ) {
        //se utiliza la función itemsIndexed para iterar a través de la lista de notes
        // y crear un componente NoteCard para cada nota
        itemsIndexed(notes) { index, note ->
            NoteCard(
                index = index, // índice de la nota en la lista.
                note = note,   //nota que se está procesando en ese momento.
                onBookmarkChange = onBookmarkChange,
                onDeleteNote = onDeleteNote,
                onNoteClicked = onNoteClicked
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    index: Int, // Índice de la tarjeta en la lista
    note: Note, // Datos de la nota que se va a mostrar
    onBookmarkChange: (note: Note) -> Unit, // Función de cambio de favorito
    onDeleteNote: (Long) -> Unit, // Función para eliminar la nota
    onNoteClicked: (Long) -> Unit, // Función para manejar el clic en la tarjeta
) {
    // Determina si el índice de la tarjeta es par o impar
    val isEvenIndex = index % 2 == 0
    // Define la forma (shape) de la tarjeta en función del índice
    val shape = when {
        isEvenIndex -> {
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
    // Define el icono de favorito (bookmark) en función del estado de la nota
    val icon = if (note.isBookMarked) Icons.Default.BookmarkRemove
    else Icons.Outlined.BookmarkAdd
    // Crea una tarjeta (Card) que se usara para representar las notas
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = shape,  // Forma de la tarjeta
        onClick = { onNoteClicked(note.id) } // Maneja el clic en la tarjeta
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            // Muestra el título de la nota en negritas con un máximo de 1 línea
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            // Muestra el contenido de la nota con un máximo de 4 líneas y puntos suspensivos en caso de desbordamiento
            Text(
                text = note.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            // Muestra dos iconos en una fila para eliminar y cambiar el estado de favorito de la nota
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // BotónIcono para eliminar la nota
                IconButton(onClick = { onDeleteNote(note.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
                // BotónIcono para cambiar el estado de favorito de la nota
                IconButton(onClick = { onBookmarkChange(note) }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevHome() {
    HomeScreen(
        state = HomeState(
            notes = ScreenViewState.Success(notes)
        ),
        onBookmarkChange = {},
        onDeleteNote = {},
        onNoteClicked = {}
    )
}

val placeHolderText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas porttitor nunc vel metus mollis suscipit. Phasellus nec eros id ex aliquam scelerisque. Phasellus quis feugiat eros. Nam sodales ante ac lorem convallis tempus. Sed lacinia consequat diam at ultrices. Nullam lacinia dignissim aliquam. Proin sit amet quam efficitur, euismod nunc eu, aliquam orci. Ut mattis orci a purus ultricies sodales. Pellentesque odio quam, aliquet nec accumsan et, pharetra et lacus. Pellentesque faucibus, dolor quis iaculis fringilla, ligula nisl imperdiet massa, vel volutpat velit elit ac magna. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus pharetra dolor nec magna condimentum volutpat. "

val notes = listOf(
    Note(
        title = "Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = Date()
    ),
    Note(
        title = "JetPack Compose",
        content = "Testing",
        createdDate = Date(),
        isBookMarked = true,

        ),
    Note(
        title = "Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = Date()
    ),
    Note(
        title = "JetPack Compose",
        content = placeHolderText,
        createdDate = Date(),
        isBookMarked = true,

        ),
    Note(
        title = "Room Database",
        content = placeHolderText,
        createdDate = Date()
    ),
    Note(
        title = "JetPack Compose",
        content = placeHolderText + placeHolderText,
        createdDate = Date(),
        isBookMarked = true,
    ),
)





