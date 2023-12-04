package com.example.proyectofinal_jma

import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal_jma.data.DataSourceNotesOrHomework
import com.example.proyectofinal_jma.data.NotaEntity
import com.example.proyectofinal_jma.navigation.AppScreens
import com.example.proyectofinal_jma.navigation.NavigationDestination
import com.example.proyectofinal_jma.playback.AndroidAudioPlayer
import com.example.proyectofinal_jma.record.AndroidAudioRecorder
import com.example.proyectofinal_jma.sizeScreen.WindowInfo
import com.example.proyectofinal_jma.sizeScreen.rememberWindowInfo
import com.example.proyectofinal_jma.ui.theme.ProyectoFinal_JMATheme
import com.example.proyectofinal_jma.ui.theme.Shapes
import com.example.proyectofinal_jma.viewModel.AppViewModelProvider
import com.example.proyectofinal_jma.viewModel.NoteDetails
import com.example.proyectofinal_jma.viewModel.NoteDetailsEdit
import com.example.proyectofinal_jma.viewModel.NoteEditViewModel
import com.example.proyectofinal_jma.viewModel.NoteEntryViewModel
import com.example.proyectofinal_jma.viewModel.NoteUiState
import com.example.proyectofinal_jma.viewModel.NoteUiStateEdit
import com.example.proyectofinal_jma.viewModel.toNoteDetails
import com.example.proyectofinal_jma.viewModel.toNoteDetailsEdit
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object ItemEditDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.nota
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun Edit(
    contentPadding: PaddingValues,
    viewModel: NoteEditViewModel
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
        },
        titulo = stringResource(id = R.string.tituloAudio),
        text = stringResource(id = R.string.contenidoAudio)
    )
    LazyColumn(
        contentPadding=contentPadding,
        modifier = Modifier.padding(top = 10.dp, start = 8.dp, end = 8.dp )){
        items(DataSourceNotesOrHomework.text){
            NoteEditBody(
                noteUiState = viewModel.noteUiStateEdit,
                onNoteValueChange = viewModel::updateUiStateEdit,
                viewModel = viewModel)
        }
    }
}

