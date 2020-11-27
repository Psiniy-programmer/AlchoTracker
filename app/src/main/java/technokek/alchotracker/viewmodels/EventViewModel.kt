package technokek.alchotracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.network.ApiRepo

class EventViewModel : ViewModel() {

    private var mApiRepo: ApiRepo = ApiRepo()
    private var events: MutableLiveData<ArrayList<EventModel>>? = null

    init {
        if (events == null) {
            events = mApiRepo.getEvent()
        }
    }

    fun getEvents(): MutableLiveData<ArrayList<EventModel>>? {
        return events
    }
}
