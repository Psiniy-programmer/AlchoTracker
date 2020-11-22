package technokek.alchotracker.data

import android.graphics.Color
import androidx.lifecycle.LiveData
import technokek.alchotracker.data.models.FriendModel

class FriendLiveData : LiveData<MutableList<FriendModel>>() {

    init {
        value = (0..19).map { FriendModel(it.toString()) }.toMutableList()
        for (i in 0..19) {
            value!![i].avatar.eraseColor(Color.RED)
        }
    }
}
