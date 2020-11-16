package ro.dev.db2limited_ratecurrency.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import ro.dev.db2limited_ratecurrency.data.remote.service.IApiServiceCBR
import ro.dev.db2limited_ratecurrency.data.remote.service.IApiServiceNBU
import ro.dev.db2limited_ratecurrency.data.remote.service.IApiServicePB
import ro.dev.db2limited_ratecurrency.utills.Constants.BASE_URL_CBR
import ro.dev.db2limited_ratecurrency.utills.Constants.BASE_URL_NBU
import ro.dev.db2limited_ratecurrency.utills.Constants.BASE_URL_PB

object ApiClient {

    private fun retrofitPB(): Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_PB)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun retrofitNBU(): Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_NBU)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun retrofitCBR(): Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_CBR)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    val retrofitServicePB: IApiServicePB by lazy {
        retrofitPB().create(IApiServicePB::class.java)
    }
    val retrofitServiceNBU: IApiServiceNBU by lazy {
        retrofitNBU().create(IApiServiceNBU::class.java)
    }

    val retrofitServiceCBR: IApiServiceCBR by lazy {
        retrofitCBR().create(IApiServiceCBR::class.java)
    }
}