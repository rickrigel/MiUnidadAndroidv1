package com.test.unidadeshabitacionales

data class AnnauncementsListDataModel(var notice: MutableList<AnnauncementsDataModel> = emptyList<AnnauncementsDataModel>().toMutableList())

data class AnnauncementsDataModel(
    var detail: String = "",
    var disLike: Int = 0,
    var like: Int = 0,
    var title: String = ""
)
