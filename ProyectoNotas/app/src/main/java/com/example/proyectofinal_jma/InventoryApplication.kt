package com.example.proyectofinal_jma

import android.app.Application
import com.example.proyectofinal_jma.data.AppContainer
import com.example.proyectofinal_jma.data.AppDataContainer
//clase que se inicia al empezar a correr la aplicación
class InventoryApplication:Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}