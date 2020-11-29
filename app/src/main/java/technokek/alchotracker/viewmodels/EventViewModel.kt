package technokek.alchotracker.viewmodels

import androidx.lifecycle.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.EventLiveData
import technokek.alchotracker.data.models.EventModel

class EventViewModel : ViewModel() {

    var events = EventLiveData(HOT_STOCK_REF)
        private set
    private val mMediatorLiveData = MediatorLiveData<MutableList<EventModel>>()

    init {
        mMediatorLiveData.addSource(events) {
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
        private val HOT_STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("events")
    }
}
