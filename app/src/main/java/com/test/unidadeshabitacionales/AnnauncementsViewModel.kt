package com.test.unidadeshabitacionales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnnauncementsViewModel : ViewModel() {

    val firestoreService = FirestoreService()
    private val _userData= MutableLiveData<UserDataModel>()
    val userData: LiveData<UserDataModel> get() = _userData
    private val _noticeData= MutableLiveData<AnnauncementsListDataModel>()
    val noticeData: LiveData<AnnauncementsListDataModel> get() = _noticeData

    suspend fun getNotice() {
        val result = firestoreService.fetchDocument("comunication", "8592a9HVkvW4M5d7lPMj", AnnauncementsListDataModel::class.java)
        result.onSuccess { notice ->
            _noticeData.value = notice
        }.onFailure { error ->
            println("Error: $error")
        }
    }

    suspend fun getUser(storedUser: String) {
        val result = firestoreService.fetchDocument("users", storedUser, UserDataModel::class.java)
        result.onSuccess { user ->
            _userData.value = user
        }.onFailure { error ->
            println("Error: $error")
        }
    }

    fun updateNotice(title: String, detail: String, like: Int, disLike: Int, index: Int, data: AnnauncementsListDataModel) {

// Create a new `AnnouncementModel` object with the provided parameters.
        val newData =
            AnnauncementsDataModel(detail = detail, disLike = disLike, like = like, title = title)

// Replace the element at the specified index in the `notice` list.
        data.notice.removeAt(index)
        data.notice.add(index, newData)
        firestoreService.saveDataToFirestore("comunication", "8592a9HVkvW4M5d7lPMj", data) { error ->
            if (error != null) {
                println("Error guardando encuesta: ${error.message}")
            } else {
                println("Encuesta guardada exitosamente")
            }
        }
    }

    fun addNotice(title: String, detail: String, dataModel: AnnauncementsListDataModel) {
        dataModel.notice.add(AnnauncementsDataModel(detail, 0, 0, title))
        firestoreService.saveDataToFirestore("comunication", "8592a9HVkvW4M5d7lPMj", dataModel) { error ->
            if (error != null) {
                println("Error guardando encuesta: ${error.message}")
            } else {
                println("Encuesta guardada exitosamente")
            }
        }
    }
}