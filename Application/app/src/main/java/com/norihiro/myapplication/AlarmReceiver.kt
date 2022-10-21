package com.norihiro.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "CHANNEL_ID_SPAJAM"
    }

    lateinit var notificationManager: NotificationManager

    override fun onReceive(p0: Context?, p1: Intent?) {
        notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(p0)
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val notifyId = 0
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notifyId,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("これはタイトルです")
                .setContentText("これはテキストです from intent")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(12345, builder.build())
    }

    private fun createNotificationChannel() {
        // Android 8.0以上の場合のみ実行する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "お試し通知"
            val descriptionText = "お試しで通知を送るための通知チャンネルです😊！"
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "AlarmManager Tests"
            notificationManager.createNotificationChannel(
                notificationChannel)
        }
    }
}