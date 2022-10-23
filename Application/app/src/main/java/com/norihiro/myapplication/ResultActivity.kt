package com.norihiro.myapplication

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            finish()
        }

        val colorcode = intent.getIntExtra("COLOR", 0)
        findViewById<ImageView>(R.id.resultCircle).setColorFilter(colorcode)

        val getIconName = intent.getStringExtra("PICTURE")
        findViewById<TextView>(R.id.resultPicture).text = getIconName + "を獲得しました！おめでとうございます！"

        val text = intent.getStringExtra("INPUT_STRING")
        findViewById<TextView>(R.id.resultTextView).text = text

        val from = intent.getStringExtra("FROM") // もしInputActivityからきた場合：InputActivity, HomeActivityからきた場合：HomeActivity
        if (from == "InputActivity" ) {
            findViewById<Button>(R.id.backToHomeButton).text = "パフェに追加する"
        } else if (from == "HomeActivity") {
            findViewById<Button>(R.id.backToHomeButton).text = "ホームに戻る"
        }

    }
}