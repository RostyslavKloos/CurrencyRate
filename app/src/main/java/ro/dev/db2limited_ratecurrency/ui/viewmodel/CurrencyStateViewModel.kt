package ro.dev.db2limited_ratecurrency.ui.viewmodel

import androidx.lifecycle.*
import ro.dev.db2limited_ratecurrency.data.model.responseNBU.CurrencyResponseNBU
import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.CurrencyResponsePBbyDate
import ro.dev.db2limited_ratecurrency.data.remote.ApiClient
import ro.dev.db2limited_ratecurrency.data.remote.RemoteDataSource
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Resource

class CurrencyStateViewModel : ViewModel() {
    private val remoteDataSource: RemoteDataSource = RemoteDataSource(ApiClient)
    private val repository: CurrencyRepository = CurrencyRepository(remoteDataSource)

    private val _datePB = MutableLiveData<String>()
    private val _currencyResponsePBbyDate = _datePB.switchMap { date ->
        repository.getCurrencyPBbyDate(date)
    }

    private val _dateNBU = MutableLiveData<String>()
    private val _currencyResponseNBU = _dateNBU.switchMap { date ->
        repository.getCurrencyNBU(date)
    }

    val currencyResponsePBbyDate: LiveData<Resource<CurrencyResponsePBbyDate>> = _currencyResponsePBbyDate
    val currencyResponseNBU: LiveData<Resource<CurrencyResponseNBU>> = _currencyResponseNBU

    fun setDatePB(date: String) {
        _datePB.value = date
    }

    fun setDateNBU(date: String) {
        _dateNBU.value = date
    }
}