package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.*
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.models.SearchFriendModel

class FriendListViewModel : ViewModel() {

    private lateinit var friends: FriendListLiveData
    var mediatorFriendLiveData = MediatorLiveData<MutableList<FriendModel>>()
        private set

    fun setFriendID(uid: String) {
        friends = FriendListLiveData(HOT_STOCK_REF, uid)
        mediatorFriendLiveData.addSource(friends) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorFriendLiveData.postValue(it)
                }
            } else {
                mediatorFriendLiveData.value = null
            }
        }
    }

    companion object {
        private val HOT_STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("users")
    }
}