package ro.dev.db2limited_ratecurrency.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ro.dev.db2limited_ratecurrency.data.model.responseCBR.ValCurs


/*
http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=01/04/2011&date_req2=27/04/2011&VAL_NM_RQ=R01235

http://www.cbr.ru/scripts/XML_val.asp?d=0 - VAL_NM_RQ reference
 */

interface IApiServiceCBR {
    @GET("XML_dynamic.asp")
    suspend fun getCurrencyCBR(
        @Query("date_req1", encoded = true) date1: String,
        @Query("date_req2", encoded = true) date2: String,
        @Query("VAL_NM_RQ") valRQ: String = "R01235"
    ): Response<ValCurs>
}