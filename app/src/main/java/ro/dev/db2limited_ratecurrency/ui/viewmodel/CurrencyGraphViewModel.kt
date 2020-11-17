package ro.dev.db2limited_ratecurrency.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ro.dev.db2limited_ratecurrency.data.domain.model.responseCBR.DateParamsCBR
import ro.dev.db2limited_ratecurrency.data.domain.model.responseCBR.ValCurs
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Resource

class CurrencyGraphViewModel @ViewModelInject constructor(repository: CurrencyRepository) : ViewModel() {

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