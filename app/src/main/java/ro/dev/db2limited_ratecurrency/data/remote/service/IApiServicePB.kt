package ro.dev.db2limited_ratecurrency.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName
import ro.dev.db2limited_ratecurrency.data.model.responsePB.CurrencyResponsePB
import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.CurrencyResponsePBbyDate

//https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=11
//https://api.privatbank.ua/p24api/exchange_rates?json&date=01.12.2014

interface IApiServicePB {
    @GET("pubinfo?")
    suspend fun getCurrencyPB(
        @Query("coursid") courseId: Int,
        @QueryName(encoded = true) json: String = "json",
        @QueryName(encoded = true) exchange: String = "exchange",
    ) : Response<CurrencyResponsePB>


    @GET("exchange_rates?")
    suspend fun getCurrencyPBbyDate(
        @Query("date") date: String,
        @QueryName(encoded = true) json: String = "json",
    ) : Response<CurrencyResponsePBbyDate>
}