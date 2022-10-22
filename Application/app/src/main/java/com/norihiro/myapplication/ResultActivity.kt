package com.norihiro.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            finish()
        }

        val from = intent.getStringExtra("FROM") // もしInputActivityからきた場合：InputActivity, HomeActivityからきた場合：HomeActivity
        if (from == "InputActivity" ) {
            findViewById<Button>(R.id.backToHomeButton).text = "パフェに追加する"
        } else if (from == "HomeActivity") {
            findViewById<Button>(R.id.backToHomeButton).text = "ホームに戻る"
        }

    }
}