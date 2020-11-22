package technokek.alchotracker.data

import android.graphics.Color
import androidx.lifecycle.LiveData
import technokek.alchotracker.data.models.EventModel

class EventLiveData : LiveData<MutableList<EventModel>>() {

    init {
        value = (0..29).map { EventModel(it.toString()) }.toMutableList()
        for (i in 0..29) {
            value!![i].avatar.eraseColor(Color.GREEN)
        }
    }
}
