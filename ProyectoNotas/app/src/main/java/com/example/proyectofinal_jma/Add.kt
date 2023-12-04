package com.example.proyectofinal_jma

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal_jma.Notification.NotificacionProgramada
import com.example.proyectofinal_jma.Notification.NotificacionProgramada.Companion.NOTIFICACION_ID
import com.example.proyectofinal_jma.data.DataSourceNotesOrHomework
import com.example.proyectofinal_jma.navigation.AppScreens
import com.example.proyectofinal_jma.playback.AndroidAudioPlayer
import com.example.proyectofinal_jma.record.AndroidAudioRecorder
import com.example.proyectofinal_jma.sizeScreen.WindowInfo
import com.example.proyectofinal_jma.sizeScreen.rememberWindowInfo
import com.example.proyectofinal_jma.ui.theme.Shapes
import com.example.proyectofinal_jma.viewModel.AppViewModelProvider
import com.example.proyectofinal_jma.viewModel.NoteDetails
import com.example.proyectofinal_jma.viewModel.NoteEntryViewModel
import com.example.proyectofinal_jma.viewModel.NoteUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar


@Composable
fun Add(
    contentPadding: PaddingValues,
    viewModel: NoteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val context= LocalContext.current
    val audioRecorder = AndroidAudioRecorder(context)
    optionsAudio(
        show =  viewModel.showOptionsAudio ,
        onDismiss = {
            audioRecorder.stop()
            viewModel.updateShowOptionsAudio(false)
        },
        onConfirm = {
            viewModel.updateFileNumb(viewModel.fileNumb+1)
            Log.d("filename",""+viewModel.fileNumb)
            audioRecorder.start(File("dummy"),viewModel.fileNumb)
            viewModel.updateHasAudio(true)
            viewModel.updateUrisAudiosList(audioRecorder.getContentUri())
        },
        titulo = stringResource(id = R.string.tituloAudio),
        text = stringResource(id = R.string.contenidoAudio)
    )
    LazyColumn(
        contentPadding=contentPadding,
        modifier = Modifier.padding(top = 10.dp, start = 8.dp, end = 8.dp )){
        items(DataSourceNotesOrHomework.text){
            NoteEntryBody(
                noteUiState = viewModel.noteUiState,
                onNoteValueChange = viewModel::updateUiState,
                viewModelPhoto=viewModel)
        }
    }
}

@Composable
fun NoteEntryBody(
    noteUiState: NoteUiState,
    onNoteValueChange: (NoteDetails) -> Unit,
    viewModelPhoto: NoteEntryViewModel
) {
    NoteInputForm(
        noteDetails = noteUiState.noteDetails,
        onValueChange =  onNoteValueChange,
        viewModel = viewModelPhoto)
}
//CUERPO
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputForm(
    noteDetails: NoteDetails,
    onValueChange: (NoteDetails) -> Unit = {},
    viewModel: NoteEntryViewModel
) {
    TextField(
        value = noteDetails.contenido,
        onValueChange = {
            onValueChange(noteDetails.copy(contenido = it))
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent),
        singleLine= false,
        textStyle = MaterialTheme.typography.bodyMedium)
    Text("MULTIMEDIA:")
    viewImages(viewModel = viewModel)
    viewVideos(viewModel = viewModel)
    viewAudios(viewModel = viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteHomework(
    modifier:  Modifier= Modifier,
    navController: NavController,
    viewModel: NoteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val windowsSize= rememberWindowInfo()
    viewModel.updateOptionNote(stringResource(id = R.string.nota))
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.padding_4)),
        topBar = {
            if(windowsSize.screenWindthInfo is WindowInfo.WindowType.Compact ){
                TopNoteEstructureCompact(modifier = modifier, navController = navController, viewModel = viewModel)
            }else if(windowsSize.screenWindthInfo is WindowInfo.WindowType.Medium){
                TopNoteEstructureMedium(modifier = modifier, navController = navController, viewModel = viewModel)
            }else{
                TopNoteEstructureExpanded(modifier = modifier, navController = navController, viewModel = viewModel)
            }
        },
        bottomBar = {

            Row (
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){

                Column ( modifier =modifier.weight(.25f)){
                ImageCapture(viewModel = viewModel, modifier = modifier)

                }
                Column ( modifier =modifier.weight(.25f)){
                    VideoCapture(viewModel = viewModel, modifier = modifier)
                }
                Column ( modifier =modifier.weight(.25f)){
                    AudioCapture(viewModel = viewModel, modifier = modifier)
                }
                Column ( modifier =modifier.weight(.25f)){
                    Recordatorio(viewModel = viewModel, modifier = modifier)
                }
            }


        }
    ) {
        Add(contentPadding = it,viewModel)
    }
}

