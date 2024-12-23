package com.test.unidadeshabitacionales

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoreViewModel : ViewModel() {

    val firestoreService = FirestoreService()
    private val _storeData= MutableLiveData<StoresDataModel>()
    val storeData: LiveData<StoresDataModel> get() = _storeData

    suspend fun getStore() {
        val result = firestoreService.fetchDocument("products", "8592a9HVkvW4M5d7lPMj", StoresDataModel::class.java)
        result.onSuccess { store ->
            _storeData.value = store
        }.onFailure { error ->
            println("Error fetching document: $error")
        }
    }

    fun uploadProduct(title: String, detail: String, price: Int, image: ByteArray, dataModel: StoresDataModel) {
        firestoreService.uploadFile(image, "", "image/png"){ result ->
            result.fold(
                onSuccess = { downloadUrl ->
                    val newData = StoreDataModel(downloadUrl, title, detail, price)
                    dataModel.product.add(newData)
                    firestoreService.saveDataToFirestore("products", "8592a9HVkvW4M5d7lPMj", dataModel) { error ->
                        if (error != null) {
                            println("Error guardando producto: ${error.message}")
                        } else {
                            println("Producto guardado exitosamente")
                        }
                    }
                },
                onFailure = { error ->
                }
            )
        }
    }
}