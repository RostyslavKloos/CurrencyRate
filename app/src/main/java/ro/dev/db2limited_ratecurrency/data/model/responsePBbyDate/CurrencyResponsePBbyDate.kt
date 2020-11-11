package ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate

data class CurrencyResponsePBbyDate(
    val bank: String,
    val baseCurrency: Int,
    val baseCurrencyLit: String,
    val date: String,
    val exchangeRate: List<ExchangeRate>
)