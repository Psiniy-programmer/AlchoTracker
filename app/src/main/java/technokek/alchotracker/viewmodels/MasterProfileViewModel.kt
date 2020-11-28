package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.MasterProfileLiveData
import technokek.alchotracker.data.models.MasterProfileModel

class MasterProfileViewModel : ViewModel() {
    var profile = MasterProfileLiveData(dbRef)
    private val mMediatorLiveData = MediatorLiveData<MasterProfileModel>()

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