package com.test.unidadeshabitacionales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventViewModel : ViewModel() {

    val firestoreService = FirestoreService()
    private val _eventData = MutableLiveData<EventsListDataModel>()
    val eventData: LiveData<EventsListDataModel> get() = _eventData

    suspend fun getEvent() {
        val result = firestoreService.fetchDocument(
            "events",
            "NdnbzDSgKUdKvbJs1KPd",
            EventsListDataModel::class.java)
        result.onSuccess { event ->
            _eventData.value = event
        }.onFailure { error ->
            println("Error fetching document: $error")
        }
    }

    fun addEvent(title: String, area: String, startTime: String, endTime: String, eventDay: String,
                 dataArray: EventsListDataModel) {
        val newData = EventsDataModel(area, startTime, endTime, eventDay, title)
        dataArray.event.add(newData)
        firestoreService.saveDataToFirestore(
            "events",
            "NdnbzDSgKUdKvbJs1KPd",
            dataArray
        ) { error ->
            if (error != null) {
                println("Error guardando evento: ${error.message}")
            } else {
                println("Evento guardado exitosamente")
            }
        }
    }
}