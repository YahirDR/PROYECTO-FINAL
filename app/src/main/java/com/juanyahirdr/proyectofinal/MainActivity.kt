package com.juanyahirdr.proyectofinal

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanyahirdr.proyectofinal.ui.theme.ProyectoFinalTheme
import org.w3c.dom.Text
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoFinalTheme{
                createNote()
            }
        }
    }
}
//VERSION DE PUEBRA
@OptIn(ExperimentalMaterial3Api::class)
@Composable //INDICA QUE ES UN ELEMENTO GRAFICO
fun createNote(){
    //VARIABLES:
    var titulo by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }


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

//La preview sirve para ver el estado de la app sin tener que iniciar el emulador
@Preview
@Composable
fun PreviewText(){
    //INIDICAR QUE SE QUIERE PREVISUALIZAR
    createNote()
}
