package com.example.proyectofinal_jma

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal_jma.data.NotaEntity
import com.example.proyectofinal_jma.navigation.AppNavigation
import com.example.proyectofinal_jma.navigation.AppScreens
import com.example.proyectofinal_jma.sizeScreen.WindowInfo
import com.example.proyectofinal_jma.sizeScreen.rememberWindowInfo
import com.example.proyectofinal_jma.ui.theme.ProyectoFinal_JMATheme
import com.example.proyectofinal_jma.ui.theme.Shapes
import com.example.proyectofinal_jma.viewModel.AppViewModelProvider
import com.example.proyectofinal_jma.viewModel.HomeViewModel
import com.example.proyectofinal_jma.viewModel.NoteEntryViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var instancia: MainActivity // Variable global que almacena la instancia de la primera activity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instancia=this
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.RECORD_AUDIO),0)
        setContent {
            ProyectoFinal_JMATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                    obtenerCacheDir(cacheDir)
                }
            }
        }
    }

    fun cambiarIdioma(idioma:String){
        val recursos=resources
        val displayMetrics=resources.displayMetrics
        val configuracion=resources.configuration
        configuracion.setLocale(Locale(idioma))
        recursos.updateConfiguration(configuracion,displayMetrics)
        configuracion.locale= Locale(idioma)
        resources.updateConfiguration(configuracion,displayMetrics)
    }
}
var notaDrop:NotaEntity= NotaEntity(0,"","","")

@Composable
fun HomeworkCard(
    homework: NotaEntity,
    modifierEdit: Modifier= Modifier,
    viewModelHome: HomeViewModel,
    nota:NotaEntity,
    modifier: Modifier=Modifier
){
    Card (
        modifier = modifier
            .padding(
            top=dimensionResource(id = R.dimen.padding_4),
            end=dimensionResource(id = R.dimen.padding_4),
            start = dimensionResource(id = R.dimen.padding_4)
            )
    ){
        Row (
            modifier= modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_4))
                .padding(end = dimensionResource(id = R.dimen.padding_4))
                .sizeIn(minHeight = dimensionResource(id = R.dimen.padding_anchor_24))
        ){
            Box(
               modifier= modifierEdit
                   .align(CenterVertically)
            ){
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription =null,
                    modifier = modifier
                        .width(
                            width = 60.dp
                        )
                        .padding(end = 2.dp)
                        .aspectRatio(1f),
                    tint =MaterialTheme.colorScheme.primary)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row{
                    Text(
                        text = homework.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier= modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.padding_8)
                            )
                            .weight(.74f))
                    Spacer(Modifier.weight(.01f))
                    Text(
                        text = homework.fecha,
                        style = MaterialTheme.typography.bodySmall,
                        modifier=modifier.weight(.25f))
                }
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = modifier.padding(0.dp)
                ){
                    Text(
                        text = homework.contenido,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier= modifier
                            .weight(.9f)
                            .padding(end = 3.dp))
                        Button(
                            onClick = {
                                notaDrop=nota
                                viewModelHome.updateShow(true)
                            },
                            colors =  ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            modifier= modifier
                                .weight(.1f)
                                .align(Alignment.Top)
                                .offset(y = -5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.trash),
                                contentDescription =null,
                                modifier = modifier
                                    .size(
                                        width = dimensionResource(id = R.dimen.padding_anchor_36),
                                        height = dimensionResource(id = R.dimen.padding_anchor_36)
                                    )
                                    .aspectRatio(1f)
                                    .align(Alignment.Top)
                                    .offset(y = -5.dp),
                                tint = MaterialTheme.colorScheme.primary)

                        }
                }
            }

        }
    }

}

