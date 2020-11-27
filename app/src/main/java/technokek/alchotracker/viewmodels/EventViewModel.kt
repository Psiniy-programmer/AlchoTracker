package technokek.alchotracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import technokek.alchotracker.data.EventLiveData
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.network.ApiRepo

class EventViewModel : ViewModel() {

    var events = EventLiveData()
        private set

    init {
        viewModelScope.launch {
            events.value = ApiRepo.getEvent()
        }
    }
}
