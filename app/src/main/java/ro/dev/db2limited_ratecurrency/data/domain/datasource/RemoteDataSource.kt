package ro.dev.db2limited_ratecurrency.data.domain.datasource

import ro.dev.db2limited_ratecurrency.data.domain.remote.service.IApiServiceCBR
import ro.dev.db2limited_ratecurrency.data.domain.remote.service.IApiServiceNBU
import ro.dev.db2limited_ratecurrency.data.domain.remote.service.IApiServicePB

class RemoteDataSource(private val serviceNBU: IApiServiceNBU, private val servicePB: IApiServicePB, private val serviceCBR: IApiServiceCBR ) : BaseDataSource() {

    suspend fun getCurrencyNBU(date: String) = getResult { serviceNBU.getCurrencyNBU(date) }

    suspend fun getCurrencyPBbyDate(date: String) = getResult { servicePB.getCurrencyPBbyDate(date) }

    suspend fun getCurrencyCBR(date1: String, date2: String) = getResult { serviceCBR.getCurrencyCBR(date1, date2) }
}
