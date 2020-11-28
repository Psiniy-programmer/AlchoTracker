package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.ProfileLiveData
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.data.models.ProfileModel

class ProfileViewModel : ViewModel() {
    var profile = ProfileLiveData(dbRef)
    private val mMediatorLiveData = MediatorLiveData<ProfileModel>()

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
        private val sRef = FirebaseStorage.getInstance().getReference("1")
    }
}