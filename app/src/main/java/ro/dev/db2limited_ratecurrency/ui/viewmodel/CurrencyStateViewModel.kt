package ro.dev.db2limited_ratecurrency.ui.viewmodel

import androidx.lifecycle.*
import ro.dev.db2limited_ratecurrency.data.model.responseNBU.CurrencyResponseNBU
import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.CurrencyResponsePBbyDate
import ro.dev.db2limited_ratecurrency.data.remote.ApiClient
import ro.dev.db2limited_ratecurrency.data.remote.RemoteDataSource
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Resource

const val STATE_PB = "PB_STATE"
const val STATE_NBU = "NBU_STATE"

class CurrencyStateViewModel(private val state: SavedStateHandle) : ViewModel() {

    fun saveResponsePB(responsePBbyDate: CurrencyResponsePBbyDate) {
        state.set(STATE_PB, responsePBbyDate)
    }

    fun getResponsePB() : LiveData<CurrencyResponsePBbyDate> {
        return state.getLiveData(STATE_PB)
    }

    fun getResponseNBU() : LiveData<CurrencyResponseNBU> {
        return state.getLiveData(STATE_NBU)
    }

    fun saveResponseNBU(responseNBU: CurrencyResponseNBU) {
        state.set(STATE_NBU, responseNBU)
    }

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
//
//    private val _tempViewPB = MutableLiveData<View>()
//    private val _tempViewNBU = MutableLiveData<View>()

    val currencyResponsePBbyDate: LiveData<Resource<CurrencyResponsePBbyDate>> = _currencyResponsePBbyDate
    val currencyResponseNBU: LiveData<Resource<CurrencyResponseNBU>> = _currencyResponseNBU

    fun setDatePB(date: String) {
        _datePB.value = date
    }

//    fun setViewPB(view: View) {
//        _tempViewPB.value = view
//    }
//
//    fun setViewNBU(view: View) {
//        _tempViewNBU.value = view
//    }

    fun setDateNBU(date: String) {
        _dateNBU.value = date
    }
}