@Composable
fun TitleNoteEntryBody(
    noteUiState: NoteUiState,
    onNoteValueChange: (NoteDetails) -> Unit
) {
    TitleTextNote(
        noteDetails = noteUiState.noteDetails,
        onValueChange =  onNoteValueChange)
}
//TITULO DE LA NOTA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextNote(
    noteDetails: NoteDetails,
    onValueChange: (NoteDetails) -> Unit = {}
) {
    TextField(
        value = noteDetails.titulo,
        onValueChange = {
            onValueChange(noteDetails.copy(titulo = it))
        },
        modifier = Modifier
            .padding(top = 11.dp)
            .fillMaxWidth(),
        shape = Shapes.extraLarge,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = false,
        label={ Text(text = stringResource(id = R.string.titulo)) },
        textStyle = MaterialTheme.typography.bodyMedium)
}
//ESTE ES EL BUENO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNoteEstructureCompact(
    modifier: Modifier,
    navController: NavController,
    viewModel: NoteEntryViewModel
){
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center
        ){
            Column{
                Button(
                    onClick = {navController.navigate(route = AppScreens.MainScreen.route) },
                    modifier = modifier
                        .height(55.dp)
                        .width(55.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary)

                }
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column(
                modifier =modifier.weight(.5f)
            ){
                TitleNoteEntryBody(noteUiState = viewModel.noteUiState, onNoteValueChange = viewModel::updateUiState)
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            //ACEPTAR
            Column{
                val message= LocalContext.current.applicationContext
                //BOTON LISTO
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveNote()
                            navController.navigate(route = AppScreens.MainScreen.route)
                            Toast.makeText(message,"Nota agregada",Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = modifier
                        .height(55.dp)
                        .width(55.dp),
                    contentPadding = PaddingValues(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary)
                }

            }
        }
        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_4)))
        Row(modifier = modifier
            .height(60.dp)
            .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier =modifier.weight(.6f)
            ) {

            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column(

            ) {
                ExposedDropdownMenuBox(
                    modifier = modifier
                        .padding(top = 4.dp, end = 8.dp)
                        .fillMaxWidth(),
                    expanded = viewModel.isExpanded,
                    onExpandedChange = {viewModel.updateIsExpanded(it)}
                ) {
                    TextField(
                        value = viewModel.optionNote,
                        onValueChange = {},
                        readOnly = true,
                        label ={ Text(stringResource(id = R.string.tipo))},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = viewModel.isExpanded,
                        onDismissRequest = {viewModel.isExpanded=false}
                    ) {
                        var text=stringResource(id = R.string.nota)
                        var text2=stringResource(id = R.string.tarea)
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = {
                                viewModel.updateOptionNote(text)
                                viewModel.isExpanded=false
                                viewModel.updateRecordatorios(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text2) },
                            onClick = {
                                viewModel.updateOptionNote(text2)
                                viewModel.isExpanded=false
                                viewModel.updateRecordatorios(true)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Muy importante") },
                            onClick = {
                                viewModel.updateOptionNote(text2)
                                viewModel.isExpanded=false
                                viewModel.updateRecordatorios(true)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNoteEstructureMedium(
    modifier: Modifier,
    navController: NavController,
    viewModel: NoteEntryViewModel,
){
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center
        ){
            Column{
                val message= LocalContext.current.applicationContext
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveNote()
                            navController.navigate(route = AppScreens.MainScreen.route)
                            Toast.makeText(message,"Nota agregada",Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = modifier
                        .height(55.dp)
                        .width(55.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column(
                modifier =modifier.weight(.5f)
            ){
                TitleNoteEntryBody(noteUiState = viewModel.noteUiState, onNoteValueChange = viewModel::updateUiState)
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column{
                Button(
                    onClick = { viewModel.updateShowCancel(true)  },
                    modifier = modifier
                        .height(55.dp)
                        .width(55.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary)
                    MyDialog(
                        show = viewModel.showCancel,
                        onDismiss = { viewModel.updateShowCancel(false) },
                        onConfirm = {
                            navController.navigate(route = AppScreens.MainScreen.route)
                        },
                        titulo = stringResource(id = R.string.cancelarRegistro),
                        text = stringResource(id = R.string.cancelarRegistrotext)
                    )
                }
            }
        }
        //segunda fila
        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_4)))
        Row(modifier = modifier
            .height(60.dp)
            .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier =modifier.weight(.65f)
            ) {

            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column(
                modifier =modifier.weight(.35f)
            ) {
                ExposedDropdownMenuBox(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    expanded = viewModel.isExpanded,
                    onExpandedChange = {viewModel.updateIsExpanded(it)}
                ) {
                    TextField(
                        value = viewModel.optionNote,
                        onValueChange = {},
                        readOnly = true,
                        label ={ Text(stringResource(id = R.string.tipo))},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = viewModel.isExpanded,
                        onDismissRequest = {viewModel.isExpanded=false}
                    ) {
                        var text=stringResource(id = R.string.nota)
                        var text2=stringResource(id = R.string.tarea)
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = {
                                viewModel.updateOptionNote(text)
                                viewModel.isExpanded=false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text2) },
                            onClick = {
                                viewModel.updateOptionNote(text2)
                                viewModel.isExpanded=false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_8)))
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNoteEstructureExpanded(
    modifier: Modifier,
    navController: NavController,
    viewModel: NoteEntryViewModel
    ){
    val coroutineScope=rememberCoroutineScope()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center
        ){
            Column{
                val message= LocalContext.current.applicationContext
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveNote()
                            navController.navigate(route = AppScreens.MainScreen.route)
                            Toast.makeText(message,"Nota agregada",Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = modifier
                        .height(55.dp)
                        .width(55.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
            Column(
                modifier =modifier.weight(.45f)
            ) {
                TitleNoteEntryBody(noteUiState = viewModel.noteUiState, onNoteValueChange = viewModel::updateUiState)
            }
            Column (
                modifier =modifier.weight(.25f),
                verticalArrangement = Arrangement.Center
            ){
                Row{
                    Row ( modifier =modifier.weight(.25f)){
                        ImageCapture(viewModel = viewModel, modifier = modifier)
                    }
                    Row ( modifier =modifier.weight(.25f)){
                        VideoCapture(viewModel = viewModel, modifier = modifier)
                    }
                    Row ( modifier =modifier.weight(.25f)){
                        AudioCapture(viewModel = viewModel, modifier = modifier)
                    }
                    Row ( modifier =modifier.weight(.25f)){
                        Recordatorio(viewModel = viewModel, modifier = modifier)
                    }
                }
            }
            Column(
                modifier =modifier.weight(.14f)
            ) {
                ExposedDropdownMenuBox(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    expanded = viewModel.isExpanded,
                    onExpandedChange = {viewModel.updateIsExpanded(it)}
                ) {
                    TextField(
                        value = viewModel.optionNote,
                        onValueChange = {},
                        readOnly = true,
                        label ={ Text(stringResource(id = R.string.tipo))},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = viewModel.isExpanded,
                        onDismissRequest = {viewModel.isExpanded=false}
                    ) {
                        var text=stringResource(id = R.string.nota)
                        var text2=stringResource(id = R.string.tarea)
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = {
                                viewModel.updateOptionNote(text)
                                viewModel.isExpanded=false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text2) },
                            onClick = {
                                viewModel.updateOptionNote(text2)
                                viewModel.isExpanded=false
                            }
                        )
                    }
                }
            }
            Column {
                Button(
                    onClick = { viewModel.updateShowCancel(true)  },
                    modifier = modifier
                        .height(55.dp)
                        .width(55.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription =null,
                        modifier = modifier
                            .aspectRatio(1f),
                        tint = MaterialTheme.colorScheme.primary)
                    MyDialog(
                        show = viewModel.showCancel,
                        onDismiss = { viewModel.updateShowCancel(false) },
                        onConfirm = {
                            navController.navigate(route = AppScreens.MainScreen.route)
                        },
                        titulo = stringResource(id = R.string.cancelarRegistro),
                        text = stringResource(id = R.string.cancelarRegistrotext)
                    )
                }
            }
        }
}
//TOMAR FOTO
@Composable
fun ImageCapture(
    viewModel: NoteEntryViewModel,
    modifier:Modifier
){
    val context = LocalContext.current
    var uri=ComposeFileProvider.getImageUri(context)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if(success){
                viewModel.updatehasImage(success)
                viewModel.updateUrisList(uri)
            }
        }
    )
    Button(
        onClick = {
            uri = ComposeFileProvider.getImageUri(context)
            cameraLauncher.launch(uri)
        },

        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
    ){
        Icon(
            Icons.Default.Face,
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
//TOMAR VIDEO
@Composable
fun VideoCapture(
    viewModel: NoteEntryViewModel,
    modifier:Modifier
){
    val context = LocalContext.current
    var uri=ComposeFileProvider.getVideoUri(context)
    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = { success ->
            viewModel.updatehasVideo(success)
            //viewModel.updateVideoUri(uri)
            viewModel.updateUrisVideosList(uri)
        }
    )
    Button(
        onClick = {
            uri = ComposeFileProvider.getVideoUri(context)
            videoLauncher.launch(uri)
        },

        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
    ){
        Icon(
            Icons.Outlined.PlayArrow,
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

//TOMAR AUDIO
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioCapture(
    viewModel: NoteEntryViewModel,
    modifier:Modifier
){
    Button(
        onClick = {
            viewModel.updateShowOptionsAudio(true)
        },

        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
    ){
        Icon(
            Icons.Default.Call,
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }

}

@Composable
fun filesCapture(
    viewModel: NoteEntryViewModel,
    modifier: Modifier
){
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.updatehasImage(uri != null)
            if(viewModel.hasImage){
                viewModel.updateUrisList(uri)
            }
        }
    )
    Button(
        onClick = {
            imagePicker.launch("image/*")
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.padding(end = 5.dp)
    ) {
        Icon(painter = painterResource(id = R.drawable.file), contentDescription ="" )
    }
}

@Composable
fun Recordatorio(
    viewModel: NoteEntryViewModel,
    modifier:Modifier
){
    Button(
        onClick = {
            viewModel.updateOptionsRecordatorios(true)
        },
        enabled=viewModel.recordatorios,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
    ){

            Icon(
                Icons.Rounded.DateRange,

                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.primary
            )

       // calendar(viewModel = viewModel)
        Reloj(viewModel = viewModel)
        opcionesRecordatorios(viewModel = viewModel)
    }
}

//MOSTRAR VIDEO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewImages(
    viewModel: NoteEntryViewModel,
    modifier:Modifier=Modifier
) {
    Row(
        modifier= modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column {
            Row(
                modifier=modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row (
                    modifier=modifier.weight(.7f),
                    horizontalArrangement = Arrangement.End
                ){
                    filesCapture(viewModel = viewModel, modifier = modifier)

                }
            }
            if (viewModel.hasImage){
                LazyRow(modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)) {
                    items(viewModel.urislist.toList()) { uri ->
                        Surface(
                            onClick = {
                                viewModel.updateMostrarImagen(true)
                                viewModel.updateUriMostrar(uri)
                            },
                            modifier = modifier
                                .size(width = 100.dp, height = 120.dp)
                        ){
                            AsyncImage(
                                model = uri,
                                modifier = Modifier.fillMaxWidth(),
                                contentDescription = "Selected image",
                            )
                        }
                    }
                }
                mostrarImagen(viewModel = viewModel, uri = viewModel.uriMostrar)
            }
        }
    }
}
//MOSTRAR VIDEOS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewVideos(
    viewModel: NoteEntryViewModel,
    modifier:Modifier=Modifier
) {
    Row(
        modifier= modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column {
            Row(
                modifier=modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row (
                    modifier=modifier.weight(.6f),
                    horizontalArrangement = Arrangement.End
                ){

                }
            }
            if (viewModel.hasVideo){
                LazyRow(modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)) {
                    items(viewModel.urisVideoslist.toList()) { uri ->
                        Surface(
                            onClick = {
                                viewModel.updateMostrarVideo(true)
                                viewModel.updateUriMostrar(uri)
                            },
                            modifier = modifier
                                .size(width = 120.dp, height = 120.dp)
                        ){
                            Icons.Default.PlayArrow
                        }
                    }
                }
                mostrarVideo(viewModel = viewModel, uri = viewModel.uriMostrar)
            }
        }
    }
}
//MOSTRA AUDIOS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewAudios(
    viewModel: NoteEntryViewModel,
    modifier:Modifier=Modifier
) {
    Row(
        modifier= modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column {
            Row(
                modifier=modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    modifier=modifier.weight(.6f),
                    horizontalArrangement = Arrangement.End
                ){

                }
            }
            if (viewModel.hasAudio){
                LazyRow(modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)) {
                    items(viewModel.urisAudiosList.toList()) { uri ->
                        Surface(
                            onClick = {
                                viewModel.updateMostrarAudio(true)
                                viewModel.updateUriMostrar(uri)
                            },
                            modifier = modifier
                                .size(width = 100.dp, height = 120.dp)
                        ){
                            Icon(painter = painterResource(id = R.drawable.audio_port), contentDescription = "")
                        }
                        Log.d("audiosPrueba",""+uri)
                    }
                }
                Reproducir(viewModel = viewModel, uri = viewModel.uriMostrar )
            }
        }
    }
}


@Composable
fun Reproducir(
    viewModel: NoteEntryViewModel,
    uri: Uri
){
    if(viewModel.mostrarAudio){
        val audioPlayer = AndroidAudioPlayer(LocalContext.current)
        AlertDialog(
            onDismissRequest = {
                viewModel.updateMostrarAudio(false)
                audioPlayer.stop()
            },
            confirmButton = {
                TextButton(onClick = {
                    audioPlayer.playFile(uri)
                }) {
                    Text(text = stringResource(id = R.string.reproducir))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    audioPlayer.stop()
                    viewModel.updateMostrarAudio(false)
                }) {
                    Text(text = stringResource(id = R.string.cancelar))
                }
            },
            title = { Text(stringResource(id = R.string.opciones)) }
        )

    }
}

@Composable
fun mostrarImagen(
    viewModel: NoteEntryViewModel,
    uri: Uri?
){
    if(viewModel.mostrarImagen){
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = { viewModel.updateMostrarImagen(false) }
        ) {
            AsyncImage(
                model = uri,
                modifier = Modifier.fillMaxSize(.9f),
                contentDescription = "Amplied image",
            )
        }
    }
}

@Composable
fun mostrarVideo(
    viewModel: NoteEntryViewModel,
    uri: Uri
){
    if(viewModel.mostrarVideo){
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = { viewModel.updateMostrarVideo(false) }
        ) {
            val context = LocalContext.current
            VideoPlayer(videoUri = uri, context)
        }
    }
}

@Composable
fun VideoPlayer(videoUri: Uri, context:Context) {
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
    }
    // Usar disposableEffect para liberar el exoPlayer cuando el composable se elimina del árbol de composición
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release() // Liberar el exoPlayer
        }
    }
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxWidth(.8f)
    )
}
//MOSTRAR
@Composable
fun opcionesRecordatorios(
    viewModel: NoteEntryViewModel,
){
    if(viewModel.showOptionRecordatorios){
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = {
                viewModel.updateOptionsRecordatorios(false)
                viewModel.updateCalcular(false)
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(.9f)
            ) {
                Column (
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ){
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Row(
                            modifier = Modifier
                                .weight(.5f)
                                .padding(2.dp)
                        ) {

                        }
                    }
                    Text(
                        text = stringResource(id = R.string.Recordatorio),
                        modifier = Modifier.padding(5.dp))
                    Text(
                        text = viewModel.hora,
                        modifier = Modifier.padding(5.dp))
                    Button(
                        onClick = {
                            viewModel.updateShowReloj(true)},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.definirHora))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateNotificaciones(true)
                                viewModel.updateCalcular(true)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.confirmar))
                        }
                        if(viewModel.calcular){
                            var time= LocalTime.now()
                            var time2=LocalTime.of(viewModel.hour,viewModel.minute,0)
                            val dif=time.until(time2, ChronoUnit.MILLIS)
                            Log.d("micros",""+dif)
                            if(dif>0){
                                Notificaciones(dif,viewModel)
                            }
                            viewModel.updateOptionsRecordatorios(false)
                            viewModel.updateCalcular(false)
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Reloj(
    viewModel: NoteEntryViewModel
){
    if(viewModel.showReloj){
        val state= rememberTimePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.updateShowReloj(false) },
            confirmButton = {
                Button(
                    onClick = { viewModel.updateShowReloj(false)  }
                ) {
                    Text(text = stringResource(id = R.string.confirmar))
                }
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.updateShowReloj(false)  }
                ) {
                    Text(text = stringResource(id = R.string.cancelar))
                }
            }
        ) {
            TimePicker(state = state, modifier = Modifier.fillMaxWidth())
        }
        val hour=state.hour
        val minut=state.minute
        hour?.let {
            minut?.let{
                viewModel.updateHora("$hour:$minut:00")
                viewModel.updateHour(hour)
                viewModel.updateMinute(minut)
            }
        }
    }
}

