package com.norihiro.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.*


class StorageSnippets {

    companion object {
        private val TAG = "FirestoreSnippets"
    }

    private val storage = Firebase.storage
    private val storageRef = storage.reference

    private val packageName: String = "com.norihiro.myapplication"
    private val appInternalStoragePrefix: String = "/data/data/${packageName}/files"

    fun storeFile(file: File, storeRef: StorageReference, completion:  ((StorageReference) -> Unit)? = null) {
        val stream = FileInputStream(file)

        var uploadTask = storeRef.putStream(stream)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            completion?.invoke(taskSnapshot.storage)
        }
    }

    fun storeFileAtRootWithUUIDRenamed(file: File, completion:  ((String) -> Unit)? = null) {
        val stream = FileInputStream(file)

        val uuidString = UUID.randomUUID().toString()
        val storeRef = storageRef.child(uuidString)

        var uploadTask = storeRef.putStream(stream)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            completion?.invoke(taskSnapshot.storage.downloadUrl.toString())
        }
    }

    fun loadFilePhoto(dbUrl: String, internalStoragePath: String, completion: ((Bitmap) -> Unit)?) {
        val loadRef = storage.getReferenceFromUrl(dbUrl)

        val localFile = File(internalStoragePath, loadRef.name)
        loadRef.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
            val stream: FileInputStream = FileInputStream(localFile)
            val bmData: Bitmap = BitmapFactory.decodeStream(BufferedInputStream(stream))
            completion?.invoke(bmData)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    fun loadFileMusic(dbUrl: String, internalStoragePath: String, completion: ((File) -> Unit)?) {
        val loadRef = storage.getReferenceFromUrl(dbUrl)

        val localFile = File(internalStoragePath, loadRef.name)
        loadRef.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
            completion?.invoke(localFile)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}