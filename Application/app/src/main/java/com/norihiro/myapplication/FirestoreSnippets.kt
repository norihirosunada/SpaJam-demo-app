package com.norihiro.myapplication

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreSnippets {

    companion object {
        private val TAG = "FirestoreSnippets"
    }

    private val db: FirebaseFirestore = Firebase.firestore // class initの時点で代入される

    fun addDocument(targetCollection: CollectionReference, data: HashMap<String, *>, completion: ((String) -> Unit)? = null) {
        targetCollection.add(data)
            .addOnSuccessListener { docRef ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${docRef.id}")
                completion?.invoke(docRef.id)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error adding document", e)
            }
    }

    fun addDocumentByCollectionName(collectionName: String, data: HashMap<String, *>, completion: ((String) -> Unit)? = null) {
        addDocument(db.collection(collectionName), data, completion)
    }

    fun addDocumentAtRootCollection(data: HashMap<String, *>, completion: ((String) -> Unit)? = null) {
        addDocumentByCollectionName("root", data, completion)

    }

    fun getDocuments(targetCollection: CollectionReference, completion: ((MutableList<QueryDocumentSnapshot>) -> Unit)?) {
        targetCollection.get()
            .addOnSuccessListener { result ->
                val retval = mutableListOf<QueryDocumentSnapshot>()
                for (document in result) {
                    retval += document
                }
                completion?.invoke(retval)
            }
    }

    fun getDocumentsByCollectionName(collectionName: String, completion: ((MutableList<QueryDocumentSnapshot>) -> Unit)?) {
        getDocuments(db.collection(collectionName), completion)
    }

    fun getDocumentsAtRootCollection(completion: ((MutableList<QueryDocumentSnapshot>) -> Unit)?) {
        getDocumentsByCollectionName("root", completion)
    }


}