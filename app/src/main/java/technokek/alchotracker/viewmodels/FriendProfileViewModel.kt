package technokek.alchotracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.FriendProfileLiveData
import technokek.alchotracker.data.FriendToggleRequestLiveData
import technokek.alchotracker.data.models.FriendProfileModel
import technokek.alchotracker.data.models.FriendToggleRequestModel

class FriendProfileViewModel(application: Application, uid: String) :
    AndroidViewModel(application) {
    var profile = FriendProfileLiveData(dbRef, aRef, uid)
    var requests = FriendToggleRequestLiveData(dbRef, aRef, uid)
    private val mMediatorProfileLiveData = MediatorLiveData<FriendProfileModel>()
    private val mMediatorRequestsLiveData =
        MediatorLiveData<FriendToggleRequestModel>()

    init {
        mMediatorProfileLiveData.addSource(profile) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorProfileLiveData.postValue(it)
                }
            } else {
                mMediatorProfileLiveData.value = null
            }
        }
        mMediatorRequestsLiveData.addSource(requests) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorRequestsLiveData.postValue(it)
                }
            } else {
                mMediatorProfileLiveData.value = null
            }
        }
    }

    fun addFriend() {
        CoroutineScope(Dispatchers.IO).launch {
            requests.addFriend()
        }
    }

    fun deleteFriend() {
        CoroutineScope(Dispatchers.IO).launch {
            requests.deleteFriend()
        }
    }

    fun cancelRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            requests.cancelRequest()
        }
    }

    fun getChatID() {
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val aRef = FirebaseAuth.getInstance()
    }
}
