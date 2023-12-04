package com.example.proyectofinal_jma.Notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import com.example.proyectofinal_jma.R
import okhttp3.internal.notify

class NotificacionProgramada:BroadcastReceiver() {
    companion object{
       var NOTIFICACION_ID=1
    }
    override fun onReceive(context: Context, intent: Intent?) {
       crearNotificacion(context)
    }

    private fun crearNotificacion(context: Context){
        val notificacion=NotificationCompat.Builder(context, "CanalNotas")
            .setSmallIcon(R.drawable.pending_actions)
            .setContentTitle("Tarea pendiente")
            .setContentText("Tienes una tarea programada")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Recuerda que tienes que realizar una tarea. ¡No lo olvides! Buena suerte")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val manager=context.getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager
        manager.notify(NOTIFICACION_ID,notificacion)
    }
}