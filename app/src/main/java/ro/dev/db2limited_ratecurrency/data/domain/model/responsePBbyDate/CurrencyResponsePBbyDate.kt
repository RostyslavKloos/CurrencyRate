package ro.dev.db2limited_ratecurrency.data.domain.model.responsePBbyDate

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyResponsePBbyDate(
    val bank: String,
    val baseCurrency: Int,
    val baseCurrencyLit: String,
    val date: String,
    val exchangeRate: List<ExchangeRate>
): Parcelable