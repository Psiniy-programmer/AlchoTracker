package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.FriendLiveData
import technokek.alchotracker.data.models.FriendModel

class FriendViewModel : ViewModel() {

    var friends = FriendLiveData(HOT_STOCK_REF)
        private set
    private val mMediatorLiveData = MediatorLiveData<MutableList<FriendModel>>()

    init {
        mMediatorLiveData.addSource(friends) {
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
            .getReference("users")
    }
}
