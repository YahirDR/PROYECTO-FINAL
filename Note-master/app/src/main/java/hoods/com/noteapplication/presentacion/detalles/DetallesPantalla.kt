package hoods.com.noteapplication.presentacion.detalles

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.w3c.dom.Text

@Composable
fun DetallesPantalla(
    //Parametros
    modifier: Modifier = Modifier,
    noteId:Long,
    asistenteF: DetallesAsistidosF,
    navegacionUp: () ->Unit
    ){
    val verModelo = viewModel(
        modelClass = DetallesPantallaView ::class.java,
        //El factory nos ayudar a contruir el modelClass
        factory = detallasDePantallaF(
            notaId =noteId,
            ayudaF = asistenteF
        )
    )
    // Obtener el estado desde el ViewModel
    val estado = verModelo.estado
    // llamamos a la función "detallePatalla" para crear la pantalla de detalles
    detallePatalla(
        modifier = modifier,
        actualizandoNota = estado.isActualizandoNota,
        isFormBlanco = estado.isActualizandoNota,
        title = estado.title,
        content = estado.content,
        isBookMarker = estado.isBookMarker,
        notanMarcadaCambia = verModelo::notamMrcadaCambiar,
        contentCambia = verModelo ::contenidoCambiar,
        titleCambia = verModelo::tituloCambiar,
        botonClick = {
            // Al hacer clic en el botón, se llama a la función en el ViewModel y luego se navega hacia atrás
            verModelo.agregarActualizadorNota()
            navegacionUp()
        },
        navegacion = navegacionUp
    )
}

@Composable
private fun detallePatalla(
    //Parametros
    modifier: Modifier,
    actualizandoNota: Boolean,
    title: String,
    content:String,
    isBookMarker: Boolean,
    notanMarcadaCambia:(Boolean) ->Unit,
    isFormBlanco: Boolean,
    titleCambia:(String) ->Unit,
    contentCambia:(String) ->Unit,
    botonClick:() ->Unit,
    navegacion:()->Unit
){
    // Creamos una columna
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Llamar a la función "seccionTop" para crear la sección superior de la pantalla de nuestra app
        seccionTop(
            title = title,
            isBookMarker = isBookMarker,
            notanMarcadaCambia = notanMarcadaCambia,
            titleCambia =titleCambia,
            navegacion = navegacion
        )
        Spacer(modifier = Modifier.Companion.size(12.dp))
        // Mostrar el botón de actualización solo si el formulario está en blanco
        AnimatedVisibility(isFormBlanco) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Cambiar el icono del botón dependiendo del estado de actualización
                IconButton(onClick = botonClick) {
                    val icono = if(actualizandoNota) Icons.Default.Update
                        else  Icons.Default.Check
                    Icon(
                        imageVector = icono,
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.Companion.size(12.dp))
        // Llamar a la función "notasTextField" para mostrar un campo de texto para el contenido
        notasTextField(
            modifier = Modifier.weight(1f),
            value = content,
            label = "contenido",
            valueCambio = contentCambia
        )
    }
}

@Composable
fun seccionTop(
    //Parametros
    modifier: Modifier = Modifier,
    title: String,
    isBookMarker: Boolean,
    notanMarcadaCambia:(Boolean) ->Unit,
    titleCambia:(String) ->Unit,
    navegacion:()->Unit
){
    // Se crea una fila que contiene elementos de la sección superior
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón para navegar hacia atrás
        IconButton(onClick = navegacion) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )

        }
        notasTextField(
            modifier = Modifier.weight(1f),
            value = title,
            label = "title",
            labelAling = TextAlign.Center,
            valueCambio = titleCambia
        )
        // Botón para marcar o desmarcar la nota
        IconButton(onClick = {notanMarcadaCambia(!isBookMarker)}) {
            val icono = if (isBookMarker) Icons.Default.BookmarkRemove
                else Icons.Outlined.BookmarkAdd
            Icon(
                imageVector = icono,
                contentDescription = null
            )

        }
    }
}
@Composable
private  fun notasTextField(
    //Parametros
    modifier: Modifier,
    value: String,
    label:String,
    labelAling: TextAlign? = null,
    valueCambio: (String) ->Unit,
){
    // Se crea un campo de texto con estilo contorneado
    OutlinedTextField(
        value = value,
        onValueChange = valueCambio,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            focusedContainerColor =  Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = "Insert $label",
                textAlign = labelAling,
                modifier = modifier.fillMaxWidth()
            )
        }
    )
}