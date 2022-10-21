package com.norihiro.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.norihiro.myapplication.BuildConfig.AzureAPIKey
import com.norihiro.myapplication.BuildConfig.AzureEndPoint
import com.norihiro.myapplication.databinding.FragmentTestRestBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestRestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestRestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentTestRestBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestRestBinding.inflate(inflater, container, false)
        binding.button.setOnClickListener {
            languageTask()
        }
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_test_rest, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun languageTask() {
        binding.textView2.text = "Detecting Language"

        val text = binding.editText.text

        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val json = "{" +
                "\"kind\": \"LanguageDetection\"," +
                "\"parameters\": { \"modelVersion\": \"latest\" }," +
                "\"analysisInput\":{ \"documents\":[{ \"id\":\"1\",\"text\": \"$text\" }] }" +
                "}"
        val requestBody = RequestBody.create(mediaType, json)
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AzureEndPoint)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build().create(AzureService::class.java)

//        コルーチン呼び出し
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = retrofit.postLanguageDetection(requestBody)
                binding.textView2.text = response.results.documents[0].detectedLanguage.name
            } catch (e: Exception) {
                e.printStackTrace()
                binding.textView2.text = "Detecting Failed"
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TestRestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestRestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

interface AzureService {
    @Headers(
        "Content-Type: application/json",
        "Ocp-Apim-Subscription-Key:$AzureAPIKey"
    )
    @POST("language/:analyze-text?api-version=2022-05-01")
    suspend fun postLanguageDetection(@Body body: RequestBody): LanguageInfo
}

data class LanguageInfo(
    val kind: String,
    val results: Results,
)

data class Results(
    val documents: List<Documents>,
    val errors: List<String>,
    val modelVersion: String,
)

data class Documents(
    val id: Int,
    val detectedLanguage: DetectedLanguage,
    val warnings: List<String>,
)

data class DetectedLanguage(
    val name: String,
    val iso6391Name: String,
    val confidenceScore: Double,
)
