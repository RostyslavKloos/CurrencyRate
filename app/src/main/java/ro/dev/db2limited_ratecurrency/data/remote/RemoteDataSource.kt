package ro.dev.db2limited_ratecurrency.data.remote

class RemoteDataSource(private val apiClient: ApiClient) : BaseDataSource() {

    suspend fun getCurrencyNBU(date: String) = getResult { apiClient.retrofitServiceNBU.getCurrencyNBU(date) }

    suspend fun getCurrencyPBbyDate(date: String) = getResult { apiClient.retrofitServicePB.getCurrencyPBbyDate(date) }

    suspend fun getCurrencyCBR(date1: String, date2: String) = getResult { apiClient.retrofitServiceCBR.getCurrencyCBR(date1, date2) }
}
