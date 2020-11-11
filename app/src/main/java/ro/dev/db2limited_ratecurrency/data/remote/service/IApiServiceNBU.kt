package ro.dev.db2limited_ratecurrency.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName
import ro.dev.db2limited_ratecurrency.data.model.responseNBU.CurrencyResponseNBU
import ro.dev.db2limited_ratecurrency.data.model.responseNBUbyCode.CurrencyResponseNBUbyCode

//https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=20200302&json
//https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=EUR&date=20200302&json

interface IApiServiceNBU {
    @GET("exchange_rates?")
    suspend fun getCurrencyNBU(
        @Query("date") date: String,
        @QueryName(encoded = true) json: String = "json",
        @QueryName(encoded = true) exchange: String = "exchange"
    ): Response<CurrencyResponseNBU>

    @GET("exchange_rates?")
    suspend fun getCurrencyNBUbyCode(
        @Query("date") date: String,
        @Query("valcode") valcode: String = "USD",
        @QueryName(encoded = true) json: String = "json"
        ): Response<CurrencyResponseNBUbyCode>
}