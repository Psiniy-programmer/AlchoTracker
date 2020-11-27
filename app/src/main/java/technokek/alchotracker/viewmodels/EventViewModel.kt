package technokek.alchotracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.Api
import technokek.alchotracker.data.EventLiveData
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.network.ApiRepo

class EventViewModel : ViewModel() {

    private var mApiRepo: ApiRepo = ApiRepo()
    private var events: MutableLiveData<ArrayList<EventModel>>? = null

    fun init() {
        if (events != null) {
            return
        }
        events = mApiRepo.getEvent()
    }

    fun getEvents() : MutableLiveData<ArrayList<EventModel>>? {
        return events
    }
}