@Composable
fun ListElements(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    notaList: List<NotaEntity>,
    onNoteClick: (NotaEntity) -> Unit,
    viewModelHome: HomeViewModel
) {
    val windowsSize= rememberWindowInfo()
    val coroutineScope = rememberCoroutineScope()
    val message= LocalContext.current.applicationContext
    MyDialog(
        show = viewModelHome.show ,
        onDismiss = { viewModelHome.updateShow(false)},
        onConfirm = {
            coroutineScope.launch {
                viewModelHome.deleteNote(notaDrop)
                Toast.makeText(message,"Nota eliminada", Toast.LENGTH_SHORT).show()
            }
            viewModelHome.updateShow(false)
        },
        titulo = stringResource(id = R.string.eliminarNota),
        text = stringResource(id = R.string.preguntaeliminar))
    if(windowsSize.screenWindthInfo is WindowInfo.WindowType.Compact){
        LazyColumn(
            contentPadding=contentPadding,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_4))){
            items(items = notaList, key = { it.id }){ nota->
                HomeworkCard(
                    homework = nota,
                    modifierEdit = Modifier.clickable {
                        onNoteClick(nota)
                    },
                    viewModelHome,
                    nota
                )
            }
        }
    }else if (windowsSize.screenWindthInfo is WindowInfo.WindowType.Medium){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding=contentPadding,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_8)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_8)),
            content = {
                itemsIndexed(items = notaList, key = { index,nota-> nota.id}){ index,nota->
                    HomeworkCard(
                        homework = nota,
                        modifierEdit = Modifier.clickable {
                            onNoteClick(nota)
                        },
                        viewModelHome,
                        nota
                    )
                }
            }
        )
    }else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding=contentPadding,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_8)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_8)),
            content = {
                itemsIndexed(items = notaList, key = { index,nota-> nota.id}){ index,nota->
                    HomeworkCard(
                        homework = nota,
                        modifierEdit = Modifier.clickable {
                            onNoteClick(nota)
                        },
                        viewModelHome,
                        nota
                    )
                }
            }
        )
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    modifier: Modifier= Modifier,
    navController: NavController,
    viewModel: NoteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelHome: HomeViewModel= viewModel(factory = AppViewModelProvider.Factory),
    navigateToItemUpdate: (NotaEntity) -> Unit,
) {
    val homeUiState by viewModelHome.homeUiState.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.padding_4)),
        topBar = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = modifier.padding(end = 8.dp, start = 8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = { /*TODO*/ },
                    modifier = modifier
                        .height(50.dp)
                        .width(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
                TextField(
                    value = viewModel.textSearch,
                    onValueChange = {viewModel.updateTextSearch(it)},
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .height(50.dp)
                        .fillMaxWidth(),
                    trailingIcon = { Icon(
                        painter = painterResource(id = R.drawable.search) ,
                        contentDescription = null)},
                    shape = Shapes.large,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium)

            }
        },
        bottomBar = {
            Row (
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Card (
                    modifier = modifier
                        .padding(
                            bottom = dimensionResource(id = R.dimen.padding_anchor_16)
                        )
                        .clip(Shapes.small)
                        .width(250.dp)
                        .align(CenterVertically),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
                ){
                    Row (
                        modifier = modifier
                            .padding(dimensionResource(id = R.dimen.padding_4))
                            .fillMaxWidth()
                    ){
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .padding(
                                    end = dimensionResource(id = R.dimen.padding_8)
                                )
                                .weight(.33f)
                        ){
                            Button(
                                onClick = {
                                    navController.navigate(route = AppScreens.LanguageScreen.route)
                                },
                                modifier = modifier
                                    .height(40.dp)
                                    .width(60.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.translate),
                                    contentDescription =null,
                                    modifier = modifier
                                        .aspectRatio(1f),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.idioma),
                                style = MaterialTheme.typography.bodyMedium)
                        }
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .padding(
                                    start = dimensionResource(id = R.dimen.padding_8),
                                    end = dimensionResource(id = R.dimen.padding_8)
                                )
                                .weight(.33f)
                        ){
                            Button(
                                onClick = {
                                    navController.navigate(route = AppScreens.AddScreen.route)
                                },
                                modifier = modifier
                                    .height(60.dp)
                                    .width(60.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = null,
                                    modifier = modifier
                                        .aspectRatio(1f)
                                )
                            }
                        }
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier.weight(.33f)
                        ){
                            Button(
                                onClick = {
                                    navController.navigate(route = AppScreens.MainScreen.route)
                                },
                                modifier = modifier
                                    .height(40.dp)
                                    .width(60.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.home),
                                    contentDescription =null,
                                    modifier = modifier
                                        .aspectRatio(1f),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.principal),
                                style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    ) {
        HomeBody(notaList = homeUiState.noteList, contentPadding = it, onNoteClick= navigateToItemUpdate, viewModelHome = viewModelHome)

    }
}

@Composable
private fun HomeBody(
    notaList: List<NotaEntity>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onNoteClick: (NotaEntity) -> Unit,
    viewModelHome: HomeViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (notaList.isEmpty()) {
            LazyColumn(
                contentPadding=contentPadding,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ){
                item{
                    Text(
                        text = stringResource(R.string.sinNotas),
                        textAlign = TextAlign.Center,
                        modifier= modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        } else {
            ListElements(
                notaList = notaList,
                contentPadding = contentPadding,
                onNoteClick = { onNoteClick(it) },
                viewModelHome = viewModelHome
            )
        }
    }
}

@Composable
fun MyDialog(
    show:Boolean,
    onDismiss:()->Unit,
    onConfirm:()->Unit,
    titulo:String,
    text:String
){
    if(show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = {onConfirm() }) {
                    Text(text = stringResource(id = R.string.confirmar))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(id = R.string.cancelar))
                }
            },
            title = { Text(titulo) },
            text = { Text(text) }
        )
    }
}

@Composable
fun optionsAudio(
    show:Boolean,
    onDismiss:()->Unit,
    onConfirm:()->Unit,
    titulo:String,
    text:String
){
    if(show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = stringResource(id = R.string.grabar))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(id = R.string.parar))
                }
            },
            title = { Text(titulo) },
            text = { Text(text) }
        )
    }
}