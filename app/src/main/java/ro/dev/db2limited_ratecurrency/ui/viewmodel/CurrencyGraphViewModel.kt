package ro.dev.db2limited_ratecurrency.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import ro.dev.db2limited_ratecurrency.data.model.responseCBR.DateParamsCBR
import ro.dev.db2limited_ratecurrency.data.model.responseCBR.ValCurs
import ro.dev.db2limited_ratecurrency.data.remote.ApiClient
import ro.dev.db2limited_ratecurrency.data.remote.RemoteDataSource
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Resource

class CurrencyGraphViewModel : ViewModel() {

    private val remoteDataSource: RemoteDataSource = RemoteDataSource(ApiClient)
    private val repository: CurrencyRepository = CurrencyRepository(remoteDataSource)

    private val _startDate = MutableLiveData<String>()
    val startDate get() = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate get() = _endDate

    private val _params: MutableLiveData<DateParamsCBR> = MutableLiveData()
    private val _currencyResponseCBR = _params.switchMap {
        repository.getCurrencyCBR(it._date1, it._date2)
    }
    val currencyResponseCBR: LiveData<Resource<ValCurs>> = _currencyResponseCBR

    fun setStartDate(date: String) {
        _startDate.value = date
    }

    fun setEndDate(date: String) {
        _endDate.value = date
    }

    fun setDateParamsCBR(params: DateParamsCBR) {
        if (_params.value == params)
            return
        _params.postValue(params)
    }
}