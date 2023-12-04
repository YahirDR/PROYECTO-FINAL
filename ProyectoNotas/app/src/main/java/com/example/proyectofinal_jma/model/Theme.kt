package com.example.proyectofinal_jma.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Theme(
    @DrawableRes val miniature:Int,
    @StringRes val titleCard:Int,
    @DrawableRes val check:Int,
)
