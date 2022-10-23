package com.norihiro.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class ResultActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_result)

        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
//            UnityにJSON送信
            communicationBridge.callFromUnityWithOneParameter("{\"Pafe\": [{\"id\":0,\"color\":\"44FF45\",\"ingredient\":\"apple\",\"date\":20221001},{\"id\":1,\"color\":\"44FF46\",\"ingredient\":\"mango\",\"date\":20221002},{\"id\":2,\"color\":\"44FF47\",\"ingredient\":\"orange\",\"date\":20221003}]}")
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