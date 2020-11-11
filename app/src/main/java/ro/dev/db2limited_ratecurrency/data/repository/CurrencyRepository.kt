package ro.dev.db2limited_ratecurrency.data.repository

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ro.dev.db2limited_ratecurrency.data.remote.RemoteDataSource
import ro.dev.db2limited_ratecurrency.utills.Resource
import ro.dev.db2limited_ratecurrency.utills.performGetOperation

class CurrencyRepository(private val remoteDataSource: RemoteDataSource) {

    fun getCurrencyPB(courseId: Int) =
        performGetOperation { remoteDataSource.getCurrencyPB(courseId) }

    fun getCurrencyNBU(date: String) = performGetOperation { remoteDataSource.getCurrencyNBU(date) }

    fun getCurrencyPBbyDate(date: String) = performGetOperation {
        remoteDataSource.getCurrencyPBbyDate(date)
    }

    fun getCurrencyNBUbyCode(date: String) = performGetOperation {
        remoteDataSource.getCurrencyNBUbyCode(date)
    }
}
