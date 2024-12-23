package com.test.unidadeshabitacionales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val firestoreService = FirestoreService()
    private val _userData= MutableLiveData<UserDataModel>()
    val userData: LiveData<UserDataModel> get() = _userData
    private val _storeData= MutableLiveData<StoresDataModel>()
    val storeData: LiveData<StoresDataModel> get() = _storeData
    private val _eventData= MutableLiveData<EventsListDataModel>()
    val eventData: LiveData<EventsListDataModel> get() = _eventData
    suspend fun getUser(storedUser: String) {
        val result = firestoreService.fetchDocument("users", storedUser, UserDataModel::class.java)
        result.onSuccess { user ->
            _userData.value = user
        }.onFailure { error ->
            println("Error fetching document: $error")
        }
    }
    suspend fun getStore() {
        val result = firestoreService.fetchDocument("products", "8592a9HVkvW4M5d7lPMj", StoresDataModel::class.java)
        result.onSuccess { store ->
            _storeData.value = store
        }.onFailure { error ->
            println("Error fetching document: $error")
        }
    }
    suspend fun getEvents() {
        val result = firestoreService.fetchDocument("events", "NdnbzDSgKUdKvbJs1KPd", EventsListDataModel::class.java)
        result.onSuccess { notice ->
            _eventData.value = notice
        }.onFailure { error ->
            println("Error fetching document: $error")
        }
    }
}