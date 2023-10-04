package com.juanyahirdr.proyectofinal

import android.os.Build
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanyahirdr.proyectofinal.ui.theme.ProyectoFinalTheme
import org.w3c.dom.Text
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    val notes : List<String> = listOf(
        "Nota 1",
        "Nota 2",
        "Nota 3",
        "Nota 4",
        "Nota 5",
        "Nota 6",
        "Nota 7",
        "Nota 8",
        "Nota 9",
        "Nota 10",
        "Nota 11",
        "Nota 12",
    )
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoFinalTheme{
                pantalla(notes)
            }
        }
    }
}
//################################ Pantalla: Crear Notas ################################
//PROTOTIPO CREAR NOTA
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable //INDICA QUE ES UN ELEMENTO GRAFICO
fun screenCreateNote(){
    //VARIABLES:
    var titulo by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    val date by remember { mutableStateOf(Calendar.getInstance().time) }
    val formatDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val finalFormarDate = formatDate.format(date)

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text= "Titulo:")
        TextField(value = titulo, onValueChange = { titulo=it})
        Spacer(modifier = Modifier.height(16.dp))
        Text(text= "Cuerpo:")
        TextField(value = body, onValueChange = { body=it})
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Fecha:")
        Text(""+finalFormarDate)
        buttuns()

    }
}

@Composable
fun buttuns() {
    Row( modifier = Modifier.padding(10.dp)) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Guardar")
        }
        Spacer(modifier = Modifier.width(10.dp))//añade una separacion
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Cancelar")
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Eliminar")
        }
    }
}

//################################ Pantalla: INICIO (Aqui se deben ver las notas creadas) ################################
@Composable
fun pantalla(notes : List<String>){
    //Se usa para mostrar solo los objetos en pantalla
    LazyColumn(
         contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)

    ){
        items(notes){
            item ->  ListItemRow(item)
        }
    }
}
@Composable
fun ListItemRow(item: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 16.dp, vertical = 18.dp)

    ) {
        Row {
            Text(item,modifier = Modifier.weight(1f).fillMaxWidth(),
                style =  MaterialTheme.typography.bodySmall
            ) //CONTIEN LOS VALORES DE CADA ELEMENTO DE LA LISTA
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Editar")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun pantallaInicioPreview(){
    val notes : List<String> = listOf(
        "Nota 1",
        "Nota 2",
        "Nota 3",
        "Nota 4",
        "Nota 5",
        "Nota 6",
        "Nota 7",
        "Nota 8",
        "Nota 9",
        "Nota 10",
        "Nota 11",
        "Nota 12",
    )
    ProyectoFinalTheme(darkTheme = true){
        pantalla(notes)
    }
}
//################################################################
//La preview sirve para ver el estado de la app sin tener que iniciar el emulador
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewText(){
    //INIDICAR QUE SE QUIERE PREVISUALIZAR
    //screenCreateNote()
}
