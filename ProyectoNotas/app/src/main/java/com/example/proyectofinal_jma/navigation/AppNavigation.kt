package com.example.proyectofinal_jma.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectofinal_jma.AddNoteHomework
import com.example.proyectofinal_jma.App
import com.example.proyectofinal_jma.EditNoteHomework
import com.example.proyectofinal_jma.LanguageSettings
import com.example.proyectofinal_jma.data.NotaEntity

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.MainScreen.route
    ){
        var nota=NotaEntity(0,"","","")
        composable(route =AppScreens.MainScreen.route){
            App(modifier = Modifier, navController,
               navigateToItemUpdate={
                    navController.navigate("${AppScreens.EditScreen.route}/${it.id}")
                    nota=it
                })
        }
        composable(route =AppScreens.AddScreen.route){
            AddNoteHomework(modifier = Modifier,navController)
        }
        composable(
            route =AppScreens.EditScreen.route+"/{id}",
            arguments = listOf(navArgument(name = "id") {
                type = NavType.IntType
            })
        ){
            EditNoteHomework(modifier = Modifier,navController)
        }
        composable(route =AppScreens.LanguageScreen.route){
            LanguageSettings(modifier = Modifier,navController)
        }
    }
}