package com.norihiro.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import java.time.LocalDateTime

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.toInputActivityButton).setOnClickListener {
            if (isRegistered()) {
                // 今日の独り言を登録していない場合
                val intent = Intent(application, ResultActivity::class.java)
                val from = "HomeActivity"
                intent.putExtra("FROM", from) // ボタン描画の変更
                startActivity(intent)
            } else {
                // まだ今日の独り言を登録していない場合
                val intent = Intent(application, InputActivity::class.java)
                startActivity(intent)
            }

        }

        findViewById<TextView>(R.id.shareButton).setOnClickListener {
            // SNSシェアの処理を書く
        }

        // 画面文字列描画
        val localDateTime = LocalDateTime.now()
        findViewById<TextView>(R.id.dateTextViewHome).text = "${localDateTime.monthValue} / ${localDateTime.dayOfMonth}"

    }

    fun isRegistered(): Boolean {
        return false
    }

}