package com.test.unidadeshabitacionales

import com.google.firebase.Timestamp
import com.google.type.Date
import java.time.Instant


data class EventsListDataModel(val event: MutableList<EventsDataModel> = emptyList<EventsDataModel>().toMutableList())

data class EventsDataModel(
    val area: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var eventDate: String = "",
    var title: String = ""
)
