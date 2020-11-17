package ro.dev.db2limited_ratecurrency.data.repository

import ro.dev.db2limited_ratecurrency.data.domain.datasource.RemoteDataSource
import ro.dev.db2limited_ratecurrency.utills.performGetOperation

class CurrencyRepository(private val remoteDataSource: RemoteDataSource) {

    fun getCurrencyNBU(date: String) = performGetOperation { remoteDataSource.getCurrencyNBU(date) }

    fun getCurrencyPBbyDate(date: String) = performGetOperation {
        remoteDataSource.getCurrencyPBbyDate(date)
    }

    fun getCurrencyCBR(date1: String, date2: String) =
        performGetOperation { remoteDataSource.getCurrencyCBR(date1, date2) }
}
