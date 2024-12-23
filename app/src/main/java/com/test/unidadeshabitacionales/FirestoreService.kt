package com.test.unidadeshabitacionales

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    // Fetch all documents from a collection
    suspend fun fetchData(collection: String): Result<List<DocumentSnapshot>> {
        return try {
            val snapshot = db.collection(collection).get().await()
            Result.success(snapshot.documents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch a specific document by ID
    suspend fun <T> fetchDocument(collection: String, documentID: String, valueType: Class<T>): Result<T> {
        return try {
            val documentSnapshot: DocumentSnapshot = db.collection(collection)
                .document(documentID)
                .get()
                .await()

            val data = documentSnapshot.toObject(valueType)
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(Exception("El documento esta vacío"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun <T> saveDataToFirestore(
        collection: String,
        documentID: String? = null,
        data: T,
        completion: (Exception?) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val gson = Gson()

        try {
            // Convert the data object to a Map using Gson
            val jsonData = gson.toJson(data)
            val dataMap: Map<String, Any> = gson.fromJson(jsonData, object : TypeToken<Map<String, Any>>() {}.type)

            val collectionRef: CollectionReference = db.collection(collection)

            if (documentID != null) {
                // Save with a specified document ID
                collectionRef.document(documentID).set(dataMap, SetOptions.merge())
                    .addOnSuccessListener { completion(null) }
                    .addOnFailureListener { error -> completion(error) }
            } else {
                // Save with an auto-generated document ID
                collectionRef.add(dataMap)
                    .addOnSuccessListener { completion(null) }
                    .addOnFailureListener { error -> completion(error) }
            }
        } catch (error: Exception) {
            completion(error)
        }
    }

    fun uploadFile(
        data: ByteArray,
        fileName: String,
        fileType: String,
        completion: (Result<String>) -> Unit
    ) {
        // Create a reference to Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference.child("uploads/$fileName")

        // Metadata for the file (optional)
        val metadata = StorageMetadata.Builder()
            .setContentType(fileType) // e.g., "image/jpeg" or "application/pdf"
            .build()

        // Upload the file
        storageRef.putBytes(data, metadata)
            .addOnSuccessListener { taskSnapshot ->
                // Retrieve the download URL
                storageRef.downloadUrl
                    .addOnSuccessListener { downloadUrl ->
                        completion(Result.success(downloadUrl.toString()))
                    }
                    .addOnFailureListener { error ->
                        completion(Result.failure(error))
                    }
            }
            .addOnFailureListener { error ->
                completion(Result.failure(error))
            }
    }

}
