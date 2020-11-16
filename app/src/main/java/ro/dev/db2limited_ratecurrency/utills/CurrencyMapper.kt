package ro.dev.db2limited_ratecurrency.utills

import com.jjoe64.graphview.series.DataPoint
import ro.dev.db2limited_ratecurrency.data.model.responseCBR.Record
import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.ExchangeRate
import java.text.SimpleDateFormat
import java.util.*

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


class CurrencyMapperCBR: Mapper<List<Record>, Array<DataPoint>> {
    override fun mapFrom(type: List<Record>): Array<DataPoint> {
        val mappedArray = mutableListOf<DataPoint>()

        type.forEach {index ->
            var string = index.Value
            string = string.substring(0, 2) + '.' + string.substring(2+1)

            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
            val date = formatter.parse(index.Date)
            mappedArray.add(DataPoint(date, string.toDouble()))
        }

        return mappedArray.toTypedArray()
    }
}