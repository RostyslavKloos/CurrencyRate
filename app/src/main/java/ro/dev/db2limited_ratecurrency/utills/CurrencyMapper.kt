package ro.dev.db2limited_ratecurrency.utills

import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.ExchangeRate

// Filtering existing array and create new one with necessary currency
class CurrencyMapper: Mapper<List<ExchangeRate>, List<ExchangeRate>> {
    override fun mapFrom(type: List<ExchangeRate>): List<ExchangeRate> {
        val currency = arrayListOf("RUB", "EUR", "USD")
        val mappedArray = arrayListOf<ExchangeRate>()

        type.forEach{
            if (currency.contains(it.currency))
                mappedArray.add(it)
        }
        return mappedArray
    }
}

interface Mapper<R, D> {
    fun mapFrom(type: R): D
}