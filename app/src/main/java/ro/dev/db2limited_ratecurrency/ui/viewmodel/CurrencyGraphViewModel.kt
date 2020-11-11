package ro.dev.db2limited_ratecurrency.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import ro.dev.db2limited_ratecurrency.data.model.responseNBUbyCode.CurrencyResponseNBUbyCode
import ro.dev.db2limited_ratecurrency.data.remote.ApiClient
import ro.dev.db2limited_ratecurrency.data.remote.RemoteDataSource
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Resource

class CurrencyGraphViewModel: ViewModel() {

    private val remoteDataSource: RemoteDataSource = RemoteDataSource(ApiClient)
    private val repository: CurrencyRepository = CurrencyRepository(remoteDataSource)

    private val _startDateNBU = MutableLiveData<String>()
    private val _currencyResponseNBUbyCode = _startDateNBU.switchMap { date ->
        repository.getCurrencyNBUbyCode(date)
    }

    val currencyResponseNBUbyCode: LiveData<Resource<CurrencyResponseNBUbyCode>> = _currencyResponseNBUbyCode

    fun setDateNBU(date: String) {
        _startDateNBU.value = date
    }
}