package hoods.com.noteapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hoods.com.noteapplication.presentation.bookmark.BookmarkViewModel
import hoods.com.noteapplication.presentation.detail.DetailAssistedFactory
import hoods.com.noteapplication.presentation.home.HomeViewModel
import hoods.com.noteapplication.presentation.navigation.NoteNavigation
import hoods.com.noteapplication.presentation.navigation.Screens
import hoods.com.noteapplication.presentation.navigation.navigateToSingleTop
import hoods.com.noteapplication.ui.theme.NoteApplicationTheme
import javax.inject.Inject

@AndroidEntryPoint // Esta anotación se usa para indicar que la clase MainActivity está siendo utilizada con Hilt
class MainActivity : ComponentActivity() {
    //se utiliza en el código que proporcionaste para marcar
    // propiedad assistedFactory como un punto de inyección de dependencias.
    @Inject
    lateinit var assistedFactory: DetailAssistedFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    NoteApp()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NoteApp() {
        // Se obtienen instancias de los ViewModels necesarios.
        val homeViewModel: HomeViewModel = viewModel()
        val bookmarkViewModel: BookmarkViewModel = viewModel()
        // Se crea un controlador de navegación para manejar la navegación entre pantallas.
        val navController = rememberNavController()
        // Se utiliza una variable para rastrear la pestaña actual (Inicio o Notas Favoritas).
        var currentTab by remember { mutableStateOf(TabScreen.Home) }
        // Se crea la interfaz de usuario con un Scaffold, que incluye un BottomAppBar y un FloatingActionButton.
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Se crean dos InputChips para cambiar entre las pestañas de Inicio y Notas Favoritas.
                            InputChip(
                                selected = currentTab == TabScreen.Home,
                                onClick = {
                                    currentTab = TabScreen.Home
                                    navController.navigateToSingleTop(route = Screens.Home.name)
                                },
                                label = { Text(text = "Inicio") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null
                                    )
                                },

                                )
                            Spacer(modifier = Modifier.size(12.dp))
                            InputChip(
                                selected = currentTab == TabScreen.BookMark,
                                onClick = {
                                    currentTab = TabScreen.BookMark
                                    // Navega a la pantalla de notas favoritas cuando se selecciona la pestaña "Notas Favoritas".
                                    navController.navigateToSingleTop(route = Screens.Bookmark.name)
                                },
                                label = { Text(text = "Notas Favoritas") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null
                                    )
                                },

                                )
                        }
                    },
                    // Se agrega un botón flotante en la parte inferior para agregar nuevas notas.
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            // Navega a la pantalla de detalles cuando se hace clic en el botón flotante.
                            navController.navigateToSingleTop(route = "${Screens.Detail}")
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                )
            },
        ) {
            // Se muestra la pantalla de navegación principal, que cambia según la pestaña actual.
            NoteNavigation(
                modifier = Modifier.padding(it),
                navHostController = navController,
                homeViewModel = homeViewModel,
                bookmarkViewModel = bookmarkViewModel,
                assistedFactory = assistedFactory
            )
        }

    }
}
//Se define una enumeración llamada Screens para representar las diferentes pantallas de la aplicación.
//Home
//BookMark
enum class TabScreen {
    Home, BookMark
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteApplicationTheme {
        Greeting("Android")
    }
}