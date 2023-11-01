package hoods.com.noteapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hoods.com.noteapplication.presentation.bookmark.BookmarkScreen
import hoods.com.noteapplication.presentation.bookmark.BookmarkViewModel
import hoods.com.noteapplication.presentation.detail.DetailAssistedFactory
import hoods.com.noteapplication.presentation.detail.DetailScreen
import hoods.com.noteapplication.presentation.home.HomeScreen
import hoods.com.noteapplication.presentation.home.HomeViewModel
//Se define una enumeración llamada Screens para representar las diferentes pantallas de la aplicación.
//Home
//Detail
//Bookmark
enum class Screens {
    Home, Detail, Bookmark
}

@Composable
fun NoteNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    bookmarkViewModel: BookmarkViewModel,
    assistedFactory: DetailAssistedFactory,
) {
    // Se define un contenedor de navegación (NavHost) con un controlador de navegación y un destino de inicio.
    NavHost(
        navController = navHostController,
        startDestination = Screens.Home.name //INICIO
    ) {
        // Definición de la pantalla "INICIO".
        composable(route = Screens.Home.name) {
            // Se recopila el estado actual del ViewModel asociado a la pantalla de inicio.
            val state by homeViewModel.state.collectAsState()
            // Se muestra la pantalla "Home" pasando el estado y las funciones necesarias.
            HomeScreen(
                state = state,
                onBookmarkChange = homeViewModel::onBookMarkChange,
                onDeleteNote = homeViewModel::deleteNote,
                onNoteClicked = {
                    navHostController.navigateToSingleTop(
                        // Navega a la pantalla "Detail" con el ID de la nota como argumento.
                        route = "${Screens.Detail.name}?id=$it"
                    )
                }
            )
        }
        // Definición de la pantalla "nota favorita".
        composable(route = Screens.Bookmark.name) {
            // Se recopila el estado actual del ViewModel asociado a la pantalla de marcadores.
            val state by bookmarkViewModel.state.collectAsState()
            // Se muestra la pantalla "Bookmark" pasando el estado y las funciones necesarias.
            BookmarkScreen(
                state = state,
                modifier = modifier,
                onBookMarkChange = bookmarkViewModel::onBookmarkChange,
                onDelete = bookmarkViewModel::deleteNote,
                onNoteClicked = {
                    navHostController.navigateToSingleTop(
                        route = "${Screens.Detail.name}?id=$it"
                    )
                }
            )
        }
        // Definición de la pantalla "Detail" con un argumento "id".
        composable(
            route = "${Screens.Detail.name}?id={id}",
            arguments = listOf(
                navArgument("id") {
                    NavType.LongType
                    defaultValue = -1L
                }
            )
        //se utiliza backStackEntry para acceder a información sobre la entrada actual en la pila de navegación.
        // Cuando navegas entre diferentes pantallas en tu aplicación, cada pantalla visitada se coloca
        // una pila, y backStackEntry se refiere a la entrada más reciente (o actual) en esa pila de navegación.
        ) { backStackEntry ->
            // Se recupera el ID de la nota del argumento.
            val id = backStackEntry.arguments?.getLong("id") ?: -1L
            // Se muestra la pantalla "Detail" pasando el ID de la nota y el controlador de navegación.
            DetailScreen(
                noteId = id,
                assistedFactory = assistedFactory,
                navigateUp = { navHostController.navigateUp() }
            )
        }
    }

}
// Función para navegar a una pantalla específica con la opción singleTop.
fun NavHostController.navigateToSingleTop(route: String) {
    navigate(route) {
        // Se configura la navegación con la opción singleTop para evitar duplicados en la pila.
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}//La función navigateToSingleTop se utiliza para controlar la navegación y evitar duplicados en la pila de navegación.










