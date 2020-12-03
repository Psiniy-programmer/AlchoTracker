package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.CalendarLiveData
import technokek.alchotracker.data.models.CalendarModel

class CalendarViewModel : ViewModel() {
    private var calendarEvents = CalendarLiveData(STOCK_REF)
    var mMediatorLiveData = MediatorLiveData<MutableMap<LocalDate, MutableList<CalendarModel>>>()
        private set

    init {
        mMediatorLiveData.addSource(calendarEvents) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
    }

    fun pushData(date: LocalDate, event: CalendarModel) {
        // В корутине делаем запрос на изменение данных в БД
        CoroutineScope(Dispatchers.IO).launch {
            calendarEvents.pushDataToDB(date, event)
        }
    }

    fun pushDeleteEvent(date: LocalDate, calendarModel: CalendarModel) {
        // В корутине делаем запрос на изменение данных в БД
        CoroutineScope(Dispatchers.IO).launch {
            calendarEvents.pushDeleteEventToDB(date, calendarModel)
        }
    }

    companion object {
        private val STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("events")
    }
}
