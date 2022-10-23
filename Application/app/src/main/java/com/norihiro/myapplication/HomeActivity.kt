package com.norihiro.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import java.time.LocalDateTime

class HomeActivity : AppCompatActivity() {

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