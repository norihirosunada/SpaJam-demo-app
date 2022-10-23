package com.norihiro.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.w3c.dom.Text
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import java.time.LocalDateTime

class HomeActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(applicationContext) }

    // 1. データを保持するリストを用意
    var foods: List<Foods> = emptyList()
    var unityPlayer: UnityPlayer? = null

    val communicationBridge = CommunicationBridge(
        object :CommunicationBridge.CommunicationCallback {

            override fun onNoParamCall() {
                Log.d("UnityCalled", "Callback with no parameter")
            }

            override fun onOneParamCall(param: String) {
                Log.d("UnityCalled",
                    "Callback with one parameter: $param")
            }

            override fun onTwoParamCall(param1: String, param2: Int) {
                Log.d("UnityCalled",
                    "Callback with two parameters: $param1, $param2")
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        unityPlayer = UnityPlayer(this)
        window.clearFlags(1024)

        findViewById<ConstraintLayout>(R.id.unityPafe)?.addView(
            unityPlayer, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        unityPlayer?.requestFocus()


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

        communicationBridge.callToUnitySendJson("")

        findViewById<TextView>(R.id.shareButton).setOnClickListener {
            // SNSシェアの処理を書く

            communicationBridge.callToUnityWithNoMessage()
            communicationBridge.callFromUnityWithOneParameter("hogehoge")
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

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        unityPlayer?.windowFocusChanged(hasFocus)
    }

    override fun onResume() {
        super.onResume()
        unityPlayer?.resume()
    }
}