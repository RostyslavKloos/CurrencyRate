package ro.dev.db2limited_ratecurrency.data.model.responseNBUbyCode

data class CurrencyResponseNBUbyCodeItem(
    val cc: String,
    val exchangedate: String,
    val r030: Int,
    val rate: Double,
    val txt: String
)