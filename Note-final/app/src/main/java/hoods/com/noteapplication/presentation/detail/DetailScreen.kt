package hoods.com.noteapplication.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    noteId: Long,
    assistedFactory: DetailAssistedFactory,
    navigateUp: () -> Unit,
) {
    // Creamos una instancia del ViewModel utilizando Dagger Hilt y la fábrica asistida.
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailedViewModelFactory(
            noteId = noteId,
            assistedFactory = assistedFactory
        )
    )

    val state = viewModel.state
    // Llamamos a la función DetailScreen pasando los datos del ViewModel.
    DetailScreen(
        modifier = modifier,
        isUpdatingNote = state.isUpdatingNote,
        isFormNotBlank = viewModel.isFormNotBlank,
        title = state.title,
        content = state.content,
        isBookMark = state.isBookmark,
        onBookMarkChange = viewModel::onBookMarkChange,
        onContentChange = viewModel::onContentChange,
        onTitleChange = viewModel::onTitleChange,
        onBtnClick = {
            // Cuando se hace clic en el botón, se llama a la función addOrUpdateNote del ViewModel
            // para crear o actualizar la nota y luego se navega hacia atrás.
            viewModel.addOrUpdateNote()
            navigateUp()
        },
        onNavigate = navigateUp
    )
}
//Contenido
@Composable
private fun DetailScreen(
    modifier: Modifier,
    isUpdatingNote: Boolean,       // Indica si se está editando una nota existente.
    title: String,                 // El título de la nota.
    content: String,               // El contenido de la nota.
    isBookMark: Boolean,           // Indica si la nota está marcada como favorita.
    onBookMarkChange: (Boolean) -> Unit, // Callback para cambiar el estado de favorito de la nota.
    isFormNotBlank: Boolean,       // Indica si los campos del formulario no están en blanco.
    onTitleChange: (String) -> Unit,   // Callback para cambiar el título de la nota.
    onContentChange: (String) -> Unit, // Callback para cambiar el contenido de la nota.
    onBtnClick: () -> Unit,        // Callback para manejar el clic en el botón (Guardar o Actualizar).
    onNavigate: () -> Unit,        // Callback para navegar hacia atrás.
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Muestra la parte superior de la pantalla, que incluye el título y la opción de marcador.
        TopSection(
            title = title,
            isBookMark = isBookMark,
            onBookmarkChange = onBookMarkChange,
            onTitleChange = onTitleChange,
            onNavigate = onNavigate
        )
        Spacer(modifier = Modifier.size(12.dp))
        // Si el formulario no está en blanco, se muestra el botón.
        AnimatedVisibility (isFormNotBlank) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Muestra un IconButton para guardar o actualizar la nota, dependiendo del estado.
                IconButton(onClick = onBtnClick) {
                    val icon = if (isUpdatingNote) Icons.Default.Update
                    else Icons.Default.Check
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }
        Spacer(modifier = Modifier.size(12.dp))
        // Permite al usuario editar el contenido de la nota en un campo de texto.
        NotesTextField(
            modifier = Modifier.weight(1f),
            value = content,
            label = "Contenido",
            onValueChange = onContentChange
        )

    }
}


@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    title: String,                 // El título de la nota.
    isBookMark: Boolean,           // Indica si la nota está marcada como favorita.
    onBookmarkChange: (Boolean) -> Unit, // Callback para cambiar el estado de favorito de la nota.
    onTitleChange: (String) -> Unit,   // Callback para cambiar el título de la nota.
    onNavigate: () -> Unit,        // Callback para navegar hacia atrás.
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // IconButton de navegación hacia atrás.
        IconButton(onClick = onNavigate) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )

        }
        // Campo de texto para editar el título de la nota.
        NotesTextField(
            modifier = Modifier.weight(1f),
            value = title,
            label = "Titulo",
            labelAlign = TextAlign.Center,
            onValueChange = onTitleChange
        )
        // IconButton para cambiar el estado de favorito de la nota.
        IconButton(onClick = { onBookmarkChange(!isBookMark) }) {
            val icon = if (isBookMark) Icons.Default.BookmarkRemove
            else Icons.Outlined.BookmarkAdd
            Icon(
                imageVector = icon,
                contentDescription = null
            )

        }
    }
}
//CREAR NOTA
//campo de texto estilizado que se usa para capturar la entrada de texto en la aplicación.
@Composable
private fun NotesTextField(
    modifier: Modifier,
    value: String,                // El valor actual del campo de texto (el texto).
    onValueChange: (String) -> Unit, // Callback que se invoca cuando cambia el valor del campo de texto.
    label: String,                // Etiqueta que describe el propósito del campo de texto.
    labelAlign: TextAlign? = null  // La alineación del texto de la etiqueta (opcional).
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = "Agregar texto $label",
                textAlign = labelAlign,
                modifier = modifier.fillMaxWidth()
            )
        }
    )
}















