package hoods.com.noteapplication.presentation.bookmark

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
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.presentation.home.HomeScreen
import hoods.com.noteapplication.presentation.home.HomeState
import hoods.com.noteapplication.presentation.home.NoteCard
import hoods.com.noteapplication.presentation.home.notes

@Composable
fun BookmarkScreen(
    state: BookmarkState, // El estado actual de la pantalla de marcadores
    modifier: Modifier = Modifier, // Modificador para personalizar la apariencia
    onBookMarkChange: (note: Note) -> Unit, // Función para cambiar el estado de favorito de una nota
    onDelete: (Long) -> Unit, // Función para eliminar una nota
    onNoteClicked: (Long) -> Unit // Función para manejar la acción cuando se hace clic en una nota
) {
    // Usamos when para manejar los diferentes estados de vista de acuerdo al estado actual.
    when (state.notes) {
        // Si el estado es de carga, se muestra un indicador de progreso.
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }
        // Si el estado es de éxito, se obtienen las notas marcadas como favoritas y se muestran en una lista.
        is ScreenViewState.Success -> {
            val notes = state.notes.data // extraen las notas del estado actual
            LazyColumn( //usamos LazyColumn para mostrar la lista de notas.
                contentPadding = PaddingValues(4.dp),
                modifier = modifier,
            ) {
                //se utiliza la función itemsIndexed para iterar a través de la lista de notes
                // y crear un componente NoteCard para cada nota
                itemsIndexed(notes) { index: Int, note: Note ->
                    NoteCard(
                        index = index,
                        note = note,
                        onBookmarkChange = onBookMarkChange,
                        onDeleteNote = onDelete,
                        onNoteClicked = onNoteClicked
                    )
                }
            }
        }
        // Si el estado es un error, se muestra un mensaje de error o un mensaje predeterminado.
        is ScreenViewState.Error -> {
            Text(
                text = state.notes.message ?: "Error Desconocido",
                color = MaterialTheme.colorScheme.error
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PrevBookMark() {
    BookmarkScreen(
        state = BookmarkState(
            notes = ScreenViewState.Success(notes)
        ),
        onBookMarkChange = {},
        onDelete = {},
        onNoteClicked = {}
    )
}















