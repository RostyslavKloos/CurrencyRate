package ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExchangeRate(
    val baseCurrency: String,
    val currency: String,
    val purchaseRate: Double,
    val purchaseRateNB: Double,
    val saleRate: Double,
    val saleRateNB: Double
): Parcelable