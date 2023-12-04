package com.example.proyectofinal_jma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.proyectofinal_jma.R

val nunito= FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_extralight, FontWeight.ExtraLight),
    Font(R.font.nunito_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    displaySmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

)