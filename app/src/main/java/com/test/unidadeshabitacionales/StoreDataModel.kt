package com.test.unidadeshabitacionales

data class StoresDataModel(val product: MutableList<StoreDataModel> = emptyList<StoreDataModel>().toMutableList())

data class StoreDataModel(
    val image: String = "",
    val title: String = "",
    val detail: String = "",
    val price: Int = 0
)