package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.CurrentUserLiveData
import technokek.alchotracker.data.FriendLiveData
import technokek.alchotracker.data.FriendRequestLiveData
import technokek.alchotracker.data.models.FriendModel

class FriendViewModel : ViewModel() {

    private val friends = FriendLiveData(HOT_STOCK_REF)
    private val friendRequests = FriendRequestLiveData(HOT_STOCK_REF)
    private val currentUser = CurrentUserLiveData(HOT_STOCK_REF)
    var mediatorFriendLiveData = MediatorLiveData<MutableList<FriendModel>>()
        private set
    var mediatorRequestLiveData = MediatorLiveData<MutableList<FriendModel>>()
        private set
    var mediatorCurrentUser = MediatorLiveData<FriendModel>()
        private set

    init {
        mediatorFriendLiveData.addSource(friends) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorFriendLiveData.postValue(it)
                }
            } else {
                mediatorFriendLiveData.value = null
            }
        }

        mediatorRequestLiveData.addSource(friendRequests) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorRequestLiveData.postValue(it)
                }
            } else {
                mediatorFriendLiveData.value = null
            }
        }

        mediatorCurrentUser.addSource(currentUser) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorCurrentUser.postValue(it)
                }
            } else {
                mediatorCurrentUser.value = null
            }
        }
    }

    fun acceptRequest(uid: String, pos: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            friendRequests.acceptRequest(uid, pos, currentUser.value!!)
        }
    }

    fun denyRequest(uid: String, pos: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            friendRequests.denyRequest(uid, pos, currentUser.value!!)
        }
    }

    companion object {
        private val HOT_STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("users")
    }
}
