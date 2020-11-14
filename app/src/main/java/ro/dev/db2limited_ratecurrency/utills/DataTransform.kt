package ro.dev.db2limited_ratecurrency.utills

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ro.dev.db2limited_ratecurrency.utills.Resource.Status.ERROR
import ro.dev.db2limited_ratecurrency.utills.Resource.Status.SUCCESS
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

fun roundDoubleTo(value: Double, scale: Int, mode: Int = BigDecimal.ROUND_HALF_UP): String {
    return value.toBigDecimal().setScale(scale, mode).toPlainString()
}

fun <T> performGetOperation(networkCall: suspend () -> Resource<T>): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            emit(Resource.success(responseStatus.data!!))

        } else if (responseStatus.status == ERROR) {
            emit(Resource.error(responseStatus.message!!))
        }
    }

fun getDateFormatNBU(obj: Any): String {
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.ROOT)
    return sdf.format(obj)
}

fun getDateFormanPB(obj: Any) : String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    return sdf.format(obj)
}
