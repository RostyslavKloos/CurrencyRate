package ro.dev.db2limited_ratecurrency.data.remote

class RemoteDataSource(private val apiClient: ApiClient) : BaseDataSource() {
    suspend fun getCurrencyPB(courseId: Int) =
        getResult { apiClient.retrofitServicePB.getCurrencyPB(courseId) }

    suspend fun getCurrencyNBU(date: String) = getResult { apiClient.retrofitServiceNBU.getCurrencyNBU(date) }

    suspend fun getCurrencyPBbyDate(date: String) = getResult { apiClient.retrofitServicePB.getCurrencyPBbyDate(date) }

    suspend fun getCurrencyNBUbyCode(date: String) = getResult { apiClient.retrofitServiceNBU.getCurrencyNBUbyCode(date) }

}
