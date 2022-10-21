package com.norihiro.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import io.grpc.Context.Storage
import java.util.Date


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    // Firestore
    val firestoreIns = FirestoreSnippets()
    var storeDocIds = mutableListOf<String>()
    var loadDocIds = mutableListOf<String>()
    var loadDocData = mutableListOf<List<Any>>()

    // Storage
    val storageIns = StorageSnippets()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener {
            val data = hashMapOf(
                "Integer" to 0,
                "createdAt" to Timestamp(Date())
            )
            firestoreIns.addDocumentAtRootCollection(data) { docId ->
                storeDocIds += docId
                Log.d("StoreIds", storeDocIds.toString())
            }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            firestoreIns.getDocumentsAtRootCollection { docs ->
                for (document in docs) {
                    loadDocIds += document.id
                    loadDocData += listOf(document.data)
                }
                Log.d("LoadDocIds", loadDocIds.toString())
                Log.d("LoadDocData", loadDocData.toString())
            }
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            storageIns.loadFilePhoto("gs://spajam2022-demo.appspot.com/スクリーンショット 2022-09-22 12.47.26.png", applicationContext.filesDir.path) { bmData ->
                findViewById<ImageView>(R.id.image1).setImageBitmap(bmData)
            }
        }

        findViewById<ImageView>(R.id.image1).setImageResource(R.drawable.sample)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }
}