@Composable
fun NoteEditBody(
    noteUiState: NoteUiStateEdit,
    onNoteValueChange: (NoteDetailsEdit) -> Unit,
    viewModel: NoteEditViewModel
) {
    NoteInputEditForm(
        noteDetails = noteUiState.noteDetails,
        onValueChange =  onNoteValueChange,
        viewModel = viewModel)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputEditForm(
    noteDetails: NoteDetailsEdit,
    onValueChange: (NoteDetailsEdit) -> Unit = {},
    viewModel: NoteEditViewModel
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
        viewImagesEdit(viewModel = viewModel)
        viewVideosEdit(viewModel = viewModel)
        viewAudiosEdit(viewModel = viewModel)
}

@Composable
fun TitleNoteEditBody(
    noteUiState: NoteUiStateEdit,
    onNoteValueChange: (NoteDetailsEdit) -> Unit
) {
    TitleTextNoteEdit(
        noteDetails = noteUiState.noteDetails,
        onValueChange =  onNoteValueChange)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextNoteEdit(
    noteDetails: NoteDetailsEdit,
    onValueChange: (NoteDetailsEdit) -> Unit = {}
) {
    TextField(
        value = noteDetails.titulo,
        onValueChange = {
            onValueChange(noteDetails.copy(titulo = it))
        },
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        shape = Shapes.large,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        label={ Text(text = stringResource(id = R.string.titulo)) },
        textStyle = MaterialTheme.typography.bodyMedium)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteHomework(
    modifier:  Modifier= Modifier,
    navController: NavController,
    viewModel: NoteEditViewModel= viewModel(factory = AppViewModelProvider.Factory)
) {
    viewModel.updateOptionNoteEdit(viewModel.noteUiStateEdit.noteDetails.tipo)
    val windowsSize= rememberWindowInfo()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.padding_4)),
        topBar = {
            if(windowsSize.screenWindthInfo is WindowInfo.WindowType.Compact ){
                TopNoteEstructureCompactEdit(modifier = modifier, navController = navController, viewModel = viewModel)
            }else if(windowsSize.screenWindthInfo is WindowInfo.WindowType.Medium){
                TopNoteEstructureMediumEdit(modifier = modifier, navController = navController, viewModel = viewModel)
            }else{
                TopNoteEstructureExpandedEdit(modifier = modifier, navController = navController, viewModel = viewModel)
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
                        .align(Alignment.CenterVertically),
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
        Edit(contentPadding = it, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNoteEstructureCompactEdit(
    modifier: Modifier,
    navController: NavController,
    viewModel: NoteEditViewModel
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
                            viewModel.updateNote()
                            navController.navigate(route = AppScreens.MainScreen.route)
                            Toast.makeText(message,"Nota actualizada", Toast.LENGTH_SHORT).show()
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
                TitleNoteEditBody(noteUiState = viewModel.noteUiStateEdit, onNoteValueChange = viewModel::updateUiStateEdit)
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column{
                Button(
                    onClick = { viewModel.updateShowCancelEdit(true)},
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
                        show = viewModel.showCancelEdit,
                        onDismiss = { viewModel.updateShowCancelEdit(false) },
                        onConfirm = {
                            navController.navigate(route = AppScreens.MainScreen.route)
                        },
                        titulo = stringResource(id = R.string.cancelarActualización),
                        text = stringResource(id = R.string.cancelarActualizaciontext)
                    )
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
                Row ( modifier =modifier.weight(.25f)){
                    ImageCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    VideoCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    AudioCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    RecordatorioEdit(viewModel = viewModel, modifier = modifier)
                }
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column(
                modifier =modifier.weight(.4f)
            ) {
                ExposedDropdownMenuBox(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    expanded = viewModel.isExpanded,
                    onExpandedChange = {viewModel.updateIsExpandedEdit(it)}
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
                                viewModel.updateOptionNoteEdit(text)
                                viewModel.isExpanded=false
                                viewModel.updateRecordatorios(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text2) },
                            onClick = {
                                viewModel.updateOptionNoteEdit(text2)
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
fun TopNoteEstructureMediumEdit(
    modifier: Modifier,
    navController: NavController,
    viewModel: NoteEditViewModel
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
                            viewModel.updateNote()
                            navController.navigate(route = AppScreens.MainScreen.route)
                            Toast.makeText(message,"Nota actualizada", Toast.LENGTH_SHORT).show()
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
                TitleNoteEditBody(noteUiState = viewModel.noteUiStateEdit, onNoteValueChange = viewModel::updateUiStateEdit)
            }
            Spacer(Modifier.width(dimensionResource(id = R.dimen.padding_2)))
            Column{
                Button(
                    onClick = { viewModel.updateShowCancelEdit(true) },
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
                        show = viewModel.showCancelEdit,
                        onDismiss = { viewModel.updateShowCancelEdit(false) },
                        onConfirm = {
                            navController.navigate(route = AppScreens.MainScreen.route)
                        },
                        titulo = stringResource(id = R.string.cancelarActualización),
                        text = stringResource(id = R.string.cancelarActualizaciontext)
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
                Row ( modifier =modifier.weight(.25f)){
                    ImageCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    VideoCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    AudioCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    RecordatorioEdit(viewModel = viewModel, modifier = modifier)
                }
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
                    onExpandedChange = {viewModel.updateIsExpandedEdit(it)}
                ) {
                    TextField(
                        value = viewModel.noteUiStateEdit.noteDetails.tipo,
                        onValueChange = {},
                        readOnly = true,
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
                                viewModel.updateOptionNoteEdit(text)
                                viewModel.isExpanded=false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text2) },
                            onClick = {
                                viewModel.updateOptionNoteEdit(text2)
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
fun TopNoteEstructureExpandedEdit(
    modifier: Modifier,
    navController: NavController,
    viewModel: NoteEditViewModel
){
    val coroutineScope= rememberCoroutineScope()
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
                        viewModel.updateNote()
                        navController.navigate(route = AppScreens.MainScreen.route)
                        Toast.makeText(message,"Nota actualizada", Toast.LENGTH_SHORT).show()
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
            TitleNoteEditBody(noteUiState = viewModel.noteUiStateEdit, onNoteValueChange = viewModel::updateUiStateEdit)
        }
        Column (
            modifier =modifier.weight(.25f),
            verticalArrangement = Arrangement.Center
        ){
            Row{
                Row ( modifier =modifier.weight(.25f)){
                    ImageCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    VideoCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    AudioCaptureEdit(viewModel = viewModel, modifier = modifier)
                }
                Row ( modifier =modifier.weight(.25f)){
                    RecordatorioEdit(viewModel = viewModel, modifier = modifier)
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
                onExpandedChange = {viewModel.updateIsExpandedEdit(it)}
            ) {
                TextField(
                    value = viewModel.noteUiStateEdit.noteDetails.tipo,
                    onValueChange = {},
                    readOnly = true,
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
                            viewModel.updateOptionNoteEdit(text)
                            viewModel.isExpanded=false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text2) },
                        onClick = {
                            viewModel.updateOptionNoteEdit(text2)
                            viewModel.isExpanded=false
                        }
                    )
                }
            }
        }
        Column {
            Button(
                onClick = { viewModel.updateShowCancelEdit(true)},
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
                    show = viewModel.showCancelEdit,
                    onDismiss = { viewModel.updateShowCancelEdit(false) },
                    onConfirm = {
                        navController.navigate(route = AppScreens.MainScreen.route)
                    },
                    titulo = stringResource(id = R.string.cancelarActualización),
                    text = stringResource(id = R.string.cancelarActualizaciontext)
                )
            }
        }
    }
}



@Composable
fun ImageCaptureEdit(
    viewModel: NoteEditViewModel,
    modifier:Modifier
){
    val context = LocalContext.current
    var uri=ComposeFileProvider.getImageUri(context)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if(success) {
                viewModel.updatehasImage(success)
                viewModel.updateImageUri(uri)
                viewModel.updateUrisList(uri)
            }
        }
    )
    Button(
        onClick = {
            uri = ComposeFileProvider.getImageUri(context)
            cameraLauncher.launch(uri)
        },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ){
        Icon(
            painter = painterResource(id = R.drawable.gallery),
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun VideoCaptureEdit(
    viewModel: NoteEditViewModel,
    modifier:Modifier
){
    val context = LocalContext.current
    var uri=ComposeFileProvider.getVideoUri(context)
    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = { success ->
            if(success){
                viewModel.updatehasVideo(success)
                viewModel.updateUrisVideosList(uri)
            }
        }
    )
    Button(
        onClick = {
            uri = ComposeFileProvider.getVideoUri(context)
            videoLauncher.launch(uri)
        },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ){
        Icon(
            painter = painterResource(id = R.drawable.video),
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun filesCapture(
    viewModel: NoteEditViewModel,
    modifier: Modifier
){
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.updatehasImage(uri != null)
            if(viewModel.hasImage){
                viewModel.updateImageUri(uri)
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
fun RecordatorioEdit(
    viewModel: NoteEditViewModel,
    modifier:Modifier
){
    Button(
        onClick = {
            viewModel.updateOptionsRecordatorios(true)
        },
        enabled=viewModel.recordatorios,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ){
        if(viewModel.recordatorios){
            Icon(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.primary
            )
        }else{
            Icon(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        RelojEdit(viewModel = viewModel)
        opcionesRecordatoriosEdit(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewImagesEdit(
    viewModel: NoteEditViewModel,
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
                Text(text = stringResource(id = R.string.imagenes)+viewModel.cantidad,
                    modifier=modifier.weight(.3f))
                Row (
                    modifier=modifier.weight(.7f),
                    horizontalArrangement = Arrangement.End
                ){
                    filesCapture(viewModel = viewModel, modifier = modifier)
                    Button(
                        enabled = viewModel.cantidad!=0,
                        onClick = {
                            viewModel.deleteLastUri()
                        },
                        contentPadding = PaddingValues(10.dp)) {
                        Text(text = stringResource(id = R.string.eliminarUltimafoto))
                    }
                }
            }
            if (viewModel.hasImage || (viewModel.isEditar && !viewModel.hasImage)){
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
                        mostrarImagenEdit(viewModel = viewModel, uri = viewModel.uriMostrar)
                    }
                }
            }
        }
    }
}

@Composable
fun mostrarImagenEdit(
    viewModel: NoteEditViewModel,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewVideosEdit(
    viewModel: NoteEditViewModel,
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
                Text(text = stringResource(id = R.string.videos)+viewModel.cantidadVideos,
                    modifier=modifier.weight(.4f))
                Row (
                    modifier=modifier.weight(.6f),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        enabled = viewModel.cantidadVideos!=0,
                        onClick = {
                            viewModel.deleteLastUriVideos()
                        },
                        contentPadding = PaddingValues(10.dp)) {
                        Text(text = stringResource(id = R.string.eliminarUltimoVideo))
                    }
                }
            }
            if (viewModel.hasVideo || (viewModel.isEditar && !viewModel.hasVideo)){
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
                                .size(width = 100.dp, height = 120.dp)
                        ){
                            Icon(painter = painterResource(id = R.drawable.video_logo), contentDescription = "")
                        }
                        mostrarVideoEdit(viewModel = viewModel, uri = viewModel.uriMostrar)
                    }
                }
            }
        }
    }
}

@Composable
fun mostrarVideoEdit(
    viewModel: NoteEditViewModel,
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioCaptureEdit(
    viewModel: NoteEditViewModel,
    modifier:Modifier
){
    Button(
        onClick = {
            viewModel.updateShowOptionsAudio(true)
        },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ){
        Icon(
            painter = painterResource(id = R.drawable.audio),
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewAudiosEdit(
    viewModel: NoteEditViewModel,
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
                Text(text = stringResource(id = R.string.audios)+viewModel.cantidadAudios,
                    modifier=modifier.weight(.4f))
                Row (
                    modifier=modifier.weight(.6f),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        enabled = viewModel.cantidadAudios!=0,
                        onClick = {
                            viewModel.deleteLastUriAudios()
                            viewModel.updateFileNumb(viewModel.fileNumb-1)
                        },
                        contentPadding = PaddingValues(10.dp)) {
                        Text(text = stringResource(id = R.string.eliminarUltimoAudio))
                    }
                }
            }
            if (viewModel.hasAudio || (viewModel.isEditar  && !viewModel.hasAudio)){
                LazyRow(modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)) {
                    items(viewModel.urisAudioslist.toList()) { uri ->
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
                    }
                }
                ReproducirEdit(viewModel = viewModel, uri = viewModel.uriMostrar )
            }
        }
    }
}

@Composable
fun ReproducirEdit(
    viewModel: NoteEditViewModel,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelojEdit(
    viewModel: NoteEditViewModel
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
fun opcionesRecordatoriosEdit(
    viewModel: NoteEditViewModel,
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
                            Button(
                                onClick = {
                                    viewModel.updateShowReloj(true)},
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = stringResource(id = R.string.definirHora))
                            }
                        }
                    }
                    Text(
                        text = stringResource(id = R.string.Recordatorio),
                        modifier = Modifier.padding(5.dp))
                    Text(
                        text = viewModel.hora,
                        modifier = Modifier.padding(5.dp))
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
                            var time2= LocalTime.of(viewModel.hour,viewModel.minute,0)
                            val dif=time.until(time2, ChronoUnit.MILLIS)
                            if(dif>0) {
                                NotificacionesEdit(dif, viewModel)
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

@Composable
fun NotificacionesEdit(
    milisegundos: Long,
    viewModel: NoteEditViewModel
){
    if(viewModel.notificacion){
        val context= LocalContext.current
        val idCanal= "CanalNotas"

        LaunchedEffect(Unit){
            crearCanalNotificacion(idCanal,context)
        }
        notificacionProgramada(context,milisegundos, viewModel.idNotificacion)
    }
}