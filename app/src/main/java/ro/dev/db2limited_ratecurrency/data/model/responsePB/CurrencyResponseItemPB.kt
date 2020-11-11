package ro.dev.db2limited_ratecurrency.data.model.responsePB

data class CurrencyResponseItemPB(
    val base_ccy: String,
    val buy: Double,
    val ccy: String,
    val sale: Double
)