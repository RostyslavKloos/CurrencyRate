package ro.dev.db2limited_ratecurrency.data.model.responseNBU

data class CurrencyResponseItemNBU(
    val cc: String,
    val exchangedate: String,
    val r030: Int,
    val rate: Double,
    val txt: String
)