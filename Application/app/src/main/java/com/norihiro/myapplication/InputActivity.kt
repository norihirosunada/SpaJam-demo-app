package com.norihiro.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.time.LocalDateTime

class InputActivity : AppCompatActivity() {

    var backToHome: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        findViewById<Button>(R.id.toBackButton).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.makeIngredientButton).setOnClickListener {
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setMessage("これから作る材料は後で変更できないよ！")
                .setPositiveButton("いいよ", { dialog, which ->

                    // Input内容をAPIに通す

                    // DBにInput内容を保存する

                    // APIに通した結果をDBに保存する

                    val intent = Intent(application, ResultActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    val from = "InputActivity"
                    intent.putExtra("FROM", from) // ResultActivityの描画内容がこれに依存
                    startActivity(intent)
                })
                .setNegativeButton("やだ", { dialog, which ->

                    dialog.dismiss()
                })
                .show()
        }

        val localDateTime = LocalDateTime.now()
        findViewById<TextView>(R.id.dateTextViewHome).text = "${localDateTime.monthValue} / ${localDateTime.dayOfMonth}"
    }

    override fun onPause() {
        super.onPause()
        backToHome = true // ResultActivityから一気に戻るために必要な処理
    }

    override fun onResume() {
        super.onResume()
        if (backToHome) { // ResultActivityからHomeActivityへ一気に戻るために必要な処理
            finish()
        }
    }


}