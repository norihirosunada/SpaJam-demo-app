package com.norihiro.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.ColorUtils
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
import kotlin.random.Random

class InputActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(applicationContext) }

    var backToHome: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // Debug用
//        db?.FoodDao()?.insert(
//            Foods(
//                color = "#FF44FF45",
//                name = "Orange",
//                date = "2022/10/23",
//            ))
//        db?.FoodDao()?.insert(
//            Foods(
//                color = "#FF44FF46",
//                name = "Grape",
//                date = "2022/10/22",
//            ))

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

                    // APIに通した結果をDBに保存する
                    languageTask(text) { data, color ->

                        // 画面遷移
                        val intent = Intent(application, ResultActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        val from = "InputActivity"
                        intent.putExtra("FROM", from) // ResultActivityの描画内容がこれに依存
                        intent.putExtra("PICTURE", data)
                        intent.putExtra("COLOR", color)
                        intent.putExtra("INPUT_STRING", text)
                        startActivity(intent)
                    }

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

    private fun languageTask(text: String, completion: ((String, Int) -> Unit)? = null) {

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
                    val negative =
                        sentimentAnalysisResponse.results.documents[0].confidenceScores.negative
                    val positive =
                        sentimentAnalysisResponse.results.documents[0].confidenceScores.positive
                    val neutral =
                        sentimentAnalysisResponse.results.documents[0].confidenceScores.neutral

//                    val negativeBaseColor = [0.66, 0.55 + 0.45 * negative, 1]
//                    val negativeColor = listOf<Float>(238.0F, 198.0F + (162.0F * negative), 1.0F)
//                    val positiveColor = listOf<Float>(25.2F, 198.0F + (162.0F * negative), 1.0F)
//                    val negativeColor = listOf<Float>(238.0F, 0.55F + (0.45F * negative), 1.0F)
//                    val positiveColor = listOf<Float>(25.2F, 0.55F + (0.45F * negative), 1.0F)
                    val negativeColor = FloatArray(3)
                    negativeColor[0] = 25.2F
                    negativeColor[1] = 0.55F + (0.45F * negative)
                    negativeColor[2] = 1.0F

                    val positiveColor = FloatArray(3)
                    negativeColor[0] = 25.2F
                    negativeColor[1] = 0.55F + (0.45F * negative)
                    negativeColor[2] = 1.0F

                    Log.d("COLOR_INT", ColorUtils.HSLToColor(negativeColor).toString())
                    Log.d("COLOR_INT", ColorUtils.HSLToColor(positiveColor).toString())

                    val rgbNegative = String.format("#%06X", ColorUtils.HSLToColor(negativeColor))
                    val rgbPositive = String.format("#%06X", ColorUtils.HSLToColor(positiveColor))

                    val base_fill = listOf<String>(
                        "chocolate",
                        "cookies?2",
                        "cookies?",
                        "cookies",
                        "cream",
                        "imo_souce",
                        "imo2",
                        "orange",
                        "source",
                    )
                    val decorate = listOf<String>(
                        "merenge_caramel",
                        "merenge_pink",
                        "merenge_white",
                        "merenge",
                        "pokky",
                        "pudding",
                    )
                    val decorate_poji = listOf<String>(
                        "icecream",
                        "kabocha",
                    )

                    var index = 0
                    var data = "cream"
                    if (LocalDateTime.now().dayOfMonth <= 12) {
                        index = Random.nextInt(0, base_fill.size)
                        data = base_fill.get(index)
                    } else if (LocalDateTime.now().dayOfMonth <= 20) {
                        index = Random.nextInt(0, decorate.size)
                        data = base_fill.get(index)
                    }  else if (LocalDateTime.now().dayOfMonth == 21) {
                        data = "pudding"
                    } else if (LocalDateTime.now().dayOfMonth == 21) {
                        data = "pokky"
                    } else if (LocalDateTime.now().dayOfMonth == 22) {
                        data = "kabocha"
                    }else if (LocalDateTime.now().dayOfMonth == 23) {
                        data = "icecream"
                    }

                    Log.d("INDEX", index.toString())
                    Log.d("DATA", data)

                    Log.d("COLOR_STRING", rgbNegative)
                    Log.d("COLOR_STRING", rgbPositive)

                    Log.d("response", sentiment)

                    if (rgbNegative < rgbPositive) {
                        db?.FoodDao()?.insert(
                            Foods(
                                color = rgbPositive,
                                name = data,
                                date = "2022/10/23",
                            ))
                        completion?.invoke(data, ColorUtils.HSLToColor(negativeColor))
                    } else {
                        db?.FoodDao()?.insert(
                            Foods(
                                color = rgbNegative,
                                name = data,
                                date = "2022/10/23",
                            ))
                        completion?.invoke(data, ColorUtils.HSLToColor(positiveColor))
                    }

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
    val positive: Float,
    val neutral: Float,
    val negative: Float,
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

