package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.MembersLiveData
import technokek.alchotracker.data.models.FriendModel

class MembersViewModel : ViewModel() {

    private lateinit var mMembersLiveData: MembersLiveData
    var mediatorLiveData = MediatorLiveData<MutableList<FriendModel>>()

    fun setEventID(eventID: String) {
        mMembersLiveData = MembersLiveData(HOT_STOCK_REF, HOT_STOCK_EVENTS, eventID)

        mediatorLiveData.addSource(mMembersLiveData) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorLiveData.postValue(it)
                }
            } else {
                mediatorLiveData.value = null
            }
        }
    }

    companion object {
        private val HOT_STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("users")
        private val HOT_STOCK_EVENTS = FirebaseDatabase
            .getInstance()
            .getReference("events")
    }
}
