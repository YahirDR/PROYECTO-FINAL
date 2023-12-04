package com.example.proyectofinal_jma.sizeScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowInfo(): WindowInfo{
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWindthInfo = when{
            configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenHeighInfo =when{
            configuration.screenHeightDp < 400 -> WindowInfo.WindowType.Compact
            configuration.screenHeightDp < 900 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}
//Clase para detectar los tipos de ventana
data class WindowInfo(
    val screenWindthInfo: WindowType,  // Tipo de ventana en función del ancho de pantalla
    val screenHeighInfo: WindowType,  // Tipo de ventana en función del alto de pantalla
    val screenWidth: Dp,  //Ancho de pantalla en unidades densidad independientes (Dp)
    val screenHeight: Dp  // Alto de pantalla en unidades densidad independientes (Dp)

){
    sealed class WindowType{ //clase sellada
        object Compact: WindowType() //Ventanas pequeñas (telefonos)
        object Medium: WindowType() //Ventana para tabletas pequeñas (mediana)
        object  Expanded: WindowType()  //Ventana para tables grandes
    }
}