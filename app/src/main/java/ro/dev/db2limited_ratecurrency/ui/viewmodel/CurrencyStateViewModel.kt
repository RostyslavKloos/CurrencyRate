package ro.dev.db2limited_ratecurrency.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ro.dev.db2limited_ratecurrency.data.domain.model.responseNBU.CurrencyResponseNBU
import ro.dev.db2limited_ratecurrency.data.domain.model.responsePBbyDate.CurrencyResponsePBbyDate
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Constants.STATE_NBU
import ro.dev.db2limited_ratecurrency.utills.Constants.STATE_PB
import ro.dev.db2limited_ratecurrency.utills.Resource

class CurrencyStateViewModel @ViewModelInject constructor(repository: CurrencyRepository) : ViewModel() {
    private val state: SavedStateHandle = SavedStateHandle()
    private val _datePB = MutableLiveData<String>()
    private val _currencyResponsePBbyDate = _datePB.switchMap { date ->
        repository.getCurrencyPBbyDate(date)
    }

    private val _dateNBU = MutableLiveData<String>()
    private val _currencyResponseNBU = _dateNBU.switchMap { date ->
        repository.getCurrencyNBU(date)
    }

    val currencyResponsePBbyDate: LiveData<Resource<CurrencyResponsePBbyDate>> =
        _currencyResponsePBbyDate
    val currencyResponseNBU: LiveData<Resource<CurrencyResponseNBU>> = _currencyResponseNBU

    val isRefreshingLiveDataPB: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshingLiveDataNBU: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isRefreshingLiveDataPB.value = false
        isRefreshingLiveDataNBU.value = false
    }

    fun setDatePB(date: String) {
        _datePB.value = date
        isRefreshingLiveDataPB.value = true
    }

    fun setDateNBU(date: String) {
        _dateNBU.value = date
        isRefreshingLiveDataNBU.value = true
    }

    fun getIsRefreshingDataPB(): LiveData<Boolean> {
        return isRefreshingLiveDataPB
    }

    fun getIsRefreshingDataNBU(): LiveData<Boolean> {
        return isRefreshingLiveDataNBU
    }


    fun saveResponsePB(responsePBbyDate: CurrencyResponsePBbyDate) {
        state.set(STATE_PB, responsePBbyDate)
    }

    fun getResponsePB(): LiveData<CurrencyResponsePBbyDate> {
        return state.getLiveData(STATE_PB)
    }

    fun getResponseNBU(): LiveData<CurrencyResponseNBU> {
        return state.getLiveData(STATE_NBU)
    }

    fun saveResponseNBU(responseNBU: CurrencyResponseNBU) {
        state.set(STATE_NBU, responseNBU)
    }
}