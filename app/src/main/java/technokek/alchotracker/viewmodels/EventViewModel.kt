package technokek.alchotracker.viewmodels

import androidx.lifecycle.ViewModel
import technokek.alchotracker.data.EventLiveData

class EventViewModel : ViewModel() {

    var mEventLiveData = EventLiveData()
        private set
}
