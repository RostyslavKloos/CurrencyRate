package ro.dev.db2limited_ratecurrency.data.model.responsePB

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class CurrencyResponsePB() : ArrayList<CurrencyResponseItemPB>(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CurrencyResponsePB> {
        override fun createFromParcel(parcel: Parcel): CurrencyResponsePB {
            return CurrencyResponsePB(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyResponsePB?> {
            return arrayOfNulls(size)
        }
    }
}