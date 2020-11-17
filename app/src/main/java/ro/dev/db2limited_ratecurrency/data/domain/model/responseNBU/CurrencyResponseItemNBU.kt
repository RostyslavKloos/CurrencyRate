package ro.dev.db2limited_ratecurrency.data.domain.model.responseNBU

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyResponseItemNBU(
    val cc: String,
    val exchangedate: String,
    val r030: Int,
    val rate: Double,
    val txt: String
) : Parcelable