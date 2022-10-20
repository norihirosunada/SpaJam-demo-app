package com.norihiro.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val CHANNEL_ID = "TEST_APP_CHANNEL_1"

        // Android 8.0以上の場合のみ実行する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2-1. NotificationChannel オブジェクトを作成
            val name = "お試し通知"
            val descriptionText = "お試しで通知を送るための通知チャンネルです😊！"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // 2-2. チャネルをシステムに登録
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("これはタイトルです")
            .setContentText("これはテキストです")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var index = 0;

        findViewById<Button>(R.id.button1).setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(index, builder.build())
                index += 1
            }
        }


    }


}