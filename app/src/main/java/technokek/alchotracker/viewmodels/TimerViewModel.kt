package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.TimerLiveData
import technokek.alchotracker.data.models.TimerModel

class TimerViewModel : ViewModel() {
    private var timerEvents = TimerLiveData(STOCK_REF)
    var mMediatorLiveData = MediatorLiveData<MutableList<TimerModel>>()
        private set

    init {
        mMediatorLiveData.addSource(timerEvents) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
    }

    companion object {
        private val STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("events")
    }
}