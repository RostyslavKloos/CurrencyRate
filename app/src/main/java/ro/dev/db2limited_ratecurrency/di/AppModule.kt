package ro.dev.db2limited_ratecurrency.di

import androidx.lifecycle.SavedStateHandle
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import ro.dev.db2limited_ratecurrency.data.domain.datasource.RemoteDataSource
import ro.dev.db2limited_ratecurrency.data.domain.remote.service.IApiServiceCBR
import ro.dev.db2limited_ratecurrency.data.domain.remote.service.IApiServiceNBU
import ro.dev.db2limited_ratecurrency.data.domain.remote.service.IApiServicePB
import ro.dev.db2limited_ratecurrency.data.repository.CurrencyRepository
import ro.dev.db2limited_ratecurrency.utills.Constants.BASE_URL_CBR
import ro.dev.db2limited_ratecurrency.utills.Constants.BASE_URL_NBU
import ro.dev.db2limited_ratecurrency.utills.Constants.BASE_URL_PB
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    @Named("retrofitCBR")
    fun provideRetrofitCBR(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL_CBR)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    @Singleton
    @Provides
    @Named("retrofitNBU")
    fun provideRetrofitNBU(okHttpClient: OkHttpClient): Retrofit =  Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL_NBU)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    @Named("retrofitPB")
    fun provideRetrofitPB(okHttpClient: OkHttpClient): Retrofit =
         Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_PB)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideRetrofitServicePB(@Named("retrofitPB")retrofit: Retrofit): IApiServicePB = retrofit.create(IApiServicePB::class.java)

    @Provides
    fun provideRetrofitServiceNBU(@Named("retrofitNBU")retrofit: Retrofit): IApiServiceNBU = retrofit.create(IApiServiceNBU::class.java)

    @Provides
    fun provideRetrofitServiceCBR(@Named("retrofitCBR")retrofit: Retrofit): IApiServiceCBR = retrofit.create(IApiServiceCBR::class.java)

    @Singleton
    @Provides
    fun provideCurrencyRepository(remoteDataSource: RemoteDataSource): CurrencyRepository = CurrencyRepository(remoteDataSource)

    @Singleton
    @Provides
        fun provideRemoteDataSource(servicePB: IApiServicePB, serviceNBU: IApiServiceNBU, serviceCBR: IApiServiceCBR): RemoteDataSource =
        RemoteDataSource(serviceNBU, servicePB, serviceCBR)
}