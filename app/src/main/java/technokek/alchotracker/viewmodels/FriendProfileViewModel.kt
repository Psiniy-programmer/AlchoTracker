package technokek.alchotracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.FriendProfileLiveData
import technokek.alchotracker.data.models.FriendProfileModel

class FriendProfileViewModel(application: Application, uid: String) : AndroidViewModel(application) {
    var profile = FriendProfileLiveData(dbRef, uid)
    private val mMediatorLiveData = MediatorLiveData<FriendProfileModel>()

    init {
        mMediatorLiveData.addSource(profile) {
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
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
    }
}