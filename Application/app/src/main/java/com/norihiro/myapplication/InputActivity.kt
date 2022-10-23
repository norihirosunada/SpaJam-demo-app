package com.norihiro.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import kotlinx.coroutines.DefaultExecutor.thread
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.time.LocalDateTime
import kotlin.concurrent.thread
import androidx.room.TypeConverter
import com.squareup.moshi.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class InputActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(applicationContext) }

    // 1. データを保持するリストを用意
    var foods: List<Foods> = emptyList()

    var backToHome: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // Debug用
        db?.FoodDao()?.insert(
            Foods(
                color = "44FF45",
                name = "Orange",
                date = "2022/10/23",
            ))
        db?.FoodDao()?.insert(
            Foods(
                color = "44FF46",
                name = "Grape",
                date = "2022/10/22",
            ))

        findViewById<Button>(R.id.toBackButton).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.makeIngredientButton).setOnClickListener {
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setMessage("これから作る材料は後で変更できないよ！")
                .setPositiveButton("いいよ") { dialog, which ->

                    // Input内容をAPIに通す
                    val editText = findViewById<EditText>(R.id.editTextTextMultiLine)
                    val text = editText.text.toString()

                    // DBにInput内容を保存する
                    db?.FoodDao()?.insert(
                        Foods(
                            color = "Data",
                            name = "Macaron",
                            date = "2022/10/01",
                        ))

                    // APIに通した結果をDBに保存する

                    languageTask(text)

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

                    // 画面遷移
                    val intent = Intent(application, ResultActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    val from = "InputActivity"
                    intent.putExtra("FROM", from) // ResultActivityの描画内容がこれに依存
                    startActivity(intent)
                }
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

    private fun languageTask(text: String) {

        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val json = """
            {
                "kind": "SentimentAnalysis",
                "parameters": {
                    "modelVersion": "latest",
                    "opinionMining": "False"
                },
                "analysisInput":{
                    "documents":[
                        {
                            "id":"1",
                            "language":"ja",
                            "text": "$text"
                        }
                    ]
                }
            }
        """.trimIndent()
        val requestBody = RequestBody.create(mediaType, json)
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.AzureEndPoint)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build().create(SentimentService::class.java)

//        コルーチン呼び出し
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
////                val response = retrofit.postLanguageDetection(requestBody)
////                binding.textView2.text = response.results.documents[0].detectedLanguage.name
//            } catch (e: Exception) {
//                e.printStackTrace()
////                binding.textView2.text = "Detecting Failed"
//            }
//        }
        thread {
            try {
//                val service: WeatherService = retrofit.create(WeatherService::class.java)
                val sentimentAnalysisResponse = retrofit.postSentimentAnalysis(requestBody).execute().body()
                    ?: throw IllegalStateException("bodyがnullだよ！")

                Handler(Looper.getMainLooper()).post {
                    val sentiment = sentimentAnalysisResponse.results.documents[0].sentiment
                    Log.d("response", sentiment)
                }
            } catch (e: Exception) {
                Log.d("response", "debug $e")
            }
        }
    }

}

interface SentimentService {
    @Headers(
        "Content-Type: application/json",
        "Ocp-Apim-Subscription-Key:${BuildConfig.AzureAPIKey}"
    )
    @POST("language/:analyze-text?api-version=2022-05-01")
    fun postSentimentAnalysis(@Body body: RequestBody): Call<SentimentInfo>
}

data class SentimentInfo(
    val kind: String,
    val results: SAResults,
)

data class SAResults(
    val documents: List<SADocuments>,
    val errors: List<String>,
    val modelVersion: String,
)

data class SADocuments(
    val id: Int,
    val sentiment: String,
    val confidenceScores: ConfidenceScores,
    val sentences: List<Sentences>,
    val warnings: List<String>,
)

data class ConfidenceScores(
    val positive: Double,
    val neutral: Double,
    val negative: Double,
)

data class Sentences(
    val sentiment: String,
    val confidenceScores: ConfidenceScores,
    val offset: Int,
    val length: Int,
    val text: String,
)

@JsonClass(generateAdapter = true)
data class Pafe(
    var id: Int,
    var color: String,
    var ingredient: String,
    var date: Int
)

