package com.norihiro.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.w3c.dom.Text
import java.time.LocalDateTime

class HomeActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(applicationContext) }

    // 1. データを保持するリストを用意
    var foods: List<Foods> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.toInputActivityButton).setOnClickListener {
            if (isRegistered()) {
                // 今日の独り言を登録している場合
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

    override fun onResume() {
        super.onResume()


        val listPafe_db = mutableListOf<Pafe>()


        val tmp = db?.FoodDao()?.getAll()
        if (tmp != null) {
            foods = tmp!!
            for (food in foods) {
                val pafe = Pafe(food.id, food.color, food.name, food.date.replace("/", "").toInt()) // FoodからPafeに型変換
                listPafe_db.add(pafe)
            }
        }

//                    val listPafe = listOf<Pafe>(
//                        Pafe(0, "44FF45", "apple", 20221001),
//                        Pafe(1, "44FF46", "mango", 20221002),
//                        Pafe(2, "44FF47", "orange", 20221003)
//                    )

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val type = Types.newParameterizedType(List::class.java, Pafe::class.java)
        val listAdapter: JsonAdapter<List<Pafe>> = moshi.adapter(type)
        val multiUserJson = listAdapter.toJson(listPafe_db)
        val messageToUnity = """
                            {"Pafe": ${multiUserJson}}
                        """.trimIndent()

        Log.d("JSONTEXT", messageToUnity)
    }

}