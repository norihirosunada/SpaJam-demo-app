package com.norihiro.myapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ref: https://codechacha.com/ja/android-alarmmanager/

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)  // 1.アラーム条件が満たされたとき、レシーバに渡されるインテントを設定します。
        val pendingIntent = PendingIntent.getBroadcast(     // 2. AlarmManagerがインテントを持っているが、一定の時間が経ったの背後に伝達するため、PendingIntentにする必要があります。 PendingIntentのrequestCode引数として NOTIFICATION_IDを伝えました。複数PendingIntentを使用する場合requestCodeを別の方法でくれるが、この例では、1つのアラームのみを登録するので、 NOTIFICATION_IDを使用しました。 flagは下から再び説明します
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_IMMUTABLE) // Android12以上では FLAG_IMMUTABLEを指定する必要があるらしい（https://akira-watson.com/android/alarmmanager-timer.html)

        findViewById<ToggleButton>(R.id.toggleButton).setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val toastMessage = if (isChecked) {   //  3. ToggleButtonが押されると isChecked=true、再び押されるisChecked=falseになります。
                val triggerTime = (SystemClock.elapsedRealtime()  // 4. Elapsed timeを使用し、現在の時刻から60秒後にアラームが発生するように設定しました。時間はmsに設定します。
                        + 10 * 1000) // 単位は[ms], この例では60秒後
                alarmManager.set(   // 5. set()を利用して、引数を渡します。 ELAPSED_REALTIME_WAKEUPは、以下に再度説明します。
                    AlarmManager.ELAPSED_REALTIME_WAKEUP, // 省電力モードであろうが通知を送信するタイプ
                    triggerTime,
                    pendingIntent
                )
                "Onetime Alarm On"
            } else {
                alarmManager.cancel(pendingIntent)    // 6.アラームを解除するときは、登録したPendingIntentを引数として渡します。
                "Onetime Alarm Off"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        })

        // Intent イコール ブロードキャストメッセージ
    }


}