@Composable
fun Notificaciones(
    milisegundos: Long,
    viewModel: NoteEntryViewModel
){
    if(viewModel.notificacion){
        val context= LocalContext.current
        val idCanal= "CanalNotas"

        LaunchedEffect(Unit){
            crearCanalNotificacion(idCanal,context)
        }
        viewModel.updateIdNotificacion()
        notificacionProgramada(context,milisegundos,viewModel.idNotificacion)
    }
}

fun crearCanalNotificacion(
    idCanal: String,
    context: Context
){
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        val nombre= "CanalNotas"
        val descripcion="Canal de notificaciones notas"
        val importancia=NotificationManager.IMPORTANCE_DEFAULT
        val canal=NotificationChannel(idCanal,nombre,importancia)
            .apply {
                description=descripcion
            }
        val notificationManager:NotificationManager=
            context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        notificationManager.createNotificationChannel(canal)
    }
}

@SuppressLint("ScheduleExactAlarm")
fun notificacionProgramada(context: Context, milisegundos:Long,id:Int){
    val intent=Intent(context,NotificacionProgramada::class.java)
    Log.d("micros",""+ id)
    val pendingIntent=PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    var alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        Calendar.getInstance().timeInMillis+milisegundos,
        pendingIntent
    )
}

var cache: File? = null
fun obtenerCacheDir(cacheDir: File){
    cache = cacheDir
}
