package com.norihiro.myapplication

import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

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
            communicationBridge.callToUnitySendJson("{\"Pafe\": [{\"id\":0,\"color\":\"44FF45\",\"ingredient\":\"apple\",\"date\":20221001},{\"id\":1,\"color\":\"44FF46\",\"ingredient\":\"mango\",\"date\":20221002},{\"id\":2,\"color\":\"44FF47\",\"ingredient\":\"orange\",\"date\":20221003}]}")
            finish()
        }

        val positiveVal = intent.getFloatExtra("POSITIVE", 0.0F)
        Log.d("POSITIVE_VAL", positiveVal.toString())
        val negativeVal = intent.getFloatExtra("NEGATIVE", 0.0F)
        Log.d("NEGATIVE_VAL", negativeVal.toString())
        if(positiveVal < negativeVal) {
            findViewById<ImageView>(R.id.resultCircle).setColorFilter(Color.parseColor("#603D26"))
        } else {
            findViewById<ImageView>(R.id.resultCircle).setColorFilter(Color.parseColor("#F84B3D"))
        }
        val colorcode = intent.getIntExtra("COLOR", 0)
//        findViewById<ImageView>(R.id.resultCircle).setColorFilter(Color.parseColor("#F84B3D"))

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